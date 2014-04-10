#!/bin/bash

JSVC_EXECUTABLE="$( which jsvc )"
JSVC_PID_FILE=/tmp/kvstore.pid

if [ -z "$JSVC_USER" ]; then
  JSVC_USER="$USER"
fi

DIST_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../" && pwd )"
LIB_DIR="$DIST_DIR/lib"
CONF_DIR="$DIST_DIR/conf"

JAVA_EXEC="$( which java )"
JAVA_CLASSPATH="$LIB_DIR/kvstore-1.0.jar"
JAVA_MAIN_CLASS="cn.edu.sjtu.se.kvstore.db.KVDaemon"
JAVA_OPTS="-Ddistribution.dir=$DIST_DIR"

# set JAVA_HOME to your directory.

JAVA_HOME=/home/hadoop/softwares/jdk1.7.0_25

export JSVC_EXECUTABLE JSVC_PID_FILE JSVC_USER DIST_DIR CONF_DIR JAVA_EXEC \
  JAVA_CLASSPATH JAVA_MAIN_CLASS
