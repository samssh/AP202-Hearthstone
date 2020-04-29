import configs.ConfigFactory;
import view.MyFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class Test extends JPanel {


    public static void main(String[] args) {
        String out = "./src/main/resources/images/cards/small-gray/";
        File file = new File("./src/main/resources/images/cards/small");
        File[] files = file.listFiles();
//        File file1 = files[0];
//        int min=1000;
        for (File value : Objects.requireNonNull(files)) {
            try {
                BufferedImage image = ImageIO.read(value);
                BufferedImage result = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_USHORT_GRAY);
                Graphics2D graphic = result.createGraphics();
                graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphic.drawImage( image, 0, 0, null);
                graphic.dispose();
                File output = new File(out + value.getName());
                ImageIO.write(result, "png", output);

            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
////        System.out.println(l.getHeight());
////        System.out.println(l.getWidth());
////        System.out.println(file1.getName());
//        try {
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            File file = new File("./src/main/resources/fonts");
//            File[] files = file.listFiles();
//            for (File value : Objects.requireNonNull(files)) {
//                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, value));
//            }
//        } catch (IOException | FontFormatException e) {
//            e.printStackTrace();
//        }
//        ConfigFactory.getInstance("DEFAULT");
////        Connector connector = new Connector("HIBERNATE_CONFIG");
////        ModelLoader modelLoader = new ModelLoader(connector);
//        JFrame frame = new MyFrame();
//        frame.setLayout(null);
//        JPanel panel = new Test();
//        panel.setLayout(null);
//        frame.setContentPane(panel);
//        panel.setBounds(0, 0, 1200, 700);
//        panel.setBackground(Color.red);


//        String out = "./src/main/resources/images/cards/small-gray/";
//        File file = new File("./src/main/resources/images/cards/big-gray");
//        File[] files = file.listFiles();
//        for (File value : Objects.requireNonNull(files)) {
//            String s = value.getName().replace(' ','-').substring(0,value.getName().length()-4)+"=";
//            String s1 = value.getPath().replace('\\','/');
//            System.out.println(s+s1);
//        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
//        g.setColor(Color.blue);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font("War Priest Condensed", Font.PLAIN, 35));
        graphics2D.drawString("Win RATE", 23, 23);
        graphics2D.drawString("win rate", 23, 123);
        graphics2D.setFont(new Font("War Priest 3D", Font.PLAIN, 35));
        graphics2D.drawString("Win RATE", 223, 23);
        graphics2D.drawString("win rate", 223, 123);
        graphics2D.setFont(new Font("War Priest Expanded", Font.PLAIN, 35));
        graphics2D.drawString("Win RATE", 523, 23);
        graphics2D.drawString("win rate", 523, 123);
        graphics2D.setFont(new Font("War Priest Rotate Regular", Font.PLAIN, 35));
        graphics2D.drawString("Win RATE", 523, 323);
        graphics2D.drawString("win rate", 523, 423);
        graphics2D.setFont(new Font("War Priest", Font.PLAIN, 35));
        graphics2D.drawString("Win RATE", 23, 323);
        graphics2D.drawString("win rate", 23, 423);
    }

    private static BufferedImage gray(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                BufferedImage.TYPE_USHORT_GRAY);
        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        graphic.dispose();
        return result;
    }
}
