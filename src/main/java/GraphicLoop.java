import configs.ConfigFactory;
import hibernate.Connector;
import util.ModelLoader;
import view.MyFrame;
import view.util.CardBox;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GraphicLoop extends JButton {


    public static void main(String[] args) throws IOException {
//        File file = new File("./src/main/resources/images/cards/small");
//        File[] files = file.listFiles();
////        File file1 = files[0];
////        int min=1000;
//        for (File value : Objects.requireNonNull(files)) {
//            String s1 = value.getName().substring(0,value.getName().length()-4)+"=";
//            s1=s1.replace(' ','-');
//            String d= value.getPath().replace('\\','/');
//            System.out.println(s1+d);
//        }
////        System.out.println(l.getHeight());
////        System.out.println(l.getWidth());
////        System.out.println(file1.getName());
        ConfigFactory.getInstance("DEFAULT");
        Connector connector = new Connector("HIBERNATE_CONFIG");
        ModelLoader modelLoader = new ModelLoader(connector);
        JFrame frame = new MyFrame();
        frame.setLayout(null);
        JPanel panel= new JPanel();
        panel.setLayout(null);
        frame.setContentPane(panel);
        panel.setBounds(0,0,1200,700);
        panel.setBackground(Color.red);
        modelLoader.getCards().add(modelLoader.getCards().get(modelLoader.getCards().size()-1));
        modelLoader.getCards().add(0,modelLoader.getCards().get(0));
//        CardBox cardBox = new CardBox(2,3,panel,true);
//        cardBox.setCardList(modelLoader.getCards());
//        cardBox.setLocation(100,100);
//        cardBox.setTitle("TEST");
//        panel.add(cardBox);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("sadscasc");
    }
}
