import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class HttpcService {

    private String[] input;
    private String method = "GET";  // GET is the default Request Type
    private boolean isShowHeader = false;   // Not show the header is the default setting
    private HashMap<String, String> headers = new HashMap<>();
    private String body = "";
    private String url = "";
    private boolean isPrintToFile = false;
    private String fileName = "";
    private int redirectionTimes = 0;

    public HttpcService(String[] args) throws Exception {
        this.input = args;
        initService();
    }

    public void initService() throws Exception {
        for (int i = 0; i < this.input.length; i++) {
            if (this.input[i].toLowerCase().equals("post")) {
                this.method = "POST";
            } else if (this.input[i].equals("-v")) {
                this.isShowHeader = true;
            } else if (this.input[i].equals("-h")) {
                //looking for next item, and put it in the Hashmap
                i++;
                String[] header = this.input[i].split(":", 2);
                if (header.length < 2) {
                    throw new Exception("Header's format is invalid");
                } else {
                    String key = header[0];
                    String value = header[1];
                    this.headers.put(key, value);
                }
            } else if (this.input[i].equals("-d")) {
                this.body = this.input[++i];
            } else if (this.input[i].equals("-f")) {
                String filePath = this.input[++i];
                this.body = readFromFile(filePath);
            } else if (this.input[i].matches(".*http://(.)*")) {
                this.url = this.input[i];
            } else if (this.input[i].equals("-o")) {
                this.isPrintToFile = true;
                this.fileName = this.input[++i];
            }
        }
    }

    public void serve() throws Exception {
        if (this.input[0].equals("help")) {
            this.printHelp();
        } else {
            Request req = new Request(this.method, this.headers, this.body, this.url);
            Response res = req.send();

            while (res.status.matches("3\\d{2}")) {
                if (redirectionTimes > 10) {
                    throw new Exception("Redirection too many times!");
                }
                this.redirectionTimes++;
                if (this.isShowHeader) {
                    if (isPrintToFile) {
                        printOutputToFile(res.toString());
                    } else {
                        System.out.println(res.toString());
                    }
                } else {
                    if (isPrintToFile) {
                        printOutputToFile(res.showBody());
                    } else {
                        System.out.println(res.showBody());
                    }
                }
                Request rq = new Request("GET", new HashMap<>(), "", res.headers.get("Location"));
                res = rq.send();
            }
            if (isShowHeader) {
                if (isPrintToFile) {
                    printOutputToFile(res.toString());
                } else {
                    System.out.println(res);
                }
            } else {
                if (isPrintToFile) {
                    printOutputToFile(res.showBody());
                } else {
                    System.out.println(res.showBody());
                }
            }
        }
    }

    public void printHelp() {

        if (this.input.length == 1 && this.input[0].equals("help")) {
            System.out.println("httpc is a curl-like application but supports HTTP protocol only.");
            System.out.println("Usage:");
            System.out.println("    httpc command [arguments]");
            System.out.println("The commands are:");
            System.out.println("    get     executes a HTTP GET request and prints the response.");
            System.out.println("    post    executes a HTTP POST request and prints the response.");
            System.out.println("    help    prints this screen.");
            System.out.println();
            System.out.println("Use \"httpc help [command]\" for more information about a command.");
        } else if (this.input.length == 2 && this.input[0].equals("help") && this.input[1].equals("get")) {
            System.out.println("usage: httpc get [-v] [-h key:value] URL");
            System.out.println();
            System.out.println("    -v      Prints the detail of the response such as protocol, status, and headers.");
            System.out.println("    -h      key:value Associates headers to HTTP Request with the format 'key:value'.");
        } else if (this.input.length == 2 && this.input[0].equals("help") && this.input[1].equals("post")) {
            System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL");
            System.out.println();
            System.out.println("Post executes a HTTP POST request for a given URL with inline data or from file");
            System.out.println();
            System.out.println("    -v      Prints the detail of the response such as protocol, status, and headers.");
            System.out.println("    -h      key:valueAssociates headers to HTTP Request with the format 'key:value'.");
            System.out.println("    -d      stringAssociates an inline data to the body HTTP POST request.");
            System.out.println("    -f      fileAssociates the content of a file to the body HTTP POST request.");
            System.out.println();
            System.out.println("Either [-d] or [-f] can be used but not both.");
        } else {
            System.out.println("Unknown Command");
        }
    }

    public static String readFromFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString().trim();
    }

    public void printOutputToFile(String content) {
        try {
            File file = new File(this.fileName);
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
