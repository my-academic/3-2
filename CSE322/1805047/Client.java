

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
    static int port = 5047;

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
    }
}
