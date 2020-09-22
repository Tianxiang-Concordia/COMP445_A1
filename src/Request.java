import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Request {

    String method;
    HashMap<String, String> headers;
    String body;
    String url;

    public Request(String method, HashMap<String, String> headers, String body, String url) {
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.url = url;
    }

    public Response send() {

        String responseStr = "";

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

            responseStr = response.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response(responseStr);

    }
}
