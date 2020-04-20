package view;

import configs.Config;
import configs.ConfigFactory;
import controller.Loop;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private int fps;

    public MyFrame() {
        this.config();
        this.setVisible(true);
        new Loop(fps, this::update).start();
    }

    private void config() {
        Config frameConfig = ConfigFactory.getInstance("").getConfig("FRAME_CONFIG");
        setSize(new Dimension(frameConfig.getProperty(Integer.class, "width")
                , frameConfig.getProperty(Integer.class, "height")));
        setDefaultCloseOperation(frameConfig.getProperty(Integer.class, "closeOperation"));
        setResizable(frameConfig.getProperty(Boolean.class, "resizable"));
        setUndecorated(frameConfig.getProperty(Boolean.class, "undecorated"));
        setTitle(frameConfig.getProperty(String.class, "title"));
        fps = frameConfig.getProperty(Integer.class, "fps");
    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        super.revalidate();
        super.repaint();
    }

    private void update() {
        super.revalidate();
        super.repaint();
    }
}