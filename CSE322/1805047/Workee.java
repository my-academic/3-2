import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Workee extends Thread {
    Socket socket;
    final int chunkSize = 16 * 1024;

    String first = """
             <html>
            	<head>
            		<meta http-equiv=\"Content-Type\" content=\"
            """;

    String second_content_type = """
            text/html""";

    String third = """
            \">
            	</head>
            	<body>""";

    String fourth_content = "";
    String fifth = """
            </body>
            </html>""";

    ArrayList<String> paths = new ArrayList<>();
    HashMap<String, String[]> directoryItems = new HashMap<>();
    static final String path = "root";
    File log = HTTPServerSkeleton.file;

    public void addPaths(String path) {

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

    public Workee(Socket socket) {
        this.socket = socket;
        addPaths(path);
        // System.out.println(paths);
    }

    public String fileRead(String key) throws IOException {

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

    public void header(PrintWriter pr, String content_type, long l, String code, String message, boolean download,
            String filename) throws IOException {

        String str = "";
        str += ("HTTP/1.1 " + code + " " + message + "\r\n");
        str += ("Server: Java HTTP Server: 1.0\r\n");
        str += ("Date: " + new Date() + "\r\n");
        if (download)
            str += ("Content-Disposition: attachment;\r\nfilename=\"" + filename + "\"\r\n");
        str += ("Content-Type: " + content_type + "\r\n");
        str += ("Content-Length: " + l + "\r\n");
        str += ("\r\n");
        pr.write(str);
        FileWriter fr = new FileWriter(log, true);
        fr.write("\nhttp response: \n" + str);
        fr.close();
    }

    public boolean checkNullInput(String input, String msg) throws IOException {
        if (input == null) {
            System.out.println(msg);
            socket.close();
            return true;
        }
        return false;
    }

    public void run() {
        // buffers
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream inputByte = socket.getInputStream();
            DataInputStream dis = new DataInputStream(inputByte);
            PrintWriter pr = new PrintWriter(socket.getOutputStream());

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // System.out.println(dis.read());
            String input = in.readLine();

            System.out.println("input : " + input);

            // String content = "<html>Hello</html>";
            if (input == null) {
                socket.close();
                return;
            }

            if (input.startsWith("Client Connected")) {
                while (socket.isConnected()) {
                    input = in.readLine();
                    if (checkNullInput(input, "client disconnected"))
                        return;

                    if (input.startsWith("error")) {
                        System.out.println(input);
                    } else if (input.startsWith("uploading")) {
                        System.out.println(input);
                        input = in.readLine();
                        if (checkNullInput(input, "client disconnected"))
                            return;

                        if (input.startsWith("success")) {
                            System.out.println(input);
                        }
                    }

                }
            }

            if (input.startsWith("starting upload")) {
                // System.out.println("here");
                // InputStream inputByte = socket.getInputStream();
                // System.out.println("here2");
                // DataInputStream dis = new DataInputStream(inputByte);
                // System.out.println("here3");
                // in.read();
                dos.writeInt(1);
                // System.out.println("here \t" + dis.readAllBytes());
                String filename = dis.readUTF();
                var fileSize = dis.readLong();
                BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
                // System.out.println(filename + " \there4");
                byte[] bytes = new byte[chunkSize];
                int count = 0, total = 0;
                while (fileSize > 0 && (count = dis.read(bytes, 0, (int) Math.min(bytes.length, fileSize))) != -1) {
                    // System.out.println(count);
                    outputFile.write(bytes, 0, count);
                    outputFile.flush();
                    total += count;
                    // System.out.println("uploading " + total);
                }
                System.out.println(total + "\t bytes uploaded. " + filename + " uploaded.");
                outputFile.close();
                dis.close();
                inputByte.close();
                socket.close();
                return;
            }

            String[] requestParam = input.split(" ");
            // System.out.println(requestParam.length);
            if (requestParam.length < 3) {
                socket.close();
                return;
            }
            String path = requestParam[1];
            FileWriter fr = new FileWriter(log, true);
            fr.write("\nhttp request:\n" + input);
            fr.close();

            if (path.startsWith("/root")) {
                if (input.length() > 0) {
                    if (requestParam[0].equals("GET")) {
                        String pathWithoutLeadingSlash = path.substring(1);
                        if (!this.paths.contains(pathWithoutLeadingSlash)) {
                            fourth_content = "<h2> Error 404: page not found</h2>";
                            String content = first + second_content_type + third + fourth_content + fifth;
                            header(pr, "text/html", content.length(), "404", "NOT_FOUND", false, null);
                            pr.write(content);
                            pr.flush();
                            socket.close();
                            return;
                        }

                        for (String s : this.paths) {
                            if (pathWithoutLeadingSlash.equals(s)) {

                                var f = new File(s);

                                if (f.isFile()) {

                                    var fis = new FileInputStream(f);
                                    byte[] bytes = new byte[chunkSize];
                                    OutputStream out = socket.getOutputStream();
                                    if (s.endsWith(".txt")) {
                                        header(pr, "text/html", f.length(), "200", "OK", false, null);
                                        pr.flush();
                                        int count;
                                        while ((count = fis.read(bytes)) > 0) {
                                            out.write(bytes, 0, count);
                                            out.flush();
                                        }
                                        out.close();
                                        fis.close();
                                        socket.close();
                                        return;
                                    } else if (s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg")) {
                                        header(pr, "image/jpeg", f.length(), "200", "OK", false, null);
                                        pr.flush();
                                        int count;
                                        while ((count = fis.read(bytes)) > 0) {
                                            out.write(bytes, 0, count);
                                            out.flush();
                                        }
                                        out.close();
                                        fis.close();
                                        socket.close();
                                        return;
                                    } else {
                                        var splitted = s.split("/");
                                        var filename = splitted[splitted.length - 1];
                                        header(pr, "application/force-download", f.length(), "200", "OK", true,
                                                filename);
                                        pr.flush();
                                        int count;
                                        while ((count = fis.read(bytes)) > 0) {
                                            out.write(bytes, 0, count);
                                            out.flush();
                                        }
                                        out.close();
                                        fis.close();
                                        socket.close();
                                        return;
                                    }
                                }

                                var list = directoryItems.get(s);
                                fourth_content = """
                                        <ul>
                                        """;
                                for (String s1 : list) {
                                    File file = new File(s + "/" + s1);
                                    if (file.isDirectory()) {
                                        fourth_content += "<li><a href=\"" + path + "/" + s1 + "\" ><b><i>" + s1
                                                + "</i></b></a> </li>";
                                    } else {

                                        fourth_content += "<li><a href=\"" + path + "/" + s1 + "\" target=\"_blank\">"
                                                + s1 + "</a> </li>";
                                    }

                                }
                                fourth_content += "</ul>";
                                String content = first + second_content_type + third + fourth_content + fifth;
                                header(pr, "text/html", content.length(), "200", "OK", false, null);
                                pr.write(content);
                                pr.flush();
                            }
                        }

                    }
                }
            }
            else {
                fourth_content = "<h2> Error 404: page not found</h2>";
                String content = first + second_content_type + third + fourth_content + fifth;
                header(pr, "text/html", content.length(), "404", "NOT_FOUND", false, null);
                pr.write(content);
                pr.flush();
                socket.close();
                return;
            }
            // System.out.println("closing connection");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
