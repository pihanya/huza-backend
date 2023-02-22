#!/bin/bash

set -e

CURRENT_DIR=$PWD

if [[ $(basename $PWD) == "scripts" ]]; then
    PROJECT_DIR=$(dirname $PWD)
    SCRIPTS_DIR=$PWD
else
    PROJECT_DIR=$PWD
    SCRIPTS_DIR=$PWD/scripts
fi
source $SCRIPTS_DIR/.env

TEMP_DIR="$PROJECT_DIR/.temp"

DEPLOY_TEMP_DIR="$TEMP_DIR/huza-deploy"
mkdir -p $DEPLOY_TEMP_DIR

function on_exit {
    rm -rf $DEPLOY_TEMP_DIR
    cd $CURRENT_DIR
}
trap on_exit EXIT

PROJECT_VERSION=$(grep 'version=' "$PROJECT_DIR/gradle.properties" | sed 's/.*version=\(.*\)/\1/')

cd $PROJECT_DIR
$PROJECT_DIR/gradlew build
cd -

cat >$DEPLOY_TEMP_DIR/run.sh <<EOF
#!/usr/bin/bash
java -jar huza.jar \\
    --server.port=$HUZA_PORT \\
    --spring.profiles.active=dev \\
    --spring.datasource.url=jdbc:postgresql://pg:5432/studs \\
    --spring.datasource.username=$HELIOS_USER \\
    --spring.datasource.password= \\
    --spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
EOF
chmod +x $DEPLOY_TEMP_DIR/run.sh

cp -f "$PROJECT_DIR/build/huza-app/libs/huza-app-$PROJECT_VERSION.jar" $DEPLOY_TEMP_DIR/huza.jar

scp -r -P 2222 $DEPLOY_TEMP_DIR/. $HELIOS_USER@se.ifmo.ru:/home/studs/$HELIOS_USER/huza
