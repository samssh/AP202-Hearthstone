package ir.sam.hearthstone.hibernate;

import ir.sam.hearthstone.model.account.Player;
import ir.SAM.hearthstone.model.main.*;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ResourceLoader;
import ir.sa.hearthstone.model.main.*;
import ir.sam.hearthstone.model.main.*;

import java.util.List;

public class DatabaseFiller {
    private static void fill() {
        writeHero();
        writeCart();
    }

    private static void writeHero() {
        HeroPower magePower = new HeroPower("Fireblast","Deal 1 damage",2);
        connector.save(new Hero("Mage", "She is a skilled wizard who has special skills in using spells."
                , 30,magePower));
        HeroPower roguePower = new HeroPower("Rubbery","Steal a card from opponent's hand and add it to your hand",3);
        connector.save(new Hero("Rogue", "He is a thief and most of his abilities are in stealing from the enemy!"
                , 30,roguePower));
        HeroPower warlockPower = new HeroPower("Life Tap","Draw a card and take 2 damage Or add a random minion  1/1.",0);
        connector.save(new Hero("Warlock",
                "You will never see anyone beyond him. He passes on his life and property and sacrifices something to win the war.",
                35,warlockPower));
        HeroPower hunterPower = new HeroPower("Caltrops","After your opponent plays a minion, deal 1 damage to it",0);
        connector.save(new Hero("Hunter", "She is keen to attack quickly. And because it has a strong interest in hunting," +
                " most of its minions are animal-type.", 30,hunterPower));
        HeroPower priestPower= new HeroPower("Heal","Restore 4 Health.",2);
        connector.save(new Hero("Priest", "Although she has few soldiers, her soldiers are loyal. She has many skills in healing, healing and reviving.",
                30,priestPower));
    }

    private static void writeCart() {
        ClassOfCard neutral = new ClassOfCard("Neutral");
        connector.save(neutral);
        ClassOfCard mage = new ClassOfCard("Mage");
        connector.save(mage);
        ClassOfCard rogue = new ClassOfCard("Rogue");
        connector.save(rogue);
        ClassOfCard warlock = new ClassOfCard("Warlock");
        connector.save(warlock);
        ClassOfCard hunter = new ClassOfCard("Hunter");
        connector.save(hunter);
        ClassOfCard priest = new ClassOfCard("Priest");
        connector.save(priest);


        connector.save(new Spell("Polymorph",
                "transforms a target minion into a 1/1 Sheep.", 3,
                mage, Rarity.Rare, 4));
        connector.save(new Spell("Pyroblast", "Deal 10 damage.", 5,
                mage, Rarity.Epic, 10));
        connector.save(new Spell("Friendly Smith", "Discover a weapon from any class. Add it to your Adventure Deck with +2/+2.",
                2, rogue, Rarity.Common, 1));
        connector.save(new Spell("Preparation", "The next spell you cast this turn costs (2) less.",
                6, rogue, Rarity.Epic, 0));
        connector.save(new Minion("Crazed Chemist", "Combo: Give a friendly minion +4 Attack.",
                4, rogue, Rarity.Common, 5, 4, 4));


        connector.save(new Minion("Ratcatcher", "Rush Battlecry: Destroy a friendly minion and gain its Attack and Health.",
                5, warlock, Rarity.Epic, 3, 2, 2));
        connector.save(new Minion("Dreadscale", "At the end of your turn, deal 1 damage to all other minions.",
                7, warlock, Rarity.Legendary, 3, 4, 2));

        connector.save(new Minion("Wisp", "", 6, neutral, Rarity.Common, 0, 1, 1));
        connector.save(new Minion("Potion Vendor", "Battlecry: Restore 2 Health to all friendly characters.",
                4, neutral, Rarity.Common, 1, 1, 1));
        connector.save(new Minion("Kobold Sandtrooper", "Deathrattle: Deal 3 damage to enemy hero.",
                5, neutral, Rarity.Common, 2, 2, 1));
        connector.save(new Minion("Arena Fanatic", "Battlecry: Give all minions in your hand +1/+1.",
                4, neutral, Rarity.Common, 4, 2, 3));
        connector.save(new Minion("Young Priestess", "At the end of your turn, give another random friendly minion +1 Health.",
                3, neutral, Rarity.Rare, 1, 2, 1));
        connector.save(new Minion("Questing Adventurer", "Whenever you play a card, gain +1/+1."
                , 5, neutral, Rarity.Rare, 3, 2, 2));
        connector.save(new Minion("Arena Patron", "Overkill: Summon another Arena Patron.",
                4, neutral, Rarity.Rare, 5, 3, 3));
        connector.save(new Minion("Khartut Defender", "Taunt, Reborn Deathrattle:Restore 3 Health to your hero.",
                3, neutral, Rarity.Rare, 6, 3, 4));
        connector.save(new Minion("Weaponized Pinata", "Deathrattle: add a random Legendary minion to your hand",
                5, neutral, Rarity.Epic, 4, 4, 3));
        connector.save(new Minion("Big Game Hunter", "Battlecry: Destroy a minion with 7 or more attack.",
                6, neutral, Rarity.Epic, 5, 4, 2));
        connector.save(new Minion("Baron Geddon", "At the end of your turn, deal 2 damage to all other characters.",
                6, neutral, Rarity.Legendary, 7, 7, 5));
        connector.save(new Minion("Malygos", "Spell Damage +5.",
                7, neutral, Rarity.Legendary, 6, 4, 12));


        connector.save(new Spell("Deadly Shot", "Destroy a random enemy minion.",
                3, neutral, Rarity.Common, 3));
        connector.save(new Spell("Starfall", "Choose One -Deal 5 damage to a minion; or 2 damage to all enemy minions.",
                5, neutral, Rarity.Rare, 5));
        connector.save(new Spell("Overflow", "Restore 5 Health to all characters. Draw 5 cards.",
                6, neutral, Rarity.Rare, 7));
        connector.save(new Spell("The Boomship", "Summon 3 random minions from your hand. Give them Rush",
                7, neutral, Rarity.Legendary, 9));
        connector.save(new Spell("Blessing of the Ancients","Twinspell. Give your minions +1/+1.",
                5,neutral,Rarity.Common,3));

        connector.save(new Quest("Strength in Numbers", "Sidequest: Spend 10 Mana on minions." +
                "Reward: Summon a minion from your deck.", 3, neutral, Rarity.Common, 1));
        connector.save(new Quest("Learn Draconic", "Sidequest: Spend 8 Mana on spells. Reward: Summon a 6/6 Dragon.",
                6, neutral, Rarity.Common, 1));


        connector.save(new Weapon("Arcanite Reaper", "", 4, neutral,
                Rarity.Common, 5, 5, 2));
        connector.save(new Weapon("Blood Fury", "", 4, neutral,
                Rarity.Common, 3, 3, 8));
        connector.save(new Weapon("Ashbringer", "", 3, neutral,
                Rarity.Epic, 5, 5, 3));

        connector.save(new Spell("Arcane Shot", "Deal 2 damage.", 5, hunter, Rarity.Common,
                1));
        connector.save(new Minion("Swamp King Dred", "After your opponent plays a minion, attack it.",
                7, hunter, Rarity.Legendary, 7, 9, 9));

        connector.save(new Spell("Sand Breath", "Give a minion +1/+2. Give it Divine Shield if you're holding a Dragon.",
                4, priest, Rarity.Common, 1));
        connector.save(new Minion("High Priest Amet", "Whenever you summon a minion, set its Health equal to this minion's.",
                6, priest, Rarity.Legendary, 4, 2, 7));


        connector.save(new Minion("Tomb Warden", "Taunt, Battlecry: Summon a copy of this minion.", 4, neutral, Rarity.Rare,
                8, 3, 6));
        connector.save(new Minion("Security Rover", "Whenever this minion takes damage, summon a 2/3 Mech with Taunt.",
                3, neutral, Rarity.Rare, 6, 2, 6));
        connector.save(new Minion("Curio Collector", "Whenever you draw a card, gain +1/+1.", 6
                , neutral, Rarity.Rare, 5, 4, 4));
        connector.save(new Minion("Sathrovarr", "Choose a friendly minion. Add a copy of it to your hand, deck and battlefield.",
                7, neutral, Rarity.Legendary, 9, 5, 5));


        connector.save(new Spell("Sprint", "Draw 4 cards", 3, neutral
                , Rarity.Common, 7));
        connector.save(new Spell("Swarm of Locusts", "Summon seven 1/1 Locusts with Rush.", 4
                , neutral, Rarity.Rare, 6));
        connector.save(new Spell("Pharaoh's Blessing", "Give a minion +4/+4, Divine Shield, and Taunt.", 5,
                neutral, Rarity.Rare, 6));
        connector.save(new Spell("Book of Specters", "Draw 3 cards. Discard any spells drawn.", 4,
                neutral, Rarity.Epic, 2));

        connector.save(new Passive("Mana Jump","start with an extra Mana"));
        connector.save(new Passive("Nurse","At the end of your turn,Restore 2 Health to random friendly minion"));
        connector.save(new Passive("Off Card","The cost of all cards is reduced by one mana"));
        connector.save(new Passive("Twice Draw","Draw two Card each turn"));
        connector.save(new Passive("Warriors","if a minion dies on the playground, your hero gain two defence"));
    }

    private static Connector connector;

    public static void main(String[] args) {
        ResourceLoader.setArgs(args);
        connector =  new Connector(ConfigFactory.getInstance().getConfigFile("SERVER_HIBERNATE_CONFIG")
                ,System.getenv("HearthStone password"));
        fill();
        List<Hero> heroes = connector.fetchAll(Hero.class);
        heroes.forEach(System.out::println);
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
        System.out.println();
        System.out.println();
        List<Passive> passives = connector.fetchAll(Passive.class);
        passives.forEach(System.out::println);
        connector.close();
        System.exit(0);
    }
}
