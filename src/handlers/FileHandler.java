package handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {
    public static void writeToFile(String fileName, boolean fl, String content) {
        if (fl) {
            try {
                File file = new File(fileName);
                FileOutputStream fos;
                fos = new FileOutputStream(file);
                fos.write(content.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
