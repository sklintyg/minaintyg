/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.minaintyg.web.web.security.saml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.PrivateKey;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensaml.Configuration;
import org.opensaml.common.*;
import org.opensaml.saml2.metadata.*;
import org.opensaml.samlext.idpdisco.DiscoveryResponse;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.keyinfo.*;
import org.opensaml.xml.signature.*;
import org.opensaml.xml.signature.impl.SignatureImpl;
import org.opensaml.xml.util.AttributeMap;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;

@RunWith(MockitoJUnitRunner.class)
public class MetaDataGeneratorTest {

    private static final String ENTITY_ID = "entityId";
    private static final String ENTITY_BASE_URL = "http://localhost";

    @Mock
    private KeyManager keyManager;

    @Mock
    private XMLObjectBuilderFactory builderFactory;

    @InjectMocks
    private MetadataGenerator metadataGenerator = new MetadataGenerator();

    @Before
    public void setup() {
        metadataGenerator.setEntityId(ENTITY_ID);
        metadataGenerator.setEntityBaseURL(ENTITY_BASE_URL);

        AssertionConsumerService assertionConsumer = mock(AssertionConsumerService.class);
        when(assertionConsumer.getUnknownAttributes()).thenReturn(mock(AttributeMap.class));

        setupSAMLObjectBuilder(EntityDescriptor.DEFAULT_ELEMENT_NAME, EntityDescriptor.class, null);
        setupSAMLObjectBuilder(SPSSODescriptor.DEFAULT_ELEMENT_NAME, SPSSODescriptor.class, null);
        setupSAMLObjectBuilder(NameIDFormat.DEFAULT_ELEMENT_NAME, NameIDFormat.class, null);
        setupSAMLObjectBuilder(AssertionConsumerService.DEFAULT_ELEMENT_NAME, AssertionConsumerService.class, assertionConsumer);
        setupSAMLObjectBuilder(SingleLogoutService.DEFAULT_ELEMENT_NAME, SingleLogoutService.class, null);
    }

    @Test
    public void testGenerateMetadata() throws Exception {
        EntityDescriptor res = metadataGenerator.generateMetadata();

        assertNotNull(res);
        verify(res).setEntityID(ENTITY_ID);
        verify(res, never()).setID(anyString());
    }

    @Test
    public void testGenerateMetadataIdSet() throws Exception {
        final String id = "id";
        metadataGenerator.setId(id);
        EntityDescriptor res = metadataGenerator.generateMetadata();

        assertNotNull(res);
        verify(res).setID(id);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGenerateMetadataSignMetaDataFalse() throws Exception {
        Configuration.getMarshallerFactory().registerMarshaller(EntityDescriptor.DEFAULT_ELEMENT_NAME, mock(Marshaller.class));
        when(((SAMLObjectBuilder<EntityDescriptor>) builderFactory.getBuilder(EntityDescriptor.DEFAULT_ELEMENT_NAME)).buildObject().getElementQName()).thenReturn(EntityDescriptor.DEFAULT_ELEMENT_NAME);
        metadataGenerator.setSignMetadata(false);

        EntityDescriptor res = metadataGenerator.generateMetadata();

        assertNotNull(res);
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateMetadataSignMetaDataFalseMessageEncodingException() throws Exception {
        metadataGenerator.setSignMetadata(false);
        metadataGenerator.generateMetadata();
    }

    @Test
    public void testGenerateExtendedMetadata() {
        final String encryptionKey = "encryptionKey";
        final String signingKey = "signingKey";
        final String entityAlias = "entityAlias";
        final String tlsKey = "tlsKey";
        metadataGenerator.setEncryptionKey(encryptionKey);
        metadataGenerator.setSigningKey(signingKey);
        metadataGenerator.setEntityAlias(entityAlias);
        metadataGenerator.setTlsKey(tlsKey);

        ExtendedMetadata res = metadataGenerator.generateExtendedMetadata();

        assertNotNull(res);
        assertTrue(res.isIdpDiscoveryEnabled());
        assertTrue(res.getIdpDiscoveryResponseURL().startsWith(ENTITY_BASE_URL));
        assertTrue(res.getIdpDiscoveryURL().startsWith(ENTITY_BASE_URL));
        assertEquals(encryptionKey, res.getEncryptionKey());
        assertEquals(signingKey, res.getSigningKey());
        assertEquals(entityAlias, res.getAlias());
        assertEquals(tlsKey, res.getTlsKey());
        assertTrue(res.isLocal());
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateMetadataEntityIdMissing() throws Exception {
        metadataGenerator.setEntityId(null);
        metadataGenerator.generateMetadata();
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateMetadataEntityBaseUrlMissing() throws Exception {
        metadataGenerator.setEntityBaseURL(null);
        metadataGenerator.generateMetadata();
    }

    @Test
    public void testGetServerKeyInfo() throws Exception {
        final String alias = "alias";
        Credential credential = mock(Credential.class);
        SecurityConfiguration secConfig = mock(SecurityConfiguration.class);
        when(credential.getPrivateKey()).thenReturn(mock(PrivateKey.class));
        when(secConfig.getKeyInfoGeneratorManager()).thenReturn(mock(NamedKeyInfoGeneratorManager.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager()).thenReturn(mock(KeyInfoGeneratorManager.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential)).thenReturn(mock(KeyInfoGeneratorFactory.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential).newInstance()).thenReturn(mock(KeyInfoGenerator.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential).newInstance().generate(credential)).thenReturn(mock(KeyInfo.class));
        Configuration.setGlobalSecurityConfiguration(secConfig);
        when(keyManager.getCredential(alias)).thenReturn(credential);

        KeyInfo serverKeyInfo = metadataGenerator.getServerKeyInfo(alias);

        assertNotNull(serverKeyInfo);
        verify(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential).newInstance()).generate(credential);
    }

    @Test(expected = SAMLRuntimeException.class)
    public void testGetServerKeyInfoSecurityException() throws Exception {
        final String alias = "alias";
        Credential credential = mock(Credential.class);
        SecurityConfiguration secConfig = mock(SecurityConfiguration.class);
        when(credential.getPrivateKey()).thenReturn(mock(PrivateKey.class));
        when(secConfig.getKeyInfoGeneratorManager()).thenReturn(mock(NamedKeyInfoGeneratorManager.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager()).thenReturn(mock(KeyInfoGeneratorManager.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential)).thenReturn(mock(KeyInfoGeneratorFactory.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential).newInstance()).thenReturn(mock(KeyInfoGenerator.class));
        when(secConfig.getKeyInfoGeneratorManager().getDefaultManager().getFactory(credential).newInstance().generate(credential)).thenThrow(new SecurityException());
        Configuration.setGlobalSecurityConfiguration(secConfig);
        when(keyManager.getCredential(alias)).thenReturn(credential);

        metadataGenerator.getServerKeyInfo(alias);
    }

    @Test(expected = RuntimeException.class)
    public void testGetServerKeyInfoDoesNotExist() {
        final String alias = "nonExistent";
        metadataGenerator.getServerKeyInfo(alias);
    }

    @Test(expected = RuntimeException.class)
    public void testGetServerKeyInfoMissingPrivateKey() {
        final String alias = "aliasWithoutPrivateKey";
        when(keyManager.getCredential(alias)).thenReturn(mock(Credential.class));
        metadataGenerator.getServerKeyInfo(alias);
    }

    @Test
    public void testGetKeyDescriptor() {
        setupSAMLObjectBuilder(KeyDescriptor.DEFAULT_ELEMENT_NAME, KeyDescriptor.class, null);
        Configuration.getBuilderFactory().registerBuilder(KeyDescriptor.DEFAULT_ELEMENT_NAME, builderFactory.getBuilder(KeyDescriptor.DEFAULT_ELEMENT_NAME));
        UsageType type = UsageType.SIGNING;
        KeyInfo key = mock(KeyInfo.class);
        KeyDescriptor res = metadataGenerator.getKeyDescriptor(type, key);

        assertNotNull(res);
        verify(res).setUse(type);
        verify(res).setKeyInfo(key);
    }

    @Test
    public void testGetDiscoveryService() {
        final String entityAlias = "entityAlias";
        setupSAMLObjectBuilder(DiscoveryResponse.DEFAULT_ELEMENT_NAME, DiscoveryResponse.class, null);

        DiscoveryResponse res = metadataGenerator.getDiscoveryService(ENTITY_BASE_URL, entityAlias);

        assertNotNull(res);
        verify(res).setBinding(DiscoveryResponse.IDP_DISCO_NS);
        verify(res).setLocation(anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSignSAMLObject() throws Exception {
        final String signatureAlgorithm = "signatureAlgorithm";
        SignableSAMLObject signableObject = mock(SignableSAMLObject.class);
        when(signableObject.getElementQName()).thenReturn(Signature.DEFAULT_ELEMENT_NAME);
        Credential signingCredential = mock(Credential.class);
        XMLObjectBuilder<Signature> builder = mock(XMLObjectBuilder.class);
        SignatureImpl signature = mock(SignatureImpl.class);
        when(signature.getSignatureAlgorithm()).thenReturn(signatureAlgorithm);
        when(signature.getCanonicalizationAlgorithm()).thenReturn(signatureAlgorithm);

        when(signature.getKeyInfo()).thenReturn(mock(KeyInfo.class));
        when(builder.buildObject(Signature.DEFAULT_ELEMENT_NAME)).thenReturn(signature);
        Configuration.getBuilderFactory().registerBuilder(Signature.DEFAULT_ELEMENT_NAME, builder);
        Configuration.getMarshallerFactory().registerMarshaller(Signature.DEFAULT_ELEMENT_NAME, mock(Marshaller.class));

        try {
            metadataGenerator.signSAMLObject(signableObject, signingCredential);
            fail("should throw");
        } catch (MessageEncodingException e) {
            assertTrue(e.getCause() instanceof SignatureException);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends SAMLObject> void setupSAMLObjectBuilder(QName qname, Class<T> type, T mock) {
        SAMLObjectBuilder<T> builder = mock(SAMLObjectBuilder.class);
        when(builder.buildObject()).thenReturn(mock != null ? mock : mock(type));
        when(builder.buildObject(qname)).thenReturn(mock != null ? mock : mock(type));
        when(builderFactory.getBuilder(qname)).thenReturn(builder);
    }
}
