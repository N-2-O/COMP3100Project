# COMP3100 Distributed Systems Project

This is a project for the design and implementation of a client-side simulator that includes basic scheduling functionalities and a simple job dispatcher compatible with [ds-sim server](https://github.com/distsys-MQ/ds-sim) on Ubuntu using JDK 17. There are currently 3 algorithms implemented:
- First Capable (FC)
- Largest Round Robin (LRR)
- First Available Capable Round Robin (FACRR)

## Stage 1

This is a project for the design and implementation of a client-side simulator that includes basic scheduling functionalities and a simple job dispatcher using LRR algorithm.

## Stage 2

Built on stage 1, this project involves the design and implementation of a new scheduling algorithm FACRR. It offers significant improvements in turnaround times compared to four baseline algorithms (FC, FF, BF, WF) at comparable utilisation rates and rental cost.

<img alt="Turnaround Time Comparison" src="https://github.com/N-2-O/COMP3100Project/blob/master/images/TurnaroundTimeAll.png" width="60%" >
<img alt="Server Utilisation Comparison" src="https://github.com/N-2-O/COMP3100Project/blob/master/images/ServerUtilisation.png" width="60%" >
<img alt="Rental Cost Comparison" src="https://github.com/N-2-O/COMP3100Project/blob/master/images/RentalCost.png" width="60%" >

## Usage

Compile: 
`$ javac COMP3100Project46461019/*.java`

Running: 
`$ java COMP3100Project46461019.DSClient -lrr | -facrr | -fc`

Testing: 
- Stage 1 comparison: `$ ./testLrr.sh` 
- Stage 2 comparison results: `$ ./testFacrr.sh`

Author: Nathan Ho
