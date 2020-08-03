package ir.sam.hearthstone.client.view;

import java.awt.*;

public interface Painter {
    void paint(Graphics2D graphics2D);

    int getWidth();

    int getHeight();
}
