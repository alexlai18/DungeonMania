package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.inventoryitem.buildableitem.*;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Arrays;


public class WeaponTests {
    @Test
    @DisplayName("Testing that checking of resources is correct")
    public void testCheckResources() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Wood wood = new Wood(new Position(0, 1), null, "wood");
        Arrows arrow1 = new Arrows(new Position(0, 2), null, "arrow1");
        Arrows arrow2 = new Arrows(new Position(0, 3), null, "arrow2");
        Arrows arrow3 = new Arrows(new Position(0, 4), null, "arrow3");

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
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Wood wood = new Wood(new Position(0, 4), null, "wood");
        Arrows arrow1 = new Arrows(new Position(0, 1), null, "arrow1");
        Arrows arrow2 = new Arrows(new Position(0, 2), null, "arrow2");
        Arrows arrow3 = new Arrows(new Position(0, 3), null, "arrow3");

        player.collectEntity(wood);
        player.collectEntity(arrow1);
        player.collectEntity(arrow2);
        player.collectEntity(arrow3);

        assertDoesNotThrow(() -> {
            player.buildEntity("bow");
        });
        assertTrue(player.getInventoryList().size() == 1);
        assertTrue(player.getInventoryList().stream().allMatch(Bow.class :: isInstance));
    }

    @Test
    @DisplayName("Testing that Shield is created in inventory")
    public void testShieldCreation() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Wood wood1 = new Wood(new Position(0, 1), null, "wood1");
        Wood wood2 = new Wood(new Position(0, 2), null, "wood2");
        Treasure treasure = new Treasure(new Position(0, 3), null, "treasure");
        Key key = new Key(new Position(0, 4), null, 1, "key");

        player.collectEntity(wood1);
        player.collectEntity(wood2);
        player.collectEntity(key);
        player.collectEntity(treasure);

        // Tests that they use the treasure first
        
        assertDoesNotThrow(() -> {
            player.buildEntity("shield");
        });
        assertTrue(player.getInventoryList().stream().anyMatch(Key.class :: isInstance));
        assertTrue(player.getInventoryList().stream().anyMatch(Shield.class :: isInstance));

        Wood wood3 = new Wood(new Position(0, 5), null, "wood3");
        Wood wood4 = new Wood(new Position(1, 0), null, "wood4");
        player.collectEntity(wood3);
        player.collectEntity(wood4);

        // Tests that it can still use Key to build a shield
        assertDoesNotThrow(() -> {
            player.buildEntity("shield");
        });
        assertTrue(player.getInventoryList().size() == 2);
        assertTrue(player.getInventoryList().stream().allMatch(Shield.class :: isInstance));

        // Checking if the resources are updated correctly
        assertThrows(InvalidActionException.class, () -> {
            player.buildEntity("shield");
        });
    }

    @Test
    @DisplayName("Testing that if there is no resources, an item will not be built")
    public void testNoResources() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
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
    @DisplayName("Testing that sword is working")
    public void testSword() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Sword sword = new Sword(new Position(0, 1), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        player.collectEntity(sword);
        // Should be 1 + 2
        assertEquals(3, player.getAttack());
    }

    @Test
    @DisplayName("Testings that bow is working")
    public void testBow() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Bow bow = new Bow(null, null, newConfig.getBowDurability(), "bow");
        player.collectEntity(bow);
        // Should be 1 * 2
        assertEquals(2, player.getAttack());
    }

    @Test 
    @DisplayName("Testings that shield is working")
    public void testShield() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Shield shield = new Shield(null, null, newConfig.getShieldDurability(), newConfig.getShieldDefence(), "shield");
        player.collectEntity(shield);
        // Defence should be 1
        assertEquals(1, player.getDefence());
    }

    @Test
    @DisplayName("Testing that bow and sword work in tandem")
    public void testBowandSword() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Sword sword = new Sword(new Position(0, 1), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        Bow bow = new Bow(null, null, newConfig.getBowDurability(), "bow");
        player.collectEntity(sword);
        player.collectEntity(bow);

        // Should be (1 + 2) * 2
        assertEquals(6, player.getAttack());
    }

    @Test
    @DisplayName("Testing if weapons are removed from inventory if durability runs out")
    public void testDurability() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Sword sword = new Sword(new Position(0, 1), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        player.collectEntity(sword);
        assertEquals(player.getInventoryList(), Arrays.asList(sword));

        assertEquals(player.getWeaponryList(), Arrays.asList(sword));

        // Durability is 1
        sword.decreaseDurability();

        // Should be called every tick/round
        player.checkWeaponDurability();

        assertEquals(player.getWeaponryList(), new ArrayList<>());
        assertEquals(player.getInventoryList(), new ArrayList<>());

        Bow bow = new Bow(null, null, newConfig.getBowDurability(), "bow");
        player.collectEntity(bow);
        assertEquals(player.getInventoryList(), Arrays.asList(bow));
        assertEquals(player.getWeaponryList(), Arrays.asList(bow));

        // Durability is 1
        bow.decreaseDurability();
        player.checkWeaponDurability();
        assertEquals(player.getWeaponryList(), new ArrayList<>());
        assertEquals(player.getInventoryList(), new ArrayList<>());

        Shield shield = new Shield(null, null, newConfig.getShieldDurability(), newConfig.getShieldDefence(), "shield");
        player.collectEntity(shield);
        assertEquals(player.getInventoryList(), Arrays.asList(shield));
        assertEquals(player.getWeaponryList(), Arrays.asList(shield));

        // Durability is 1
        shield.decreaseDurability();
        player.checkWeaponDurability();
        assertEquals(player.getWeaponryList(), new ArrayList<>());
        assertEquals(player.getInventoryList(), new ArrayList<>());
    }

    @Test
    @DisplayName("Testing duplicate weapons attack")
    public void testDuplicateWeaponsAttack() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Bow bow = new Bow(null, null, newConfig.getBowDurability(), "bow");
        Bow bow2 = new Bow(null, null, newConfig.getBowDurability(), "bow2");
        Sword sword = new Sword(new Position(0, 1), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword");
        Sword sword2 = new Sword(new Position(0, 2), null, newConfig.getSwordDurability(), newConfig.getSwordAttack(), "sword2");

        player.collectEntity(bow);
        player.collectEntity(bow2);
        player.collectEntity(sword);
        player.collectEntity(sword2);

        // Should be (1 + 2 + 2) * 4
        assertEquals(20, player.getAttack());
    }

    @Test
    @DisplayName("Testing duplicate weapons defence")
    public void testDuplicateWeaponsDefence() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("simple");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);

        Shield shield = new Shield(null, null, newConfig.getShieldDurability(), newConfig.getShieldDefence(), "shield");
        Shield shield2 = new Shield(null, null, newConfig.getShieldDurability(), newConfig.getShieldDefence(), "shield2");

        player.collectEntity(shield);
        player.collectEntity(shield2);

        // Should be 1 + 1
        assertEquals(2, player.getDefence());
    }
}
