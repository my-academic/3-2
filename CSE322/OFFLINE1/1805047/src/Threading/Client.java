package Threading;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    final static int chunkSize = 1024 * 1024;
    static int port = 9999;

    public static boolean isValid (String filename) {
        return 
        filename.endsWith(".txt") 
        || filename.endsWith(".png") 
        || filename.endsWith(".jpg") 
        || filename.endsWith(".mp4")
        || filename.endsWith(".mkv")
        ;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        System.out.println("Connection established");
        System.out.println("Remote port: " + socket.getPort());
        System.out.println("Local port: " + socket.getLocalPort());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Scanner scanner = new Scanner(System.in);
        bw.flush();
        bw.write("Client Connected");
        bw.newLine();
        bw.flush();

        while (true) {
            System.out.print("enter file name: ");
            String filename = scanner.nextLine();
            // System.out.println(filename);
            if(!isValid(filename)) {
                System.out.println("file name is not valid");
                // bw.flush();
                bw.write("error: name format is invalid...");
                bw.newLine();
                bw.flush();
        
                continue;
            }
            else {
                System.out.println("file name is valid");
                bw.write("uploading => " + filename);
                bw.newLine();
                bw.flush();
            }
            Socket s = new Socket("localhost", port);
            Thread upload = new Upload(s, filename, bw);
            upload.start();
        }

        // buffers
        // ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        // ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        // BufferedWriter bw = new BufferedWriter(new
        // OutputStreamWriter(socket.getOutputStream()));
        // // ObjectOutputStream oos = new ObjectOutputSteam(out);
        // OutputStream out = socket.getOutputStream();
        // DataOutputStream dos = new DataOutputStream(out);
        // bw.flush();
        // dos.flush();
        // bw.write("uploading...");
        // bw.newLine();
        // bw.flush();
        // // dos.flush();
        // // socket.
        // String filename = "time_lapse.mkv";
        // File file = new File("client/" + filename);
        // var fis = new FileInputStream(file);
        // System.out.println("filename sending");
        // // dos.flush();
        // dos.writeUTF(filename);
        // dos.flush();
        // System.out.println("filename sent successfully");
        // dos.writeLong(file.length());
        // dos.flush();
        // byte[] bytes = new byte[chunkSize];
        // int count;
        // int total = 0;
        // while ((count = fis.read(bytes)) > 0) {
        // dos.write(bytes, 0, count);
        // dos.flush();
        // total += count;
        // // System.out.println(total);
        // }
        // System.out.println("closing connection");
        // dos.close();
        // fis.close();
        // socket.close();

        //
        // while (true) {
        // // pr.flush();
        // System.out.println("msg");
        // // break;
        // try {
        // TimeUnit.SECONDS.sleep(1);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // socket.close();
    }
}
