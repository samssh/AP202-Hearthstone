package ir.sam.hearthstone.server.model.client;

import lombok.Getter;

import java.util.Objects;

public abstract class Overview {
    @Getter
    protected final String name, imageName;

    public Overview(String name, String imageName) {
        this.name = name;
        this.imageName = imageName;
    }

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
