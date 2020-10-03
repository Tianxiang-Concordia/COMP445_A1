import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response {

    String status;
    HashMap<String, String> headers = new HashMap<>();
    String body;
    String rawData;

    public Response(String rawData) {
        this.rawData = rawData;
        this.init();
    }

    public void init() {
        String[] responseArr = rawData.split("\r\n");
        //Get the response status
        Pattern statusRegExp = Pattern.compile("(\\d{3})");
        Matcher m = statusRegExp.matcher(responseArr[0]);
        while (m.find())
            this.status = m.group(1);

        //Get the response header and body
        for (int i = 1; i < responseArr.length; i++) {
            if (responseArr[i].equals("")) {
                String[] bodyArr = Arrays.copyOfRange(responseArr, ++i, responseArr.length);
                this.body = String.join("", bodyArr);
                break;
            } else {
                String[] header = responseArr[i].split(": ");
                this.headers.put(header[0], header[1]);
            }
        }
    }

    public String toString() {
        return rawData;
    }

    public String getBody() {
        return body;
    }
}
