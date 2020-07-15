package ir.sam.hearthstone.util;

public interface Visitable<Visitor> {
    void accept(Visitor visitor);
}
