package ir.sam.hearthstone.view.graphics_engine;

public class IllegalIndex extends RuntimeException{
    public IllegalIndex(int index) {
        super(String.format("index %d not in bound",index));
    }
}
