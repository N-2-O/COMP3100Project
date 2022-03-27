package COMP3100Project46461019;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/*
Author: Nathan Ho
SID: 46461019

DSClient is compatible with ds-sim available at https://github.com/distsys-MQ/ds-sim. DSClient acts as a job scheduler.
*/

public class DSClient {
    public static void handshake(BufferedReader input, DataOutputStream output) {
        // Implements handhake protocol of ds-sim
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

    public static ArrayList<ServerInfo> readServer(BufferedReader input, DataOutputStream output) {
        // Reads information of available servers provided by server
        ArrayList<ServerInfo> servers = new ArrayList<>(); 
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
            for (int i = 0; i < size; i++) {
                sResponse = input.readLine(); //expect server information
                System.out.println("Server >\t" + sResponse);
                ServerInfo toAdd = new ServerInfo(sResponse.split(" "));
                servers.add(toAdd);
                // toAdd.printServer();
            }

            output.write(("OK\n").getBytes());
            output.flush();
            System.out.println("Client >\tOK");

            sResponse = input.readLine(); 
            System.out.println("Server >\t" + sResponse); //expect .

            output.write(("REDY\n").getBytes());
            output.flush();
            System.out.println("Client >\tREDY");

            sResponse = input.readLine(); 
            System.out.println("Server >\t" + sResponse);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return servers;
    }

    public static void scheduleJobs(ServerInfo[] servers, BufferedReader input, DataOutputStream output) {
        try {
            output.write(("REDY\n").getBytes());
            output.flush();
            System.out.println("Client >\tREDY");

            String sResponse = input.readLine(); //expect jobs
            System.out.println("Server >\t" + sResponse);
        }
        catch (Exception e) {
            System.out.println(e);
        }        
    }

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

    public static void viewServers(ServerInfo[] servers) {
        // Print server information
        for (int i = 0; i < servers.length; i++) {
            servers[i].printServer();
        }
    }
    
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

    public static ServerInfo[] filterLargestServers(ServerInfo[] servers, String largestServer) {
        // Filter servers of type with largest core capacity only
        int num = 0;
        for (int i = 0; i < servers.length; i ++) {
            if (servers[i].getName().equals(largestServer)) {
                num++;
            }
        }
        ServerInfo[] filtered = new ServerInfo[num];
        int index = 0;
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getName().equals(largestServer)) {
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
            System.out.println("<------------read------------>");

            ArrayList<ServerInfo> serversList = readServer(dIn, dOut);
            ServerInfo[] servers = serversList.toArray(new ServerInfo[serversList.size()]);
            servers = filterLargestServers(servers, findLargestServer(servers));
            // viewServers(servers);
            System.out.println("<------------quit------------>");
            quit(dIn, dOut);

            dIn.close();
            dOut.close();
            s.close();
            // viewServers(servers);
        }
        catch (Exception e) {
        }
    }
}