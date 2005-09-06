# Edit DBPATH and BASEPATH for your installation
export DBPATH="c:/derby"  # where the derby db and log go
export BASEPATH="c:/svnrepos/jdo/trunk/tck20" # your tck20 install dir


export CLASSPATH="$BASEPATH/iut_jars/derbytools.jar;$BASEPATH/iut_jars/derby.jar"
rm -rf $DBPATH/jdotckdb
java -Dderby.system.home=$DBPATH org.apache.derby.tools.ij schema1.sql
#java -Dderby.system.home=$DBPATH org.apache.derby.tools.ij schema2.sql
