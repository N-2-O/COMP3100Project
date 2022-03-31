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
            
            ServerInfo[] servers = readServer(input, output);

            if (alg.equals("LRR")) {
                servers = filterServers(servers, findLargestServer(servers));
                roundRobin(sResponse, servers, input, output);
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
            String simEvent;
            JobInfo job; 
            int i = 0;
            while (!sResponse.equals("NONE")) {
                simEvent = sResponse.split(" ")[0];
                if (simEvent.equals("JOBN")) {
                    job = new JobInfo(sResponse.split(" "));
                    String jobSchd = "SCHD " + job.getIndex() + " " + servers[i].getName() + " " + servers[i].getID() + "\n";
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
     * Prints out server information
     * @param servers - An array containing servers to be printed
     */
    public static void viewServers(ServerInfo[] servers) {
        // Print server information
        for (int i = 0; i < servers.length; i++) {
            servers[i].printServer();
        }
    }
    
    /**
     * Finds the largest server type in a given array of servers
     * @param servers - The array of servers provided by server
     * @return The name of the largest server
     */
    public static String findLargestServer(ServerInfo[] servers) {
        // Finds the name of the largest server
        String largestServer = servers[0].getName();
        int largestCore = servers[0].getCores();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getCores() > largestCore) {
                largestServer = servers[i].getName();
                largestCore = servers[i].getCores();
            }
        }
        return largestServer;
    }

    /**
     * Filter servers of the given server type
     * @param servers - The array of servers
     * @param serverName - The name of the server to be filtered
     * @return An array containing servers of the same type
     */
    public static ServerInfo[] filterServers(ServerInfo[] servers, String serverName) {
        int num = 0;
        for (int i = 0; i < servers.length; i ++) {
            if (servers[i].getName().equals(serverName)) {
                num++;
            }
        }
        ServerInfo[] filtered = new ServerInfo[num];
        int index = 0;
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getName().equals(serverName)) {
                filtered[index] = servers[i];
                index++;
            }
        }
        return filtered;
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
            else {
                incorrectUsage();
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}