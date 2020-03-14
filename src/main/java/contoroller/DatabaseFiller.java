package contoroller;

import hibernate.*;
import model.*;

import java.util.List;

public class DatabaseFiller {
    public static void fill(){
        Connector connector= Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        writeHero();
        connector.commit();
        connector.beginTransaction();
        writeCart();
        connector.commit();
        connector.close();
    }
    // hero
    private static void writeHero(){
        new Hero("Mage","She is a skilled wizard who has special skills in making spells."
                ,30).saveOrUpdate();
        new Hero("Rogue",
                "He is a thief and most of his abilities are in stealing from the enemy!"
                ,30).saveOrUpdate();
        new Hero("Warlock",
                "You will never see anyone beyond him. He passes on his life and property and sacrifices something to win the war."
                ,35).saveOrUpdate();
    }
    // carts
    private static void writeCart(){
        ClassOfCard mage=new ClassOfCard("Mage");
        mage.saveOrUpdate();
        ClassOfCard rogue=new ClassOfCard("Rogue");
        rogue.saveOrUpdate();
        ClassOfCard warlock=new ClassOfCard("Warlock");
        warlock.saveOrUpdate();
        ClassOfCard neutral=new ClassOfCard("Neutral");
        neutral.saveOrUpdate();
        new Spell("polymorph",
                "transforms a target minion into a 1/1 Sheep.",
                mage,Rarity.Rare,4).saveOrUpdate();
        new Spell("Pyroblast",
                "Deal 10 damage.",
                mage,Rarity.Epic,10).saveOrUpdate();
        new Minion("Archmage Antonidas","Whenever you cast a spell, put a 'Fireball' spell into your hand.,",
                mage,Rarity.Legendary,7,5,7);
        new Spell("Friendly Smith",
                "Discover a weapon from any class. Add it to your Adventure Deck with +2/+2.",
                rogue,Rarity.Common,1).saveOrUpdate();
        new Spell("Preparation",
                "The next spell you cast this turn costs (2) less.",
                rogue,Rarity.Epic,0).saveOrUpdate();
        new Minion("Crazed Chemist","Combo: Give a friendly minion +4 Attack.",
                rogue,Rarity.Common,5,4,4).saveOrUpdate();
        new Spell("Spirit Bomb","Deal 4 damage to a minion and your hero.",
                warlock,Rarity.Common,1).saveOrUpdate();
        new Minion("Ratcatcher","Rush Battlecry: Destroy a friendly minion and gain its Attack and Health.",
                warlock,Rarity.Epic,3,2,2).saveOrUpdate();
        new Minion("Dreadscale","At the end of your turn, deal 1 damage to all other minions.",
                warlock,Rarity.Legendary,3,4,2).saveOrUpdate();
        new Minion("Living Monument","Taunt",neutral
                ,Rarity.Common,10,10,10).saveOrUpdate();
        new Minion("Wisp","",neutral,Rarity.Common,0,1,1).saveOrUpdate();
        new Minion("Potion Vendor","Battlecry: Restore 2 Health to all friendly characters.",
                neutral,Rarity.Common,1,1,1).saveOrUpdate();
        new Minion("Kobold Sandtrooper","Deathrattle: Deal 3 damage to enemy hero.",
                neutral,Rarity.Common,2,2,1).saveOrUpdate();
        new Minion("Arena Fanatic","Battlecry: Give all minions in your hand +1/+1.",
                neutral,Rarity.Common,4,2,3).saveOrUpdate();
        new Minion("Young Priestess","At the end of your turn, give another random friendly minion +1 Health.",
                neutral,Rarity.Rare,1,2,1).saveOrUpdate();
        new Minion("Questing Adventurer","Whenever you play a card, gain +1/+1."
                ,neutral,Rarity.Rare,3,2,2).saveOrUpdate();
        new Minion("Arena Patron","Overkill: Summon another Arena Patron.",
                neutral,Rarity.Rare,5,3,3).saveOrUpdate();
        new Minion("Khartut Defender","Taunt, Reborn Deathrattle:Restore 3 Health to your hero.",
                neutral,Rarity.Rare,6,3,4).saveOrUpdate();
        new Minion("Weaponized Pinata","Deathrattle: add a random Legendary minion to your hand",
                neutral,Rarity.Epic,4,4,3).saveOrUpdate();
        new Minion("Big Game Hunter","Battlecry: Destroy a minion with 7 or more attack.",
                neutral,Rarity.Epic,5,4,2).saveOrUpdate();
        new Minion("Batterhead","Rush. After this attacks and kills a minion, it may attack again.",
                neutral,Rarity.Epic,8,3,12).saveOrUpdate();
        new Minion("Baron Geddon","At the end of your turn, deal 2 damage to all other characters.",
                neutral,Rarity.Legendary,7,7,5).saveOrUpdate();
        new Minion("Malygos","Spell Damage +5.",
                neutral,Rarity.Legendary,6,4,12).saveOrUpdate();
    }


    public static void main(String[] args) {
        fill();
        Connector connector=Connector.getConnector();
        connector.open();
        List heros=ManualMapping.fetchAll(Hero.class);
        heros.stream().forEach(System.out::println);
        List minions=ManualMapping.fetchAll(Minion.class);
        minions.stream().forEach(System.out::println);
        List spells=ManualMapping.fetchAll(Spell.class);
        spells.stream().forEach(System.out::println);
        connector.close();
        System.exit(0);
    }
}
