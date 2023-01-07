
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HTTPServerSkeleton {
    static final int PORT = 5047;

    static final String path = "root";

    static final ArrayList<String> paths = new ArrayList<>();
    static final HashMap<String, String[]> directoryItems = new HashMap<>();

    public static void addPaths(String path) {

        File directoryPath = new File(path);
        paths.add(path);
        String contents[] = directoryPath.list();

        directoryItems.put(path, contents);

        for (int i = 0; i < contents.length; i++) {
            var str = path + "/" + contents[i];
            var file = new File(str);
            if (file.isDirectory()) {
                addPaths(str);
            } else {
                paths.add(str);
            }
        }
    }

    public static File file = new File("log.log");

    public static void main(String[] args) throws IOException {

        file.createNewFile();


        ServerSocket serverConnect = new ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

        // addPaths(path);
//        System.out.println(paths);


        while (true) {
            Socket s = serverConnect.accept();
            // if(s.

            // open thread
            Thread workee = new Workee(s);
            workee.start();
        }

    }

}
