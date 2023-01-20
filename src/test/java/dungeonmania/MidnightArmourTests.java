package dungeonmania;


import dungeonmania.util.Position;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.InventoryItem;
import dungeonmania.entity.inventoryitem.Weapon;
import dungeonmania.entity.inventoryitem.buildableitem.*;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.exceptions.InvalidActionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTests {

    Config config = new Config("M3_config");

    @Test
    @DisplayName("Test that midnight armour can be created with sunstone and a sword")
    public void testArmourCreation() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Sword sword = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword");
        SunStone sunstone = new SunStone(null, null, "sunstone");
        Treasure treasure = new Treasure(null, null, "treasure");
        player.collectEntity(sunstone);
        player.collectEntity(sword);
        player.collectEntity(treasure);
        
        assertThrows(InvalidActionException.class, ()-> player.getPlayerInventory().findItem("treasure:1"));

        assertDoesNotThrow(() -> {
            InventoryItem weapon = player.buildEntity("midnight_armour");
            player.equipWeapon(weapon);
        });

        assertTrue(player.getInventoryList().stream().filter(MidnightArmour.class :: isInstance).count() == 1);
        assertTrue(player.getWeaponryList().stream().allMatch(MidnightArmour.class :: isInstance));
    }

    @Test
    @DisplayName("Test that midnight armour increases both attack and defence")
    public void testArmourStatChanges() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Sword sword = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword");
        SunStone sunstone = new SunStone(null, null, "sunstone");
        player.collectEntity(sunstone);
        player.collectEntity(sword);

        assertDoesNotThrow(() -> {
            InventoryItem weapon = player.buildEntity("midnight_armour");
            player.equipWeapon(weapon);
        });

        assertTrue(player.getInventoryList().stream().allMatch(MidnightArmour.class :: isInstance));
        assertTrue(player.getWeaponryList().stream().allMatch(MidnightArmour.class :: isInstance));
        assertEquals(player.getAttack(), config.getPlayerAttack() + config.getMidnightArmourAttack());
        assertEquals(player.getDefence(), config.getMidnightArmourDefence());
    }

    @Test
    @DisplayName("Test that the midnight armour never runs out")
    public void testArmourNever() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Sword sword = new Sword(null, null, config.getSwordDurability(), config.getSwordAttack(), "sword");
        SunStone sunstone = new SunStone(null, null, "sunstone");
        player.collectEntity(sunstone);
        player.collectEntity(sword);

        assertDoesNotThrow(() -> {
            InventoryItem weapon = player.buildEntity("midnight_armour");
            player.equipWeapon(weapon);
        });

        Weapon wep = (Weapon) player.getInventoryList().get(0);
        for (int i = 0; i < 50; i++) {
            wep.use();
        }

        assertTrue(player.getInventoryList().stream().allMatch(MidnightArmour.class :: isInstance));
    }

}
