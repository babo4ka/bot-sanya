#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/Bot_sanya-1.0-SNAPSHOT.jar \
    root@95.163.235.101:/root/

echo 'Restart server...'

ssh -T -i ~/.ssh/id_rsa root@95.163.235.101 << EOF

pgrep java | xargs kill -9
nohup java -jar Bot_sanya-1.0-SNAPSHOT.jar > log.txt &

EOF

echo 'end...'