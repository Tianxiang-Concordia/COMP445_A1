import java.util.HashMap;

public class Response {
    String status;
    HashMap<String, String> headers;
    String body;
    String rawData;

    public Response(String status, HashMap<String, String> headers, String body, String rawData) {
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.rawData = rawData;
    }

    public String toString(){
        return rawData;
    }
    public String showBody(){
        return body;
    }
}
