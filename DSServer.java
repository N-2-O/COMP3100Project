import java.net.*;
import java.io.*;

class DSServer {
    public static void main(String args[]) {
        try {
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            
            String cResponse = dIn.readUTF();
            System.out.println();
            dOut.writeUTF(cResponse);
            dOut.flush();

            dIn.close();
            s.close();
            ss.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
