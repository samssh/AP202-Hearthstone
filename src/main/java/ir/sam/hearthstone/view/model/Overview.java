package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.view.Painter;
import lombok.Getter;

import java.awt.*;

public abstract class Overview implements Painter {
    @Getter
    protected final String name,imageName;
    public Overview(String name,String imageName){
        this.name = name;
        this.imageName = imageName;
    }
    public abstract void paint(Graphics2D graphics2D);

    public abstract int getWidth();

    public abstract int getHeight();
}
