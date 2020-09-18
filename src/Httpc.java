import java.util.Scanner;
import java.util.HashMap;

import static controllers.helpController.printHelp;

public class Httpc {
    public static void main(String[] args) {

        while (true) {
            // Read the command from the keyboard
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter the Httpc Command(-q for exit): ");
            String input = sc.nextLine();
            if (input.equals("-q")) {
                break;
            }
            //help part
            if (input.matches("^httpc help.*")) {
                printHelp(input);
                continue;
            }
            //deal with the {"Assignment":      1}
            input = input.replaceAll(":\\s+", ":");
            System.out.println(input);

            String[] inputArr = input.split(" ");
            //Request property
            String method = "GET";  // GET is the default Request Type
            boolean isShowHeader = false;   // Not show the header is the default setting
            HashMap<String, String> headers = new HashMap<>();
            String body = "";
            String filePath = "";
            String url = "";

            for (int i = 0; i < inputArr.length; i++) {
                if (inputArr[i].toLowerCase().equals("post")) {
                    method = "POST";
                } else if (inputArr[i].equals("-v")) {
                    isShowHeader = true;
                } else if (inputArr[i].equals("-h")) {
                    //looking for next item, and put it in the Hashmap
                    i++;
                    String[] header = inputArr[i].split(":");
                    if (header.length < 2) {
                        System.out.println("Header's format is invalid");
                    } else {
                        String key = header[0];
                        String value = header[1];
                        // deal with " "
                        if (key.startsWith("\"") || key.startsWith("\'")) {
                            key = key.substring(1, key.length());
                        }
                        if (value.endsWith("\"") || value.endsWith("\'")) {
                            value = value.substring(0, value.length() - 1);
                        }
                        headers.put(key, value);
                    }

                } else if (inputArr[i].equals("-d")) {
                    body = inputArr[++i];
                    // deal with " "
                    if (body.startsWith("\"") || body.startsWith("\'")) {
                        body = body.substring(1, body.length());
                    }
                    if (body.endsWith("\"") || body.endsWith("\'")) {
                        body = body.substring(0, body.length() - 1);
                    }
                } else if (inputArr[i].equals("-f")) {
                    filePath = inputArr[++i];
                } else if (inputArr[i].matches("^http://(.)*")) {
                    url = inputArr[i];
                }

            }

            System.out.println("requestMethod: " + method);
            System.out.println("isShowHeader: " + isShowHeader);
            System.out.println("headers: " + headers);
            System.out.println("body: " + body);
            System.out.println("filePath: " + filePath);
            System.out.println("url: " + url);


        }
    }
}
