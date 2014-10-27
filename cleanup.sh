#! /bin/bash

sudo rm $TOMCAT_HOME/webapps/wood -r

if [ $? -eq 0 ]; then
    echo "clean complete"
fi
