package test;
import static handlers.FileHandler.writeToFile;

public class util {
    public static void main(String[] args) {
        writeToFile("test.txt",true,"test02");
    }
}
