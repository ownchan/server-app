#!/bin/sh
APP_NAME=ownchan-server-app
CONF_DIR=/opt/ownchan-server
APP_DIR=${CONF_DIR}/app
LOG_DIR=${APP_DIR}/logs
JAVA_EXECUTABLE=/usr/bin/java
PATH_TO_JAR=${APP_DIR}/${APP_NAME}.jar
PID_PATH_NAME=/tmp/${APP_NAME}-pid
JAVA_PARAMS="-DappMode=production -DconfDir=${CONF_DIR} -DlogDir=${LOG_DIR}"
APP_PARAMS=""

case $1 in
    start)
        echo "Starting $APP_NAME ..."
        if [ ! -f $PID_PATH_NAME ] || [ ! -n "$(ps -p $(cat $PID_PATH_NAME) -o pid=)" ]; then
            nohup $JAVA_EXECUTABLE $JAVA_PARAMS -jar $PATH_TO_JAR $APP_PARAMS /tmp 2>> /dev/null >> /dev/null &
                        echo $! > $PID_PATH_NAME
            echo "$APP_NAME started."
        else
            echo "$APP_NAME is already running."
        fi
        ;;
    stop)
        if [ -f $PID_PATH_NAME ] && [ -n "$(ps -p $(cat $PID_PATH_NAME) -o pid=)" ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "Stopping $APP_NAME ..."
            kill $PID;
            echo "$APP_NAME stopped."
            rm $PID_PATH_NAME
        else
            echo "$APP_NAME is not running."
        fi
        ;;
    status)
        if [ -f $PID_PATH_NAME ] && [ -n "$(ps -p $(cat $PID_PATH_NAME) -o pid=)" ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$APP_NAME is running with PID $PID."
        else
            echo "$APP_NAME is not running."
        fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        ;;
esac
