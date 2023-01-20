package dungeonmania;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.entity.staticentity.*;
import dungeonmania.exceptions.InvalidActionException;

public class UsableItemTests {
    @Test
    @DisplayName("Testing that the potions are queued")
    public void testPotionQueue() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        InvisibilityPotion invispotion = new InvisibilityPotion(null, new ArrayList<>(), newConfig.getInvisibilityPotionDuration(), "invispotion");

        // Testing that you can only use item if it is in inventory
        assertThrows(InvalidActionException.class, ()-> {
            player.useItem("invispotion", dungeon);
        });
        player.collectEntity(invispotion);

        InvincibilityPotion invincepotion = new InvincibilityPotion(null, new ArrayList<>(), newConfig.getInvincibilityPotionDuration(), "invincepotion");
        player.collectEntity(invincepotion);

        assertDoesNotThrow(()-> {
            player.useItem("invispotion", dungeon);
        });

        assertDoesNotThrow(()-> {
            player.useItem("invincepotion", dungeon);
        });

        assertTrue(player.getPotionQueue().contains(invincepotion));
    }

    @Test
    @DisplayName("Testing that the potions replace after durability")
    public void testPotionNext() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        InvisibilityPotion invispotion = new InvisibilityPotion(null, new ArrayList<>(), newConfig.getInvisibilityPotionDuration(), "invispotion");
        player.collectEntity(invispotion);
        assertDoesNotThrow(()-> {
            player.useItem("invispotion", dungeon);
        });

        assertEquals(player.getCurrentPotion(), invispotion);

        InvincibilityPotion invincepotion = new InvincibilityPotion(null, new ArrayList<>(), newConfig.getInvincibilityPotionDuration(), "invincepotion");
        player.collectEntity(invincepotion);
        assertDoesNotThrow(()-> {
            player.useItem("invincepotion", dungeon);
        });
        
        // Durability should run out
        player.getCurrentPotion().use();
        // This should replace the potion after the durability wears out (Takes from queue)
        player.getCurrentPotion().checkPotionStatus(player);

        assertEquals(player.getCurrentPotion(), invincepotion);
    }

    @Test
    @DisplayName("Testing that the Bomb explodes")
    public void testBombExplosion() {
        Config newConfig = new Config("bomb_radius_2");
        Dungeon dungeon = new Dungeon("zombies", "bomb_radius_2", 1);
        List<Entity> entityList = new ArrayList<>();

        Player player = new Player(new Position(2, 1), new ArrayList<>(), newConfig, "", dungeon);
        dungeon.setPlayer(player);

        FloorSwitch fs = new FloorSwitch(new Position(2, 3), false, null, "", new int[] {0});
        Sword sword = new Sword(new Position(1, 1), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        Wood wood = new Wood(new Position(1, 3), null, "wood");
        Boulder boulder = new Boulder(new Position(2, 2), null, "");
        Key key = new Key(new Position(3, 1), null, 1, "key");
        InvincibilityPotion invin = new InvincibilityPotion(new Position(3, 2), null, newConfig.getInvincibilityPotionDuration(), "invin");
        InvisibilityPotion invis = new InvisibilityPotion(new Position(3, 3), null, newConfig.getInvisibilityPotionDuration(), "invis");
        Sword sword2 = new Sword(new Position(10, 10), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword2");
        
        entityList.add(fs);
        entityList.add(sword);
        entityList.add(wood);
        entityList.add(boulder);
        entityList.add(key);
        entityList.add(invin);
        entityList.add(invis);
        entityList.add(sword2);

        dungeon.setEntities(entityList);
        dungeon.updateAdjacentEntities();

        player.move(Direction.DOWN);
        dungeon.updateAdjacentEntities();
        
        Bomb bomb = new Bomb(new Position(100, 1000), Arrays.asList(boulder, fs), newConfig.getBombRadius(), "bomb", dungeon);
        dungeon.updateAdjacentEntities();
        player.collectEntity(bomb);
        assertDoesNotThrow(() -> player.useItem("bomb", dungeon));
        dungeon.updateAdjacentEntities();
        assertTrue(dungeon.getEntities().size() == 1);
    }

    @Test
    @DisplayName("Testing that the Bomb explodes when boulder pushed on floor switch")
    public void testBombExplosionAfterBoulderMove() {
        Config newConfig = new Config("bomb_radius_2");
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        List<Entity> entityList = new ArrayList<>();

        Player player = new Player(new Position(2, 4), new ArrayList<>(), newConfig, "", dungeon);
        dungeon.setPlayer(player);
        Bomb bomb = new Bomb(new Position(2, 4), null, newConfig.getBombRadius(), "bomb", dungeon);
        Boulder boulder = new Boulder(new Position(2, 2), null, "");
        FloorSwitch fs = new FloorSwitch(new Position(2, 3), false, null, "", new int[] {0});
        entityList.add(fs);
        entityList.add(boulder);
        dungeon.setEntities(entityList);

        player.collectEntity(bomb);

        assertDoesNotThrow(() -> player.useItem("bomb", dungeon));
        player.setPosition(new Position(2, 1));
        dungeon.updateAdjacentEntities();
        
        player.move(Direction.DOWN);
        dungeon.updateAdjacentEntities(); 
        assertTrue(dungeon.getEntities().size() == 0);
    }

    @Test
    @DisplayName("Bomb doesn't explode out of range")
    public void testOutOfRangeBomb() {
        Config newConfig = new Config("bomb_radius_2");
        Dungeon dungeon = new Dungeon("zombies", "bomb_radius_2", 1);
        List<Entity> entityList = new ArrayList<>();

        Player player = new Player(new Position(2, 1), new ArrayList<>(), newConfig, "", dungeon);
        dungeon.setPlayer(player);

        FloorSwitch FloorSwitch = new FloorSwitch(new Position(2, 3), false, null, "", new int[] {0});
        Sword sword = new Sword(new Position(8, 8), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        Wood wood = new Wood(new Position(10, 3), null, "wood");
        Boulder boulder = new Boulder(new Position(2, 2), null, "");
        Key key = new Key(new Position(10, 10), null, 1, "key");
        InvincibilityPotion invin = new InvincibilityPotion(new Position(6, 6), null, newConfig.getInvincibilityPotionDuration(), "invin");
        InvisibilityPotion invis = new InvisibilityPotion(new Position(6, 5), null, newConfig.getInvisibilityPotionDuration(), "invis");
        entityList.add(FloorSwitch);
        entityList.add(sword);
        entityList.add(wood);
        entityList.add(boulder);
        entityList.add(key);
        entityList.add(invin);
        entityList.add(invis);

        dungeon.setEntities(entityList);
        dungeon.updateAdjacentEntities();

        player.move(Direction.DOWN);
        dungeon.updateAdjacentEntities();
        
        Bomb bomb = new Bomb(new Position(100, 1000), Arrays.asList(boulder, FloorSwitch), newConfig.getBombRadius(), "bomb", dungeon);
        dungeon.updateAdjacentEntities();
        player.collectEntity(bomb);
        assertDoesNotThrow(() -> player.useItem("bomb", dungeon));
        dungeon.updateAdjacentEntities();
        assertTrue(dungeon.getEntities().size() == 5);
    }

    @Test
    @DisplayName("Testing bomb radius 0")
    public void testBombRadiusZero() {
        Config newConfig = new Config("bomb_radius_0");
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_0", 1);
        List<Entity> entityList = new ArrayList<>();

        Player player = new Player(new Position(2, 1), new ArrayList<>(), newConfig, "", dungeon);
        dungeon.setPlayer(player);
        Boulder boulder = new Boulder(new Position(2, 2), null, "");
        FloorSwitch fs = new FloorSwitch(new Position(2, 3), false, null, "", new int[] {0});
        entityList.add(fs);
        entityList.add(boulder);
        dungeon.setEntities(entityList);
        
        dungeon.updateAdjacentEntities(); 
        player.move(Direction.DOWN);
        
        player.setPosition(new Position(2, 4));
        dungeon.updateAdjacentEntities(); 
        Bomb bomb = new Bomb(new Position(2, 4), null, newConfig.getBombRadius(), "bomb", dungeon);
        player.collectEntity(bomb);
        assertDoesNotThrow(() -> player.useItem("bomb", dungeon));
        
        dungeon.updateAdjacentEntities();
        
        assertTrue(dungeon.getEntities().size() == 2);
    }

    @Test
    @DisplayName("Testing no currentPotion")
    public void testNoCurrentPotion() {
        Config newConfig = new Config("simple");
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        assertEquals(player.getCurrentPotion(), null);

        InvisibilityPotion invispotion = new InvisibilityPotion(null, new ArrayList<>(), newConfig.getInvisibilityPotionDuration(), "invispotion");

        player.collectEntity(invispotion);
        assertDoesNotThrow(()-> {
            player.useItem("invispotion", dungeon);
        });

        // Durability should run out
        player.getCurrentPotion().use();
        // This should replace the potion after the durability wears out (Takes from queue)
        player.getCurrentPotion().checkPotionStatus(player);

        assertEquals(player.getCurrentPotion(), null);
    }

    @Test
    @DisplayName("Test that bomb cannot be picked up after it has been put down")
    public void testCannotPickUpAgain() {
        Config newConfig = new Config("simple");
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        List<Entity> entityList = new ArrayList<>();

        // Both sword and wood are in range if it does explode
        Sword sword = new Sword(new Position(1, 2), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        Wood wood = new Wood(new Position(1, 0), null, "wood");
        
        Bomb bomb = new Bomb(new Position(1, 1), Arrays.asList(sword, wood), newConfig.getBombRadius(), "bomb", dungeon);
        entityList.add(bomb);
        entityList.add(sword);
        entityList.add(wood);
        dungeon.setEntities(entityList);

        Player player = new Player(new Position(1, 1), new ArrayList<>(), newConfig, "", dungeon);
        player.collectEntity(bomb);

        assertTrue(player.getInventoryList().contains(bomb));

        assertDoesNotThrow(()-> {
            player.useItem("bomb", dungeon);
        });

        assertFalse(player.getInventoryList().contains(bomb));
        assertEquals(dungeon.getEntities(), Arrays.asList(bomb, sword, wood));
        
        // Try picking up
        player.collectEntity(bomb);
        assertFalse(player.getInventoryList().contains(bomb));
    }

}
