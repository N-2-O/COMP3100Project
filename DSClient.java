import java.net.*;
import java.io.*;
import java.util.*;

public class DSClient {
    public static void main(String args[]) {
        try {
            Socket s = new Socket("localhost", 6666);
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

            Scanner scanner = new Scanner(System.in);
            System.out.print(">\t");
            String userIn = scanner.nextLine();

            dOut.writeUTF(userIn);
            dOut.flush();
            
            String sResponse = dIn.readUTF();
            System.out.println(">\t" + sResponse);

            dIn.close();
            s.close();
            scanner.close();
            s.close();
            scanner.close();
        }
        catch (Exception e) {
        }
    }
}