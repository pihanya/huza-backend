#!/bin/bash

set -e

CURRENT_DIR=$PWD

while [ $# -ge 1 ]; do
    case "$1" in
        --)
            shift
            break
            ;;
        -c|--cache)
            CACHE=1
            ;;
        --remote)
            REMOTE=1
            ;;
        -h)
            echo "Display some help"
            exit 0
            ;;
    esac

    shift
done


if [[ $(basename $PWD) == "scripts" ]]; then
    PROJECT_DIR=$(dirname $PWD)
    SCRIPTS_DIR=$PWD
else
    PROJECT_DIR=$PWD
    SCRIPTS_DIR=$PWD/scripts
fi
source $SCRIPTS_DIR/.env

STATIC_DIR="$PROJECT_DIR/huza-app/src/main/resources/static"

TEMP_DIR="$PROJECT_DIR/.temp"
FRONTEND_DIR="$TEMP_DIR/huza-frontend"
FRONTEND_BUILD_DIR="$FRONTEND_DIR/build"

function on_exit {
    # If not caching
    if [[ -z ${CACHE+x} ]]; then
        rm -rf $FRONTEND_DIR
    fi
    cd $CURRENT_DIR
}
trap on_exit EXIT

# If not caching or frontend directory does not exist
if [[ ( -z ${CACHE+x} ) || ( ! -d "$FRONTEND_DIR" ) ]]; then
    rm -rf $FRONTEND_DIR
    mkdir -p $FRONTEND_DIR
    git clone git@github.com:IgorAnohin/literate-guacamole.git $FRONTEND_DIR
fi

cd $FRONTEND_DIR
git pull --force origin maser

# If not caching or frontend directory does not exist
if [[ ! ( -z ${REMOTE+x} ) ]]; then
    sed \
        -i '' \
        "s#http://localhost:4242#http://$REMOTE_HOST:$HUZA_PORT#" \
        src/repository/api_paths.js
else
    sed \
        -i '' \
        "s#http://localhost:4242#http://localhost:$HUZA_PORT#" \
        src/repository/api_paths.js
fi

npm install

rm -rf $FRONTEND_BUILD_DIR
npm run build

rsync -r "$FRONTEND_BUILD_DIR/" $STATIC_DIR
