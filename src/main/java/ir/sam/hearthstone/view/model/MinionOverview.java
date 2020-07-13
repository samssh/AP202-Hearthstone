package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.main.Minion;
import ir.sam.hearthstone.resource_manager.ImageLoader;

import java.awt.*;

public class MinionOverview extends CardOverview {


    public MinionOverview(Minion minion) {
        super(minion, 1, false);
        small = ImageLoader.getInstance().getMinion(imageName);
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.drawImage(small,0,0,null);
        int w =small.getWidth(),h = small.getHeight();
        graphics2D.drawString(""+hp,67*w/90,100*h/120);
        graphics2D.drawString(""+att,12*w/90,100*h/120);
    }
}
