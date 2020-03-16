package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;

public class Console {
    private PrintStream out=System.out;
    private InputStream in=System.in;
    private PrintStream filePrinter;

    {
        try {
            filePrinter = new PrintStream(new File("s.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Console console=new Console();
    private Console(){}

    public static Console getConsole(){
        return console;
    }

    public void print(String s){
        filePrinter.println(s);
        out.println(s);
    }

    public String read(){
        return null;
    }

}
