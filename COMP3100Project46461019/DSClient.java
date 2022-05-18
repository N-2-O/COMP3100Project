package COMP3100Project46461019;

import java.net.*;
import java.io.*;

/**
 * Author: Nathan Ho
 * SID: 46461019
 * DSClient is compatible with ds-sim available at https://github.com/distsys-MQ/ds-sim. DSClient acts as a job scheduler.
 */

public class DSClient {
    /**
     * Implements handhake protocol of ds-sim
     * @param input - The input stream
     * @param output - The output stream
     */
    public static void handshake(BufferedReader input, DataOutputStream output) {
        try {
            output.write(("HELO\n").getBytes());
            output.flush();

            input.readLine(); //expect "OK"

            String username = System.getProperty("user.name");
            output.write(("AUTH " + username + "\n").getBytes());
            output.flush();

            input.readLine(); //expect "OK"
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Reads information of available servers provided by server
     * @param input - The input stream
     * @param output - The output stream
     * @return An array containing the servers available provided by the server
     */
    public static ServerInfo[] readServer(BufferedReader input, DataOutputStream output) {
        ServerInfo[] servers = new ServerInfo[0]; //init empty array
        try {
            output.write(("GETS All\n").getBytes());
            output.flush();
            
            String sResponse = input.readLine(); //expect DATA

            output.write(("OK\n").getBytes());
            output.flush();

            String[] data = sResponse.split(" ");
            int size = Integer.parseInt(data[1]);
            servers = new ServerInfo[size];
            for (int i = 0; i < size; i++) {
                sResponse = input.readLine(); //expect server information
                ServerInfo toAdd = new ServerInfo(sResponse.split(" "));
                servers[i] = toAdd;
                // toAdd.printServer();
            }

            output.write(("OK\n").getBytes());
            output.flush();

            sResponse = input.readLine(); //expect .
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return servers;
    }
    
    /**
     * Schedules jobs to the given servers using the given algorithm
     * @param input - The input stream
     * @param output - The output stream 
     * @param alg - The scheduling algorithm
     */
    public static void scheduleJobs(BufferedReader input, DataOutputStream output, String alg) {
        try {
            output.write(("REDY\n").getBytes());
            output.flush();

            String sResponse = input.readLine(); //expect jobs
            
            if (alg.equals("LRR")) {
                ServerInfo[] servers = readServer(input, output);
                servers = ServerInfo.filterServersByName(servers, ServerInfo.findLargestServerType(servers));
                roundRobin(sResponse, servers, input, output);
            }
            else if (alg.equals("FC")) {
                firstCapable(sResponse, input, output);
            }
            else if (alg.equals("SC")) {
                ServerInfo[] servers = readServer(input, output);
                customSchedule(sResponse, servers, input, output);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }        
    }

    /**
     * Implements Round Robin Scheduling
     * NOTE: This function assumes all the servers meets all requirements of the job
     * NOTE: This function currently only implements the SCHD event, others are ignored
     * @param sResponse - Initial response to first REDY by client
     * @param servers - Array of servers to be utilised
     * @param input - The input stream
     * @param output - The output Stream
     */
    public static void roundRobin(String sResponse, ServerInfo[] servers, BufferedReader input, DataOutputStream output) {
        try {
            String simEvent, jobSchd;
            JobInfo job; 
            int i = 0;
            while (!sResponse.equals("NONE")) {
                simEvent = sResponse.split(" ")[0];
                if (simEvent.equals("JOBN")) {
                    job = new JobInfo(sResponse.split(" "));
                    jobSchd = "SCHD " + job.getIndex() + " " + servers[i].getName() + " " + servers[i].getID() + "\n";
                    i++;
                    output.write(jobSchd.getBytes());
                    output.flush();
                    
                    sResponse = input.readLine(); //expect ok
                    if (i >= servers.length) {
                        i = 0;
                    } 
                }
                output.write(("REDY\n").getBytes());
                output.flush();

                sResponse = input.readLine(); //expect JOBN or other command
            }
        }
        catch (Exception e) {
            System.out.println();
        }
    }

    /**
     * Implements First Capable Scheduling
     * @param sResponse - Initial response to first REDY by client
     * @param input - The input stream
     * @param output - The output Stream
     */
    public static void firstCapable(String sResponse, BufferedReader input, DataOutputStream output) {
        try {
            String simEvent, getsString, jobSchd;
            JobInfo job; 
            while (!sResponse.equals("NONE")) {
                simEvent = sResponse.split(" ")[0];
                if (simEvent.equals("JOBN")) {
                    job = new JobInfo(sResponse.split(" ")); //job to be scheduled   
                    getsString = "GETS Capable " + job.getCores() + " " + job.getMem() + " " + job.getDisk() + "\n";
                    output.write(getsString.getBytes()); //gets capable servers
                    output.flush();
            
                    sResponse = input.readLine(); //expect DATA X Y 
                    //where X is the number of capable servers

                    output.write(("OK\n").getBytes());
                    output.flush();

                    String[] data = sResponse.split(" ");
                    int size = Integer.parseInt(data[1]); //number of capable servers
                    
                    sResponse = input.readLine(); // expect first capable server
                    ServerInfo server = new ServerInfo(sResponse.split(" ")); //save server information

                    for (int i = 0; i < size-1; i++) {
                        input.readLine(); //expect all + read capable servers not needed
                    }

                    output.write(("OK\n").getBytes());
                    output.flush();

                    sResponse = input.readLine(); //expect .  
                    
                    jobSchd = "SCHD " + job.getIndex() + " " + server.getName() + " " + server.getID() + "\n"; //schedule the job
                    output.write(jobSchd.getBytes());
                    output.flush();

                    sResponse = input.readLine(); //expect OK  
                }
                output.write(("REDY\n").getBytes());
                output.flush();

                sResponse = input.readLine(); //expect JOBN or other command
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void customSchedule(String sResponse, ServerInfo[] servers, BufferedReader input, DataOutputStream output) {
        try {
            String simEvent, jobSchd;
            JobInfo job; 
            while (!sResponse.equals("NONE")) {
                simEvent = sResponse.split(" ")[0];
                if (simEvent.equals("JOBN")) {
                    job = new JobInfo(sResponse.split(" "));
                    ServerInfo server = ServerInfo.findClosestCore(servers, job, 2);
                    jobSchd = "SCHD " + job.getIndex() + " " + server.getName() + " " + server.getID() + "\n";
                    output.write(jobSchd.getBytes());
                    output.flush();
                    
                    sResponse = input.readLine(); //expect ok
                }
                output.write(("REDY\n").getBytes());
                output.flush();

                sResponse = input.readLine(); //expect JOBN or other command
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Implements the protocol to terminate connection
     * @param input - The input Stream
     * @param output - The output stream
     */
    public static void quit(BufferedReader input, DataOutputStream output) {
        try {
            output.write(("QUIT\n").getBytes());
            output.flush();

            input.readLine(); //expect "QUIT"
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * Prints usage information
     */
    public static void incorrectUsage() {
        System.out.println("Usage:");
        System.out.println("\t $ java COMP3100Project46461019.DSClient -lrr");
        System.exit(1);
    }
    public static void main(String args[]) {
        try {
            String alg;
            final int defaultPort = 50000;
            if (args.length < 1) {
                incorrectUsage();
            }
            if (args[0].equals("-lrr")) {
                alg = "LRR";
            }
            else if (args[0].equals("-fc")) {
                alg = "FC";
            }
            else if (args[0].equals("-cs")) {
                alg = "CS";
            }
            else {
                alg = "";
                incorrectUsage();
            }
            System.out.println("DSClient simulator started");
            Socket s = new Socket("localhost", defaultPort);
            BufferedReader dIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            handshake(dIn, dOut);
            scheduleJobs(dIn, dOut, alg);
            quit(dIn, dOut);

            dIn.close();
            dOut.close();            
            s.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}