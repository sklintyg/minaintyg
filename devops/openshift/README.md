## DINTYG setup

##### Create pipeline

    ~/intyg/oc/./oc process pipelinetemplate-test-webapp -p APP_NAME=minaintyg-test -p STAGE=test -p SECRET=nosecret -p TESTS="restAssuredTest,protractorTest" BACKING_SERVICES="intygstjanst-test" | ~/intyg/oc/./oc apply -f -
    
##### Create env var secret and config map

    oc create -f test/configmap-vars.yaml
    oc create -f test/secret-vars.yaml
    
##### Create file secret and config map

    oc create configmap "minaintyg-test-config" --from-file=test/config/
    oc create secret generic "minaintyg-test-env" --from-file=test/env/ --type=Opaque
    oc create secret generic "minaintyg-test-certifikat" --from-file=test/certifikat/ --type=Opaque



# DEMO setup
Nedanstående kräver att mysql, activemq och redis finns uppsatt i demointyg, samt att man är inloggad och har oc redo.

### 1. Config och Secrets
Gå till /devops/openshift i filsystemet.
       
##### 1.1 Create secrets and config maps
Hemliga resurser (keystores, lösenord till keystores etc.) kan behöva kopieras in för hand i respektive mapp och sedan plockas bort.

    oc create -f demo/configmap-vars.yaml
    oc create -f demo/secret-vars.yaml
    oc create configmap "minaintyg-demo-config" --from-file=demo/config/
    oc create secret generic "minaintyg-demo-env" --from-file=demo/env/ --type=Opaque
    oc create secret generic "minaintyg-demo-certifikat" --from-file=demo/certifikat/ --type=Opaque

### 2. Sätt upp deployment
Deployment skall triggas på varje dintyg.minaintyg-test-verified.
    
    oc process deploytemplate-webapp \
        -p APP_NAME=minaintyg-demo \
        -p IMAGE=docker-registry.default.svc:5000/dintyg/minaintyg-test-verified:latest \
        -p STAGE=demo -p DATABASE_NAME=minaintyg \
        -p HEALTH_URI=/ \
        -o yaml | oc apply -f -

Man vill eventuellt lägga till en trigger. Det kan ske direkt i "Edit YAML"

     triggers:
        - imageChangeParams:
            automatic: true
            containerNames:
              - minaintyg-demo
            from:
              kind: ImageStreamTag
              name: 'minaintyg-test-verified:latest'
              namespace: dintyg
          type: ImageChange
        - type: ConfigChange
