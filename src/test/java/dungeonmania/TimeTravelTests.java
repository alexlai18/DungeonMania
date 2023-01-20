package dungeonmania;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

import static dungeonmania.TestUtils.*;

public class TimeTravelTests {
    // W  W  W  W
    // TP VP CP 
    //    W  T  P          
    // M  W  B  FS
    // SS    TT Swd
    // D  K  ZTS
    // E
    
    @Test
    @DisplayName("Time Turner 1 tick")
    public void testTimeTurnerOneTicker() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel", "simple");
        dmc.tick(Direction.DOWN);
        DungeonResponse prevDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse expectedOldPlayer = getPlayer(prevDungeonRes).get();
        dmc.tick(Direction.LEFT);
        DungeonResponse currentDungeonRes = assertDoesNotThrow(() -> dmc.rewind(1));
        EntityResponse oldPlayer = getEntities(currentDungeonRes, "older_player").get(0);
        EntityResponse currentPlayer = getPlayer(currentDungeonRes).get();
                
        assertEquals(currentPlayer.getPosition(), new Position(2, 4));
        assertEquals(oldPlayer.getPosition(), expectedOldPlayer.getPosition());
    }
    
    @Test
    @DisplayName("Time Turner 5 tick")
    public void testTimeTurnerFiveTicker() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("time_travel", "M3_config");
        EntityResponse oldPlayer = getPlayer(initDungeonRes).get();
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        DungeonResponse currentDungeonRes = assertDoesNotThrow(() -> dmc.rewind(5));
        EntityResponse currPlayer = getPlayer(currentDungeonRes).get();
        assertEquals(oldPlayer.getPosition(), getEntities(currentDungeonRes, "older_player").get(0).getPosition());
        assertEquals(new Position(2, 2), currPlayer.getPosition());
        
        assertEquals(1, countEntityOfType(currentDungeonRes, "sword"));
        assertEquals(1, countEntityOfType(currentDungeonRes, "time_turner"));
        assertEquals(new Position(2, 3), getEntities(currentDungeonRes, "boulder").get(0).getPosition());
        assertEquals(new Position(0, 3), getEntities(currentDungeonRes, "mercenary").get(0).getPosition());
    }
    
    @Test
    @DisplayName("Test older self disappears after time turner use")
    public void testOlderSelfDisappearsTurner() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel", "M3_config");

        dmc.tick(Direction.DOWN);
        EntityResponse oldPlayer = getPlayer(dmc.tick(Direction.DOWN)).get();
        dmc.tick(Direction.LEFT);

        DungeonResponse currentDungeonRes = assertDoesNotThrow(() -> dmc.rewind(1));

        // asserting that the old player exists in the dungeon, with the current player
        assertTrue(currentDungeonRes.getEntities().stream().anyMatch(e -> e.getType().equals("older_player")));

        // asserting the currentPlayer and oldPlayer positions
        EntityResponse currentPlayer = getPlayer(currentDungeonRes).get();
        assertEquals(currentPlayer.getPosition(), new Position(2, 4));
        assertEquals(oldPlayer.getPosition(), getEntities(currentDungeonRes, "older_player").get(0).getPosition());
        
        // asserting that after 1 tick (as it is only rewinded 1 tick), the old player disappears
        currentDungeonRes = dmc.tick(Direction.LEFT);
        assertEquals(getEntities(currentDungeonRes, "player").get(0).getPosition(), new Position(1, 4));
        assertTrue(getEntities(currentDungeonRes, "older_player").isEmpty());
    }
    
    @Test
    @DisplayName("Test time travel portal works")
    public void testTimePortalWorks() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");
        for (int i = 0; i < 13; i++) {
            dmc.tick(Direction.UP);
            dmc.tick(Direction.DOWN);
        }
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        DungeonResponse currentDungeonRes = dmc.tick(Direction.LEFT);
        EntityResponse newPlayer = getPlayer(currentDungeonRes).get();
        // Check passed through portal
        assertEquals(newPlayer.getPosition(), new Position(0, 1));
        // Check old player at start
        EntityResponse oldPlayer = getEntitiesStream(currentDungeonRes, "older_player").findFirst().get();
        assertEquals(oldPlayer.getPosition(), new Position(3, 2));

        currentDungeonRes = dmc.tick(Direction.LEFT);
        assertEquals(new Position(-1, 1), getPlayer(currentDungeonRes).get().getPosition());
        assertEquals(new Position(3, 1), getEntities(currentDungeonRes, "older_player").get(0).getPosition());
    }
    
    @Test
    @DisplayName("Test older self interact still called")
    public void testOlderInteract() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel", "M3_config");
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        assertDoesNotThrow(() -> dmc.interact("zombie_toast_spawner:1"));
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.rewind(5));
        // 3 more ticks
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse currentDungeonRes = dmc.tick(Direction.DOWN);
        // assert destroyed and player gone
        assertEquals(getEntities(currentDungeonRes, "zombie_toast_spawner").isEmpty(), true);
        assertEquals(getEntities(currentDungeonRes, "older_player").isEmpty(), true);
    }

    @Test
    @DisplayName("Test older self item tick still called")
    public void testOlderItemTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_bomb", "M3_config");
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        assertDoesNotThrow(() -> dmc.tick("invincibility_potion:1"));
        assertDoesNotThrow(() -> dmc.tick("bomb:1"));
        dmc.tick(Direction.DOWN);
        assertDoesNotThrow(() -> dmc.rewind(5));
        DungeonResponse currentDungeonRes = dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        currentDungeonRes = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(currentDungeonRes, "bomb").isEmpty(), false);
    }
    
    @Test
    @DisplayName("Test time travel persists inventory")
    // For example picks up sword, goes back in time. The sword is on the groud and in the inventory
    public void testInventoryPersists() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel", "M3_config");
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        assertDoesNotThrow(() -> dmc.rewind(5));
        DungeonResponse currentDungeonRes = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(currentDungeonRes, "sword").isEmpty(), false);
        assertEquals(getInventory(currentDungeonRes, "sword").size(), 1);
    }
    
    @Test
    @DisplayName("Test time travel fights older self")
    public void testFightOlderSelf() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.rewind(5));
        DungeonResponse currentDungeonRes = dmc.tick(Direction.UP);
        // battle occured so old player should die
        assertEquals(getEntities(currentDungeonRes, "older_player").isEmpty(), true);
        assertEquals(getPlayer(currentDungeonRes).orElse(null), null);
    }

    @Test
    @DisplayName("Test time travel fights older self with invincibility potion")
    public void testFightInvincibilityPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.rewind(5));
        assertDoesNotThrow(() -> dmc.tick("invincibility_potion:1"));
        DungeonResponse currentDungeonRes = dmc.tick(Direction.UP);
        // battle occured so old player should die
        assertEquals(getEntities(currentDungeonRes, "older_player").isEmpty(), true);
        assertNotEquals(getPlayer(currentDungeonRes).get(), null);
    }
    
    @Test
    @DisplayName("Older self sun stone no battle")
    public void testOlderSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_wood", "M3_config");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        // the player has a sun stone at this point
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        // Goes into portal
        DungeonResponse currentDungeonRes = dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.LEFT);
            currentDungeonRes = dmc.tick(Direction.RIGHT);
        }

        // Player overlaps with the older player (Player has no sunstone but older player picks it up)
        currentDungeonRes = dmc.tick(Direction.DOWN);
        //System.out.println(getEntities(currentDungeonRes, "older_player").get(0).getPosition());
        assertEquals(getPlayer(currentDungeonRes).get().getPosition(), getEntities(currentDungeonRes, "older_player").get(0).getPosition());
        currentDungeonRes = dmc.tick(Direction.DOWN);
        
        // skips battle
        // Fights mercenary twice
        assertEquals(currentDungeonRes.getBattles().size(), 2);
    }

    @Test
    @DisplayName("Older self invisibility potion no battle")
    public void testOlderInvisPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel", "M3_config");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        // Use invisibility potion
        DungeonResponse currentDungeonRes = dmc.tick(Direction.DOWN);
        String invisId = getInventory(currentDungeonRes, "invisibility_potion").get(0).getId();
        assertDoesNotThrow(()-> dmc.tick(invisId));

        // Go to portal and enter
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        // Go to fight older player
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        currentDungeonRes = dmc.tick(Direction.RIGHT);
        String invinceId = getInventory(currentDungeonRes, "invincibility_potion").get(0).getId();
        assertDoesNotThrow(()-> dmc.tick(invinceId));

        // Overlaps with older player
        currentDungeonRes = dmc.tick(Direction.UP);
        assertEquals(getPlayer(currentDungeonRes).get().getPosition(), getEntities(currentDungeonRes, "older_player").get(0).getPosition());

        // skips battle
        // Fights mercenary twice
        assertEquals(currentDungeonRes.getBattles().size(), 2);
    }
    
    
    @Test 
    @DisplayName("Current self sun stone no battle")
    public void testCurrentSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");

        // Going to the portal
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);

        // Going back to fight the old player
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse currentDungeonRes = dmc.tick(Direction.RIGHT);
        assertTrue(getInventory(currentDungeonRes, "sun_stone").size() == 2);
        
        // They don't fight
        currentDungeonRes = dmc.tick(Direction.RIGHT);

        // The player fights the mercenary twice on its way to the portal and back to the player
        assertEquals(currentDungeonRes.getBattles().size(), 2);
    }
    
    @Test
    @DisplayName("Current self midnight armour no battle")
    public void testCurrentMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");

        // Collecting resources to build the midnight armour
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        assertDoesNotThrow(()-> dmc.build("midnight_armour"));

        // Going to the portal
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);

        // Going back to fight the old player
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        
        // They don't fight
        DungeonResponse currentDungeonRes = dmc.tick(Direction.RIGHT);

        // The player fights the mercenary twice on its way to the portal and back to the player
        assertEquals(currentDungeonRes.getBattles().size(), 2);
    }

    @Test
    @DisplayName("Current self invisibility potion no battle")
    public void testCurrentInvisPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel", "M3_config");
        
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        DungeonResponse currentDungeonRes = dmc.tick(Direction.RIGHT);
        String invisId = getInventory(currentDungeonRes, "invisibility_potion").get(0).getId();
        assertDoesNotThrow(()-> dmc.tick(invisId));
        currentDungeonRes = dmc.tick(Direction.RIGHT);
        assertEquals(currentDungeonRes.getBattles().size(), 0);
    }

    // Vincent
    @Test 
    @DisplayName("Older self interacts with the map")
    public void testOlderInteractsWithMap() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        DungeonResponse currentDungeonRes = dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        Position boulderPos = getEntities(currentDungeonRes, "boulder").get(0).getPosition();

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        currentDungeonRes = dmc.tick(Direction.LEFT);

        // boulder should be moved up too
        assertEquals(boulderPos, getEntities(currentDungeonRes, "boulder").get(0).getPosition());
    }
    
    @Test
    @DisplayName("If the time travel is longer than history of dungeon, it goes to initial state")
    public void testTravelTooFar() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("time_travel_no_zombie", "M3_config");

        // The positions of entities
        Position mercPosition = getEntities(initDungeonRes, "mercenary").get(0).getPosition();
        Position coinPosition = getEntities(initDungeonRes, "treasure").get(0).getPosition();

        // Walks to time travel portal
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        // Reaches the portal and should have the same initial state
        DungeonResponse currentDungeonRes = dmc.tick(Direction.LEFT);
        assertEquals(mercPosition, getEntities(currentDungeonRes, "mercenary").get(0).getPosition());
        assertEquals(coinPosition, getEntities(initDungeonRes, "treasure").get(0).getPosition());
    }

    @Test
    @DisplayName("Older self disappears when entering time travelling portal")
    public void testOlderSelfDisappearsPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("time_travel_no_zombie", "M3_config");

        // Walks to time travel portal
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        DungeonResponse currentDungeonRes = dmc.tick(Direction.LEFT);

        assertFalse(getEntities(currentDungeonRes, "older_player").isEmpty());

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        currentDungeonRes = dmc.tick(Direction.RIGHT);

        assertTrue(getEntities(currentDungeonRes, "older_player").isEmpty());
    }
}