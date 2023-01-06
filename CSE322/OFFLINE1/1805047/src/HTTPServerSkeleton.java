
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HTTPServerSkeleton {
    static final int PORT = 9999;

    public static String readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return String.valueOf(fileData);
    }

    public static String fileRead(String key) throws IOException {

        File file = new File("index.html");
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        sb.append(key);

        return sb.toString();
    }

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
