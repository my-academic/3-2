package Threading;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Upload extends Thread{

    public Socket socket;
    public String filename;
    public BufferedWriter bufferedWriter;
    final static int chunkSize = 16 * 1024 ;


    public Upload(Socket socket, String filename) {
        this.socket = socket;
        this.filename = filename;
    }

    public Upload(Socket s, String filename, BufferedWriter bw) {
        this.socket = s;
        this.filename = filename;
        this.bufferedWriter = bw;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // ObjectOutputStream oos = new ObjectOutputSteam(out);
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.flush();
            bw.flush();
            bw.write("starting upload...");
            bw.newLine();
            bw.flush();
            // dos.flush();
            // socket.
            // String filename = "abcd.txt";
            File file = new File("client/" + filename);
            var fis = new FileInputStream(file);
            // System.out.println("filename sending");
            // dos.flush();
            int x = dis.readInt();
            // System.out.println("here" + x);
            if(x != 1) {
                socket.close();
                fis.close();
                return ;
            }

            dos.writeUTF(filename);
            dos.flush();
            // System.out.println("filename sent successfully");
            dos.writeLong(file.length());
            dos.flush();
            byte[] bytes = new byte[chunkSize];
            int count;
            int total = 0;
            while ((count = fis.read(bytes)) > 0) {
                dos.write(bytes, 0, count);
                dos.flush();
                total += count;
            }
            System.out.println(total + " bytes sent. " + filename + " uploaded.");

            System.out.println("closing connection");
            dos.close();
            fis.close();
            socket.close();
            bufferedWriter.write("successfully uploaded => " + filename);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
