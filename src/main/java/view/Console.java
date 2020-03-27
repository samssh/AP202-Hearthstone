package view;

import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Console {
    private PrintStream out = System.out;
    private InputStream in = System.in;
    private Scanner inScanner;
    private PrintStream filePrinter;

    {
        inScanner = new Scanner(in);
        try {
            filePrinter = new PrintStream(new File("s.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Getter
    private static Console console = new Console();

    private Console() {
    }

    public void print(String s) {
        filePrinter.println(s);
        out.println(s);
    }

    public String read() {
        return inScanner.nextLine();
    }

}
