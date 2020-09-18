package test;

import controllers.fileHandler;

public class util {
    public static void main(String[] args) {
        fileHandler f=new fileHandler();
        f.writeToFile("test.txt",true,"test02");
    }
}
