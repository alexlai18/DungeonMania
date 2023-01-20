package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonControllerTests {
    @Test
    @DisplayName("Dungeon test door usage")
    public void testDungeon2DoorsSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("2_doors", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player downward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we are now on the exit with the key
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Check Portal Movement")
    public void testDungeonPortalMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("portals", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 0), false);

        // get player's current position
        DungeonResponse actualDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert current position
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // Move player to 4,0 where the red portal is. This teleports player to 4,3
        // where the other portal is.
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 5), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 6), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();
        
        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(6, 6), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(6, 7), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(7, 7), false);

        // assert after movement
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we are now on the exit at 7,7
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test building a bow")
    public void testBuildBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("build_bow", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert wood is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:1", "wood"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert arrow is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("arrow")).count() == 1);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert arrow is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("arrow")).count() == 2);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert arrow is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("arrow")).count() == 3);

        // We now build a bow and check that it works as expected
        actualDungeonRes = assertDoesNotThrow(() -> dmc.build("bow"));

        // Check bow in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bow:1", "bow"))));
        
        // Check items have been removed from the inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("wood")).count() == 0);
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("arrows")).count() == 0);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 2), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we are now on the exit
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test building a shield")
    public void testBuildShield() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("build_shield", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert wood is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:1", "wood"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert wood is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:2", "wood"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        // We now build a shield and check that it works as expected
        actualDungeonRes = assertDoesNotThrow(() -> dmc.build("shield"));

        // Check shield in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("shield:1", "shield"))));
        
        // Check items have been removed from the inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:1", "wood"))));
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:2", "wood"))));
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player downward to check that we don't still move into the door
        // since we don't have the key
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we are now on the exit
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test maze walking into walls")
    public void testMazeWalkingIntoWalls() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("maze", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player leftward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement that player hasn't moved since we have a wall to our left
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement that player hasn't moved since we have a wall to our right as well.
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement that player hasn't moved since we have a wall above as well.
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement that player has moved since no wall is below us.
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test bomb works and entities get removed")
    public void testBombWorks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("bombs", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert bomb is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bomb:1", "bomb"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // Use bomb
        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("bomb:1"));

        // assert bomb is not in inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bomb:1", "bomb"))));

        // assert entities have been removed
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.equals(new EntityResponse("wall:1", "wall", new Position(5, 2), false)))); 
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.equals(new EntityResponse("treasure:1", "treasure", new Position(5, 3), false)))); 

        // assert other wall remains
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.equals(new EntityResponse("wall:2", "wall", new Position(6, 2), false)))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);
        
        // assert treasure is not in inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:1", "treasure"))));
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 2), false);

        // move player leftward to walk into the wall that used to exist
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test bombs with bomb radius 2")
    public void testBombwithBombRadius2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("bombs", "bomb_radius_2");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert bomb is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bomb:1", "bomb"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // Use bomb
        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("bomb:1"));

        // assert bomb is not in inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bomb:1", "bomb"))));

        // assert entities have been removed
        assertTrue(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("wall")).count() == 0);
        assertTrue(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("treasure")).count() == 0);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);
        
        // assert treasure is not in inventory
        assertTrue(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("treasure")).count() == 0);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 2), false);

        // move player leftward to walk into the wall that used to exist
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test bomb doesn't work and entities remain")
    public void testBombDoesntWork() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("bombs", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert bomb is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bomb:1", "bomb"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement
        assertEquals(expectedPlayer, actualPlayer);
        
        // Use bomb
        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("bomb:1"));

        // assert bomb is not in inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("bomb:1", "bomb"))));

        // assert treasure is in inventory
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.equals(new EntityResponse("treasure:1", "treasure", new Position(5, 3), false)))); 
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 3), false);

        // move player upward to walk into the wall
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement that we don't move into the wall
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(6, 3), false);

        // move player rightward to walk away
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test advanced portals - check multiple portal pairs work as expected")
    public void testAdvancedPortals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("portals_advanced", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(6, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 5), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test boulders goal and map")
    public void testBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("boulders", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 2), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 5), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 6), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 6), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 6), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 6), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 6), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 7), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 7), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 6), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 5), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 5), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we pushed all the boulders
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test zombies don't spawn")
    public void testZombiesDontSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("zombies", "no_zombie_spawning");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // assert sword is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sword:1", "sword"))));
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 4), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 4), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // Assert no zombies have spawned
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast"))); 

    }

    @DisplayName("Test zombie spawner destruction")
    public void testZombieSpawnerDestroy() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("zombies", "no_zombie_spawning");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // Test that interacting with a zombie spawner throws an invalid action exception as
        // we don't have a sword yet
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner:1"));

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // assert sword is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sword:1", "sword"))));
    
        // Test that interacting with a zombie spawner throws an invalid action exception as
        // we aren't cardinally adjacent to the spawner, we're diagonal.
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner:1"));
            
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // Check breaking the spawner does not throw an exception now since all criteria have been fulfilled.
        assertDoesNotThrow(() -> dmc.interact("zombie_toast_spawner:1"));
        actualDungeonRes = dmc.getDungeonResponseModel();
    
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward and see if we can walk into where the spawner used to be.
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // check that the zombie spawner doesn't exist
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast_spawner"))); 
    }

    @Test
    @DisplayName("Test mercenary simple")
    public void testMercenarySimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("mercenary", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:1", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:2", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:3", "treasure"))));

        // Interact with the mercenary
        actualDungeonRes = assertDoesNotThrow(() -> dmc.interact("mercenary:1"));

        // assert that mercenary still exists, if mercenary wasn't an ally, it would be removed
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary"))); 
    
        // Assert we still have two treasure since simple config only takes 1.
        assertEquals(2, actualDungeonRes.getInventory().stream().filter(e -> e.getType().equals("treasure")).count());
    }

    @Test
    @DisplayName("Test Assassin bribe")
    public void testAssassinBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("assassin", "assassin_bribe");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:1", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:2", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:3", "treasure"))));

        // Interact with the assassin
        actualDungeonRes = assertDoesNotThrow(() -> dmc.interact("assassin:1"));

        // assert that assassin still exists, if assassin wasn't an ally, it would be removed
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("assassin"))); 
    
        // Assert we still have two treasure since simple config only takes 1.
        assertEquals(2, actualDungeonRes.getInventory().stream().filter(e -> e.getType().equals("treasure")).count());
    }

    @Test
    @DisplayName("Test Assassin bribe failed")
    public void testAssassinBribeFail() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("assassin", "assassin_bribe_fail");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:1", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:2", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:3", "treasure"))));

        // Interact with the assassin
        actualDungeonRes = assertDoesNotThrow(() -> dmc.interact("assassin:1"));

        // assert that assassin still existsed
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("assassin"))); 
    
        // Assert we still have two treasure since simple assassin_bribe_fail only takes 1.
        assertEquals(2, actualDungeonRes.getInventory().stream().filter(e -> e.getType().equals("treasure")).count());

        // Move player to (7, 1)
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(7, 1), false);

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert that assassin doesn't exist after battle 
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("assassin"))); 
    
        // Assert we still have two treasure since simple assassin_bribe_fail only takes 1.
        assertEquals(2, actualDungeonRes.getInventory().stream().filter(e -> e.getType().equals("treasure")).count());

    }

    @Test
    @DisplayName("Test mercenary bribe amount 3")
    public void testMercenaryBribeAmount3() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("mercenary", "bribe_amount_3");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:1", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:2", "treasure"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("treasure:3", "treasure"))));

        // Interact with the mercenary
        actualDungeonRes = assertDoesNotThrow(() -> dmc.interact("mercenary:1"));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // assert that mercenary still exists, if mercenary wasn't an ally, it would be removed
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary"))); 
    
        // check that mercenary follows player movement
        assertEquals(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().get().getPosition(), new Position(4, 1));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(6, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        assertEquals(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().get().getPosition(), new Position(5, 1));

        // Assert we still have no treasure since this config takes 3.
        assertEquals(0, actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("treasure")).count());
    }

    @Test
    @DisplayName("Test exit goal order ")
    public void testExitGoalOrder() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("exit_goal_order", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal hasn't been completed
        assertFalse(actualDungeonRes.getGoals().equals(""));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal hasn't been completed
        assertFalse(actualDungeonRes.getGoals().equals(""));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal hasn't been completed
        assertFalse(actualDungeonRes.getGoals().equals(""));
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player left
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we finished all seperate goals.
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

        
    @Test
    @DisplayName("Test or goal success in order")
    public void testOrGoalOnly() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("check_or_exit", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test advanced and controller exceptions")
    public void testAdvancedSimple() {
        DungeonManiaController dmc = new DungeonManiaController();

        // We begin by trying to create a dungeon with a name that is invalid and check
        // it throws an illegal argument exception.
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("bruh moment", "simple"));


        // We then try to create a dungeon with the advanced map but an invalid config and check
        // it throws an illegal argument exception.
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("advanced", "bruh moment"));

        // We now actually create the dungeon for use in further testing.
        DungeonResponse initDungeonRes = dmc.newGame("advanced", "simple");

        // We test the getDungeonResponseModel() method and check it's the same as
        // our previous response since we haven't done any changes.
        DungeonResponse newResponse = dmc.getDungeonResponseModel();
        assertTrue(newResponse.equals(initDungeonRes));

        // We test that an illegal argument exception is thrown for not giving
        // a bomb, invincibility potion or invisibility potion.
        assertThrows(IllegalArgumentException.class, () -> dmc.tick("wood:1"));

        // We test that an invalid action exception is thrown for not giving
        // a bomb in our inventory.
        assertThrows(InvalidActionException.class, () -> dmc.tick("bomb:1"));

        // We test that an illegal argument exception is thrown for not giving
        // a bow or shield.
        assertThrows(IllegalArgumentException.class, () -> dmc.build("wood"));

        // We test that an invalid action exception is thrown for not having
        // enough items to build a bow in our inventory.
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        // We test that an illegal argument exception is thrown for interacting with
        // an invalid entity.
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("player:1"));

        // We test that an invalid action exception is thrown for not being in range and not having enough
        // gold to bribe the mercenary.
        assertThrows(InvalidActionException.class, () -> dmc.interact("mercenary:1"));

        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Check that we now have invincibility_potion in our inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));

        // Check that we now have invincibility_potion in our inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));

        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("invincibility_potion:1"));

        // Check that we now don't have invincibility_potion in our inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Check that we now have invisibility_potion in our inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invisibility_potion:1", "invisibility_potion"))));

        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("invisibility_potion:1"));

        
        // Check that we now don't have invisibility_potion in our inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invisibility_potion:1", "invisibility_potion"))));

    }

    @Test
    @DisplayName("Test zombie movement with invincibility potion")
    public void testZombieMovementWithInvincibility() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("zombies_invincibility", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert invincibility_potion is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));
        

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 4), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 4), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Assert zombies exist
        assertTrue(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).count() > 0);

        // We now use the invincibility potion
        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("invincibility_potion:1"));
        // assert invincibility_potion is not in inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    }


    @Test
    @DisplayName("Test Hydra movement with invincibility potion")
    public void testHydraMovementWithInvincibility() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("hydra_invincibility", "M3_config");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert invincibility_potion is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));
        

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 4), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 4), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player upward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // Assert hydra exist
        assertTrue(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("hydra")).count() > 0);

        // We now use the invincibility potion
        actualDungeonRes = assertDoesNotThrow(() -> dmc.tick("invincibility_potion:1"));
        // assert invincibility_potion is not in inventory
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("invincibility_potion:1", "invincibility_potion"))));
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test Complex Conjunction Goals work appropriately.")
    public void testComplexConjunctionGoals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("exit_conjunction_goal", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        
        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal hasn't been completed
        assertFalse(actualDungeonRes.getGoals().equals(""));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);
        
        // assert goal has been completed as string should be empty
        // since we finished all seperate goals.
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test that sunstone completes the treasure goal")
    public void testSunStoneAsTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("sunstone_goal", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test mercenary bribe failed with sunstone amount 3")
    public void testMercenaryBribeFailedWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("mercenary_fail", "bribe_amount_3");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:2", "sun_stone"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert movement 
        assertEquals(expectedPlayer, actualPlayer);

        // assert treasure in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:3", "sun_stone"))));

        // Interact with the mercenary
        assertThrows(InvalidActionException.class, () -> dmc.interact("mercenary:1"));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 1), false);

        // move player right
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // assert that mercenary does not exist
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }

    @Test
    @DisplayName("Test sunstone opens door in place of key")
    public void testSunStoneOpensDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("2_doors_sunstone", "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player downward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 3), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 4), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
        // sunstone used to open door and is retained in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3), false);

        // Move player upward, then to the right
        actualDungeonRes = dmc.tick(Direction.UP);
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // Asserting that it goes through both doors
        assertEquals(expectedPlayer, actualPlayer);

        // sunstone is used rather than key
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        // Move player to exit
        actualDungeonRes = dmc.tick(Direction.DOWN);
        // assert goal has been completed as string should be empty
        // since we are now on the exit with the key
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test that midnight armour cannot be created if zombie is in the dungeon")
    public void testArmourNoCreation() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("zombie_no_armour", "M3_config");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sword is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sword:1", "sword"))));


        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sun stone is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));

        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test that midnight armour can be built with right resources")
    public void testArmourCreated() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("midnight_armour", "M3_config");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sword is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sword:1", "sword"))));


        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sun stone is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));

        actualDungeonRes = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("midnight_armour:1", "midnight_armour"))));
    }

    @Test
    @DisplayName("Test that midnight armour has (essentially) infinite durability")
    public void testArmourSurvives() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("midnight_armour", "M3_op_merc");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sword is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sword:1", "sword"))));


        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sun stone is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sun_stone:1", "sun_stone"))));

        actualDungeonRes = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("midnight_armour:1", "midnight_armour"))));
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));


        assertTrue(actualDungeonRes.getEntities().get(0).getType() == "mercenary");

        actualDungeonRes = dmc.tick(Direction.RIGHT);
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("midnight_armour:1", "midnight_armour"))));
    }

    @Test
    @DisplayName("Test dungeon generation works as expected")
    public void testDungeonCreation() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.generateDungeon(0, 0, 10, 10, "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // Assert that we have the exit goal in the dungeon.
        assertTrue(initDungeonRes.getGoals().contains(":exit"));

        // Assert that we have an exit entity in the map.
        assertTrue(initDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("exit")));

        // Assert that we have a player on the map where we expect.
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 0), false);
        assertEquals(expectedPlayer, initPlayer);

    }

    @Test
    @DisplayName("Test negative dungeon generation doesn't throw a fit")
    public void testNegativeDungeonCreation() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.generateDungeon(-10, -10, 10, 10, "simple");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // Assert that we have the exit goal in the dungeon.
        assertTrue(initDungeonRes.getGoals().contains(":exit"));

        // Assert that we have an exit entity in the map.
        assertTrue(initDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("exit")));

        // Assert that we have a player on the map where we expect.
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(-10, -10), false);
        assertEquals(expectedPlayer, initPlayer);
    }

    @DisplayName("Test mercenary mind control, and when it runs out")
    public void testMercenaryMindControl() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("mind_control", "M3_config");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert wood is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:1", "wood"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("treasure")).count() == 1);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sun stone is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("sun_stone")).count() == 1);

        // We now build a sceptre and check that it works as expected
        actualDungeonRes = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Check sceptre in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sceptre:1", "sceptre"))));
        
        // Check items have been removed from the inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("wood")).count() == 0);
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("key")).count() == 0);
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("sun_stone")).count() == 0);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 1), false);
        
        // Interact with the mercenary
        actualDungeonRes = assertDoesNotThrow(() -> dmc.interact("mercenary:1"));

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
        
        // assert that mercenary still exists, if mercenary wasn't an ally, it would be removed
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary"))); 

        // check that mercenary follows player movement
        assertEquals(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().get().getPosition(), new Position(4, 1));


        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 2), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // The mercenary should no longer be an ally, as the mind control runs out, so we fight the mercenary
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sceptre:1", "sceptre"))));
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        

        // Back on track
        dmc.tick(Direction.LEFT);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we are now on the exit
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test that the sceptre works at a large range")
    public void testSceptreUnlimitedRange() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("mind_control", "M3_config");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        // move player rightward
        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert wood is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("wood:1", "wood"))));

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("treasure")).count() == 1);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert sun stone is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("sun_stone")).count() == 1);

        // We now build a sceptre and check that it works as expected
        actualDungeonRes = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Check sceptre in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sceptre:1", "sceptre"))));
        
        // Check items have been removed from the inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("wood")).count() == 0);
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("key")).count() == 0);
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("sun_stone")).count() == 0);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 1), false);
        
        // Interact with the mercenary
        actualDungeonRes = assertDoesNotThrow(() -> dmc.interact("mercenary:1"));

        // move player rightward
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
        
        // assert that mercenary still exists, if mercenary wasn't an ally, it would be removed
        assertTrue(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary"))); 

        // check that mercenary follows player movement
        assertEquals(actualDungeonRes.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().get().getPosition(), new Position(4, 1));


        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 2), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);

        // The mercenary should no longer be an ally, as the mind control runs out, so we fight the mercenary
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        assertFalse(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("sceptre:1", "sceptre"))));
        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        

        // Back on track
        dmc.tick(Direction.LEFT);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 2), false);

        // move player leftward
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert goal has been completed as string should be empty
        // since we are now on the exit
        assertTrue(actualDungeonRes.getGoals().equals(""));
    }

    @Test
    @DisplayName("Testing that potion effects work")
    public void testPotionEffects() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("potion_effects", "M3_config");
        
        // move player rightward
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        DungeonResponse actualDungeonRes = dmc.tick(Direction.RIGHT);

        // Should have potions and key in the inventory
        assertTrue(actualDungeonRes.getInventory().stream().filter(item -> item.getType().equals("invincibility_potion")).count() == 1);
        assertDoesNotThrow(() -> dmc.tick("invincibility_potion:1"));

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        assertFalse(actualDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }
}
