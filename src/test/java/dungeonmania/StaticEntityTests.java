package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.Arrows;
import dungeonmania.entity.inventoryitem.collectableitem.Key;
import dungeonmania.entity.inventoryitem.collectableitem.Sword;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.staticentity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class StaticEntityTests {
    /*
    ####################################################################
    #                              SETUP                               #
    ####################################################################
    */
    Config config = new Config("simple");

    /*
    ####################################################################
    #                           OVERALL TESTS                          #
    ####################################################################
    */
    @Test
    @DisplayName("Create all static entity types and check if position correct")
    public void testCreation() {
        Boulder boulder = new Boulder(new Position(1, 2), null, "");
        assertEquals(boulder.getPosition(), new Position(1, 2));

        Door door = new Door(new Position(3, 2), 0, null, "");
        assertEquals(door.getPosition(), new Position(3, 2));

        FloorSwitch floorSwitch = new FloorSwitch(new Position(2, 5), false, null, "", new int[] {0});
        assertEquals(floorSwitch.getPosition(), new Position(2, 5));
        assertEquals(false, floorSwitch.isTriggered());

        Portal portal1 = new Portal(new Position(2, 1), null, "");
        assertEquals(portal1.getPosition(), new Position(2, 1));

        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);
        assertEquals(portal2.getPosition(), new Position(5, 5));

        Wall wall = new Wall(new Position(1, 1), null, "");
        assertEquals(wall.getPosition(), new Position(1, 1));

        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(2, 2), null, "");
        assertEquals(zts.getPosition(), new Position(2, 2));
    }
    
    /*
    ####################################################################
    #                           WALL TESTS                             #
    ####################################################################
    */
    @Test
    @DisplayName("Wall blocks movement of Player")
    public void testWallBlocksMovementOfPlayer() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Wall wall = new Wall(new Position(1, 1), new ArrayList<>(), "");
        Player player = new Player(new Position(0, 1), Arrays.asList(wall), config, "", dungeon);
        wall.setAdjacentEntities(Arrays.asList(player));

        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(0, 1));
    }
    
    @Test
    @DisplayName("Wall blocks movement of Boulder")
    public void testWallBlocksBoulder() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Wall wall = new Wall(new Position(1, 1), new ArrayList<>(), "");
        Boulder boulder = new Boulder(new Position(1, 2), new ArrayList<>(Arrays.asList(wall)), "");
        Player player = new Player(new Position(1, 3), new ArrayList<>(Arrays.asList(boulder)), config, "", dungeon);
        
        wall.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(wall, player));

        player.move(Direction.UP);
        assertEquals(boulder.getPosition(), new Position(1, 2));
        assertEquals(player.getPosition(), new Position(1, 3));
    }

    /*
    ####################################################################
    #                          BOULDER TESTS                           #
    ####################################################################
    */
    @Test
    @DisplayName("Boulder moves with player if next space is empty")
    public void testBoulderMovesOnToEmptySpace() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), new ArrayList<>(), config, "", dungeon);
        Boulder boulder = new Boulder(new Position(1, 1), new ArrayList<>(Arrays.asList(player)), "");
        player.setAdjacentEntities(new ArrayList<>(Arrays.asList(boulder)));

        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(1, 1));
        assertEquals(boulder.getPosition(), new Position(2, 1));
    }

    @Test
    @DisplayName("Boulder moves with player if next space is floor switch and floor switch is triggered")
    public void testBoulderMovesOnToFloorSwitch() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), new ArrayList<>(), config, "", dungeon);
        Boulder boulder = new Boulder(new Position(0, 2), Arrays.asList(player), "");
        FloorSwitch floorSwitch = new FloorSwitch(new Position(0, 3), false, Arrays.asList(boulder), "", new int[] {0});

        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(player, floorSwitch));

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 2));
        assertEquals(boulder.getPosition(), new Position(0, 3));
        assertEquals(floorSwitch.getPosition(), new Position(0, 3));
        assertEquals(floorSwitch.isTriggered(), true);
    }

    @Test
    @DisplayName("Boulder moves with player if next space is open door")
    public void testBoulderMovesIntoDoor() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(1, 1), null, config, "", dungeon);
        Door door = new Door(new Position(0, 1), 0, null, "");
        Key key = new Key(new Position(2, 1), null, 0, "");
        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        player.collectEntity(key);

        player.move(Direction.LEFT);
        assertEquals(player.getPosition(), new Position(0, 1));

        player.move(Direction.RIGHT);
        player.move(Direction.RIGHT);
        
        Boulder boulder = new Boulder(new Position(1, 1), Arrays.asList(player), "");
        player.setAdjacentEntities(Arrays.asList(boulder));
        player.move(Direction.LEFT);

        assertEquals(player.getPosition(), new Position(1, 1));
        assertEquals(boulder.getPosition(), new Position(0, 1));

        player.move(Direction.LEFT);
        assertEquals(player.getPosition(), new Position(0, 1));
        assertEquals(boulder.getPosition(), new Position(-1, 1));

        player.move(Direction.LEFT);
        assertEquals(player.getPosition(), new Position(-1, 1));
        assertEquals(boulder.getPosition(), new Position(-2, 1));
    }

    @Test
    @DisplayName("Boulder moves with player if next space is portal and next space of portal is empty")
    public void testBoulderMovesIntoPortal() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        Boulder boulder = new Boulder(new Position(0, 0), null, "");
        Portal portal1 = new Portal(new Position(0, -1), Arrays.asList(boulder), "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, Arrays.asList(), "");
        portal1.setLinkedPortal(portal2);

        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(player, portal1));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 0));
        assertEquals(boulder.getPosition(), new Position(5, 4));

        portal1.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(portal1));
        boulder.setAdjacentEntities(Arrays.asList(portal2));
        portal2.setAdjacentEntities(Arrays.asList(boulder));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(5, 4));
        assertEquals(boulder.getPosition(), new Position(5, 3));
    }

    @Test
    @DisplayName("Boulder blocks movement of Boulder")
    public void testBoulderBlocksMovementOfBoulder() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        Boulder boulder1 = new Boulder(new Position(1, 1), null, "");
        Boulder boulder2 = new Boulder(new Position(2, 1), Arrays.asList(boulder1), "");

        player.setAdjacentEntities(Arrays.asList(boulder1));
        boulder1.setAdjacentEntities(Arrays.asList(player, boulder2));

        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Boulder shares position with Collectable")
    public void testBoulderSharesPositionWithCollectable() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        Boulder boulder = new Boulder(new Position(1, 1), null, "");
        Arrows arrow = new Arrows(new Position(2, 1), null, "");

        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(player, arrow));

        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(1, 1));
        assertEquals(boulder.getPosition(), new Position(2, 1));
    }
    /*
    ####################################################################
    #                        FLOOR SWITCH TESTS                        #
    ####################################################################
    */
    @Test
    @DisplayName("Floor switch is traversable by moving entities and not triggered")
    public void testFloorSwitchTraversable() {
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, null, "", new int[] {0});
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
    
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        dungeon.setPlayer(player);

        player.setAdjacentEntities(Arrays.asList(fs));
        fs.setAdjacentEntities(Arrays.asList(player));
        player.move(Direction.UP);
        player.setAdjacentEntities(new ArrayList<>());
        fs.setAdjacentEntities(new ArrayList<>());
        assertEquals(player.getPosition(), new Position(0, 0));
        assertFalse(fs.isTriggered());
        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 1));
        player.setAdjacentEntities(Arrays.asList(fs));
        
        Mercenary merc = new Mercenary(new Position(1, 0), null, config, dungeon, "");
        merc.setAdjacentEntities(Arrays.asList(fs));
        fs.setAdjacentEntities(Arrays.asList(merc));
        merc.moveByDirection(Direction.LEFT);
        assertEquals(merc.getPosition(), new Position(0, 0));
        assertFalse(fs.isTriggered());
        merc.setAdjacentEntities(new ArrayList<>());
        fs.setAdjacentEntities(new ArrayList<>());
        merc.moveByDirection(Direction.RIGHT);
        merc.setAdjacentEntities(Arrays.asList(fs));
        
        ZombieToast zombie = new ZombieToast(new Position(-1, 0), null, config, "", dungeon);
        fs.setAdjacentEntities(Arrays.asList(zombie));
        zombie.setAdjacentEntities(Arrays.asList(fs));
        zombie.moveByDirection(Direction.RIGHT);
        assertEquals(zombie.getPosition(), new Position(0, 0));
        assertFalse(fs.isTriggered());
        zombie.setAdjacentEntities(new ArrayList<>());
        fs.setAdjacentEntities(new ArrayList<>());
        zombie.moveByDirection(Direction.LEFT);
        assertEquals(zombie.getPosition(), new Position(-1, 0));
        assertFalse(fs.isTriggered());
        
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        fs.setAdjacentEntities(Arrays.asList(spider));
        spider.setAdjacentEntities(Arrays.asList(fs));
        spider.moveByDirection(Direction.UP);
        assertEquals(spider.getPosition(), new Position(0, 0));
        assertFalse(fs.isTriggered());
        spider.setAdjacentEntities(new ArrayList<>());
        fs.setAdjacentEntities(new ArrayList<>());
        spider.moveByDirection(Direction.DOWN);
        assertEquals(spider.getPosition(), new Position(0, 1));
        assertFalse(fs.isTriggered());
    }

    @Test
    @DisplayName("Floor switch is triggered if boulder is moved on top")
    public void testFloorSwitchTriggeredMoveBoulder() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, null, "", new int[] {0});
        Player player = new Player(new Position(0, 2), null, config, "", dungeon);        
        Boulder boulder = new Boulder(new Position(0, 1), null, "");
        
        fs.setAdjacentEntities(Arrays.asList(boulder));
        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(fs, player));

        player.move(Direction.UP);
        assertTrue(fs.isTriggered());
        assertEquals(boulder.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Floor switch is untriggered if boulder is moved off")
    public void testFloorSwitchUntriggeredMoveBoulder() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, null, "", new int[] {0});
        Boulder boulder = new Boulder(new Position(0, 1), null, "");
        Player player = new Player(new Position(0, 2), null, config, "", dungeon);
        
        fs.setAdjacentEntities(Arrays.asList(boulder));
        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(fs, player));

        player.move(Direction.UP);
        assertTrue(fs.isTriggered());
        assertEquals(boulder.getPosition(), new Position(0, 0));
        assertEquals(player.getPosition(), new Position(0, 1));
        assertEquals(fs.getPosition(), new Position(0, 0));
        
        player.setAdjacentEntities(Arrays.asList(boulder, fs));
        fs.setAdjacentEntities(Arrays.asList(player));
        boulder.setAdjacentEntities(Arrays.asList(player));

        player.move(Direction.UP);

        assertFalse(fs.isTriggered());
        assertEquals(boulder.getPosition(), new Position(0, -1));
    }

    /*
    ####################################################################
    #                             DOOR TESTS                           #
    ####################################################################
    */
    @Test
    @DisplayName("Closed door blocks movement of Player")
    public void testClosedDoorBlocksPlayer() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door = new Door(new Position(0, 0), 0, null, "");
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        
        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));         

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Closed door blocks movement of Boulder")
    public void testClosedDoorBlocksBoulder() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door = new Door(new Position(0, 0), 0, null, "");
        Boulder boulder = new Boulder(new Position(0, 1), null, "");
        Player player = new Player(new Position(0, 2), null, config, "", dungeon);
        
        door.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(door, player));
        player.setAdjacentEntities(Arrays.asList(boulder));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 2));
        assertEquals(boulder.getPosition(), new Position(0, 1));
    }
    
    @Test
    @DisplayName("Closed door opened by corresponding key")
    public void testClosedDoorOpensWithCorrespondingKey() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door = new Door(new Position(0, 0), 0, null, "");
        Player player = new Player(new Position(0, 2), null, config, "", dungeon);
        Key key = new Key(new Position(0, 1), null, 0, "");
        
        door.setAdjacentEntities(Arrays.asList(key));
        player.setAdjacentEntities(Arrays.asList(key));
        key.setAdjacentEntities(Arrays.asList(player, door));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 1));
        assertEquals(player.getKeys(), Arrays.asList(key));

        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Closed door does not open if key does not match")
    public void testClosedDoorClosedWithWrongKey() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door = new Door(new Position(0, 0), 0, null, "");
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        Key key = new Key(new Position(0, 2), null, 1, "");
        
        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        player.collectEntity(key);
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Open door remains open")
    public void testOpenDoorRemainsOpen() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Door door = new Door(new Position(0, 0), 0, null, "");
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        Key key = new Key(new Position(2, 1), null, 0, "");
        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        player.collectEntity(key);
        
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 0));
        door.setAdjacentEntities(new ArrayList<>());
        player.setAdjacentEntities(new ArrayList<>());
        
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, -1));
        door.setAdjacentEntities(Arrays.asList(player));
        player.setAdjacentEntities(Arrays.asList(door));

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 0));
        door.setAdjacentEntities(new ArrayList<>());
        player.setAdjacentEntities(new ArrayList<>());
        
        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(0, 1));
    }
    
    /*
    ####################################################################
    #                           PORTAL TESTS                           #
    ####################################################################
    */
    @Test
    @DisplayName("Player can walk into portal if next space to portal is empty (all directions)")
    public void testPlayerWalkIntoPortal() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(new ArrayList<>());

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(5, 4));

        player.setPosition(new Position(0, -2));

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(new ArrayList<>());

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(5, 6));

        player.setPosition(new Position(1, -1));

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(new ArrayList<>());
        
        player.move(Direction.LEFT);
        assertEquals(player.getPosition(), new Position(4, 5));

        player.setPosition(new Position(-1, -1));

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(new ArrayList<>());
        
        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(6, 5));
    }

    @Test
    @DisplayName("Player can walk into portal if next space to portal is empty (all directions)")
    public void testPlayerWalkIntoOtherPortal() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(5, 5), null, "");
        Portal portal2 = new Portal(new Position(0, -1), portal1, null, "");
        portal1.setLinkedPortal(portal2);

        player.setAdjacentEntities(Arrays.asList(portal2));
        portal2.setAdjacentEntities(Arrays.asList(player));
        portal1.setAdjacentEntities(new ArrayList<>());

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(5, 4));

        player.setPosition(new Position(0, -2));

        player.setAdjacentEntities(Arrays.asList(portal2));
        portal2.setAdjacentEntities(Arrays.asList(player));
        portal1.setAdjacentEntities(new ArrayList<>());

        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(5, 6));

        player.setPosition(new Position(1, -1));

        player.setAdjacentEntities(Arrays.asList(portal2));
        portal2.setAdjacentEntities(Arrays.asList(player));
        portal1.setAdjacentEntities(new ArrayList<>());

        player.move(Direction.LEFT);
        assertEquals(player.getPosition(), new Position(4, 5));

        player.setPosition(new Position(-1, -1));

        player.setAdjacentEntities(Arrays.asList(portal2));
        portal2.setAdjacentEntities(Arrays.asList(player));
        portal1.setAdjacentEntities(new ArrayList<>());

        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(6, 5));
    }

    @Test
    @DisplayName("Player can walk into portal if next space to portal is an open door")
    public void testPortalAllowsMovementIfOpenDoor() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);
        Door door = new Door(new Position(5, 4), 0, null, "");
        Key key = new Key(new Position(0, 2), null, 0, "");

        player.collectEntity(key);
        player.setPosition(new Position(5, 3));
        player.setAdjacentEntities(Arrays.asList(door));
        door.setAdjacentEntities(Arrays.asList(player));
        player.move(Direction.RIGHT);

        player.setPosition(new Position(0, 0));
        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(Arrays.asList(door));
        door.setAdjacentEntities(Arrays.asList(portal2));
        
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(5, 4));
    }
    
    @Test
    @DisplayName("Player can walk into portal if next space to portal is a floor switch")
    public void testPlayerWalkIntoPortalWithFloorSwitch() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);
        FloorSwitch floorSwitch = new FloorSwitch(new Position(5, 4), false, null, "", new int[] {0});

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(Arrays.asList(floorSwitch));
        floorSwitch.setAdjacentEntities(Arrays.asList(portal2));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(5, 4));
    }

    @Test
    @DisplayName("default portal creation")
    public void testDefaultPortal() {
        Map<String, Portal> portalColourMap = new HashMap<>();
        Portal p1 = new Portal(new Position(0, 0), new ArrayList<>(), "id");
        Portal p2 = new Portal(new Position(0, 1), new ArrayList<>(), "id2");
        p1.setLinkedPortal(p2);
        p2.setLinkedPortal(p1);
        portalColourMap.put("portal", p1);
        Portal.setColours(portalColourMap);

        assertEquals("portal", p1.getType());
    }

    @Test
    @DisplayName("Player can walk into portal if next space to portal is an exit")
    public void testPlayerWalkIntoPortalWithExit() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);
        Exit exit = new Exit(new Position(5, 4), null, "");

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(Arrays.asList(exit));
        exit.setAdjacentEntities(Arrays.asList(portal2));

        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(5, 4));
    }

    @Test
    @DisplayName("Portal blocks movement of player if next space to portal is a wall")
    public void testPortalBlocksMovementIfWall() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        Wall wall = new Wall(new Position(5, 4), null, "");
        portal1.setLinkedPortal(portal2);

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(Arrays.asList(wall));
        wall.setAdjacentEntities(Arrays.asList(portal2));
        
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Portal blocks movement of player if next space to portal is a closed door")
    public void testPortalBlocksMovementIfDoor() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);
        Door door = new Door(new Position(5, 4), 0, null, "");

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(Arrays.asList(door));
        door.setAdjacentEntities(Arrays.asList(portal2));
        
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Portal blocks movement of player if next space to portal is a zombie toast spawner")
    public void testPortalBlocksMovementIfZombieToastSpawner() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 0), null, config, "", dungeon);
        Portal portal1 = new Portal(new Position(0, -1), null, "");
        Portal portal2 = new Portal(new Position(5, 5), portal1, null, "");
        portal1.setLinkedPortal(portal2);

        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(5, 4), null, "");

        player.setAdjacentEntities(Arrays.asList(portal1));
        portal1.setAdjacentEntities(Arrays.asList(player));
        portal2.setAdjacentEntities(Arrays.asList(zts));
        zts.setAdjacentEntities(Arrays.asList(portal2));
        
        player.move(Direction.UP);
        assertEquals(player.getPosition(), new Position(0, 0));
    }
    
    /*
    ####################################################################
    #                    ZOMBIE TOAST SPAWNER TESTS                    #
    ####################################################################
    */
    @Test
    @DisplayName("Zombie toast spawner doesn't spawn zombie toast if walls cover spawner")
    public void testSpawnerNoSpawnWalls() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(10, 1), null, config, "", dungeon);
        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(1, 1), null, "");
        Wall w1 = new Wall(new Position(2, 1), null, "");
        Wall w2 = new Wall(new Position(0, 1), null, "");
        Wall w3 = new Wall(new Position(1, 0), null, "");
        Wall w4 = new Wall(new Position(1, 2), null, "");

        dungeon.setEntities(Arrays.asList(zts, w1, w2, w3, w4));
        dungeon.setPlayer(player);

        dungeon.updateAdjacentEntities();
        dungeon.movementTick(Direction.LEFT, null);
        dungeon.movementTick(Direction.RIGHT, null);
        dungeon.movementTick(Direction.DOWN, null);
        dungeon.movementTick(Direction.UP, null);

        assertFalse(dungeon.getEntities().stream().anyMatch(e -> e instanceof ZombieToast));
    }

    @Test
    @DisplayName("Zombie toast spawner can be destroyed only if player next to spawner")
    public void testZombieToastSpawnerDestroy() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        assertFalse(player.hasWeapon());

        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(1, 1), null, "");
        Sword sword = new Sword(new Position(0, 2), null, config.getSwordDurability(),  config.getSwordAttack(), "");
        player.collectEntity(sword);
        assertTrue(player.hasWeapon());
        
        player.setAdjacentEntities(Arrays.asList(zts));
        zts.setAdjacentEntities(Arrays.asList(player));
        
        zts.interact(player);
        assertFalse(dungeon.getEntities().stream().anyMatch(e -> e instanceof ZombieToastSpawner));
    }

    @Test
    @DisplayName("Zombie toast spawner blocks movement of Boulder")
    public void testZombieToastSpawnerBlocksMovementOfBoulder() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Player player = new Player(new Position(0, 1), null, config, "", dungeon);
        Boulder boulder = new Boulder(new Position(1, 1), null, "");
        ZombieToastSpawner zts = new ZombieToastSpawner(dungeon, new Position(2, 1), null, "");
        
        player.setAdjacentEntities(Arrays.asList(boulder, zts));
        boulder.setAdjacentEntities(Arrays.asList(player, zts));
        zts.setAdjacentEntities(Arrays.asList(player, boulder));

        player.move(Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(0, 1));
    }
}