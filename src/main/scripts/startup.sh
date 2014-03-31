#!/bin/bash

. "$( dirname "${BASH_SOURCE[0]}" )/kvstore_env.sh"

if [ -f "$JSVC_PID_FILE" ]; then
  echo "KVstore Daemon is already running. ($( cat "$JSVC_PID_FILE" ))" >&2
  exit 1
fi

echo 'Starting Daemon kvstore in Background.'

$JSVC_EXECUTABLE -server -cp "$JAVA_CLASSPATH" -user "$JSVC_USER" \
  -pidfile $JSVC_PID_FILE -procname "kvstore" $JAVA_OPTS $JAVA_MAIN_CLASS