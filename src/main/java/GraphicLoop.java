import configs.Config;
import configs.ConfigFactory;
import controller.Loop;
import hibernate.Connector;
import model.Card;
import model.Player;
import view.MyFrame;
import view.panel.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphicLoop extends JButton {


    public static void main(String[] args) {
        JFrame frame=new MyFrame();
        GraphicLoop b=new GraphicLoop();
        frame.add(b);
        b.setBounds(0,0,40,60);
        frame.setSize(200,300);
        frame.setLayout(null);
        frame.setVisible(true);
        if(new Scanner(System.in).nextBoolean())
            frame.repaint();


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("sadscasc");
    }
}
