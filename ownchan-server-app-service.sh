#!/bin/bash
#
# ownchan-server-app
#
# chkconfig: 345 96 30
# description:  Start up the ownchan-server-app application.
#
# processname: java
# pidfile: /var/run/ownchan-server-app.pid
#
### BEGIN INIT INFO
# Provides: ownchan-server-app
# Required-Start: $network $syslog
# Required-Stop: $network $syslog
# Should-Start: distcache
# Short-Description: start and stop ownchan-server-app application
# Description: start and stop ownchan-server-app application
## END INIT INFO

# Source function library.
. /etc/init.d/functions

# general config
PROCESS_NAME=ownchan-server-app
CONF_DIR=/opt/ownchan-server
APP_DIR=${CONF_DIR}/app
LOG_DIR=${APP_DIR}/logs
JAVA_EXECUTABLE=/usr/bin/java
PATH_TO_JAR=${APP_DIR}/${PROCESS_NAME}.jar
pidfile=${PIDFILE-/var/run/${PROCESS_NAME}.pid};
lockfile=${LOCKFILE-/var/lock/subsys/${PROCESS_NAME}};

# app config
JAVA_PARAMS="-DappMode=production -DconfDir=${CONF_DIR} -DlogDir=${LOG_DIR}"
APP_PARAMS=""

# user to run the app with
APP_USER=ownchan

case "$1" in
    start)
        PID=`pidofproc -p ${pidfile} ${PROCESS_NAME}`
        if [[ (-n ${PID}) && ($PID -gt 0) ]]; then
            logger -s "${PROCESS_NAME}(pid ${PID}) is  already running."
            exit;
        fi
        if [ -f $PATH_TO_JAR ]; then
            logger -s "Starting ${PROCESS_NAME}"
            /bin/su -l ${APP_USER} -c "nohup $JAVA_EXECUTABLE -Dprocessname=${PROCESS_NAME} $JAVA_PARAMS -jar $PATH_TO_JAR $APP_PARAMS /tmp 2>> /dev/null >> /dev/null &"
            PID=`ps -eaf|grep processname=${PROCESS_NAME}|grep -v grep|awk '{print $2}'`
            RETVAL=$?
            [ $RETVAL = 0 ] && touch ${lockfile}
            [ $RETVAL = 0 ] && echo "${PID}" > ${pidfile}
        fi
        ;;
    stop)
        PID=`pidofproc -p ${pidfile} ${PROCESS_NAME}`

        if [[ (-n ${PID}) && ($PID -gt 0) ]]; then
            logger -s "Stopping ${PROCESS_NAME}"
            /bin/su -l ${APP_USER} -c "kill $PID;"
            RETVAL=$?
            [ $RETVAL = 0 ] && rm -f ${lockfile}
            [ $RETVAL = 0 ] && rm -f ${pidfile}
        else
            logger -s "${PROCESS_NAME} is not running."
            exit;
        fi
        ;;
    status)
        status -p ${pidfile} ${PROCESS_NAME}
        RETVAL=$?
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
        ;;
esac
exit $RETVAL  
