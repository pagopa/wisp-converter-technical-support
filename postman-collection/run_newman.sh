#!/bin/bash

# install newman if not present
if [ $(npm list -g | grep -c newman) -eq 0 ]; then
  npm install -g newman
fi

# run the collection
# TODO: add your files and edit the following command
newman run wisp-converter-technical-support.postman_collection.json \
  --environment=wisp-converter-technical-support_LOCAL.postman_environment.json \
  --reporters cli,junit \
  --reporter-junit-export Results/results-TEST.xml
