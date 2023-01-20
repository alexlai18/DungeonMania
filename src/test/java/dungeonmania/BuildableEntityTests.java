package dungeonmania;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.buildableitem.Bow;
import dungeonmania.entity.inventoryitem.buildableitem.Shield;
import dungeonmania.entity.inventoryitem.collectableitem.Arrows;
import dungeonmania.entity.inventoryitem.collectableitem.Key;
import dungeonmania.entity.inventoryitem.collectableitem.Treasure;
import dungeonmania.entity.inventoryitem.collectableitem.Wood;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.exceptions.InvalidActionException;

import dungeonmania.util.Position;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
public class BuildableEntityTests {
    @Test
    @DisplayName("Testing that checking of resources is correct")
    public void testCheckResources() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        Wood wood = new Wood(new Position(0, 0), new ArrayList<>(), "wood");
        Arrows arrow1 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow1");
        Arrows arrow2 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow2");
        Arrows arrow3 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow3");
        player.collectEntity(wood);
        player.collectEntity(arrow1);
        player.collectEntity(arrow2);
        assertTrue(!player.canCraft("bow"));
        player.collectEntity(arrow3);
        assertTrue(player.canCraft("bow"));
    }
    @Test
    @DisplayName("Testing that Bow is created in inventory")
    public void testBowCreation() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        Wood wood = new Wood(new Position(0, 0), new ArrayList<>(), "wood");
        Arrows arrow1 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow1");
        Arrows arrow2 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow2");
        Arrows arrow3 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow3");
        Treasure treasure = new Treasure(null, null, "treasure");
        player.collectEntity(wood);
        player.collectEntity(arrow1);
        player.collectEntity(arrow2);
        player.collectEntity(arrow3);
        player.collectEntity(treasure);
        assertDoesNotThrow(() -> {
            player.buildEntity("bow");
        });
        assertTrue(player.getInventoryList().size() == 2);
        assertTrue(player.getInventoryList().stream().filter(Bow.class :: isInstance).count() == 1);
    }
    @Test
    @DisplayName("Testing that Shield is created in inventory")
    public void testShieldCreation() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        Wood wood1 = new Wood(new Position(0, 0), new ArrayList<>(), "wood1");
        Wood wood2 = new Wood(new Position(0, 0), new ArrayList<>(), "wood2");
        Treasure treasure = new Treasure(new Position(0, 0), new ArrayList<>(), "treasure");
        Key key = new Key(new Position(0, 0), new ArrayList<>(), 1, "key");
        Treasure treasure2 = new Treasure(null, null, "treasure2");
        player.collectEntity(wood1);
        player.collectEntity(wood2);
        player.collectEntity(key);
        player.collectEntity(treasure);
        player.collectEntity(treasure2);
        // Tests that they use the treasure first
        assertTrue(player.getWeaponryEquipped().calculateDefence() == 0);
        assertDoesNotThrow(() -> {
            player.buildEntity("shield");
        });
        assertTrue(player.getInventoryList().stream().anyMatch(Key.class :: isInstance));
        assertTrue(player.getInventoryList().stream().anyMatch(Shield.class :: isInstance));
        Wood wood3 = new Wood(new Position(0, 0), new ArrayList<>(), "wood3");
        Wood wood4 = new Wood(new Position(0, 0), new ArrayList<>(), "wood4");
        player.collectEntity(wood3);
        player.collectEntity(wood4);
        // Tests that it can still use Key to build a shield
        assertDoesNotThrow(() -> {
            player.buildEntity("shield");
        });
        assertTrue(player.getInventoryList().size() == 3);
        assertTrue(player.getInventoryList().stream().filter(Shield.class :: isInstance).count() == 2);
    }

    @Test
    @DisplayName("Testing that if there is no resources, an item will not be built")
    public void testNoResources() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        assertThrows(InvalidActionException.class, () -> {
            player.buildEntity("bow");
        });
        assertEquals(player.getInventoryList().size(), 0);
        assertThrows(InvalidActionException.class, () -> {
            player.buildEntity("shield");
        });
        assertEquals(player.getInventoryList().size(), 0);
    }

    @Test
    @DisplayName("Testing the list of buildables")
    public void testBuildableList() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        assertEquals(new ArrayList<>(), player.getBuildables());

        Wood wood = new Wood(new Position(0, 1), null, "wood");
        Arrows arrow1 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow1");
        Arrows arrow2 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow2");
        Arrows arrow3 = new Arrows(new Position(0, 0), new ArrayList<>(), "arrow3");
        player.collectEntity(wood);
        player.collectEntity(arrow1);
        player.collectEntity(arrow2);
        player.collectEntity(arrow3);

        assertEquals(Arrays.asList("bow"), player.getBuildables());

        Wood wood2 = new Wood(new Position(1, 0), null, "wood2");
        Wood wood3 = new Wood(new Position(0, 0), null, "wood3");
        Treasure coin = new Treasure(new Position(1, 0), null, "coin");
        player.collectEntity(wood2);
        player.collectEntity(wood3);
        player.collectEntity(coin);

        assertEquals(Arrays.asList("bow", "shield"), player.getBuildables());

        player.getPlayerInventory().craftBow();

        assertEquals(Arrays.asList("shield"), player.getBuildables());

        player.getPlayerInventory().craftShield(true);

        assertEquals(new ArrayList<>(), player.getBuildables());
    }

    @Test
    @DisplayName("Testing that it throws an exception if theres no resources")
    public void testNoResourcesSceptre() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        Wood wood = new Wood(null, null, "wood");
        player.collectEntity(wood);

        assertFalse(player.getPlayerInventory().canCraft("sceptre"));
        assertThrows(InvalidActionException.class, () -> player.buildEntity("sceptre"));
    }
}
