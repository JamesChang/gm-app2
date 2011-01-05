#!/bin/sh

if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/bin/java"
    else
        echo "** ERROR: java under JAVA_HOME=$JAVA_HOME cannot be executed"
        exit 1
    fi
else
    JAVACMD=`which java 2> /dev/null`
    if [ -z "$JAVACMD" ] ; then
        JAVACMD=java
    fi
fi

# Verify that it is java 6+
javaVersion=`$JAVACMD -version 2>&1 | grep "java version" |egrep -e "1\.[67]"`
if [ -z "$javaVersion" ]; then
    $JAVACMD -version
    echo "** ERROR: The Java of $JAVACMD version is not 1.6"
    exit 1
fi

if [ -z "$GMAPP_HOME" ]; then
    GMAPP_HOME=`dirname "$0"`/..
fi

# Verify minimal JVM props are set
hasMinHeapSize=`echo "$JAVA_OPTIONS" | grep "\\-Xms"`
if [ -z "$hasMinHeapSize" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -Xms256m"
fi
hasMaxHeapSize=`echo "$JAVA_OPTIONS" | grep "\\-Xmx"`
if [ -z "$hasMaxHeapSize" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -Xmx512m"
fi
hasMinPermSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:PermSize"`
if [ -z "$hasMinPermSize" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -XX:PermSize=128m"
fi
hasMaxPermSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:MaxPermSize"`
if [ -z "$hasMaxPermSize" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -XX:MaxPermSize=128m"
fi

JAVA_OPTIONS="$JAVA_OPTIONS -Dgm.app.home=$GMAPP_HOME"


LIB_DIR=$GMAPP_HOME/lib
CLASSPATH=$GMAPP_HOME/standalone.jar
# Add all jars under the lib dir to the classpath
for i in `ls $LIB_DIR/*.jar`
do
  CLASSPATH="$CLASSPATH:$i"
done

if [ -z "$GMAPP_USER" ] ; then
  GMAPP_USER=$USER
fi

if [ -z "$GMAPP_REST_PORT" ] ; then
  GMAPP_REST_PORT=8090
fi
JAVA_OPTIONS="$JAVA_OPTIONS -Djetty.port=$GMAPP_REST_PORT"

#####################################################
# Print out
#####################################################
echo
echo "GMAPP_HOME         =  $GMAPP_HOME"
echo "JETTY_CONF         =  $JETTY_CONF"
echo "GMAPP_REST_PORT    =  $GMAPP_REST_PORT"
echo "GMAPP_USER         =  $GMAPP_USER"
echo "GMAPP_RUN          =  $GMAPP_RUN"
echo "GMAPP_PID          =  $GMAPP_PID"
echo "GMAPP_CONSOLE      =  $GMAPP_CONSOLE"
echo "JETTY_ARGS         =  $JETTY_ARGS"
echo "CONFIGS            =  $CONFIGS"
echo "JAVA_OPTIONS       =  $JAVA_OPTIONS"
echo "JAVACMD            =  $JAVACMD"
echo "EXEC_FILE          =  $EXEC_FILE"
echo "RUN_CMD            =  $RUN_CMD"
echo

echo "Runing: exec $JAVACMD $JAVA_OPTIONS -cp \"$CLASSPATH\" cn.gamemate.standalone.main.Main $@"
exec "$JAVACMD" $JAVA_OPTIONS -cp "$CLASSPATH" cn.gamemate.app.standalone.Main "$@"
