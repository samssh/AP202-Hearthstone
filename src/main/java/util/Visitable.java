package util;
@FunctionalInterface
public interface Visitable<Visitor> {
    void accept(Visitor visitor);
}
