import configs.ConfigFactory;
import view.MyFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.util.Objects;

public class Test{

    private static BufferedImage gray(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(image, result);
        return result;
    }

    public static void main(String[] args) {
        String out = "./src/main/resources/images/cards/big/";
        File file = new File("./src/main/resources/images/cards/temp");
        File[] files = file.listFiles();

//        for (File value : Objects.requireNonNull(files)) {
//            System.out.print(value.getName().substring(0,value.getName().length()-4).replace(' ','-'));
//            System.out.print("=");
//            System.out.println(value.getPath().replace('\\','/'));
//        }

        for (File value : Objects.requireNonNull(files)) {
            try {
                BufferedImage image = ImageIO.read(value);
                File output = new File(out + value.getName());
                ImageIO.write(resize(image,250,350), "png", output);
            } catch (IOException e) {
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


    protected void paintComponent(Graphics graphics) {

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

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
//        BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight() - 23, img.getType());
//        Graphics ga = dest.getGraphics();
//        ga.drawImage(img,0,-20,null);
//        ga.drawImage(img, 0, 0, rect.getWidth(), rect.getHeight(), rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), null);
//        ga.dispose();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
//        BufferedImage dimg = daimg.getSubimage(0,23,daimg.getWidth(),daimg.getHeight()-23);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
}
