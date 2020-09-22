import java.net.MalformedURLException;
import java.net.URL;

public class Test {

    public static void main(String[] args) {
        String arg;
//        arg = "post http://httpbin.org/post -d {\"name\":\"bowen\"} -h Content-Type:application/json";
//        arg = "post http://httpbin.org/post -d name=yangbowen&sex=male -h Content-Type:application/x-www-form-urlencoded";
//        arg = "post -v http://httpbin.org/post -d name=yangbowen&sex=male -h Content-Type:application/x-www-form-urlencoded";
//        arg = "post -v -o 1.txt http://httpbin.org/post -d name=yangbowen&sex=male -h Content-Type:application/x-www-form-urlencoded";
//        arg = "post -v -f 1.txt http://httpbin.org/post -h Content-Type:application/json";
        arg = "help";
//        arg = "help get";
//        arg = "help post";
        Httpc.main(arg.split(" "));


    }
}
