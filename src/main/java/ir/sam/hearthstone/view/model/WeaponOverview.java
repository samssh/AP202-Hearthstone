package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.main.Weapon;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.server.logic.game.behavioral_models.WeaponLogic;

import java.awt.*;

public class WeaponOverview extends CardOverview{


    public WeaponOverview(Weapon weapon) {
        super(weapon, 1, false);
        small = ImageLoader.getInstance().getWeapon(imageName);
    }

    public WeaponOverview(WeaponLogic weaponLogic) {
        super(weaponLogic.getName(), weaponLogic.getName()
                , "class of card: " + weaponLogic.getCard().getClassOfCard().getHeroName()
                ,1,weaponLogic.getCard().getPrice(), weaponLogic.getCard().getManaFrz()
                ,weaponLogic.getAttack(),weaponLogic.getUsage(),false);
        small = ImageLoader.getInstance().getWeapon(imageName);

    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.drawImage(small, 0, 0, null);
        int w = small.getWidth(), h = small.getHeight();
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD));
        graphics2D.setFont(graphics2D.getFont().deriveFont(21.0F));
        graphics2D.drawString("" + hp, 73 * w / 100, 77 * h / 90);
        graphics2D.drawString("" + att, 14 * w / 100, 73 * h / 90);
    }
}
