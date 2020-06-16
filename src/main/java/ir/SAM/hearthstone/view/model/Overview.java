package ir.SAM.hearthstone.view.model;

import lombok.Getter;

import java.awt.*;

public abstract class Overview {
    @Getter
    protected final String name,imageName;
    public Overview(String name,String imageName){
        this.name = name;
        this.imageName = imageName;
    }
    public abstract void paint(Graphics g);
}
