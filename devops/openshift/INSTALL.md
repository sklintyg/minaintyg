# OPENSHIFT INSTALLATION GUIDE

Installation of web application minaintyg on openshift.

## 1 Pre-Installation Requirements

The following prerequisites and requirements must be satisfied in order for the minaintyg to install successfully.

### 1.1 Backing Service Dependencies

The application has the following external services: 

Provided by operations (execution environment):

* Redis Sentinel (provided)
* Redis Server (provided)
* Inera Certificate Service (Intygstjänst)

Provided elsewhere:

* Inera Service Platform (NTjP)

For all backing services their actual addresses and user accounts have to be known prior to start the installation procedure.  

### 1.3 Integration / Firewall

Mina Intyg communicates in/out with the Inera Service Platform and thus needs firewall rules for that access.

### 1.4 Certificates

Mina Intyg needs certificates, keystores and truststores for communicating over Tjänsteplattformen. The operations provider is responsible for installing these certificates in the appropriate OpenShift "secret", see detailed instructions in the OpenShift section.

### 1.5 Message Queues

Not applicable for Mina Intyg

### 1.5 Database

Not applicable for Mina Intyg

### 1.6 Access to Software Artifacts

Software artifacts are located at, and downloaded from:

* From Installing Client - https://build-inera.nordicmedtest.se/nexus/repository/releases/se/inera/intyg/minaintyg/
* From OpenShift Cluster - docker.drift.inera.se/intyg/

### 1.7 Access to OpenShift Cluster

The OpenShift user account must have the right permissions to process, create, delete and replace objects, and most certainly a VPN account and connection is required in order to access the OpenShift Cluster.

### 1.8 Client Software Tools

The installation client must have **git** and **oc** (OpenShift Client) installed and if a database schema migration is required then **java** (Java 8) and **tar** is required in order to execute the migration tool (liquibase runner).

Must have:

* git
* oc
* VPN Client (such as Cisco Any Connect) 

To run database migration tool:

* java
* tar

# 2. Installation Procedure

### 2.1 Installation Checklist

1. All Pre-Installation Requirements are fulfilled, se above
2. Check if a database migration is required
3. Ensure that the env secret and secret-envvar are up to date
4. Ensure that the configmap and configmap-envvar are up to date
5. Check that deployment works as expected 
6. Fine-tune memory settings for container and java process
7. Setup policies for number of replicas, auto-scaling and rolling upgrade strategy


### 2.2 Migrate Database Schema

Not applicable for Mina Intyg

### 2.3 Get Source for Configuration


##### 2.3.1 Clone the repository

Clone repository and switch to the release branch specified in the release notes. Substitute `<name>` below with the actual release name, such as `2018-4`.
    
    > git clone https://github.com/sklintyg/minaintyg.git
    > git checkout release/<name>
    > cd devops/openshift
    
Note that we strongly recommend using a git account that has read-only (e.g. public) access to the repo.
    
##### 2.3.2 Log-in into the cluster

Use oc to login and select the actual project, e.g:

    > oc login https://path.to.cluster
    username: ******
    password: ******
    > oc project <name>

### 2.3.3 Ensure that the latest deployment template is installed

Down load and install the latest _[deploytemplate-webapp.yaml](https://github.com/sklintyg/tools/blob/develop/devops/openshift/deploytemplate-webapp.yaml)_

Syntax to create or replace the template: 

	> oc [ create | replace ] -f deploytemplate-webapp.yaml

### 2.4 Update configuration placeholders

For security reasons, no secret properties or configuration may be checked into git. Thus, a number of placeholders needs to be replaced prior to creating or updating secrets and/or config maps.

Open _&lt;env>/secret-vars.yaml_ and replace `<value>` with expected values:

    NTJP_WS_CERTIFICATE_PASSWORD: <value>
    NTJP_WS_KEY_MANAGER_PASSWORD: <value>
    NTJP_WS_TRUSTSTORE_PASSWORD: <value>
    MVK_TRUSTSTORE_PASSWORD: <value>
    MVK_KEYSTORE_PASSWORD: <value>
    MVK_KEYSTORE_KEYPASSWORD: <value>
    FK_SAML_KEYSTORE_PASSWORD: <value>

Open _&lt;env>/configmap-vars.yaml_ and replace `<value>` with expected values. You may also need to update name of keystore/truststore files as well as their type (JKS or PKCS12)

    REDIS_HOST: <value>
    REDIS_PORT: <value>
    REDIS_SENTINEL_MASTER_NAME: <value>
    CERTIFICATE_BASEURL: http://intygstjanst-<env>:8080
    NTJP_WS_CERTIFICATE_TYPE: JKS
    NTJP_WS_TRUSTSTORE_TYPE: JKS
   
Note: Other properties might be used to define a `<value>`. As an example is the path to certificates indicated by the `CERTIFICATE_FOLDER` property and the truststore file might be defined like:
 
	NTJP_WS_TRUSTSTORE_FILE: ${CERTIFICATE_FOLDER}/truststore.jks
        
##### 2.4.1 Redis Sentinel Configuration

Redis sentinel needs at least three URL:s passed in order to work correctly. These are specified in the _redis.host_ and _redis.port_ properties respectively:

    redis.host=host1;host2;host3
    redis.port=26379;26379;26379
    
### 2.5 Prepare Certificates

The `<env>` placeholder shall be substituted with the actual name of the environment such as `stage` or `prod`.

Staging and Prod certificates are **never** committed to git. However, you may temporarily copy them to _&lt;env>/certifikat_ in order to install/update them. Typically, certificates have probably been installed separately. The important thing is that the deployment template **requires** a secret named: `minaintyg-<env>-certifikat` to be available in the OpenShift project. It will be mounted to _/opt/minaintyg-<env>/certifikat_ in the container file system.


### 2.6 Creating Config and Secrets
If you've finished updating the files above, it's now time to use **oc** to install them into OpenShift.
All commands must be executed from the same folder as this markdown file, i.e. _/minaintyg/devops/openshift_ 

Note: To delete an existing ConfigMap or Secret use the following syntax:

	> oc delete [ configmap | secret ] <name>

##### 2.6.1 Create environment variables for Secret and ConfigMap
From YAML-files, their names are hard-coded into the respective file

    > oc create -f <env>/configmap-vars.yaml
    > oc create -f <env>/secret-vars.yaml
    
##### 2.6.2 Create Secret and ConfigMap
Creates config map and secret from the contents of the _&lt;env>/env_ and _&lt;env>/config_ folders:

    > oc create configmap minaintyg-<env>-config --from-file=<env>/config/
    > oc create secret generic minaintyg-<env>-env --from-file=<env>/env/ --type=Opaque
    
##### 2.6.3 Create Secret with Certificates
If this hasn't been done previously, you may **temporarily** copy keystores into the _&lt;env>/certifikat_ folder and then install them into OpenShift using this command:

    > oc create secret generic minaintyg-<env>-certifikat --from-file=<env>/certifikat/ --type=Opaque

### 2.7 Deploy
We're all set for deploying the application. As stated in the pre-reqs, the "deploytemplate-webapp" must be installed in the OpenShift project.

**NOTE 1** You need to reference the correct docker image from the Nexus!! You must replace \<replaceme\> with a correct path to the image to deploy!!

Run the following command to create a deployment:

    > oc process deploytemplate-webapp \
        -p APP_NAME=minaintyg-<env> \
        -p IMAGE=docker.drift.inera.se/intyg/minaintyg-test:<version> \
        -p STAGE=<env> 
        -p DATABASE_NAME=notused \
        -p HEALTH_URI=/ \
        -o yaml | oc apply -f -
        
        
Alternatively, it's possible to use the deploytemplate-webapp file locally:

    > oc process -f deploytemplate-webapp.yaml -p APP_NAME=minaintyg-<env> ...

### 2.8 Verify
The pod(s) running minaintyg should become available within a few minutes use **oc** or Web Console to checkout the logs for progress:

	> oc logs dc/minaintyg-<env>

### 2.9 Routes
To publish Mina Intyg externally a corresponding OCP route has to be created. 
The internal service address is _http://minaintyg-&lt;env>:8080_. The route should only accept `HTTPS` and is responsible of TLS termination.
