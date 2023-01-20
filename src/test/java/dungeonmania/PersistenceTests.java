package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PersistenceTests {
    @Test
    @DisplayName("Test no saved game throws invalid argument exception")
    public void testNoSavedGames() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway because bees don't care what humans think is impossible."));
        assertEquals(new ArrayList<String>(), dmc.allGames());
    }
    
    @Test
    @DisplayName("Test saved games work as expected")
    public void testExpectedSavedGames() {
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

        // save the game as "bruh moment"
        assertDoesNotThrow(() -> dmc.saveGame("bruh moment"));

        // Check the list of all games has "bruh moment"
        assertEquals(Arrays.asList("bruh moment"), dmc.allGames());

        initDungeonRes = dmc.newGame("2_doors", "simple");
        initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        // Save the game as "bruh moment 2"
        assertDoesNotThrow(() -> dmc.saveGame("bruh moment 2"));

        // Check the list of all games has "bruh moment" and "bruh moment 2" 
        assertEquals(Arrays.asList("bruh moment", "bruh moment 2"), dmc.allGames());

        // load the game as "bruh moment"
        assertDoesNotThrow(() -> dmc.loadGame("bruh moment"));        

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
        
        // load the game as "bruh moment 2" 
        assertDoesNotThrow(() -> dmc.loadGame("bruh moment 2" ));        

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

        // Clean up files.
        File savedGameOne = new File("./bruh moment.ser");
        File savedGameTwo = new File("./bruh moment 2.ser");
        File savedGameList = new File("./savedGamesList.ser");
        savedGameList.delete();
        savedGameOne.delete();
        savedGameTwo.delete();
    }

    @Test
    @DisplayName("Test overriding saved games")
    public void testOverridingSavedGames() {
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

        // save the game as "bruh moment"
        assertDoesNotThrow(() -> dmc.saveGame("bruh moment"));

        // Check the list of all games has "bruh moment"
        assertEquals(Arrays.asList("bruh moment"), dmc.allGames());

        initDungeonRes = dmc.newGame("2_doors", "simple");
        initPlayer = getPlayer(initDungeonRes).get();

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 3), false);

        // move player downward
        actualDungeonRes = dmc.tick(Direction.DOWN);
        actualPlayer = getPlayer(actualDungeonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);

        // assert key is in inventory
        assertTrue(actualDungeonRes.getInventory().stream().anyMatch(item -> item.equals(new ItemResponse("key:1", "key"))));

        // Save the game as "bruh moment" to overrwrite the old game
        assertDoesNotThrow(() -> dmc.saveGame("bruh moment"));

        // Check the list of all games has only "bruh moment" 
        assertEquals(Arrays.asList("bruh moment"), dmc.allGames());

        // load the game as "bruh moment"
        assertDoesNotThrow(() -> dmc.loadGame("bruh moment"));        
        
        // Expect it to continue as it was playing the same game

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

        // Clean up files.
        File savedGameOne = new File("./bruh moment.ser");
        File savedGameList = new File("./savedGamesList.ser");
        savedGameList.delete();
        savedGameOne.delete();
    }

    @Test
    @DisplayName("Test recreating DMC still has saved games")
    public void testRecreatingDMC() {
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

        // save the game as "bruh moment"
        dmc.saveGame("bruh moment");

        // Check the list of all games has "bruh moment"
        assertEquals(Arrays.asList("bruh moment"), dmc.allGames());

        // Create a new dmc controller  
        dmc = new DungeonManiaController();

        // load the game as "bruh moment"
        // to check that the game can still be loaded
        dmc.loadGame("bruh moment");        

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

        // Clean up files.
        File savedGameOne = new File("./bruh moment.ser");
        File savedGameList = new File("./savedGamesList.ser");
        savedGameList.delete();
        savedGameOne.delete();
    }

}
