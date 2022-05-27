# COMP3100 Distributed Systems Project

## Stage 1

This is a project for the design and implementation of a client-side simulator that includes basic scheduling functionalities and a simple job dispatcher compatible with [ds-sim server](https://github.com/distsys-MQ/ds-sim) on Ubuntu using JDK 17.

## Stage 2

Built on stage 1, this project involves the design and implementation of a new scheduling algorithm called First Available Capable Round Robin (FACRR). It offers significant improvements in turnaround times compared to four baseline algorithms (FC, FF, BF, WF) at comparable utilisation rates and rental cost.

## Usage

Compile: 
`$ javac COMP3100Project46461019/*.java`

Running: 
`$ java COMP3100Project46461019.DSClient -lrr | -facrr | -fc`

Testing: 
- Stage 1 comparison: `$ ./testLrr.sh` 
- Stage 2 comparison results: `$ ./testFacrr.sh`

Author: Nathan Ho
