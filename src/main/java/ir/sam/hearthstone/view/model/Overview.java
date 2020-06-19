package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.view.Painter;
import lombok.Getter;

import java.awt.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Overview overview = (Overview) o;
        return Objects.equals(name, overview.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
