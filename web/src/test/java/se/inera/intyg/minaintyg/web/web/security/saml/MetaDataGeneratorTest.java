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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.metadata.*;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
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

    @SuppressWarnings("unchecked")
    private <T extends SAMLObject> void setupSAMLObjectBuilder(QName qname, Class<T> type, T mock) {
        SAMLObjectBuilder<T> builder = mock(SAMLObjectBuilder.class);
        when(builder.buildObject()).thenReturn(mock != null ? mock : mock(type));
        when(builderFactory.getBuilder(qname)).thenReturn(builder);
    }
}
