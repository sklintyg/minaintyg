#!/bin/bash

export CATALINA_OPTS_APPEND="\
-Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
-Dconfig.folder=/opt/$APP_NAME/config \
-Dconfig.file=/opt/$APP_NAME/config/minaintyg.properties \
-Dlogback.file=/opt/$APP_NAME/config/minaintyg-logback.xml \
-Dcertificate.folder=/opt/$APP_NAME/certifikat \
-Dcredentials.file=/opt/$APP_NAME/env/secret-env.properties \
-Dresources.folder=/tmp/resources \
-Dfile.encoding=UTF-8 \
-DbaseUrl=http://${APP_NAME}:8080 \
-Djavax.xml.transform.TransformerFactory=net.sf.saxon.TransformerFactoryImpl"
