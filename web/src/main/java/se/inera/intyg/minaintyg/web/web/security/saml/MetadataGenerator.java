/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
/* Copyright 2009 Vladimir Schäfer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.inera.intyg.minaintyg.web.web.security.saml;
import java.util.*;

import javax.xml.namespace.QName;

// CHECKSTYLE:OFF
import org.opensaml.Configuration;
import org.opensaml.common.*;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.metadata.*;
import org.opensaml.samlext.idpdisco.DiscoveryResponse;
import org.opensaml.util.URLBuilder;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.signature.*;
import org.opensaml.xml.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.saml.*;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;

/**
 * The class is responsible for generation of service provider metadata describing the application in
 * the current deployment environment. All the URLs in the metadata will be derived from information in
 * the ServletContext.
 *
 * @author Vladimir Schäfer
 */
public class MetadataGenerator extends org.springframework.security.saml.metadata.MetadataGenerator {

    private String id;
    private String entityId;
    private String entityBaseURL;
    private String entityAlias;

    private boolean requestSigned = true;
    private boolean wantAssertionSigned = true;
    private boolean signMetadata = true;

    private String signingKey = null;
    private String encryptionKey = null;
    private String tlsKey = null;

    private int assertionConsumerIndex = 0;

    // List of case-insensitive alias terms
    private static Map<String, String> aliases = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static {
        aliases.put(SAMLConstants.SAML2_POST_BINDING_URI, SAMLConstants.SAML2_POST_BINDING_URI);
        aliases.put("post", SAMLConstants.SAML2_POST_BINDING_URI);
        aliases.put("http-post", SAMLConstants.SAML2_POST_BINDING_URI);
        aliases.put(SAMLConstants.SAML2_PAOS_BINDING_URI, SAMLConstants.SAML2_PAOS_BINDING_URI);
        aliases.put("paos", SAMLConstants.SAML2_PAOS_BINDING_URI);
        aliases.put(SAMLConstants.SAML2_ARTIFACT_BINDING_URI, SAMLConstants.SAML2_ARTIFACT_BINDING_URI);
        aliases.put("artifact", SAMLConstants.SAML2_ARTIFACT_BINDING_URI);
        aliases.put("http-artifact", SAMLConstants.SAML2_ARTIFACT_BINDING_URI);
        aliases.put(SAMLConstants.SAML2_REDIRECT_BINDING_URI, SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        aliases.put("redirect", SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        aliases.put("http-redirect", SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        aliases.put(SAMLConstants.SAML2_SOAP11_BINDING_URI, SAMLConstants.SAML2_SOAP11_BINDING_URI);
        aliases.put("soap", SAMLConstants.SAML2_SOAP11_BINDING_URI);
        aliases.put(NameIDType.EMAIL, NameIDType.EMAIL);
        aliases.put("email", NameIDType.EMAIL);
        aliases.put(NameIDType.TRANSIENT, NameIDType.TRANSIENT);
        aliases.put("transient", NameIDType.TRANSIENT);
        aliases.put(NameIDType.PERSISTENT, NameIDType.PERSISTENT);
        aliases.put("persistent", NameIDType.PERSISTENT);
        aliases.put(NameIDType.UNSPECIFIED, NameIDType.UNSPECIFIED);
        aliases.put("unspecified", NameIDType.UNSPECIFIED);
        aliases.put(NameIDType.X509_SUBJECT, NameIDType.X509_SUBJECT);
        aliases.put("x509_subject", NameIDType.X509_SUBJECT);
    }

    private Collection<String> bindingsSSO = Arrays.asList("artifact", "post", "paos");
    private Collection<String> bindingsHoKSSO = Arrays.asList("artifact", "post");
    private Collection<String> bindingsSLO = Arrays.asList("post", "redirect");

    @Deprecated
    private boolean includeDiscovery = true;

    @Deprecated
    private String customDiscoveryURL;

    @Deprecated
    private String customDiscoveryResponseURL;

    private boolean includeDiscoveryExtension;

    private ExtendedMetadata extendedMetadata;

    private Collection<String> nameID = null;

    private static final Collection<String> DEFAULT_NAME_ID = Arrays.asList(
            NameIDType.EMAIL,
            NameIDType.TRANSIENT,
            NameIDType.PERSISTENT,
            NameIDType.UNSPECIFIED,
            NameIDType.X509_SUBJECT
    );

    private XMLObjectBuilderFactory builderFactory;

    private KeyManager keyManager;

    private SAMLProcessingFilter samlWebSSOFilter;
    private SAMLWebSSOHoKProcessingFilter samlWebSSOHoKFilter;
    private SAMLLogoutProcessingFilter samlLogoutProcessingFilter;
    private SAMLEntryPoint samlEntryPoint;
    private SAMLDiscovery samlDiscovery;

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataGenerator.class);

    /**
     * Default constructor.
     */
    public MetadataGenerator() {
        this.setBuilderFactory(Configuration.getBuilderFactory());
    }

    @Override
    public EntityDescriptor generateMetadata() {

        if (signingKey == null) {
            signingKey = getKeyManager().getDefaultCredentialName();
        }
        if (encryptionKey == null) {
            encryptionKey = getKeyManager().getDefaultCredentialName();
        }
        if (tlsKey == null) {
            tlsKey = null;
        }

        boolean requestSigned = isRequestSigned();
        boolean assertionSigned = isWantAssertionSigned();
        boolean signMetadata = isSignMetadata();

        Collection<String> includedNameID = getNameID();

        String entityId = getEntityId();
        String entityBaseURL = getEntityBaseURL();
        String entityAlias = getEntityAlias();

        if (entityId == null || entityBaseURL == null) {
            throw new RuntimeException("Required attributes weren't set");
        }

        SAMLObjectBuilder<EntityDescriptor> builder = (SAMLObjectBuilder<EntityDescriptor>) getBuilderFactory().getBuilder(EntityDescriptor.DEFAULT_ELEMENT_NAME);
        EntityDescriptor descriptor = builder.buildObject();
        if (id != null) {
            descriptor.setID(id);
        }
        descriptor.setEntityID(entityId);
        descriptor.getRoleDescriptors().add(buildSPSSODescriptor(entityBaseURL, entityAlias, requestSigned, assertionSigned, includedNameID));

        try {
            if (signMetadata) {
                signSAMLObject(descriptor, getKeyManager().getCredential(signingKey));
            } else {
                marshallSAMLObject(descriptor);
            }
        } catch (MessageEncodingException e) {
            throw new RuntimeException(e);
        }

        return descriptor;

    }

    @Override
    protected KeyInfo getServerKeyInfo(String alias) {
        Credential serverCredential = getKeyManager().getCredential(alias);
        if (serverCredential == null) {
            throw new RuntimeException("Key for alias " + alias + " not found");
        } else if (serverCredential.getPrivateKey() == null) {
            throw new RuntimeException("Key with alias " + alias + " doesn't have a private key");
        }
        return generateKeyInfoForCredential(serverCredential);
    }

    /**
     * Generates extended metadata. Default extendedMetadata object is cloned if present and used for defaults.
     * The following properties are always overriden from the properties of this bean:
     * discoveryUrl, discoveryResponseUrl, signingKey, encryptionKey, entityAlias and tlsKey.
     * Property local of the generated metadata is always set to true.
     *
     * @return generated extended metadata
     */
    @Override
    public ExtendedMetadata generateExtendedMetadata() {

        ExtendedMetadata metadata;

        if (extendedMetadata != null) {
            metadata = extendedMetadata.clone();
        } else {
            metadata = new ExtendedMetadata();
        }

        String entityBaseURL = getEntityBaseURL();
        String entityAlias = getEntityAlias();

        metadata.setIdpDiscoveryEnabled(isIncludeDiscovery());

        if (isIncludeDiscovery()) {
            metadata.setIdpDiscoveryURL(getDiscoveryURL(entityBaseURL, entityAlias));
            metadata.setIdpDiscoveryResponseURL(getDiscoveryResponseURL(entityBaseURL, entityAlias));
        } else {
            metadata.setIdpDiscoveryURL(null);
            metadata.setIdpDiscoveryResponseURL(null);
        }

        metadata.setEncryptionKey(encryptionKey);
        metadata.setSigningKey(signingKey);
        metadata.setAlias(entityAlias);
        metadata.setTlsKey(tlsKey);
        metadata.setLocal(true);

        return metadata;

    }

    @Override
    protected KeyInfo generateKeyInfoForCredential(Credential credential) {
        try {
            NamedKeyInfoGeneratorManager manager = Configuration.getGlobalSecurityConfiguration().getKeyInfoGeneratorManager();
            SecurityHelper.getKeyInfoGenerator(credential, null, getKeyInfoGeneratorName());
            KeyInfoGeneratorFactory factory = manager.getDefaultManager().getFactory(credential);
            return factory.newInstance().generate(credential);
        } catch (org.opensaml.xml.security.SecurityException e) {
            LOGGER.error("Can't obtain key from the keystore or generate key info: " + encryptionKey, e);
            throw new SAMLRuntimeException("Can't obtain key from keystore or generate key info", e);
        }
    }

    @Override
    protected SPSSODescriptor buildSPSSODescriptor(String entityBaseURL, String entityAlias, boolean requestSigned, boolean wantAssertionSigned, Collection<String> includedNameID) {

        SAMLObjectBuilder<SPSSODescriptor> builder = (SAMLObjectBuilder<SPSSODescriptor>) getBuilderFactory().getBuilder(SPSSODescriptor.DEFAULT_ELEMENT_NAME);
        SPSSODescriptor spDescriptor = builder.buildObject();
        spDescriptor.setAuthnRequestsSigned(requestSigned);
        spDescriptor.setWantAssertionsSigned(wantAssertionSigned);
        spDescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);

        // Name ID
        spDescriptor.getNameIDFormats().addAll(getNameIDFormat(includedNameID));

        // Populate endpoints
        int index = 0;

        // Resolve alases
        Collection<String> bindingsSSO = mapAliases(getBindingsSSO());
        Collection<String> bindingsSLO = mapAliases(getBindingsSLO());
        Collection<String> bindingsHoKSSO = mapAliases(getBindingsHoKSSO());

        // Assertion consumer MUST NOT be used with HTTP Redirect, Profiles 424, same applies to HoK profile
        for (String binding : bindingsSSO) {
            if (binding.equals(SAMLConstants.SAML2_ARTIFACT_BINDING_URI)) {
                spDescriptor.getAssertionConsumerServices().add(getAssertionConsumerService(entityBaseURL, entityAlias, assertionConsumerIndex == index, index++, getSAMLWebSSOProcessingFilterPath(), SAMLConstants.SAML2_ARTIFACT_BINDING_URI));
            }
            if (binding.equals(SAMLConstants.SAML2_POST_BINDING_URI)) {
                spDescriptor.getAssertionConsumerServices().add(getAssertionConsumerService(entityBaseURL, entityAlias, assertionConsumerIndex == index, index++, getSAMLWebSSOProcessingFilterPath(), SAMLConstants.SAML2_POST_BINDING_URI));
            }
            if (binding.equals(SAMLConstants.SAML2_PAOS_BINDING_URI)) {
                spDescriptor.getAssertionConsumerServices().add(getAssertionConsumerService(entityBaseURL, entityAlias, assertionConsumerIndex == index, index++, getSAMLWebSSOProcessingFilterPath(), SAMLConstants.SAML2_PAOS_BINDING_URI));
            }
        }

        for (String binding : bindingsHoKSSO) {
            if (binding.equals(SAMLConstants.SAML2_ARTIFACT_BINDING_URI)) {
                spDescriptor.getAssertionConsumerServices().add(getHoKAssertionConsumerService(entityBaseURL, entityAlias, assertionConsumerIndex == index, index++, getSAMLWebSSOHoKProcessingFilterPath(), SAMLConstants.SAML2_ARTIFACT_BINDING_URI));
            }
            if (binding.equals(SAMLConstants.SAML2_POST_BINDING_URI)) {
                spDescriptor.getAssertionConsumerServices().add(getHoKAssertionConsumerService(entityBaseURL, entityAlias, assertionConsumerIndex == index, index++, getSAMLWebSSOHoKProcessingFilterPath(), SAMLConstants.SAML2_POST_BINDING_URI));
            }
        }

        for (String binding : bindingsSLO) {
            if (binding.equals(SAMLConstants.SAML2_POST_BINDING_URI)) {
                spDescriptor.getSingleLogoutServices().add(getSingleLogoutService(entityBaseURL, entityAlias, SAMLConstants.SAML2_POST_BINDING_URI));
            }
            if (binding.equals(SAMLConstants.SAML2_REDIRECT_BINDING_URI)) {
                spDescriptor.getSingleLogoutServices().add(getSingleLogoutService(entityBaseURL, entityAlias, SAMLConstants.SAML2_REDIRECT_BINDING_URI));
            }
            if (binding.equals(SAMLConstants.SAML2_SOAP11_BINDING_URI)) {
                spDescriptor.getSingleLogoutServices().add(getSingleLogoutService(entityBaseURL, entityAlias, SAMLConstants.SAML2_SOAP11_BINDING_URI));
            }
        }

        // Build extensions
        Extensions extensions = buildExtensions(entityBaseURL, entityAlias);
        if (extensions != null) {
            spDescriptor.setExtensions(extensions);
        }

        // Generate key info
        if (signingKey != null) {
            spDescriptor.getKeyDescriptors().add(getKeyDescriptor(UsageType.SIGNING, getServerKeyInfo(signingKey)));
        }
        if (encryptionKey != null) {
            spDescriptor.getKeyDescriptors().add(getKeyDescriptor(UsageType.ENCRYPTION, getServerKeyInfo(encryptionKey)));
        }

        // Include TLS key with unspecified usage in case it differs from the singing and encryption keys
        if (tlsKey != null && !(tlsKey.equals(encryptionKey)) && !(tlsKey.equals(signingKey))) {
            spDescriptor.getKeyDescriptors().add(getKeyDescriptor(UsageType.UNSPECIFIED, getServerKeyInfo(tlsKey)));
        }

        return spDescriptor;

    }

    /**
     * Method iterates all values in the input, for each tries to resolve correct alias. When alias value is found,
     * it is entered into the return collection, otherwise warning is logged. Values are returned in order of input
     * with all duplicities removed.
     *
     * @param values input collection
     * @return result with resolved aliases
     */
    @Override
    protected Collection<String> mapAliases(Collection<String> values) {
        LinkedHashSet<String> result = new LinkedHashSet<>();
        for (String value : values) {
            String alias = aliases.get(value);
            if (alias != null) {
                result.add(alias);
            } else {
                LOGGER.warn("Unsupported value " + value + " found");
            }
        }
        return result;
    }

    @Override
    protected Extensions buildExtensions(String entityBaseURL, String entityAlias) {

        boolean include = false;
        Extensions extensions = new ExtensionsBuilder().buildObject();

        // Add discovery
        if (isIncludeDiscoveryExtension()) {
            DiscoveryResponse discoveryService = getDiscoveryService(entityBaseURL, entityAlias);
            extensions.getUnknownXMLObjects().add(discoveryService);
            include = true;
        }

        if (include) {
            return extensions;
        } else {
            return null;
        }

    }

    @Override
    protected KeyDescriptor getKeyDescriptor(UsageType type, KeyInfo key) {
        SAMLObjectBuilder<KeyDescriptor> builder = (SAMLObjectBuilder<KeyDescriptor>) Configuration.getBuilderFactory().getBuilder(KeyDescriptor.DEFAULT_ELEMENT_NAME);
        KeyDescriptor descriptor = builder.buildObject();
        descriptor.setUse(type);
        descriptor.setKeyInfo(key);
        return descriptor;
    }

    @Override
    protected Collection<NameIDFormat> getNameIDFormat(Collection<String> includedNameID) {

        // Resolve alases
        Collection<String> aliases = mapAliases(includedNameID);
        Collection<NameIDFormat> formats = new LinkedList<>();
        SAMLObjectBuilder<NameIDFormat> builder = (SAMLObjectBuilder<NameIDFormat>) getBuilderFactory().getBuilder(NameIDFormat.DEFAULT_ELEMENT_NAME);

        // Populate nameIDs
        for (String nameIDValue : aliases) {

            if (nameIDValue.equals(NameIDType.EMAIL)) {
                NameIDFormat nameID = builder.buildObject();
                nameID.setFormat(NameIDType.EMAIL);
                formats.add(nameID);
            }

            if (nameIDValue.equals(NameIDType.TRANSIENT)) {
                NameIDFormat nameID = builder.buildObject();
                nameID.setFormat(NameIDType.TRANSIENT);
                formats.add(nameID);
            }

            if (nameIDValue.equals(NameIDType.PERSISTENT)) {
                NameIDFormat nameID = builder.buildObject();
                nameID.setFormat(NameIDType.PERSISTENT);
                formats.add(nameID);
            }

            if (nameIDValue.equals(NameIDType.UNSPECIFIED)) {
                NameIDFormat nameID = builder.buildObject();
                nameID.setFormat(NameIDType.UNSPECIFIED);
                formats.add(nameID);
            }

            if (nameIDValue.equals(NameIDType.X509_SUBJECT)) {
                NameIDFormat nameID = builder.buildObject();
                nameID.setFormat(NameIDType.X509_SUBJECT);
                formats.add(nameID);
            }

        }

        return formats;

    }

    @Override
    protected AssertionConsumerService getAssertionConsumerService(String entityBaseURL, String entityAlias, boolean isDefault, int index, String filterURL, String binding) {
        SAMLObjectBuilder<AssertionConsumerService> builder = (SAMLObjectBuilder<AssertionConsumerService>) getBuilderFactory().getBuilder(AssertionConsumerService.DEFAULT_ELEMENT_NAME);
        AssertionConsumerService consumer = builder.buildObject();
        consumer.setLocation(getServerURL(entityBaseURL, entityAlias, filterURL));
        consumer.setBinding(binding);
        if (isDefault) {
            consumer.setIsDefault(true);
        }
        consumer.setIndex(index);
        return consumer;
    }

    @Override
    protected AssertionConsumerService getHoKAssertionConsumerService(String entityBaseURL, String entityAlias, boolean isDefault, int index, String filterURL, String binding) {
        AssertionConsumerService hokAssertionConsumer = getAssertionConsumerService(entityBaseURL, entityAlias, isDefault, index, filterURL, org.springframework.security.saml.SAMLConstants.SAML2_HOK_WEBSSO_PROFILE_URI);
        QName consumerName = new QName(org.springframework.security.saml.SAMLConstants.SAML2_HOK_WEBSSO_PROFILE_URI, AuthnRequest.PROTOCOL_BINDING_ATTRIB_NAME, "hoksso");
        hokAssertionConsumer.getUnknownAttributes().put(consumerName, binding);
        return hokAssertionConsumer;
    }

    @Override
    protected DiscoveryResponse getDiscoveryService(String entityBaseURL, String entityAlias) {
        SAMLObjectBuilder<DiscoveryResponse> builder = (SAMLObjectBuilder<DiscoveryResponse>) getBuilderFactory().getBuilder(DiscoveryResponse.DEFAULT_ELEMENT_NAME);
        DiscoveryResponse discovery = builder.buildObject(DiscoveryResponse.DEFAULT_ELEMENT_NAME);
        discovery.setBinding(DiscoveryResponse.IDP_DISCO_NS);
        discovery.setLocation(getDiscoveryResponseURL(entityBaseURL, entityAlias));
        return discovery;
    }

    @Override
    protected SingleLogoutService getSingleLogoutService(String entityBaseURL, String entityAlias, String binding) {
        SAMLObjectBuilder<SingleLogoutService> builder = (SAMLObjectBuilder<SingleLogoutService>) getBuilderFactory().getBuilder(SingleLogoutService.DEFAULT_ELEMENT_NAME);
        SingleLogoutService logoutService = builder.buildObject();
        logoutService.setLocation(getServerURL(entityBaseURL, entityAlias, getSAMLLogoutFilterPath()));
        logoutService.setBinding(binding);
        return logoutService;
    }

    /**
     * Creates URL at which the local server is capable of accepting incoming SAML messages.
     *
     * @param entityBaseURL entity ID
     * @param processingURL local context at which processing filter is waiting
     * @return URL of local server
     */
    private String getServerURL(String entityBaseURL, String entityAlias, String processingURL) {

        return getServerURL(entityBaseURL, entityAlias, processingURL, null);

    }

    /**
     * Creates URL at which the local server is capable of accepting incoming SAML messages.
     *
     * @param entityBaseURL entity ID
     * @param processingURL local context at which processing filter is waiting
     * @param parameters    key - value pairs to be included as query part of the generated url, can be null
     * @return URL of local server
     */
    private String getServerURL(String entityBaseURL, String entityAlias, String processingURL, Map<String, String> parameters) {

        StringBuffer result = new StringBuffer();
        result.append(entityBaseURL);
        if (!processingURL.startsWith("/")) {
            result.append("/");
        }
        result.append(processingURL);
        if (!processingURL.endsWith("/")) {
            result.append("/");
        }
        if (entityAlias != null) {
            result.append("alias/");
            result.append(entityAlias);
        }

        String resultString = result.toString();

        if (parameters == null || parameters.size() == 0) {

            return resultString;

        } else {

            // Add parameters
            URLBuilder returnUrlBuilder = new URLBuilder(resultString);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                returnUrlBuilder.getQueryParams().add(new Pair<>(entry.getKey(), entry.getValue()));
            }
            return returnUrlBuilder.buildURL();

        }

    }

    private String getSAMLWebSSOProcessingFilterPath() {
        if (getSamlWebSSOFilter() != null) {
            return getSamlWebSSOFilter().getFilterProcessesUrl();
        } else {
            return SAMLProcessingFilter.FILTER_URL;
        }
    }

    private String getSAMLWebSSOHoKProcessingFilterPath() {
        if (getSamlWebSSOHoKFilter() != null) {
            return getSamlWebSSOHoKFilter().getFilterProcessesUrl();
        } else {
            return SAMLWebSSOHoKProcessingFilter.WEBSSO_HOK_URL;
        }
    }

    private String getSAMLEntryPointPath() {
        if (getSamlEntryPoint() != null) {
            return getSamlEntryPoint().getFilterProcessesUrl();
        } else {
            return SAMLEntryPoint.FILTER_URL;
        }
    }

    private String getSAMLDiscoveryPath() {
        if (getSamlDiscovery() != null) {
            return getSamlDiscovery().getFilterProcessesUrl();
        } else {
            return SAMLDiscovery.FILTER_URL;
        }
    }

    private String getSAMLLogoutFilterPath() {
        if (getSamlLogoutProcessingFilter() != null) {
            return getSamlLogoutProcessingFilter().getFilterProcessesUrl();
        } else {
            return SAMLLogoutProcessingFilter.FILTER_URL;
        }
    }

    @Override
    @Autowired(required = false)
    @Qualifier("samlWebSSOProcessingFilter")
    public void setSamlWebSSOFilter(SAMLProcessingFilter samlWebSSOFilter) {
        this.samlWebSSOFilter = samlWebSSOFilter;
    }

    @Override
    @Autowired(required = false)
    @Qualifier("samlWebSSOHoKProcessingFilter")
    public void setSamlWebSSOHoKFilter(SAMLWebSSOHoKProcessingFilter samlWebSSOHoKFilter) {
        this.samlWebSSOHoKFilter = samlWebSSOHoKFilter;
    }

    @Override
    @Autowired(required = false)
    public void setSamlLogoutProcessingFilter(SAMLLogoutProcessingFilter samlLogoutProcessingFilter) {
        this.samlLogoutProcessingFilter = samlLogoutProcessingFilter;
    }

    @Override
    @Autowired(required = false)
    public void setSamlEntryPoint(SAMLEntryPoint samlEntryPoint) {
        this.samlEntryPoint = samlEntryPoint;
    }

    /**
     * Signs the given SAML message if it a {@link org.opensaml.common.SignableSAMLObject} and this encoder has signing credentials.
     *
     * @param signableObject    object to sign
     * @param signingCredential credential to sign with
     * @throws org.opensaml.ws.message.encoder.MessageEncodingException
     *          thrown if there is a problem marshalling or signing the outbound message
     */
    @SuppressWarnings("unchecked")
    protected void signSAMLObject(SAMLObject signableObject, Credential signingCredential) throws MessageEncodingException {

        if (signableObject instanceof SignableSAMLObject && signingCredential != null) {
            SignableSAMLObject signableMessage = (SignableSAMLObject) signableObject;

            XMLObjectBuilder<Signature> signatureBuilder = Configuration.getBuilderFactory().getBuilder(
                    Signature.DEFAULT_ELEMENT_NAME);
            Signature signature = signatureBuilder.buildObject(Signature.DEFAULT_ELEMENT_NAME);

            signature.setSigningCredential(signingCredential);
            try {
                SecurityHelper.prepareSignatureParams(signature, signingCredential, null, getKeyInfoGeneratorName());
            } catch (org.opensaml.xml.security.SecurityException e) {
                throw new MessageEncodingException("Error preparing signature for signing", e);
            }

            signableMessage.setSignature(signature);
            marshallSAMLObject(signableMessage);

            try {
                Signer.signObject(signature);
            } catch (SignatureException e) {
                LOGGER.error("Unable to sign protocol message", e);
                throw new MessageEncodingException("Unable to sign protocol message", e);
            }

        }
    }

    private void marshallSAMLObject(SAMLObject samlObject) throws MessageEncodingException {
        try {
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(samlObject);
            if (marshaller == null) {
                throw new MessageEncodingException("No marshaller registered for "
                        + samlObject.getElementQName() + ", unable to marshall in preperation for signing");
            }
            marshaller.marshall(samlObject);
        } catch (MarshallingException e) {
            LOGGER.error("Unable to marshall protocol message in preparation for signing", e);
            throw new MessageEncodingException("Unable to marshall protocol message in preparation for signing", e);
        }
    }

    /**
     * Name of the KeyInfoGenerator registered at default KeyInfoGeneratorManager.
     *
     * @return key info generator name
     * @see Configuration#getGlobalSecurityConfiguration()
     * @see org.opensaml.xml.security.SecurityConfiguration#getKeyInfoGeneratorManager()
     */
    protected String getKeyInfoGeneratorName() {
        return org.springframework.security.saml.SAMLConstants.SAML_METADATA_KEY_INFO_GENERATOR;
    }

    @Override
    public boolean isRequestSigned() {
        return requestSigned;
    }

    @Override
    public void setRequestSigned(boolean requestSigned) {
        this.requestSigned = requestSigned;
    }

    @Override
    public boolean isWantAssertionSigned() {
        return wantAssertionSigned;
    }

    @Override
    public void setWantAssertionSigned(boolean wantAssertionSigned) {
        this.wantAssertionSigned = wantAssertionSigned;
    }

    public boolean isSignMetadata() {
        return signMetadata;
    }

    public void setSignMetadata(boolean signMetadata) {
        this.signMetadata = signMetadata;
    }

    @Override
    public Collection<String> getNameID() {
        return nameID == null ? DEFAULT_NAME_ID : nameID;
    }

    @Override
    public void setNameID(Collection<String> nameID) {
        this.nameID = nameID;
    }

    @Override
    public String getEntityBaseURL() {
        return entityBaseURL;
    }

    @Override
    public String getEntityAlias() {
        return entityAlias;
    }

    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    @Override
    public void setEntityBaseURL(String entityBaseURL) {
        this.entityBaseURL = entityBaseURL;
    }

    @Override
    @Autowired
    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getEntityId() {
        return entityId;
    }

    public String getTlsKey() {
        return tlsKey;
    }

    public void setTlsKey(String tlsKey) {
        this.tlsKey = tlsKey;
    }

    @Override
    public Collection<String> getBindingsSSO() {
        return bindingsSSO;
    }

    @Override
    public void setBindingsSSO(Collection<String> bindingsSSO) {
        if (bindingsSSO == null) {
            this.bindingsSSO = Collections.emptyList();
        } else {
            this.bindingsSSO = bindingsSSO;
        }
    }

    @Override
    public Collection<String> getBindingsSLO() {
        return bindingsSLO;
    }

    @Override
    public void setBindingsSLO(Collection<String> bindingsSLO) {
        if (bindingsSLO == null) {
            this.bindingsSLO = Collections.emptyList();
        } else {
            this.bindingsSLO = bindingsSLO;
        }
    }

    @Override
    public Collection<String> getBindingsHoKSSO() {
        return bindingsHoKSSO;
    }

    @Override
    public void setBindingsHoKSSO(Collection<String> bindingsHoKSSO) {
        if (bindingsHoKSSO == null) {
            this.bindingsHoKSSO = Collections.emptyList();
        } else {
            this.bindingsHoKSSO = bindingsHoKSSO;
        }
    }

    @Override
    public boolean isIncludeDiscoveryExtension() {
        return includeDiscoveryExtension;
    }

    /**
     * When true discovery profile extension metadata pointing to the default SAMLEntryPoint will be generated and stored
     * in the generated metadata document.
     *
     * @param includeDiscoveryExtension flag indicating whether IDP discovery should be enabled
     */
    @Override
    public void setIncludeDiscoveryExtension(boolean includeDiscoveryExtension) {
        this.includeDiscoveryExtension = includeDiscoveryExtension;
    }

    /**
     * When true system will also automatically generate discoveryRequest and discoveryResponse addresses or
     * use values provided as customDiscoveryUrl and customDiscoveryResponseUrl and store them to the extended metadata.
     *
     * @param includeDiscovery true when user should be redirected to discovery service during SSO initialization
     */
    public void setIncludeDiscovery(boolean includeDiscovery) {
        this.includeDiscovery = includeDiscovery;
    }

    /**
     * True when IDP discovery is enabled either on local property includeDiscovery or property idpDiscoveryEnabled
     * in the extended metadata.
     *
     * @return true when discovery is enabled
     */
    @Override
    public boolean isIncludeDiscovery() {
        return includeDiscovery || (extendedMetadata != null && extendedMetadata.isIdpDiscoveryEnabled());
    }

    @Override
    public int getAssertionConsumerIndex() {
        return assertionConsumerIndex;
    }

    /**
     * Generated assertion consumer service with the index equaling set value will be marked as default.
     *
     * @param assertionConsumerIndex assertion consumer index of service to mark as default
     */
    @Override
    public void setAssertionConsumerIndex(int assertionConsumerIndex) {
        this.assertionConsumerIndex = assertionConsumerIndex;
    }

    /**
     * Custom value of IDP Discovery request URL to be included in the extended metadata. Only used when
     * includeDiscovery is set to true.
     *
     * @param customDiscoveryURL custom discovery request URL
     */
    public void setCustomDiscoveryURL(String customDiscoveryURL) {
        this.customDiscoveryURL = customDiscoveryURL;
    }

    public String getCustomDiscoveryURL() {
        return customDiscoveryURL;
    }

    /**
     * Custom value of IDP Discovery response URL to be included in the SP metadata as extension and in extended
     * metadata. Only used when includeDiscovery is set to true.
     *
     * @param customDiscoveryResponseURL custom discovery response URL
     */
    public void setCustomDiscoveryResponseURL(String customDiscoveryResponseURL) {
        this.customDiscoveryResponseURL = customDiscoveryResponseURL;
    }

    public String getCustomDiscoveryResponseURL() {
        return customDiscoveryResponseURL;
    }

    /**
     * Provides set discovery request url or generates a default when none was provided. Primarily value set on extenedMetadata property
     *  idpDiscoveryURL is used, when empty local property customDiscoveryURL is used, when empty URL is automatically generated.
     *
     * @return URL to use for IDP discovery request
     */
    @Override
    protected String getDiscoveryURL(String entityBaseURL, String entityAlias) {
        if (extendedMetadata != null && extendedMetadata.getIdpDiscoveryURL() != null && extendedMetadata.getIdpDiscoveryURL().length() > 0) {
            return extendedMetadata.getIdpDiscoveryURL();
        } else if (customDiscoveryURL != null && customDiscoveryURL.length() > 0) {
            return customDiscoveryURL;
        } else {
            return getServerURL(entityBaseURL, entityAlias, getSAMLDiscoveryPath());
        }
    }

    /**
     * Provides set discovery response url or generates a default when none was provided. Primarily value set on extenedMetadata property
     *  idpDiscoveryResponseURL is used, when empty local property customDiscoveryResponseURL is used, when empty URL is automatically generated.
     *
     * @return URL to use for IDP discovery response
     */
    @Override
    protected String getDiscoveryResponseURL(String entityBaseURL, String entityAlias) {
        if (extendedMetadata != null && extendedMetadata.getIdpDiscoveryResponseURL() != null && extendedMetadata.getIdpDiscoveryResponseURL().length() > 0) {
            return extendedMetadata.getIdpDiscoveryResponseURL();
        } else if (customDiscoveryResponseURL != null && customDiscoveryResponseURL.length() > 0) {
            return customDiscoveryResponseURL;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put(SAMLEntryPoint.DISCOVERY_RESPONSE_PARAMETER, "true");
            return getServerURL(entityBaseURL, entityAlias, getSAMLEntryPointPath(), params);
        }
    }

    @Override
    public ExtendedMetadata getExtendedMetadata() {
        return extendedMetadata;
    }

    /**
     * Default value for generation of extended metadata. Value is cloned upon each request to generate
     * new ExtendedMetadata object.
     *
     * @param extendedMetadata default extended metadata or null
     */
    @Override
    public void setExtendedMetadata(ExtendedMetadata extendedMetadata) {
        this.extendedMetadata = extendedMetadata;
    }

    public XMLObjectBuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    public void setBuilderFactory(XMLObjectBuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    /**
     * Filters for loading of paths.
     */
    public SAMLProcessingFilter getSamlWebSSOFilter() {
        return samlWebSSOFilter;
    }

    public SAMLWebSSOHoKProcessingFilter getSamlWebSSOHoKFilter() {
        return samlWebSSOHoKFilter;
    }

    public SAMLLogoutProcessingFilter getSamlLogoutProcessingFilter() {
        return samlLogoutProcessingFilter;
    }

    public SAMLEntryPoint getSamlEntryPoint() {
        return samlEntryPoint;
    }

    public SAMLDiscovery getSamlDiscovery() {
        return samlDiscovery;
    }

    public void setSamlDiscovery(SAMLDiscovery samlDiscovery) {
        this.samlDiscovery = samlDiscovery;
    }
}
// CHECKSTYLE:ON
