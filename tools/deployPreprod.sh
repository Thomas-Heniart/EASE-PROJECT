#!/bin/bash

if [[ -z "${EASE_DIR}" ]]; then
  echo "Please setup EASE_DIR environment variable. Ex:"
  echo "export EASE_DIR=~/EASE-PROJECT/"
  exit 0
else
  DIR="${EASE_DIR}"
fi

if hash mvn 2>/dev/null; then
        echo "Maven found"
    else
	curl http://mirrors.standaloneinstaller.com/apache/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.tar.gz
	mkdir /opt/apache-maven-3.5.3
	cd /opt/apache-maven-3.5.3
	tar -xvf apache-maven-3.5.3-bin.tar.gz
	rm -f apache-maven-3.5.3-bin.tar.gz	
        export PATH=/opt/apache-maven-3.5.3/bin:$PATH
    fi

read -p 'SSH login: ' login

echo $DIR

WAR=Ease-0.0.1-SNAPSHOT.war

cd "$DIR"

cd Ease

echo $(pwd)

mvn clean -DskipTests package war:war

scp target/$WAR $login@beta.ease.space:Preprod.war
