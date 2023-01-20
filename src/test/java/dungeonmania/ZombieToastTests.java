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

public class ZombieToastTests {
    Config config = new Config("simple");

    // zombie is adjacent to spawner
    @Test
    @DisplayName("Zombie is adjacent to spawner")
    public void testZombieToastSpawn() {
        Dungeon dungeon = new Dungeon("empty", "simple", 0);
        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(0, 0), new ArrayList<>(), "");
        for (int i = 0; i < 10; i++) {
            zts.tick();
        }
        Entity z = dungeon.getEntities().get(0);
        assertTrue(Position.isAdjacent(z.getPosition(), zts.getPosition()));
    }

    // generate multiple zombies
    @Test
    @DisplayName("Spawn multiple zombies according to tick")
    public void testZombieToastSpawnMultiple() {
        // call tick multiple times
        Dungeon dungeon = new Dungeon("empty", "simple", 0);
        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(0, 0), new ArrayList<>(), "");
        for (int i = 0; i < 20; i++) {
            zts.tick();
        }
        Entity z1 = dungeon.getEntities().get(0);
        Entity z2 = dungeon.getEntities().get(1);
        assertTrue(Position.isAdjacent(z1.getPosition(), zts.getPosition()));
        assertTrue(Position.isAdjacent(z2.getPosition(), zts.getPosition()));
    }

    // zombie moves from position, not in same spot
    @Test
    @DisplayName("Zombie moves from position")
    public void testZombieToastMove() {
        Dungeon dungeon = new Dungeon("empty", "simple", 0);
        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(0, 0), new ArrayList<>(), "");
        for (int i = 0; i < 10; i++) {
            zts.tick();
        }
        ZombieToast z = (ZombieToast) dungeon.getEntities().get(0);
        
        Position oldPosition = z.getPosition();
        z.setAdjacentEntities(Arrays.asList(zts));
        z.move(null);
        
        assertNotEquals(oldPosition, z.getPosition());
    }
    

    // zombie cannot move through portals
    @Test
    @DisplayName("Zombie cannot move through portals")
    public void testZombieCannotMoveThroughPortal() {
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

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        portalBlue1.setAdjacentEntities(Arrays.asList(z));
        portalRed1.setAdjacentEntities(Arrays.asList(z));
        portalGreen1.setAdjacentEntities(Arrays.asList(z));
        portalPurple1.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(portalBlue1, portalRed1, portalGreen1, portalPurple1));

        // ensure zombie has been block by portal not teleported
        z.move(null);
        assertEquals(z.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Boulder blocks movement of Zombie Toast")
    public void testBoulderBlocksMovementOfZombieToast() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Boulder boulder1 = new Boulder(new Position(0, 1), null, "");
        Boulder boulder2  = new Boulder(new Position(0, -1), null, "");
        Boulder boulder3 = new Boulder(new Position(1, 0), null, "");
        Boulder boulder4 = new Boulder(new Position(-1, 0), null, "");

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        boulder1.setAdjacentEntities(Arrays.asList(z));
        boulder2.setAdjacentEntities(Arrays.asList(z));
        boulder3.setAdjacentEntities(Arrays.asList(z));
        boulder4.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(boulder1, boulder2, boulder3, boulder4));
        z.move(null);
        assertEquals(z.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Zombie cannot move through closed door")
    public void testZombieCannotMoveThroughClosedDoor() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door1 = new Door(new Position(0, 1), 1, null, "");

        Door door2 = new Door(new Position(0, -1), 2, null, "");

        Door door3 = new Door(new Position(1, 0), 3, null, "");

        Door door4 = new Door(new Position(-1, 0), 4, null, "");

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        door1.setAdjacentEntities(Arrays.asList(z));
        door2.setAdjacentEntities(Arrays.asList(z));
        door3.setAdjacentEntities(Arrays.asList(z));
        door4.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(door1, door2, door3, door4));

        z.move(null);
        assertEquals(z.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Zombie can move through open door")
    public void testZombieCanMoveThroughOpenedDoor() {
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

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        door1.setAdjacentEntities(Arrays.asList(z));
        door2.setAdjacentEntities(Arrays.asList(z));
        door3.setAdjacentEntities(Arrays.asList(z));
        door4.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(door1, door2, door3, door4));

        z.move(null);
        assertTrue(Position.isAdjacent(z.getPosition(), new Position(0, 0)));
    }

    @Test
    @DisplayName("Zombie can move past floorswitch")
    public void testZombieCanMoveThroughFloorSwitch() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        FloorSwitch floorswitch1 = new FloorSwitch(new Position(0, 1), false, null, "", new int[] {0});

        FloorSwitch floorswitch2 = new FloorSwitch(new Position(0, -1), false, null, "", new int[] {0});

        FloorSwitch floorswitch3 = new FloorSwitch(new Position(1, 0), false, null, "", new int[] {0});

        FloorSwitch floorswitch4 = new FloorSwitch(new Position(-1, 0), false, null, "", new int[] {0});

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        floorswitch1.setAdjacentEntities(Arrays.asList(z));
        floorswitch2.setAdjacentEntities(Arrays.asList(z));
        floorswitch3.setAdjacentEntities(Arrays.asList(z));
        floorswitch4.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(floorswitch1, floorswitch2, floorswitch3, floorswitch4));

        z.move(null);
        assertTrue(Position.isAdjacent(z.getPosition(), new Position(0, 0)));

    }
    
    // Zombie gets blocked by walls 
    @Test
    @DisplayName("Wall blocks movement of Zombie Toast")
    public void testWallBlocksMovementOfZombieToast() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Wall wall1 = new Wall(new Position(0, 1), null, "");
        Wall wall2  = new Wall(new Position(0, -1), null, "");
        Wall wall3 = new Wall(new Position(1, 0), null, "");
        Wall wall4 = new Wall(new Position(-1, 0), null, "");

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        wall1.setAdjacentEntities(Arrays.asList(z));
        wall2.setAdjacentEntities(Arrays.asList(z));
        wall3.setAdjacentEntities(Arrays.asList(z));
        wall4.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(wall1, wall2, wall3, wall4));

        z.move(null);
        assertEquals(z.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Zombie Toast Spawner blocks movement of Zombie Toast")
    public void testZombieToastSpawneBlocksMovementOfZombieToast() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        ZombieToastSpawner zts1 = new ZombieToastSpawner(dungeon, new Position(0, 1), null, "");
        ZombieToastSpawner zts2 = new ZombieToastSpawner(dungeon, new Position(0, -1), null, "");
        ZombieToastSpawner zts3 = new ZombieToastSpawner(dungeon, new Position(1, 0), null, "");
        ZombieToastSpawner zts4 = new ZombieToastSpawner(dungeon, new Position(-1, 0), null, "");

        ZombieToast z = new ZombieToast(new Position(0, 0), null, config, "", dungeon);
        zts1.setAdjacentEntities(Arrays.asList(z));
        zts2.setAdjacentEntities(Arrays.asList(z));
        zts3.setAdjacentEntities(Arrays.asList(z));
        zts4.setAdjacentEntities(Arrays.asList(z));
        z.setAdjacentEntities(Arrays.asList(zts1, zts2, zts3, zts4));

        z.move(null);
        assertEquals(z.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Zombie runs up away from invincible player")
    public void testZombieRunsUpAway() {
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
    
        ZombieToast zomb = new ZombieToast(new Position(0, 2), null, config, "zomb", dungeon);
        zomb.invincibilityEffects(dungeon);
        dungeon.setEntities(Arrays.asList(player, zomb));
        zomb.setAdjacentEntities(new ArrayList<>());
        
        zomb.move(null);
        assertEquals(zomb.getPosition(), new Position(0, 3));
    }

    @Test
    @DisplayName("Zombie runs down away from invincible player")
    public void testZombieRunsDownAway() {
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

        ZombieToast zomb = new ZombieToast(new Position(0, 2), null, config, "zomb", dungeon);

        dungeon.setEntities(Arrays.asList(player, zomb));
        zomb.invincibilityEffects(dungeon);
        zomb.setAdjacentEntities(new ArrayList<>());
        zomb.move(null);
        assertEquals(zomb.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Zombie runs left away from invincible player")
    public void testZombieRunsLeftAway() {
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

        ZombieToast zomb = new ZombieToast(new Position(1, 0), null, config, "zomb", dungeon);

        dungeon.setEntities(Arrays.asList(player, zomb));
        zomb.invincibilityEffects(dungeon);
        zomb.setAdjacentEntities(new ArrayList<>());
        zomb.move(null);
        assertEquals(zomb.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Mercenary runs right away from invincible player")
    public void testZombieRunsRightAway() {
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

        ZombieToast zomb = new ZombieToast(new Position(3, 0), null, config, "zomb", dungeon);

        dungeon.setEntities(Arrays.asList(player, zomb));
        zomb.invincibilityEffects(dungeon);
        zomb.setAdjacentEntities(new ArrayList<>());
        zomb.move(null);
        assertEquals(zomb.getPosition(), new Position(4, 0));
    }
}
