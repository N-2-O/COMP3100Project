#!/bin/bash
# This file will compile the Java implementation of DSClient.
# This test file runs the reference implementation of ds-sim and the java implementation of DSClient using all given config files and outputs the results into log files.
# The differences between the reference and Java implementation are displayed in 'log/diff.log'
testFiles="./config"
dsSim="./pre-compiled"

if ! test -d "log"; then
    mkdir log
fi
l="./log"

if test -f "$l/dsRef.log" | test -f "$l/client.log" | test -f "$l/diff.log"; then
    rm $l/dsRef.log $l/client.log $l/diff.log
fi

touch $l/dsRef.log $l/client.log $l/diff.log

javac COMP3100Project46461019/DSClient.java COMP3100Project46461019/ServerInfo.java COMP3100Project46461019/JobInfo.java

for f in $testFiles/*.xml; do 
    echo "running server and client with $f"
    $dsSim/ds-server -c $f -v brief >> $l/dsRef.log & sleep 2
    $dsSim/ds-client -a lrr

    $dsSim/ds-server -c $f -v brief -n >> $l/client.log & sleep 2
    java COMP3100Project46461019.DSClient -lrr
    echo "done running with $f"
done

diff $l/dsRef.log $l/client.log > $l/diff.log
