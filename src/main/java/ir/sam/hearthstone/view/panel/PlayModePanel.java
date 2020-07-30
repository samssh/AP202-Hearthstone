package ir.sam.hearthstone.view.panel;

import ir.sam.hearthstone.client.actions.PlayModeAction;
import ir.sam.hearthstone.resource_manager.Config;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.view.util.MyMouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PlayModePanel extends JPanel implements MyMouseListener {
    private final BufferedImage background;
    private List<String> optionNames;
    private List<Integer> optionX, optionY;
    private final List<BufferedImage> optionImages;
    private int x, y, width, height;
    private final PlayModeAction playModeAction;

    public PlayModePanel(PlayModeAction playModeAction) {
        this.playModeAction = playModeAction;
        this.config();
        background = ImageLoader.getInstance().getBackground("play mode");
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        this.addMouseListener(this);
        this.optionImages = new ArrayList<>();
        for (String name : optionNames) {
            optionImages.add(ImageLoader.getInstance().getPlayMode(name));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.setFont(g.getFont().deriveFont(20.0F));
        g.setColor(Color.red);
        for (int i = 0, optionImagesSize = optionImages.size(); i < optionImagesSize; i++) {
            g.translate(optionX.get(i), optionY.get(i));
            BufferedImage image = optionImages.get(i);
            String name = optionNames.get(i);
            g.drawImage(image, 0, 0, null);
            int x = (image.getWidth() - g.getFontMetrics().stringWidth(name)) / 2;
            int y = (image.getHeight() + g.getFontMetrics().getMaxAscent());
            g.drawString(name, x, y);
            g.translate(-1 * optionX.get(i), -1 * optionY.get(i));
        }
    }

    private void config() {
        Config config = ConfigFactory.getInstance().getConfig("PLAY_MODE_PANEL");
        width = config.getProperty(Integer.class, "width");
        height = config.getProperty(Integer.class, "height");
        x = config.getProperty(Integer.class, "x");
        y = config.getProperty(Integer.class, "y");
        optionNames = config.getPropertyList(String.class, "optionNames");
        optionX = config.getPropertyList(Integer.class, "optionX");
        optionY = config.getPropertyList(Integer.class, "optionY");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0, optionNamesSize = optionNames.size(); i < optionNamesSize; i++) {
            Rectangle rectangle = new Rectangle(optionX.get(i), optionY.get(i), optionImages.get(i).getWidth(), optionImages.get(i).getHeight());
            if (rectangle.contains(e.getPoint())) {
                playModeAction.select(optionNames.get(i));
                break;
            }
        }
    }
}
