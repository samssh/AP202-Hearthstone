package view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;

public class Console {
    private PrintStream out=System.out;
    private InputStream in=System.in;
    private static Console console=new Console();
    private Console(){}

    public static Console getConsole(){
        return console;
    }

    public void print(){

    }

    public String read(){
        return null;
    }

}
