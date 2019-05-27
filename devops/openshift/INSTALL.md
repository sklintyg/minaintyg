# OPENSHIFT INSTALLATION GUIDE -- MI 2019-2


Installation of Web application Minaintyg (MI) on OpenShift.

## 1 Updates since 2019-1 (release notes)


### 1.1 New configuration properties

The following configuration properties have been added:

* `REFDATA_URL` -- Location of reference data, see below

### 1.2 Configuration of reference data

The main update is activation of the new reference data concept (master data for shared configurations). Refdata is provided as a JAR file and configured with the `REFDATA_URL` and `RESOURCES_FOLDER` parameters. Normally the default value of `RESOURCES_FOLDER` should be set to  `classpath:`. Three configuration updates is required in order to activate the new refdata:

1. Parameter `REFDATA_URL` shall be set to the actual location of the refdata JAR artefact.
2. Parameter `RESOURCES_FOLDER` or `-Dresources.folder=...` in `secret-env.sh` shall be set to `classpath:`. Though, it's recommended to remove this parameter from `secret-env.sh`. 
3. The old `resources.zip` must be removed in order to enable the `REFDATA_URL` setting. 

Latest builds of refdata can be downloaded from the Inera Nexus server. 

	https://build-inera.nordicmedtest.se/nexus/repository/releases/se/inera/intyg/refdata/refdata/1.0.0.<build-num>/refdata-1.0.0.<build-num>.jar


## 2 Pre-Installation Requirements

The following prerequisites and requirements must be satisfied in order for the minaintyg to install successfully.

### 2.1 Backing Service Dependencies

The application has the following external services: 

On premise (execution environment):

* Redis Sentinel
* Redis Server
* Inera Certificate Service (Intygstjänst, IT)

Provided elsewhere:

* Inera Service Platform (NTjP)

For all backing services their actual addresses and user accounts have to be known prior to start the installation procedure.  

### 2.2 Integration / Firewall

Mina Intyg communicates in/out with the Inera Service Platform and thus needs firewall rules for that access.

### 2.3 Certificates

Mina Intyg needs certificates, keystores and truststores for communicating over Tjänsteplattformen. The operations provider is responsible for installing these certificates in the appropriate OpenShift "secret", see detailed instructions in the OpenShift section.

### 2.4 Message Queues

Not applicable for Mina Intyg

### 2.5 Database

Not applicable for Mina Intyg

### 2.6 Access to Software Artifacts

Software artifacts are located at, and downloaded from:

* From Installing Client - [https://build-inera.nordicmedtest.se/nexus/repository/releases/se/inera/intyg/minaintyg/minaintyg/maven-metadata.xml](https://build-inera.nordicmedtest.se/nexus/repository/releases/se/inera/intyg/minaintyg/minaintyg/maven-metadata.xml)
* From OpenShift Cluster - docker.drift.inera.se/intyg/

### 2.7 Access to OpenShift Cluster

The OpenShift user account must haThe OpenShift user account must have the right permissions to process, create, delete and replace objects. A VPN account and connection is required in order to access the OpenShift Cluster.

### 2.8 Client Software Tools

The installation client must have **git** and **oc** (OpenShift Client) installed.
Must have:

* git
* oc
* VPN Client (such as Cisco Any Connect) 

### 2.9 Logstash filters (Inera Drift ELK stack)

The application logs are written to stdout/console. All pod output will be processed by logstash, where relevant data is extracted to fields. The resulting log-records (json) are sent to Elasticsearch for persistence. Kibana is used to filter, search and visualize the persisted log data.

The logstash filters and grok patterns need to be updated if any log formats are changed.
https://github.com/sklintyg/monitoring/tree/develop/logstash/

# 3 Installation Procedure

### 3.1 Installation Checklist

1. All Pre-Installation Requirements are fulfilled, se above
2. Check if the logstash filter need to be updated
3. Ensure that the secrets `minaintyg-env`, `minaintyg-certifikat` and `minaintyg-secret-envvar` are up to date
4. Ensure that the config maps `minaintyg-config` and `minaintyg-configmap-envvar` are up to date
5. Check that deployment works as expected
6. Fine-tune memory settings for container and java process
7. Setup policies for number of replicas, auto-scaling and rolling upgrade strategy


### 3.2 Migrate Database Schema

Not applicable for Mina Intyg

### 3.3 Get Source for Configuration


##### 3.3.1 Clone the repository

Clone repository and switch to the release branch specified in the release notes. Substitute `<name>` below with the actual release name, such as `2018-4`.
    
    > git clone https://github.com/sklintyg/minaintyg.git
    > git checkout release/2019-2
    > cd devops/openshift
    
Note that we strongly recommend using a git account that has read-only (e.g. public) access to the repo.
    
##### 3.3.2 Log-in into the cluster

Use **oc** to login and select the actual project, e.g:

    > oc login https://path.to.cluster
    username: ******
    password: ******
    > oc project <name>

##### 3.3.3 Ensure that the latest deployment template is installed

Down load and install the latest _[deploytemplate-webapp.yaml](https://github.com/sklintyg/tools/blob/develop/devops/openshift/deploytemplate-webapp.yaml)_. This needs to be updated regarding assigned computing resources, i.e. the requested and limited amount of CPU needs to be increased as well as the Java memory heap settings, see `JAVA_OPTS`.

Syntax to create or replace the template: 

	> oc [ create | replace ] -f deploytemplate-webapp.yaml

### 3.4 Update configuration placeholders

For security reasons, no secret properties or configuration may be checked into git. Thus, a number of placeholders needs to be replaced prior to creating or updating secrets and/or config maps.

Open _&lt;env>/secret-vars.yaml_ and replace `<value>` with expected values:

    REDIS_PASSWORD: "<password>"
    NTJP_WS_CERTIFICATE_PASSWORD: "<password>"
    NTJP_WS_KEY_MANAGER_PASSWORD: "<password>"
    NTJP_WS_TRUSTSTORE_PASSWORD: "<password>"
    FK_SAML_KEYSTORE_PASSWORD: "<password>"

Open _&lt;env>/configmap-vars.yaml_ and replace `<value>` with expected values. You may also need to update name of keystore/truststore files as well as their type (JKS or PKCS12)

    REDIS_HOST: "<hostname1[;hostname2;...]>"
    REDIS_PORT: "<port1[;port2;...]>"
    REDIS_SENTINEL_MASTER_NAME: "<name>"
    SPRING_PROFILES_ACTIVE: "prod,caching-enabled,redis-sentinel"
    CERTIFICATE_BASEURL: "http://intygstjanst:8080"
    NTJP_WS_CERTIFICATE_FILE: "${certificate.folder}/<file>"
    NTJP_WS_CERTIFICATE_TYPE: [ "JKS" | "PKCS12" ]
    NTJP_WS_TRUSTSTORE_FILE: "${certificate.folder}/<file>"
    NTJP_WS_TRUSTSTORE_TYPE: [ "JKS" | "PKCS12" ]
    FK_SAML_KEYSTORE_FILE: "file://${certificate.folder}/<file>"
    FK_SAML_KEYSTORE_ALIAS: "<alias>"
   
Note: Parameters shall follow the Java naming convention when used as in the value field, e.g. the path to certificates indicated by the `CERTIFICATE_FOLDER` property and the truststore file might be defined like:
 
	NTJP_WS_TRUSTSTORE_FILE: "${certificate.folder}/truststore.jks"
        
##### 3.4.1 Redis Sentinel Configuration

Redis sentinel requires at least three URL:s passed in order to work correctly. These are specified in the `REDIS_SERVICE_HOST` and `REDIS_SERVICE_PORT` parameters respectively:

    REDIS_HOST: "host1;host2;host3"
    REDIS_PORT: "26379;26379;26379"
    REDIS_SENTINEL_MASTER_NAME: "master"
    
### 3.5 Prepare Certificates

The `<env>` placeholder shall be substituted with the actual name of the environment such as `stage` or `prod`.

Staging and Prod certificates are **never** committed to git. However, you may temporarily copy them to _&lt;env>/certifikat_ in order to install/update them. Typically, certificates have probably been installed separately. The important thing is that the deployment template **requires** a secret named: `minaintyg-<env>-certifikat` to be available in the OpenShift project. It will be mounted to _/opt/minaintyg-<env>/certifikat_ in the container file system.


### 3.6 Creating Config and Secrets

If you've finished updating the files above, it's now time to use **oc** to install them into OpenShift.
All commands must be executed from the same folder as this markdown file, i.e. _/minaintyg/devops/openshift_ 

Note: To delete an existing ConfigMap or Secret use the following syntax:

	> oc delete [ configmap | secret ] <name>

##### 3.6.1 Create environment variables for Secret and ConfigMap
From YAML-files, their names are hard-coded into the respective file

    > oc create -f <env>/configmap-vars.yaml
    > oc create -f <env>/secret-vars.yaml
    
##### 3.6.2 Create Secret and ConfigMap
Creates config map and secret from the contents of the _&lt;env>/env_ and _&lt;env>/config_ folders:

    > oc create configmap minaintyg-<env>-config --from-file=<env>/config/
    > oc create secret generic minaintyg-<env>-env --from-file=<env>/env/ --type=Opaque
    
##### 3.6.3 Create Secret with Certificates
If this hasn't been done previously, you may **temporarily** copy keystores into the _&lt;env>/certifikat_ folder and then install them into OpenShift using this command:

    > oc create secret generic minaintyg-<env>-certifikat --from-file=<env>/certifikat/ --type=Opaque

### 3.7 Deploy

We're all set for deploying the application. As stated in the pre-reqs, the "deploytemplate-webapp" must be installed in the OpenShift project.

Note: You need to reference the correct docker image version from the Nexus! 

Run the following command to create a deployment:

    > oc process deploytemplate-webapp \
        -p APP_NAME=minaintyg \
        -p IMAGE=docker.drift.inera.se/intyg/minaintyg-test:<version> \
        -p STAGE=<env> 
        -p DATABASE_NAME=notused \
        -o yaml | oc apply -f -
        
        
Alternatively, it's possible to use the deploytemplate-webapp file locally:

    > oc process -f deploytemplate-webapp.yaml -p APP_NAME=minaintyg ...

##### 3.7.1 Computing resources
WC manages hundreds of concurrent user sessions.

Minimum production requirements are:

1. 2x CPU Cores
2. 2x GB RAM
3. 1x GB Java Heap Size (JAVA_OPTS=-Xmx1G)

### 3.8 Verify

The pod(s) running minaintyg should become available within a few minutes use **oc** or Web Console to checkout the logs for progress:

	> oc logs dc/minaintyg-<env>

### 3.9 Routes

To publish Mina Intyg externally a corresponding OCP route has to be created. The internal service listens on port 8080. The route should only accept `HTTPS` and is responsible of TLS termination.
