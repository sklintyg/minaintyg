#!/bin/bash
# Assign backing service addresses from the outer environment

#export REDIS_PASSWORD=${REDIS_PASSWORD:-redis}
export REDIS_PORT=$REDIS_SERVICE_PORT
export REDIS_HOST=$REDIS_SERVICE_HOST

# dev profile is default for pipeline
SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod,caching-enabled,redis-sentinel}
#SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-qa,caching-enabled,redis-sentinel}

export CATALINA_OPTS_APPEND="\
-Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
-Dconfig.folder=/opt/$APP_NAME/config \
-Dconfig.file=/opt/$APP_NAME/config/minaintyg.properties \
-Dlogback.file=/opt/$APP_NAME/config/minaintyg-logback.xml \
-Dcertificate.folder=/opt/$APP_NAME/certifikat \
-Dcredentials.file=/opt/$APP_NAME/env/secret-env.properties \
-Dresources.folder=/tmp/resources \
-Dfile.encoding=UTF-8 \
-Djavax.xml.transform.TransformerFactory=net.sf.saxon.TransformerFactoryImpl"
