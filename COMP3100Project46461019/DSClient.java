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
            System.out.println("Client >\tHELO");

            String sResponse = input.readLine(); //expect "OK"
            System.out.println("Server >\t" + sResponse);

            String username = "Nathan";
            output.write(("AUTH " + username + "\n").getBytes());
            output.flush();
            System.out.println("Client >\tAUTH " + username);

            sResponse = input.readLine(); //expect "OK"
            System.out.println("Server >\t" + sResponse);
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
            output.write(("REDY\n").getBytes());
            output.flush();
            System.out.println("Client >\t" + "REDY");

            String sResponse = input.readLine(); //expect jobs
            System.out.println("Server >\t" + sResponse);

            output.write(("GETS All\n").getBytes());
            output.flush();
            System.out.println("Client >\tGETS All");
            
            sResponse = input.readLine(); //expect DATA
            System.out.println("Server >\t" + sResponse);

            output.write(("OK\n").getBytes());
            output.flush();
            System.out.println("Client >\tOK");

            String[] data = sResponse.split(" ");
            int size = Integer.parseInt(data[1]);
            servers = new ServerInfo[size];
            for (int i = 0; i < size; i++) {
                sResponse = input.readLine(); //expect server information
                System.out.println("Server >\t" + sResponse);
                ServerInfo toAdd = new ServerInfo(sResponse.split(" "));
                servers[i] = toAdd;
                // toAdd.printServer();
            }

            output.write(("OK\n").getBytes());
            output.flush();
            System.out.println("Client >\tOK");

            sResponse = input.readLine(); 
            System.out.println("Server >\t" + sResponse); //expect .
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return servers;
    }

    /**
     * Schedules jobs to the given servers in a round robin fashion 
     * NOTE: This function currently only implements the SCHD command
     * @param servers - An array containing servers to be utilised
     * @param input - The input stream
     * @param output - The output stream 
     */
    public static void scheduleJobs(ServerInfo[] servers, BufferedReader input, DataOutputStream output) {
        try {
            output.write(("REDY\n").getBytes());
            output.flush();
            System.out.println("Client >\tREDY");

            String sResponse = input.readLine(); //expect jobs
            System.out.println("Server >\t" + sResponse);
            String[] job = sResponse.split(" ");

            int i = 0;
            while (!sResponse.equals("NONE")) {
                job = sResponse.split(" ");
                if (job[0].equals("JOBN")) {
                    String jobSchd = "SCHD " + job[2] + " " + servers[i].getName() + " " + servers[i].getID() + "\n";
                    i++;
                    output.write(jobSchd.getBytes());
                    output.flush();
                    System.out.println("Client >\t" + jobSchd);
                    
                    sResponse = input.readLine(); //expect ok
                    System.out.println("Server >\t" + sResponse);
                }
                if (i >= servers.length) {
                    i = 0;
                } 
                output.write(("REDY\n").getBytes());
                output.flush();
                System.out.println("Client >\tREDY");

                sResponse = input.readLine(); //expect JOBN or other command
                System.out.println("Server >\t" + sResponse); 
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
            System.out.println("Client >\t" + "QUIT");

            String sResponse = input.readLine(); //expect "QUIT"
            System.out.println("Server >\t" + sResponse);
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

    public static void main(String args[]) {
        try {
            Socket s = new Socket("localhost", 50000);
            BufferedReader dIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

            System.out.println("<------------Handshake------------>");
            handshake(dIn, dOut);
            System.out.println("<------------read and filter------------>");
            ServerInfo[] servers = readServer(dIn, dOut);
            servers = filterServers(servers, findLargestServer(servers));
            System.out.println("<------------schedule------------>");
            scheduleJobs(servers, dIn, dOut);
            System.out.println("<------------quit------------>");
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