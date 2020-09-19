import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Request {

    String method;
    HashMap<String, String> headers;
    String body;
    String filePath;
    String url;

    public Request(String method, HashMap<String, String> headers, String body, String filePath, String url) {
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.filePath = filePath;
        this.url = url;
    }

    public Response send() {

        String res_status = "";
        HashMap<String, String> res_headers = new HashMap<>();
        String res_body = "";
        String res_responseStr = "";

        try {
            // parse the Url into the host, port, path and query
            URL url = new URL(this.url);
            String host = url.getHost();
            int port = url.getPort() == -1 ? 80 : url.getPort();
            String path = url.getPath().equals("") ? "/" : url.getPath();
            String query = url.getQuery();

            //create a new request
            StringBuilder request;

            if (query == null) {
                request = new StringBuilder(this.method + " " + path + " HTTP/1.0\r\n");
            } else {
                request = new StringBuilder(this.method + " " + path + "?" + query + " HTTP/1.0\r\n");
            }

            // If the Host is not specified in the header, the default Host is added to header
            if (!headers.containsKey("Host")) {
                headers.put("Host", host);
            }

            //  If the User-Agent is not specified in the header, the default User-Agent is added to header
            if (!headers.containsKey("User-Agent")) {
                headers.put("User-Agent", "Concordia-HTTP/1.0");
            }

            headers.put("Content-Length", String.valueOf(body.length()));

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
            }

            if (!this.body.equals("")) {
                request.append("\r\n");
                request.append(this.body);
            } else if (!this.filePath.equals("")) {
                // @TODO File Upload
                request.append("\r\n");
                request.append(this.filePath);
            }

            request.append("\r\n");

            // Create socket object,send the request and get the response
            Socket socket = new Socket(host, port);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(request.toString().getBytes());
            outputStream.flush();
            StringBuilder response = new StringBuilder();
            int data = inputStream.read();
            while (data != -1) {
                response.append((char) data);
                data = inputStream.read();
            }
            socket.close();

            res_responseStr = response.toString();
            String[] responseArr = res_responseStr.split("\r\n");
            //Get the response status
            Pattern statusRegExp = Pattern.compile("(\\d{3})");
            Matcher m = statusRegExp.matcher(responseArr[0]);
            while (m.find())
                res_status = m.group(1);

            //Get the response header and body
            for (int i = 1; i < responseArr.length; i++) {
                if (responseArr[i].equals("")) {
                    res_body = responseArr[++i];
                    break;
                } else {
                    String[] header = responseArr[i].split(": ");
                    res_headers.put(header[0], header[1]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response(res_status, res_headers, res_body, res_responseStr);

    }
}
