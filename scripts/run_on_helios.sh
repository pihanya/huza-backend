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

ssh -p 2222 -t $HELIOS_USER@se.ifmo.ru "cd /home/studs/$HELIOS_USER/huza ; ./run.sh"
