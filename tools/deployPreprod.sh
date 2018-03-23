#!/bin/bash

if [[ -z "${EASE_DIR}" ]]; then
  echo "Please setup EASE_DIR environment variable. Ex:"
  echo "export EASE_DIR=~/EASE-PROJECT/"
  exit 0
else
  DIR="${EASE_DIR}"
fi

read -p 'SSH login: ' login

echo $DIR

WAR=Ease-0.0.1-SNAPSHOT.war

cd "$DIR"

cd Ease

echo $(pwd)

mvn clean -DskipTests package war:war

scp target/$WAR $login@beta.ease.space:Preprod.war
