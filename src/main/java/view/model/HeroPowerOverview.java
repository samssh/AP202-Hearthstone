package view.model;

import model.HeroPower;
import model.Passive;

import java.awt.*;

public class HeroPowerOverview extends Overview{
    private final int mana;
    public HeroPowerOverview(HeroPower heroPower) {
        super(heroPower.getName(),heroPower.getName());
        mana = heroPower.getManaFrz();
    }

    @Override
    public void paint(Graphics g) {

    }
}
