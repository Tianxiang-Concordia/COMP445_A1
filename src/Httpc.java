
public class Httpc {

    public static void main(String[] args) {
        try {
            HttpcService service = new HttpcService(args);
            service.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
