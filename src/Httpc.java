import java.util.Scanner;
import java.util.HashMap;

public class Httpc {
    public static void main(String[] args) {

        while(true){
            // Read the command from the keyboard
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter the Httpc Command: ");
            String input = sc.nextLine();
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
                    String[] header = inputArr[i].substring(1, inputArr[i].length() - 1).split(":");
                    String key = header[0];
                    String value = header[1];
                    headers.put(key, value);
                } else if (inputArr[i].equals("-d")) {
                    body = inputArr[++i];
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
