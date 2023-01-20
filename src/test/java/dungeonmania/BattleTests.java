package dungeonmania;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entity.Entity;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.buildableitem.*;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.entity.battles.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class BattleTests {
    Config config = new Config("simple");

    @Test
    @DisplayName("Test Attacking with Spider")
    public void testAttackingWithSpider() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        double spiderHealth = spider.getHealth();
        double playerHealth = player.getHealth();
        round.fight(); 

        assertEquals(spider.getHealth(), spiderHealth - (player.getAttack() / 5));
        assertEquals(player.getHealth(), playerHealth - ((spider.getAttack() - player.getDefence()) / 10));

    }

    @Test
    @DisplayName("Test Attacking with Hydra")
    public void testAttackingWithHydra() {
        Dungeon dungeon = new Dungeon("empty", "health_decrease", 1);
        Hydra hydra = new Hydra(new Position(0, 1), null, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        

        Battle battle = new Battle(player, hydra, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        double hydraHealth = hydra.getHealth();
        double playerHealth = player.getHealth();
        round.fight(); 

        assertEquals(hydra.getHealth(), hydraHealth - (player.getAttack() / 5));
        assertEquals(player.getHealth(), playerHealth - ((hydra.getAttack() - player.getDefence()) / 10));

    }

    @Test
    @DisplayName("Test Attacking with Assassin")
    public void testAttackingWithAssassin() {
        Dungeon dungeon = new Dungeon("empty", "health_decrease", 1);
        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        

        Battle battle = new Battle(player, a, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        double hydraHealth = a.getHealth();
        double playerHealth = player.getHealth();
        round.fight(); 

        assertEquals(a.getHealth(), hydraHealth - (player.getAttack() / 5));
        assertEquals(player.getHealth(), playerHealth - ((a.getAttack() - player.getDefence()) / 10));

    }

    @Test
    @DisplayName("Test Attacking with Hydra Increases Health")
    public void testAttackingWithHydraIncreasesHealth() {
        Dungeon dungeon = new Dungeon("empty", "health_increase", 1);
        Hydra hydra = new Hydra(new Position(0, 1), null, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        

        Battle battle = new Battle(player, hydra, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        double hydraHealth = hydra.getHealth();
        double playerHealth = player.getHealth();
        round.fight(); 

        assertEquals(hydra.getHealth(), hydraHealth + dungeon.getConfig().getHydraHealthIncreaseAmount());
        assertEquals(player.getHealth(), playerHealth - ((hydra.getAttack() - player.getDefence()) / 10));

    }

    @Test
    @DisplayName("Test Hydra Death")
    public void testHydraDeath() {
        Dungeon dungeon = new Dungeon("empty", "health_decrease", 1);
        Hydra hydra = new Hydra(new Position(0, 1), null, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(hydra);
        dungeon.setEntities(entities);

        Battle battle = new Battle(player, hydra, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        hydra.setHealth(0.2);
        round.fight(); 
        assertTrue(dungeon.getPlayer() == null);
        assertFalse(dungeon.getEntities().contains(hydra));

    }

    @Test
    @DisplayName("Test Invincibility Potion with Hydra No Health Increase")
    public void testInvincibilityHydraNoHealthIncrease() {
        Dungeon dungeon = new Dungeon("empty", "health_decrease", 1);
        Hydra hydra = new Hydra(new Position(0, 1), null, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        Battle battle = new Battle(player, hydra, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(hydra);
        dungeon.setEntities(entities);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        
        player.setHealth(1);
        round.fight();
        assertEquals(player.getHealth(), 1);
        assertEquals(hydra.getHealth(), 0);
    }

    @Test
    @DisplayName("Test Invincibility Potion with Hydra Health Increase")
    public void testInvincibilityHydraHealthIncrease() {
        Dungeon dungeon = new Dungeon("empty", "health_increase", 1);
        Hydra hydra = new Hydra(new Position(0, 1), null, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        Battle battle = new Battle(player, hydra, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(hydra);
        dungeon.setEntities(entities);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        
        player.setHealth(1);
        round.fight();
        assertEquals(player.getHealth(), 1);
        assertEquals(hydra.getHealth(), 0);
    }

    @Test
    @DisplayName("Test Attacking with ZombieToast")
    public void testAttackingWithZombieToast() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        ZombieToast zombie = new ZombieToast(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        

        Battle battle = new Battle(player, zombie, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        double zombieHealth = zombie.getHealth();
        double playerHealth = player.getHealth();
        round.fight(); 

        assertEquals(zombie.getHealth(), zombieHealth - (player.getAttack() / 5));
        assertEquals(player.getHealth(), playerHealth - ((zombie.getAttack() - player.getDefence()) / 10));

    }


    @Test
    @DisplayName("Test Attacking with Mercenary")
    public void testAttackingWithMercenary() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        dungeon.setPlayer(player);
        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");

        Battle battle = new Battle(player, merc, dungeon);
        Rounds round = new Rounds(battle);

        double mercHealth = merc.getHealth();
        double playerHealth = player.getHealth();
        round.setDungeon(dungeon);
        round.fight(); 

        assertEquals(merc.getHealth(), mercHealth - (player.getAttack() / 5));
        assertEquals(player.getHealth(), playerHealth - ((merc.getAttack() - player.getDefence()) / 10));

    }

    @Test
    @DisplayName("Test Spider Death")
    public void testSpiderDeath() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        dungeon.setEntities(entities);

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        spider.setHealth(0.2);
        round.fight(); 
        assertTrue(dungeon.getPlayer() == null);
        assertFalse(dungeon.getEntities().contains(spider));

    }

    @Test
    @DisplayName("Test Mercenary Death")
    public void testMercenaryDeath() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        dungeon.setPlayer(player);
        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");

        List<Entity> entities = new ArrayList<>();
        entities.add(merc);
        dungeon.setEntities(entities);

        Battle battle = new Battle(player, merc, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        merc.setHealth(0.2);
        round.fight(); 
        assertTrue(dungeon.getPlayer() != null);
        assertFalse(dungeon.getEntities().contains(merc));

    }


    @Test
    @DisplayName("Test Player Death")
    public void testPlayerDeath() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        dungeon.setEntities(entities);

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        player.setHealth(0.1);
        round.fight(); 
        assertTrue(dungeon.getEntities().contains(spider));
        assertTrue(dungeon.getPlayer() == null);

    }

    @Test
    @DisplayName("Test Zombie Death")
    public void testZombieDeath() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        ZombieToast zombie = new ZombieToast(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(zombie);
        dungeon.setEntities(entities);

        Battle battle = new Battle(player, zombie, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        zombie.setHealth(0.2);
        round.fight(); 
        assertTrue(dungeon.getPlayer() == null);
        assertFalse(dungeon.getEntities().contains(zombie));

    }

    @Test
    @DisplayName("Test Assassin Death")
    public void testAssassinDeath() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(a);
        dungeon.setEntities(entities);

        Battle battle = new Battle(player, a, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        a.setHealth(0.2);
        round.fight(); 
        assertTrue(dungeon.getPlayer() == null);
        assertFalse(dungeon.getEntities().contains(a));

    }

    @Test
    @DisplayName("Test Weapon Attacking")
    public void testAttackingWithWeapons() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        dungeon.setEntities(entities);

        // spiderHealth is 5, so player needs 25 attack points to kill the spider
        Sword sword = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword");
        Sword sword2 = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword2");
        Sword sword3 = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword3");
        Sword sword4 = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword4");
        Sword sword5 = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword5");
        Sword sword6 = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword6");

        Bow bow = new Bow(null, null, config.getBowDurability(), "bow");
        Bow bow2 = new Bow(null, null, config.getBowDurability(), "bow2");

        player.collectEntity(sword);
        player.collectEntity(sword2);
        player.collectEntity(sword3);
        player.collectEntity(sword4);
        player.collectEntity(sword5);
        player.collectEntity(sword6);
        player.collectEntity(bow);
        player.collectEntity(bow2);

        // Attack power should be (2 * 2) * (1 * 7) = 28
        round.setDungeon(dungeon);
        round.fight(); 

        assertTrue(dungeon.getPlayer() == null);
        assertFalse(dungeon.getEntities().contains(spider));
    }

    @Test
    @DisplayName("Test Weapon Defending")
    public void testWeaponDefending() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);
        
        // spiderAttack is 1
        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        dungeon.setEntities(entities);

        Shield shield = new Shield(null, null, config.getShieldDurability(), config.getShieldDefence(), "shield");
        player.collectEntity(shield);

        // Even if the player's health is 1, and spider attack is 1, the shield (defence 1) protects it from that damage
        player.setHealth(1);
        round.fight(); 
        assertTrue(dungeon.getPlayer() == null);
        assertTrue(dungeon.getEntities().contains(spider));
    }
    
    @Test
    @DisplayName("Test Weapon Running out of Durability")
    public void testOutOfDurability() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        dungeon.setEntities(entities);

        Sword sword = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword");
        player.collectEntity(sword);

        battle.battleTrigger();
        // The sword has deteriorated and is taken out of the user's inventory and weapon list
        assertTrue(player.getInventoryList().size() == 0);
        assertTrue(player.getWeaponryList().size() == 0);
    }

    @Test
    @DisplayName("Test Invincibility Potion")
    public void testInvincibility() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        Battle battle = new Battle(player, spider, dungeon);
        Rounds round = new Rounds(battle);
        round.setDungeon(dungeon);

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        dungeon.setEntities(entities);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        
        player.setHealth(1);
        round.fight();
        assertEquals(player.getHealth(), 1);
    }

    @Test
    @DisplayName("Test Battle Not Initiated if Invisible")
    public void testInvisibility() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        dungeon.setPlayer(player);
        List<Entity> entityList = new ArrayList<>();
        entityList.add(spider);
        dungeon.setEntities(entityList);
        
        spider.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(spider));

        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });

        double initialHealth = player.getHealth();

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), spider.getPosition());
        assertEquals(initialHealth, player.getHealth());
    }

    @Test
    @DisplayName("Test Battle Looped Till Entity Dies")
    public void testBattleLoop() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        dungeon.setPlayer(player);
        List<Entity> entityList = new ArrayList<>();
        entityList.add(spider);
        dungeon.setEntities(entityList);
        
        spider.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(spider));
        spider.setAttack(0);
        spider.setHealth(100);
        player.setAttack(50);

        player.move(Direction.DOWN);

        assertTrue(spider.getHealth() <= 0);
        assertFalse(dungeon.getEntities().contains(spider));
    }

    @Test
    @DisplayName("Test Battle Looped Till Player Dies")
    public void testBattleLoopLoses() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        player.setAttack(0);

        dungeon.setPlayer(player);
        List<Entity> entityList = new ArrayList<>();
        entityList.add(spider);
        dungeon.setEntities(entityList);
        
        spider.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(spider));

        player.move(Direction.DOWN);

        assertTrue(player.getHealth() <= 0);
        assertTrue(dungeon.getEntities().contains(spider));
        assertEquals(dungeon.getPlayer(), null);
    }

    @Test
    @DisplayName("Tests that player always wins with invincibility")
    public void testPlayerWithInvincibility() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        dungeon.setPlayer(player);
        List<Entity> entityList = new ArrayList<>();
        entityList.add(spider);
        dungeon.setEntities(entityList);
        
        spider.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(spider));

        InvincibilityPotion potion = new InvincibilityPotion(null, null, 1, "potion");
        player.collectEntity(potion);

        assertDoesNotThrow(() -> {
            player.useItem("potion", dungeon);
        });
        
        player.setHealth(0.1);

        dungeon.movementTick(Direction.DOWN, null);

        assertTrue(spider.getHealth() <= 0);
        assertFalse(dungeon.getEntities().contains(spider));

        // Potion must be updated in next tick
        assertTrue(player.getCurrentPotion() == null);
        dungeon.movementTick(Direction.DOWN, null);
        assertEquals(player.getCurrentPotion(), null);

        ZombieToast zombie = new ZombieToast(new Position(0, 3), null, config, "zombie", dungeon);
        dungeon.addEntity(zombie);

        InvincibilityPotion potion2 = new InvincibilityPotion(null, null, 1, "potion2");
        player.collectEntity(potion2);
        assertDoesNotThrow(() -> {
            player.useItem("potion2", dungeon);
        });

        dungeon.movementTick(Direction.DOWN, null);

        assertFalse(dungeon.getEntities().contains(zombie));
    }

    @Test
    @DisplayName("Tests weapon durability")
    public void testPlayerDurability() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        dungeon.setPlayer(player);
        List<Entity> entityList = new ArrayList<>();
        entityList.add(spider);
        dungeon.setEntities(entityList);
        
        spider.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(spider));

        Sword sword = new Sword(null, null, 1, 1, "sword");
        Shield shield = new Shield(null,  null, 1, 1, "shield");
        Bow bow = new Bow(null, null, 1, "bow");

        player.collectEntity(sword);
        player.collectEntity(shield);
        player.collectEntity(bow);

        spider.setHealth(0.1);

        assertTrue(player.getWeaponryList().size() == 3);
        dungeon.movementTick(Direction.DOWN, null);
        assertTrue(player.getWeaponryList().size() == 0);


    }
}
