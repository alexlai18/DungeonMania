package dungeonmania;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;

import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.staticentity.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.simple_goals.TreasureGoal;
import dungeonmania.entity.movingentity.Mercenary;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class SunStoneTests {
    Config config = new Config("simple");

    @Test
    @DisplayName("Test Sun Stone can be picked up")
    public void testSunStonePickup() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);

        SunStone sunstone = new SunStone(null, null, "sunstone");
        player.collectEntity(sunstone);
        assertTrue(player.getInventoryList().contains(sunstone));
    }

    @Test
    @DisplayName("Test SunStone can be used to open a door")
    public void testSunStoneOpensDoor() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Door door = new Door(new Position(0, 1), 0, null, "");

        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 0));

        SunStone sunstone = new SunStone(null, null, "sunstone");
        player.collectEntity(sunstone);

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 1));

        // Testing that the Sun Stone is retained after use
        assertTrue(player.getInventoryList().contains(sunstone));
    }

    @Test
    @DisplayName("Test that SunStone is used rather than key")
    public void testSunStoneOverKey() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Door door = new Door(new Position(0, 1), 0, null, "");

        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        Key key = new Key(null, null, 0, "key");
        player.collectEntity(key);
        SunStone sunstone = new SunStone(null, null, "sunstone");
        player.collectEntity(sunstone);

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 1));

        assertTrue(player.getInventoryList().contains(key));
        assertTrue(player.getInventoryList().contains(sunstone));
    }

    @Test
    @DisplayName("Test SunStone counts towards treasure goal")
    public void testSunStoneContributesGoal() {
        Dungeon currentDungeon = new Dungeon("mercenary_treasure_goal", "simple", 1);
        Player player = new Player(new Position(0, 0), null, config, "", currentDungeon);
        currentDungeon.setPlayer(player);
        TreasureGoal newGoal = new TreasureGoal(currentDungeon.getConfig());

        assertTrue(newGoal.evaluate(currentDungeon).contains(":treasure"));
        assertEquals(1, newGoal.getNumberofTreasureToGet());

        SunStone sunstone = new SunStone(null, null, "sunstone");
        player.collectEntity(sunstone);

        assertEquals(newGoal.evaluate(currentDungeon), "");
        assertTrue(newGoal.isCompleted());
    }

    @Test
    @DisplayName("Test SunStone cannot be used to bribe mercenaries")
    public void testSunStoneCannotBribe() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");
        SunStone sunstone = new SunStone(new Position(0, 5), null, "coin");
        player.collectEntity(sunstone);

        assertTrue(player.getInventoryList().contains(sunstone));
        
        player.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, merc));
        
        assertThrows(InvalidActionException.class, () -> player.bribe(merc));

        assertEquals(merc.getBribed(), false);
    }
}
