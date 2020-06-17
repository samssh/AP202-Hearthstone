package ir.sam.hearthstone.util;
@FunctionalInterface
public interface Visitable<Visitor> {
    void accept(Visitor visitor);
}
