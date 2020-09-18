package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class fileHandler {
    public void writeToFile(String fileName,boolean fl,String content){
        if (fl){
            try {
                File file = new File(fileName);
                FileOutputStream fos = null;
                fos = new FileOutputStream(file);
                fos.write(content.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
