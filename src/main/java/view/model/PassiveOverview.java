package view.model;

import model.Passive;
import util.ImageLoader;

import java.awt.*;

public class PassiveOverview extends Overview{

    public PassiveOverview(Passive p) {
        super(p.getName(),p.getName());
    }

    @Override
    public void paint(Graphics g) {
        Image image = ImageLoader.getInstance().getPassive(imageName);
        g.drawImage(image,0,0,null);
    }
}
