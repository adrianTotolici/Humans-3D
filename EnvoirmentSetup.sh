#!/usr/bin/env bash

red='\033[1;31m' # Red
green='\033[1;32m' # Green
yellow='\033[1;33m' # Yellow

NC='\033[0m' # No Color

function installjava {
 echo -e "${yellow} Installing Java 1.8 ... ${NC}"

 sudo add-apt-repository ppa:webupd8team/java
 sudo apt-get update
 sudo apt-get install oracle-java8-installer

 echo -e "${yellow} Setup Java path ... ${NC}"	
 sudo apt-get install oracle-java8-set-default
}

if type -p java; then
    echo -e "${green} Found java executable in PATH${NC}"
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo -e "${green} found java executable in JAVA_HOME${NC}"
    _java="$JAVA_HOME/bin/java"
else
    echo -e "${red} No java install ... ${NC}"
	installjava
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo -e "${yellow} version "$version"${NC}"
    if [[ "$version" > "1.8" ]]; then
        echo -e "${yellow} Version is more than 1.8${NC}"
    else
        echo -e "${red}version is less than 1.8${NC}"
		installjava
    fi
fi

chmod +x Humans.sh
