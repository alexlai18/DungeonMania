package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.entity.inventoryitem.collectableitem.Treasure;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.staticentity.*;
import dungeonmania.interfaces.movement.ControlledMovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTests {
    Config config = new Config("simple");
    
    @Test
    @DisplayName("swamp tile slows all entities excluding player")
    public void testSlowNonPlayerEntityMovement() {
        // ST
        // M   ST     P
        // ST  S
        //     ST 
        // ST  Z  ST
        //     ST

        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(5, 0), new ArrayList<>(), config, "", dungeon);
        Mercenary merc = new Mercenary(new Position(0, 0), new ArrayList<>(), config, dungeon, "merc");
        Spider spider = new Spider(new Position(1, 1), new ArrayList<>(), config, "spider", dungeon);
        ZombieToast zombie = new ZombieToast(new Position(1, 3), new ArrayList<>(), config, "zomb", dungeon);
        SwampTile st1 = new SwampTile(new Position(1, 0), new ArrayList<>(), "", 1);
        SwampTile st2 = new SwampTile(new Position(1, 2), new ArrayList<>(), "", 1);
        SwampTile st3 = new SwampTile(new Position(1, 4), new ArrayList<>(), "", 1);
        SwampTile st4 = new SwampTile(new Position(0, 3), new ArrayList<>(), "", 1);
        SwampTile st5 = new SwampTile(new Position(2, 3), new ArrayList<>(), "", 1);
        SwampTile st6 = new SwampTile(new Position(0, 1), new ArrayList<>(), "", 1);
        SwampTile st7 = new SwampTile(new Position(0, -1), new ArrayList<>(), "", 1);

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(merc, spider, zombie, st1, st2, st3, st4, st5, st6, st7));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.RIGHT, null);
        assertEquals(new Position(1, 0), merc.getPosition());
        assertEquals(new Position(1, 0), spider.getPosition());
        assertTrue(
            zombie.getPosition().equals(new Position(1, 2)) 
            || zombie.getPosition().equals(new Position(1, 4)) 
            || zombie.getPosition().equals(new Position(2, 3)) 
            || zombie.getPosition().equals(new Position(0, 3)) 
        );
        
        // stuck for one tick
        dungeon.movementTick(Direction.RIGHT, null);
        assertEquals(new Position(1, 0), merc.getPosition());
        assertEquals(new Position(1, 0), spider.getPosition());
        assertTrue(
            zombie.getPosition().equals(new Position(1, 2)) 
            || zombie.getPosition().equals(new Position(1, 4)) 
            || zombie.getPosition().equals(new Position(2, 3)) 
            || zombie.getPosition().equals(new Position(0, 3)) 
        );

        // move off tile
        dungeon.movementTick(Direction.RIGHT, null);
        assertEquals(new Position(2, 0), merc.getPosition());
        assertEquals(new Position(2, 0), spider.getPosition());
        assertFalse(
            zombie.getPosition().equals(new Position(1, 2)) 
            || zombie.getPosition().equals(new Position(1, 4)) 
            || zombie.getPosition().equals(new Position(2, 3)) 
            || zombie.getPosition().equals(new Position(0, 3)) 
        );
    }

    @Test
    @DisplayName("swamp tile doesn't slow movement of player")
    public void testDoesNotSlowPlayer() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        SwampTile st5 = new SwampTile(new Position(2, 1), new ArrayList<>(), "", 1);

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(st5));
        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.DOWN, null);
        dungeon.movementTick(Direction.DOWN, null);

        assertEquals(new Position(2, 2), player.getPosition());
    }

    // TODO Assumption
    @Test
    @DisplayName("movement factor of 0 doesn't slow entities")
    public void testMovementFactor0() {
        // M  ST     P
        //    S
        //    ST 
        // ST Z  ST
        //    ST
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(5, 0), new ArrayList<>(), config, "", dungeon);
        Mercenary merc = new Mercenary(new Position(0, 0), new ArrayList<>(), config, dungeon, "merc");
        Spider spider = new Spider(new Position(1, 1), new ArrayList<>(), config, "spider", dungeon);
        ZombieToast zombie = new ZombieToast(new Position(1, 3), new ArrayList<>(), config, "zomb", dungeon);
        SwampTile st1 = new SwampTile(new Position(1, 0), new ArrayList<>(), "", 0);
        SwampTile st2 = new SwampTile(new Position(1, 2), new ArrayList<>(), "", 0);
        SwampTile st3 = new SwampTile(new Position(1, 4), new ArrayList<>(), "", 0);
        SwampTile st4 = new SwampTile(new Position(0, 3), new ArrayList<>(), "", 0);
        SwampTile st5 = new SwampTile(new Position(2, 3), new ArrayList<>(), "", 0);

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(merc, spider, zombie, st1, st2, st3, st4, st5));
        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.LEFT, null);
        dungeon.movementTick(Direction.RIGHT, null);
        assertEquals(new Position(2, 0), merc.getPosition());
        assertEquals(new Position(2, 0), spider.getPosition());
        assertFalse(
            zombie.getPosition().equals(new Position(1, 2)) 
            || zombie.getPosition().equals(new Position(1, 4)) 
            || zombie.getPosition().equals(new Position(2, 3)) 
            || zombie.getPosition().equals(new Position(0, 3)) 
        );
    }

    // TODO Assumption
    @Test
    @DisplayName("swamp tile doesn't affect allies of player")
    public void testDoesNotSlowAllies() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        SwampTile st5 = new SwampTile(new Position(2, 1), new ArrayList<>(), "", 1);
        Mercenary merc = new Mercenary(new Position(2, -1), null, config, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(st5, coin, merc));
        // add merc to player observer list
        ((ControlledMovingEntity) player.getMovement()).setAllies(Arrays.asList(merc));

        player.collectEntity(coin);
        dungeon.updateAdjacentEntities();

        assertDoesNotThrow(()->player.bribe(merc));
        assertEquals(true, merc.getBribed());

        dungeon.movementTick(Direction.DOWN, null);
        dungeon.movementTick(Direction.DOWN, null);
        dungeon.movementTick(Direction.DOWN, null);

        assertEquals(new Position(2, 2), merc.getPosition());
    }

    // TODO Assumption
    @Test
    @DisplayName("swamp tile doesn't affect boulders")
    public void testDoesNotSlowBoulders() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        SwampTile st5 = new SwampTile(new Position(2, 2), new ArrayList<>(), "", 1);
        Boulder b = new Boulder(new Position(2, 1), new ArrayList<>(), "id");

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(st5, b));
        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.DOWN, null);
        dungeon.movementTick(Direction.DOWN, null);
        assertEquals(new Position(2, 3), b.getPosition());
    }

}
