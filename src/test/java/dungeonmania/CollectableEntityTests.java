package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.util.Position;

public class CollectableEntityTests {
    @Test
    @DisplayName("Test that collectable items are not in inventory when created")
    public void testConstruction() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        
        Arrows arrow = new Arrows(new Position(0, 1), null, "arrow");
        assertTrue(!arrow.getInventoryStatus());
        
        Bomb bomb = new Bomb(new Position(0, 2), null, newConfig.getBombRadius(), "bomb", dungeon);
        assertTrue(!bomb.getInventoryStatus());

        InvincibilityPotion invince = new InvincibilityPotion(new Position(0, 3), null, newConfig.getInvincibilityPotionDuration(), "invince");
        assertTrue(!invince.getInventoryStatus());

        InvisibilityPotion invis = new InvisibilityPotion(new Position(0, 4), null, newConfig.getInvisibilityPotionDuration(), "invis");
        assertTrue(!invis.getInventoryStatus());

        Key key = new Key(new Position(0, 5), null, 1, "key");
        assertTrue(!key.getInventoryStatus());

        Sword sword = new Sword(new Position(1, 0), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        assertTrue(!sword.getInventoryStatus());

        Treasure treasure = new Treasure(new Position(1, 1), null, "treasure");
        assertTrue(!treasure.getInventoryStatus());

        Wood wood = new Wood(new Position(1, 2), null, "wood");
        assertTrue(!wood.getInventoryStatus());
    }

    @Test
    @DisplayName("Test that item is collected")
    public void testCollectedItem() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        Treasure coin = new Treasure(new Position(0, 1), null, "coin");

        player.collectEntity(coin);

        assertEquals(player.getInventoryList(), Arrays.asList(coin));
    }

    @Test
    @DisplayName("Test multiple items are collected")
    public void testMultipleItems() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        
        Treasure coin = new Treasure(new Position(0, 1), null, "coin");
        Treasure coin2 = new Treasure(new Position(0, 2), null, "coin2");
        Wood wood = new Wood(new Position(0, 3), null, "wood");
        Key key = new Key(new Position(0, 4), null, 1, "key");

        player.collectEntity(coin);
        player.collectEntity(coin2);
        player.collectEntity(wood);
        player.collectEntity(key);

        assertEquals(player.getInventoryList(), Arrays.asList(coin, coin2, wood, key));
    }

    @Test
    @DisplayName("Test used items removed from inventory")
    public void testUsedItems() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        
        Treasure coin = new Treasure(new Position(0, 1), null, "coin");
        Treasure coin2 = new Treasure(new Position(0, 2), null, "coin2");
        Wood wood = new Wood(new Position(0, 3), null, "wood");
        Key key = new Key(new Position(0, 4), null, 1, "key");

        player.collectEntity(coin);
        player.collectEntity(coin2);
        player.collectEntity(wood);
        player.collectEntity(key);

        player.itemUsed(coin);
        player.itemUsed(wood);
        assertEquals(player.getInventoryList(), Arrays.asList(coin2, key));

        player.itemUsed(coin2);
        player.itemUsed(key);
        assertEquals(player.getInventoryList(), new ArrayList<>());
    }

    @Test
    @DisplayName("Cannot collect more than 1 key")
    public void testCanOnlyCollect1Key() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Key key = new Key(new Position(0,1), null, 1, "key");
        Key key2 = new Key(new Position(0,2), null, 2, "key2");

        player.collectEntity(key);

        assertEquals(player.getInventoryList(), Arrays.asList(key));

        player.collectEntity(key2);

        assertEquals(player.getInventoryList(), Arrays.asList(key));
    }
}
