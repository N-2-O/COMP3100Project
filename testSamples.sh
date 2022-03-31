#!/bin/bash

testFiles="./config"
dsSim="./pre-compiled"
log="./log"

if test -f "$log/ds.log" | test -f "$log/client.log" | test -f "$log/diff.log"; then
    rm log/ds.log log/client.log log/diff.log
fi

touch log/ds.log log/client.log log/diff.log

for f in $testFiles/*.xml; do 
    echo "running server and client with $f"
    $dsSim/ds-server -c $f -v brief >> log/ds.log & sleep 2
    $dsSim/ds-client -a lrr

    $dsSim/ds-server -c $f -v brief -n >> log/client.log & sleep 2
    java COMP3100Project46461019.DSClient -lrr
    echo "done running with $f"
done

diff log/ds.log log/client.log > log/diff.log
