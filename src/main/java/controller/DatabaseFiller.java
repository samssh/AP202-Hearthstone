package controller;

import hibernate.*;
import model.*;

import java.util.List;

public class DatabaseFiller {
    private static void fill() {
        Connector connector = Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        writeHero();
        writeCart();
        connector.commit();
        connector.close();
    }

    // hero
    private static void writeHero() {
        new Hero("Mage", "She is a skilled wizard who has special skills in using spells."
                , 30).saveOrUpdate();
        new Hero("Rogue", "He is a thief and most of his abilities are in stealing from the enemy!"
                , 30).saveOrUpdate();
        new Hero("Warlock",
                "You will never see anyone beyond him. He passes on his life and property and sacrifices something to win the war.",
                35).saveOrUpdate();
        new Hero("Saman", "he is nice master but his assistants are not(special cards release in next version)"
                , 37).saveOrUpdate();
    }

    // carts
    private static void writeCart() {
        ClassOfCard mage = new ClassOfCard("Mage");
        mage.saveOrUpdate();
        ClassOfCard rogue = new ClassOfCard("Rogue");
        rogue.saveOrUpdate();
        ClassOfCard warlock = new ClassOfCard("Warlock");
        warlock.saveOrUpdate();
        ClassOfCard saman = new ClassOfCard("Saman");
        saman.saveOrUpdate();
        ClassOfCard neutral = new ClassOfCard("Neutral");
        neutral.saveOrUpdate();
        new Spell("polymorph",
                "transforms a target minion into a 1/1 Sheep.", 3,
                mage, Rarity.Rare, 4).saveOrUpdate();
        new Spell("Pyroblast", "Deal 10 damage.", 5,
                mage, Rarity.Epic, 10).saveOrUpdate();
        new Minion("Reno the Relicologist", "Battlecry: if your deck has no duplicates, deal 10 damage randomly split among all enemy minions",
                6, mage, Rarity.Legendary, 6, 4, 6);
        new Spell("Friendly Smith", "Discover a weapon from any class. Add it to your Adventure Deck with +2/+2.",
                2, rogue, Rarity.Common, 1).saveOrUpdate();
        new Spell("Preparation", "The next spell you cast this turn costs (2) less.",
                6, rogue, Rarity.Epic, 0).saveOrUpdate();
        new Minion("Crazed Chemist", "Combo: Give a friendly minion +4 Attack.",
                4, rogue, Rarity.Common, 5, 4, 4).saveOrUpdate();
        new Spell("Spirit Bomb", "Deal 4 damage to a minion and your hero.",
                3, warlock, Rarity.Common, 1).saveOrUpdate();
        new Minion("Ratcatcher", "Rush Battlecry: Destroy a friendly minion and gain its Attack and Health.",
                5, warlock, Rarity.Epic, 3, 2, 2).saveOrUpdate();
        new Minion("Dreadscale", "At the end of your turn, deal 1 damage to all other minions.",
                7, warlock, Rarity.Legendary, 3, 4, 2).saveOrUpdate();
        new Minion("Living Monument", "Taunt",
                7, neutral, Rarity.Common, 10, 10, 10).saveOrUpdate();
        new Minion("Wisp", "", 6, neutral, Rarity.Common, 0, 1, 1).saveOrUpdate();
        new Minion("Potion Vendor", "Battlecry: Restore 2 Health to all friendly characters.",
                4, neutral, Rarity.Common, 1, 1, 1).saveOrUpdate();
        new Minion("Kobold Sandtrooper", "Deathrattle: Deal 3 damage to enemy hero.",
                5, neutral, Rarity.Common, 2, 2, 1).saveOrUpdate();
        new Minion("Arena Fanatic", "Battlecry: Give all minions in your hand +1/+1.",
                4, neutral, Rarity.Common, 4, 2, 3).saveOrUpdate();
        new Minion("Young Priestess", "At the end of your turn, give another random friendly minion +1 Health.",
                3, neutral, Rarity.Rare, 1, 2, 1).saveOrUpdate();
        new Minion("Questing Adventurer", "Whenever you play a card, gain +1/+1."
                , 5, neutral, Rarity.Rare, 3, 2, 2).saveOrUpdate();
        new Minion("Arena Patron", "Overkill: Summon another Arena Patron.",
                4, neutral, Rarity.Rare, 5, 3, 3).saveOrUpdate();
        new Minion("Khartut Defender", "Taunt, Reborn Deathrattle:Restore 3 Health to your hero.",
                3, neutral, Rarity.Rare, 6, 3, 4).saveOrUpdate();
        new Minion("Weaponized Pinata", "Deathrattle: add a random Legendary minion to your hand",
                5, neutral, Rarity.Epic, 4, 4, 3).saveOrUpdate();
        new Minion("Big Game Hunter", "Battlecry: Destroy a minion with 7 or more attack.",
                6, neutral, Rarity.Epic, 5, 4, 2).saveOrUpdate();
        new Minion("Batterhead", "Rush. After this attacks and kills a minion, it may attack again.",
                5, neutral, Rarity.Epic, 8, 3, 12).saveOrUpdate();
        new Minion("Baron Geddon", "At the end of your turn, deal 2 damage to all other characters.",
                6, neutral, Rarity.Legendary, 7, 7, 5).saveOrUpdate();
        new Minion("Malygos", "Spell Damage +5.",
                7, neutral, Rarity.Legendary, 6, 4, 12).saveOrUpdate();
        new Spell("Deadly Shot", "Destroy a random enemy minion.",
                3, neutral, Rarity.Common, 3).saveOrUpdate();
        new Spell("Gift on the Wild", "Give your minions +2/+2 and taunt",
                6, neutral, Rarity.Common, 8).saveOrUpdate();
        new Spell("Starfall", "Choose One -Deal 5 damage to a minion; or 2 damage to all enemy minions.",
                5, neutral, Rarity.Rare, 5).saveOrUpdate();
        new Spell("Overflow", "Restore 5 Health to all characters. Draw 5 cards.",
                6, neutral, Rarity.Rare, 7).saveOrUpdate();
        new Spell("The Boomship", "Summon 3 random minions from your hand. Give them Rush",
                7, neutral, Rarity.Legendary, 9).saveOrUpdate();
    }

    public static void main(String[] args) {
        fill();
        Connector connector = Connector.getConnector();
        connector.open();
        List<Hero> heros = connector.fetchAll(Hero.class);
        heros.forEach(System.out::println);
        System.out.println();
        System.out.println();
        List<Minion> minions = connector.fetchAll(Minion.class);
        minions.forEach(System.out::println);
        System.out.println();
        System.out.println();
        List<Spell> spells = connector.fetchAll(Spell.class);
        spells.forEach(System.out::println);
        System.out.println();
        System.out.println();
        List<Player> players = connector.fetchAll(Player.class);
        players.forEach(System.out::println);
        connector.close();
        System.exit(0);
    }
}
