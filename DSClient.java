import java.net.*;
import java.io.*;

public class DSClient {
    public static void handshake(BufferedReader input, DataOutputStream output) {
        try {
            output.write(("HELO\n").getBytes());
            output.flush();
            System.out.println("Client >\t" + "HELO");

            String sResponse = input.readLine(); //expect "OK"
            System.out.println("Server >\t" + sResponse);

            String username = "Nathan";
            output.write(("AUTH " + username + "\n").getBytes());
            output.flush();
            System.out.println("Client >\t" + "AUTH " + username);

            sResponse = input.readLine(); //expect "OK"
            System.out.println("Server >\t" + sResponse);

            output.write(("REDY\n").getBytes());
            output.flush();
            System.out.println("Client >\t" + "REDY");

            sResponse = input.readLine(); //expect jobs
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
    public static void main(String args[]) {
        try {
            Socket s = new Socket("localhost", 50000);
            BufferedReader dIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

            handshake(dIn, dOut);

            dIn.close();
            dOut.close();
            s.close();
        }
        catch (Exception e) {
        }
    }
}