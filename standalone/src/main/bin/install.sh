#!/bin/sh

if [ -n "$1" ]; then
    GMAPP_USER=$1
fi
if [ -z "$GMAPP_USER" ]; then
    GMAPP_USER=gamemate
fi

echo
echo "Installing gmapp as a Unix service that will run as user ${GMAPP_USER} "

curUser=`id -nu`
if [ "$curUser" != "root" ]
then
    echo
    echo "\033[31m** ERROR: Only root user can install gmapp as a service\033[0m"
    echo
    exit 1
fi

errorArtHome() {
    echo
    echo "\033[31m** ** ERROR: $1 \033[0m"
    echo
    exit 1
}

if [ "$0" = "." ] || [ "$0" = "source" ]; then
    errorArtHome "Cannot execute script with source $0"
fi

curdir=`dirname $0` || errorArtHome "Cannot find GMAPP_HOME=$curdir/.."
curdir=`cd $curdir; pwd` || errorArtHome "Cannot finddefau GMAPP_HOME=$curdir/.."
cd $curdir/.. || errorArtHome "Cannot go to GMAPP_HOME=$curdir/.."

GMAPP_HOME=`pwd`
if [ -z "$GMAPP_HOME" ] || [ "$GMAPP_HOME" = "/" ]; then
    errorArtHome "GMAPP_HOME cannot be the root folder"
fi

echo
echo "Installing gmapp with home ${GMAPP_HOME}"

echo -n "Creating user ${GMAPP_USER}..."
gmappUsername=`id -nu ${GMAPP_USER}`
if [ "$gmappUsername" = "${GMAPP_USER}" ]; then
    echo -n "already exists..."
else
    echo -n "creating..."
    useradd -m ${GMAPP_USER}
    if [ ! $? ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"

echo
echo -n "Checking configuration link and files in /etc/gmapp..."
if [ -L ${GMAPP_HOME}/etc ]; then
    echo -n "already exists, no change..."
else
    echo
    echo -n "Moving configuration dir etc to etc.original"
    mv ${GMAPP_HOME}/etc ${GMAPP_HOME}/etc.original && \
    etcOK=true
    if [ ! $etcOK ]; then
       echo
       echo "\033[31m** ERROR\033[0m"
       echo
       exit 1
    fi
    echo "\033[32mDONE\033[0m"
    if [ ! -d /etc/gmapp ]; then
        echo -n "creating dir /etc/gmapp..."
        mkdir -p /etc/gmapp && \
        etcOK=true
    fi
    if [ $etcOK = true ]; then
        echo -n "creating the link and updating dir..."
        ln -s /etc/gmapp etc && \
        cp -Ri ${GMAPP_HOME}/etc.original/* /etc/gmapp/ && \
        etcOK=true
    fi
    if [ ! $etcOK ]; then
        echo
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"

echo -n "Creating environment file /etc/gmapp/default..."
if [ -e /etc/gmapp/default ]; then
    echo -n "already exists, no change...  "
    echo "\033[33m*** Make sure your default file is up to date ***\033[0m"
else
    # Populating the /etc/gmapp/default with GMAPP_HOME and GMAPP_USER
    echo -n "creating..."
    cat ${GMAPP_HOME}/bin/gmapp.default > /etc/gmapp/default && \
    echo "export GMAPP_HOME=${GMAPP_HOME}" >> /etc/gmapp/default && \
    echo "export GMAPP_USER=${GMAPP_USER}" >> /etc/gmapp/default && \
    etcDefaultOK=true
    if [ ! $etcDefaultOK ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"
echo "** INFO: Please edit the files in /etc/gmapp to set the correct environment"
echo "Especially /etc/gmapp/default that defines GMAPP_HOME, JAVA_HOME and JAVA_OPTIONS"
echo
echo -n "Creating link ${GMAPP_HOME}/logs to /var/log/gmapp..."
if [ -L ${GMAPP_HOME}/logs ]; then
    echo -n "already a link..."
else
    echo -n "creating..."
    artLogFolder=/var/log/gmapp
    if [ ! -d "$artLogfolder" ]; then
        mkdir -p /var/log/gmapp && \
        logsOK=true
    fi
    if [ $logsOK = true ]; then
        if [ -d ${GMAPP_HOME}/logs ]; then
            mv ${GMAPP_HOME}/logs ${GMAPP_HOME}/logs.original
        fi
        ln -s /var/log/gmapp logs && \
        logsOK=true
    fi
    if [ ! $logsOK ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"

echo
echo -n "Setting file permissions to etc, logs, work, data and backup..."
chown ${GMAPP_USER} -R /etc/gmapp && \
chmod 755 -R /etc/gmapp && \
chown ${GMAPP_USER} -R ${GMAPP_HOME}/logs/ && \
chmod u+w -R ${GMAPP_HOME}/logs/ && \
mkdir -p ${GMAPP_HOME}/work/ && \
chown ${GMAPP_USER} ${GMAPP_HOME}/work/ && \
chmod u+w -R ${GMAPP_HOME}/work/ && \
mkdir -p ${GMAPP_HOME}/backup/ && \
chown ${GMAPP_USER} ${GMAPP_HOME}/backup/ && \
chmod u+w ${GMAPP_HOME}/backup/ && \
mkdir -p ${GMAPP_HOME}/data/ && \
chown ${GMAPP_USER} -R ${GMAPP_HOME}/data/ && \
chmod u+w -R ${GMAPP_HOME}/data/ && \
permChangeOK=true
if [ ! $permChangeOK ]; then
    echo "\033[31m** ERROR\033[0m"
    echo
    exit 1
fi
echo "\033[32mDONE\033[0m"
echo
echo -n "Copying the init.d/gmapp script..."
if [ -e /etc/init.d/gmapp ]; then
    echo -n "already exists..."
else
    echo -n "copying..."
    cp ${GMAPP_HOME}/bin/gmappctl /etc/init.d/gmapp
    if [ ! $? ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"
echo
# Try update-rc.d for debian/ubuntu else use chkconfig
if [ -x /usr/sbin/update-rc.d ]; then
    echo "Initializing gmapp service with update-rc.d..."
    update-rc.d gmapp defaults && \
    chkconfigOK=true
else
    echo "Initializing gmapp service with chkconfig..."
    chkconfig --add gmapp && \
    chkconfig gmapp on && \
    chkconfig --list gmapp && \
    chkconfigOK=true
fi
if [ ! $chkconfigOK ]; then
    echo "\033[31m** ERROR\033[0m"
    echo
    exit 1
fi
echo "\033[32mDONE\033[0m"
echo
echo "\033[32m************ SUCCESS *****************\033[0m"
echo "Installation of Gamemate Application Server completed"
echo "you can now check installation by running:"
echo "> service gmapp check"
echo
echo "Then activate gmapp with:"
echo "> service gmapp start"
echo
