package COMP3100Project46461019;

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class DSClient {
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

    public static ArrayList<ServerInfo> readServer(BufferedReader input, DataOutputStream output) {
        ArrayList<ServerInfo> servers = new ArrayList<ServerInfo>();
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
            for (int i = 0; i < Integer.parseInt(data[1]); i++) {
                sResponse = input.readLine(); //expect server information
                System.out.println("Server >\t" + sResponse);
                ServerInfo toAdd = new ServerInfo(sResponse.split(" "));
                servers.add(toAdd);
            }

            output.write(("OK\n").getBytes());
            output.flush();
            System.out.println("Client >\tOK");

            sResponse = input.readLine(); 
            System.out.println("Server >\t" + sResponse);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return servers;
    }

    public static void quit(BufferedReader input, DataOutputStream output) {
        try {
            output.write(("REDY\n").getBytes());
            output.flush();
            System.out.println("Client >\tREDY");

            String sResponse = input.readLine(); //expect jobs
            System.out.println("Server >\t" + sResponse);

            output.write(("QUIT\n").getBytes());
            output.flush();
            System.out.println("Client >\t" + "QUIT");

            sResponse = input.readLine(); //expect "QUIT"
            System.out.println("Server >\t" + sResponse);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void viewServers(ArrayList<ServerInfo> servers) {
        for (int i = 0; i < servers.size(); i++) {
            System.out.print(servers.get(i).serverName + " ");
            System.out.print(servers.get(i).serverID + " ");
            System.out.print(servers.get(i).status + " ");
            System.out.print(servers.get(i).startTime + " ");
            System.out.print(servers.get(i).core + " ");
            System.out.print(servers.get(i).memory + " ");
            System.out.print(servers.get(i).disk + " ");
            System.out.println();
        }
    }
    public static void main(String args[]) {
        try {
            Socket s = new Socket("localhost", 50000);
            BufferedReader dIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

            System.out.println("<------------Handshake------------>");
            handshake(dIn, dOut);
            System.out.println("<------------read------------>");
            ArrayList<ServerInfo> servers = readServer(dIn, dOut);
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