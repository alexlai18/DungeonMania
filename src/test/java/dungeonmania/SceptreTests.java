package dungeonmania;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.buildableitem.Sceptre;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.entity.movingentity.Assassin;
import dungeonmania.entity.movingentity.Mercenary;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.util.Position;

public class SceptreTests {

    Config config = new Config("M3_config");

    @Test
    @DisplayName("Test that sceptre is created in inventory")
    public void testCreateSceptre() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);

        // Making a sceptre with wood + key + sunstone
        Wood wood = new Wood(null, null, "wood");
        Key key = new Key(null, null, 1, "key");
        SunStone sunstone = new SunStone(null, null, "sunstone");

        player.collectEntity(wood);
        player.collectEntity(key);
        player.collectEntity(sunstone);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));

        // Making a sceptre with wood + treasure + sunstone
        Wood wood2 = new Wood(null, null, "wood2");
        Treasure coin = new Treasure(null, null, "coin");
        SunStone sunstone2 = new SunStone(null, null, "sunstone2");

        player.collectEntity(wood2);
        player.collectEntity(coin);
        player.collectEntity(sunstone2);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));

        // Making a sceptre with 2 arrows + key + sunstone
        Arrows arrow = new Arrows(null, null, "arrow1");
        Arrows arrow2 = new Arrows(null, null, "arrow2");
        Key key2 = new Key(null, null, 2, "key2");
        SunStone sunstone3 = new SunStone(null, null, "sunstone3");

        player.collectEntity(arrow);
        player.collectEntity(arrow2);
        player.collectEntity(key2);
        player.collectEntity(sunstone3);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));

        // Making a sceptre with 2 arrows + treasure + sunstone
        Arrows arrow3 = new Arrows(null, null, "arrow3");
        Arrows arrow4 = new Arrows(null, null, "arrow4");
        Treasure coin2 = new Treasure(null, null, "coin2");
        SunStone sunstone4 = new SunStone(null, null, "sunstone4");

        player.collectEntity(arrow3);
        player.collectEntity(arrow4);
        player.collectEntity(coin2);
        player.collectEntity(sunstone4);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));

        // Asserting that it throws an exception if there aren't enough resources
        assertThrows(InvalidActionException.class, () -> player.buildEntity("sceptre"));
    }

    @Test
    @DisplayName("Test that sceptre only lasts for a limited time")
    public void testsceptreRunsOut() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        // Making a sceptre with wood + key + sunstone
        Wood wood = new Wood(null, null, "wood");
        Key key = new Key(null, null, 1, "key");
        SunStone sunstone = new SunStone(null, null, "sunstone");
        Treasure treasure = new Treasure(null, null, "treasure");

        player.collectEntity(wood);
        player.collectEntity(key);
        player.collectEntity(sunstone);
        player.collectEntity(treasure);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().filter(Sceptre.class :: isInstance).count() == 1);
        
        // Duration is 3 seconds
        Sceptre scep = (Sceptre) player.getInventoryList().get(1);
        scep.activate();
        player.useSceptre(dungeon, dungeon.getEntities());
        player.useSceptre(dungeon, dungeon.getEntities());
        player.useSceptre(dungeon, dungeon.getEntities());
        
        // Gets deleted in the next tick
        player.useSceptre(dungeon, dungeon.getEntities());

        assertFalse(player.getInventoryList().stream().anyMatch(Sceptre.class :: isInstance));
    }

    @Test
    @DisplayName("Test that sceptre mind controls a mercenary")
    public void testsceptreMindControls() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        // Making a sceptre with wood + key + sunstone
        Wood wood = new Wood(null, null, "wood");
        Key key = new Key(null, null, 1, "key");
        SunStone sunstone = new SunStone(null, null, "sunstone");

        player.collectEntity(wood);
        player.collectEntity(key);
        player.collectEntity(sunstone);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));
        
        // Duration is 3 seconds
        Mercenary merc = new Mercenary(new Position(100, 100), null, config, dungeon, "merc");
        dungeon.setEntities(Arrays.asList(merc));
        player.mindControl(merc, true);

        player.useSceptre(dungeon, dungeon.getEntities());
        player.useSceptre(dungeon, dungeon.getEntities());
        player.useSceptre(dungeon, dungeon.getEntities());

        assertTrue(merc.getMindControlled());
        // Effect runs out in next tick
        player.useSceptre(dungeon, dungeon.getEntities());

        assertFalse(merc.getMindControlled());
        assertFalse(player.getInventoryList().stream().anyMatch(Sceptre.class :: isInstance));
    }

    @Test
    @DisplayName("Test that sceptre mind controls an Assassin")
    public void testsceptreMindControlsAssassin() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        // Making a sceptre with wood + key + sunstone
        Wood wood = new Wood(null, null, "wood");
        Key key = new Key(null, null, 1, "key");
        SunStone sunstone = new SunStone(null, null, "sunstone");

        player.collectEntity(wood);
        player.collectEntity(key);
        player.collectEntity(sunstone);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));
        
        // Duration is 3 seconds
        Assassin a = new Assassin(new Position(100, 100), null, dungeon, "merc");
        dungeon.setEntities(Arrays.asList(a));
        player.mindControl(a, true);

        player.useSceptre(dungeon, dungeon.getEntities());
        player.useSceptre(dungeon, dungeon.getEntities());
        player.useSceptre(dungeon, dungeon.getEntities());

        assertTrue(a.getMindControlled());
        // Effect runs out in next tick
        player.useSceptre(dungeon, dungeon.getEntities());

        assertFalse(a.getMindControlled());
        assertFalse(player.getInventoryList().stream().anyMatch(Sceptre.class :: isInstance));
    }

    @Test
    @DisplayName("Player tries to mind control a dead mercenary")
    public void testMindControlDead() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        // Making a sceptre with wood + key + sunstone
        Wood wood = new Wood(null, null, "wood");
        Key key = new Key(null, null, 1, "key");
        SunStone sunstone = new SunStone(null, null, "sunstone");

        player.collectEntity(wood);
        player.collectEntity(key);
        player.collectEntity(sunstone);

        assertDoesNotThrow(() -> player.buildEntity("sceptre"));
        assertTrue(player.getInventoryList().stream().allMatch(Sceptre.class :: isInstance));

        Mercenary merc = new Mercenary(new Position(100, 100), null, config, dungeon, "merc");
        dungeon.setEntities(Arrays.asList(merc));

        merc = null;
        Sceptre sceptre = (Sceptre) player.getInventoryList().get(0);
        player.mindControl(merc, true);
        assertFalse(sceptre.getActivated());
        player.useSceptre(dungeon, dungeon.getEntities());
        assertFalse(sceptre.getActivated());
    }
}
