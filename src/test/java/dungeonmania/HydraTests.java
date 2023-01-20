package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.collectableitem.InvincibilityPotion;
import dungeonmania.entity.inventoryitem.collectableitem.Key;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HydraTests {
    Config config = new Config("simple");


    // Hydra moves from position, not in same spot
    @Test
    @DisplayName("Hydra moves from position")
    public void testHydraMove() {
        Dungeon dungeon = new Dungeon("empty", "simple", 0);

        Hydra hydra = new Hydra(new Position(0, 0), new ArrayList<Entity>(), "hydra", dungeon);
        
        Position oldPosition = hydra.getPosition();
        hydra.move(null);
        
        assertNotEquals(oldPosition, hydra.getPosition());
    }
    

    // Hydra cannot move through portals
    @Test
    @DisplayName("Hydra cannot move through portals")
    public void testHydraCannotMoveThroughPortal() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Portal portalBlue1 = new Portal(new Position(0, 1), null, "");
        Portal portalBlue2 = new Portal(new Position(100, 0), null, "");
        portalBlue1.setLinkedPortal(portalBlue2);
        portalBlue2.setLinkedPortal(portalBlue1);

        Portal portalRed1 = new Portal(new Position(0, -1), null, "");
        Portal portalRed2 = new Portal(new Position(100, 1), null, "");
        portalRed1.setLinkedPortal(portalRed2);
        portalRed2.setLinkedPortal(portalRed1);

        Portal portalGreen1 = new Portal(new Position(1, 0), null, "");
        Portal portalGreen2 = new Portal(new Position(100, 2), null, "");
        portalGreen1.setLinkedPortal(portalGreen2);
        portalGreen2.setLinkedPortal(portalGreen1);

        Portal portalPurple1 = new Portal(new Position(-1, 0), null, "");
        Portal portalPurple2 = new Portal(new Position(100, 3), null, "");
        portalPurple1.setLinkedPortal(portalPurple2);
        portalPurple2.setLinkedPortal(portalPurple1);

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        portalBlue1.setAdjacentEntities(Arrays.asList(h));
        portalRed1.setAdjacentEntities(Arrays.asList(h));
        portalGreen1.setAdjacentEntities(Arrays.asList(h));
        portalPurple1.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(portalBlue1, portalRed1, portalGreen1, portalPurple1));

        // ensure zombie has been block by portal not teleported
        h.move(null);
        assertEquals(h.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Boulder blocks movement of Hydra")
    public void testBoulderBlocksMovementOfHydra() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Boulder boulder1 = new Boulder(new Position(0, 1), null, "");
        Boulder boulder2  = new Boulder(new Position(0, -1), null, "");
        Boulder boulder3 = new Boulder(new Position(1, 0), null, "");
        Boulder boulder4 = new Boulder(new Position(-1, 0), null, "");

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        boulder1.setAdjacentEntities(Arrays.asList(h));
        boulder2.setAdjacentEntities(Arrays.asList(h));
        boulder3.setAdjacentEntities(Arrays.asList(h));
        boulder4.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(boulder1, boulder2, boulder3, boulder4));
        h.move(null);
        assertEquals(h.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Hydra cannot move through closed door")
    public void testHydraCannotMoveThroughClosedDoor() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door1 = new Door(new Position(0, 1), 1, null, "");

        Door door2 = new Door(new Position(0, -1), 2, null, "");

        Door door3 = new Door(new Position(1, 0), 3, null, "");

        Door door4 = new Door(new Position(-1, 0), 4, null, "");

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        door1.setAdjacentEntities(Arrays.asList(h));
        door2.setAdjacentEntities(Arrays.asList(h));
        door3.setAdjacentEntities(Arrays.asList(h));
        door4.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(door1, door2, door3, door4));

        h.move(null);
        assertEquals(h.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Hydra can move through open door")
    public void testHydraCanMoveThroughOpenedDoor() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door1 = new Door(new Position(0, 1), 1, null, "");
        Key key1 = new Key(new Position(100, 0), null, 1, "");
        
        Door door2 = new Door(new Position(0, -1), 2, null, "");
        Key key2 = new Key(new Position(100, 1), null, 2, "");

        Door door3 = new Door(new Position(1, 0), 3, null, "");
        Key key3 = new Key(new Position(100, 2), null, 3, "");

        Door door4 = new Door(new Position(-1, 0), 4, null, "");
        Key key4 = new Key(new Position(100, 3), null, 4, "");

        // make player open doors
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        door1.setAdjacentEntities(Arrays.asList(player));
        door2.setAdjacentEntities(Arrays.asList(player));
        door3.setAdjacentEntities(Arrays.asList(player));
        door4.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door1, door2, door3, door4));

        player.collectEntity(key1);
        player.collectEntity(key2);
        player.collectEntity(key3);
        player.collectEntity(key4);

        // open top
        player.move(Direction.UP);
        player.move(Direction.DOWN);
        // open right
        player.move(Direction.RIGHT);
        player.move(Direction.LEFT);
        // open left
        player.move(Direction.LEFT);
        player.move(Direction.RIGHT);
        // open bottom
        player.move(Direction.DOWN);
        // move out of way
        player.move(Direction.DOWN);

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        door1.setAdjacentEntities(Arrays.asList(h));
        door2.setAdjacentEntities(Arrays.asList(h));
        door3.setAdjacentEntities(Arrays.asList(h));
        door4.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(door1, door2, door3, door4));

        h.move(null);
        assertTrue(Position.isAdjacent(h.getPosition(), new Position(0, 0)));
    }

    @Test
    @DisplayName("Hydra can move past floorswitch")
    public void testHydraCanMoveThroughFloorSwitch() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        FloorSwitch floorswitch1 = new FloorSwitch(new Position(0, 1), false, null, "", new int[] {0});

        FloorSwitch floorswitch2 = new FloorSwitch(new Position(0, -1), false, null, "", new int[] {0});

        FloorSwitch floorswitch3 = new FloorSwitch(new Position(1, 0), false, null, "", new int[] {0});

        FloorSwitch floorswitch4 = new FloorSwitch(new Position(-1, 0), false, null, "", new int[] {0});

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        floorswitch1.setAdjacentEntities(Arrays.asList(h));
        floorswitch2.setAdjacentEntities(Arrays.asList(h));
        floorswitch3.setAdjacentEntities(Arrays.asList(h));
        floorswitch4.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(floorswitch1, floorswitch2, floorswitch3, floorswitch4));

        h.move(null);
        assertTrue(Position.isAdjacent(h.getPosition(), new Position(0, 0)));

    }
    
    // Hydra gets blocked by walls 
    @Test
    @DisplayName("Wall blocks movement of Hydra")
    public void testWallBlocksMovementOfHydra() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Wall wall1 = new Wall(new Position(0, 1), null, "");
        Wall wall2  = new Wall(new Position(0, -1), null, "");
        Wall wall3 = new Wall(new Position(1, 0), null, "");
        Wall wall4 = new Wall(new Position(-1, 0), null, "");

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        wall1.setAdjacentEntities(Arrays.asList(h));
        wall2.setAdjacentEntities(Arrays.asList(h));
        wall3.setAdjacentEntities(Arrays.asList(h));
        wall4.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(wall1, wall2, wall3, wall4));

        h.move(null);
        assertEquals(h.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Zombie Toast Spawner blocks movement of Hydra")
    public void testZombieToastSpawneBlocksMovementOfHydra() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        ZombieToastSpawner zts1 = new ZombieToastSpawner(dungeon, new Position(0, 1), null, "");
        ZombieToastSpawner zts2 = new ZombieToastSpawner(dungeon, new Position(0, -1), null, "");
        ZombieToastSpawner zts3 = new ZombieToastSpawner(dungeon, new Position(1, 0), null, "");
        ZombieToastSpawner zts4 = new ZombieToastSpawner(dungeon, new Position(-1, 0), null, "");

        Hydra h = new Hydra(new Position(0, 0), null, "", dungeon);
        zts1.setAdjacentEntities(Arrays.asList(h));
        zts2.setAdjacentEntities(Arrays.asList(h));
        zts3.setAdjacentEntities(Arrays.asList(h));
        zts4.setAdjacentEntities(Arrays.asList(h));
        h.setAdjacentEntities(Arrays.asList(zts1, zts2, zts3, zts4));

        h.move(null);
        assertEquals(h.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Hydra runs up away from invincible player")
    public void testHydraRunsUpAway() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        
        // Player is now invincible
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
    
        Hydra hydra = new Hydra(new Position(0, 2), null, "hydra", dungeon);
        hydra.invincibilityEffects(dungeon);
        dungeon.setEntities(Arrays.asList(player, hydra));
        hydra.setAdjacentEntities(new ArrayList<>());
        
        hydra.move(null);
        assertEquals(hydra.getPosition(), new Position(0, 3));
    }

    @Test
    @DisplayName("Hydra runs down away from invincible player")
    public void testHydraRunsDownAway() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);

        Player player = new Player(new Position(0, 3), new ArrayList<>(), config, "", dungeon);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        
        // Player is now invincible
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());

        Hydra hydra = new Hydra(new Position(0, 2), null, "hydra", dungeon);

        dungeon.setEntities(Arrays.asList(player, hydra));
        hydra.invincibilityEffects(dungeon);
        hydra.setAdjacentEntities(new ArrayList<>());
        hydra.move(null);
        assertEquals(hydra.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Hydra runs left away from invincible player")
    public void testHydraRunsLeftAway() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);

        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "", dungeon);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        
        // Player is now invincible
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());

        Hydra hydra = new Hydra(new Position(1, 0), null, "hydra", dungeon);

        dungeon.setEntities(Arrays.asList(player, hydra));
        hydra.invincibilityEffects(dungeon);
        hydra.setAdjacentEntities(new ArrayList<>());
        hydra.move(null);
        assertEquals(hydra.getPosition(), new Position(0, 0));
    }
    
}
