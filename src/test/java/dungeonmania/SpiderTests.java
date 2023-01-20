package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.InvincibilityPotion;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.*;
import dungeonmania.util.Position;


public class SpiderTests {
        /*
    ####################################################################
    #                              SETUP                               #
    ####################################################################
    */
    Config config = new Config("simple");
    
    /*
    ####################################################################
    #                              TESTS                               #
    ####################################################################
    */
    @Test
    @DisplayName("Spider Movement")
    public void testSpiderMovement() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        spider.setAdjacentEntities(new ArrayList<>());
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        // Right
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 0));
        // Down
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 1));
        // Down
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 2));
        // Left
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 2));
        // Left
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 2));
        // Up ==
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 1));
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 0));
        // Right
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        // Repeats 
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 0));

    }

    @Test
    @DisplayName("Spider shares position with Door")
    public void testSpiderSharesDoorPosition() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Door door = new Door(new Position(0, 0), 0, null, "");
        door.setAdjacentEntities(Arrays.asList(spider));
        spider.setAdjacentEntities(Arrays.asList(door));
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));

    }

    @Test
    @DisplayName("Spider shares position with Wall")
    public void testSpiderSharesWallPosition() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Wall wall = new Wall(new Position(0, 0), null, "");
        wall.setAdjacentEntities(Arrays.asList(spider));
        spider.setAdjacentEntities(Arrays.asList(wall));
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));

    }

    @Test
    @DisplayName("Spider shares position with Portal")
    public void testSpiderSharesPortalPosition() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        
        Portal portal1 = new Portal(new Position(0, 0), null, "");
        Portal portal2 = new Portal(new Position(100, 0), null, "");
        portal1.setLinkedPortal(portal2);
        portal2.setLinkedPortal(portal1);
        
        portal1.setAdjacentEntities(Arrays.asList(spider));
        spider.setAdjacentEntities(Arrays.asList(portal1));
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));

    }

    @Test
    @DisplayName("Spider shares position with Exit")
    public void testSpiderSharesExitPosition() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        
        FloorSwitch floorswitch1 = new FloorSwitch(new Position(0, 0), false, null, "", new int[] {0});
        
        floorswitch1.setAdjacentEntities(Arrays.asList(spider));
        spider.setAdjacentEntities(Arrays.asList(floorswitch1));
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));

    }

    // Exit 
    // Door 

    @Test
    @DisplayName("Spider reverses direction when it hits a boulder")
    public void testSpiderReverseBoulderHit() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        
        Boulder boulder = new Boulder(new Position(1, 0), null, "");
        
        // Up
        spider.setAdjacentEntities(new ArrayList<>());
        boulder.setAdjacentEntities(new ArrayList<>());
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));

        spider.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(spider));
        // don't move
        spider.move(null); 
        // reverse directon and move again
        assertEquals(spider.getPosition(), new Position(0, 0));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 0));


    }  
    
    @Test
    @DisplayName("Spider Follows Visual Example 2")
    public void testSpiderVisExample2() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Boulder boulder = new Boulder(new Position(0, 2), null, "");
        spider.setAdjacentEntities(new ArrayList<>());
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        // Right
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 0));
        // Down
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 1));
        // Down
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 2));

        spider.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(spider));
       
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 2));

        // reverse directon and move again
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 1));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 0));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 0));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 1));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 2));

        spider.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(spider));

        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 2));

        // reverse directon and move again
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 1));
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 0));


    }   

    @Test
    @DisplayName("Spider hits boulder initally")
    public void testSpiderhitsboulderinitially() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        
        Boulder boulder = new Boulder(new Position(0, 0), null, "");
        
        
        spider.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(spider));

        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 1));
        
        // Spider Should not move again
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 1));

    }  


    @Test
    @DisplayName("Spider Movement when Player is Invincible")
    public void testSpiderMovementInvincible() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 0);
        Player player = new Player(new Position(3, 3), null, config, "player", dungeon);

        InvincibilityPotion invince = new InvincibilityPotion(null, null, config.getInvincibilityPotionDuration(), "invince");
        
        // Player is now invincible
        player.collectEntity(invince);
        assertDoesNotThrow(()-> {
            player.useItem("invince", dungeon);
        });
        dungeon.setPlayer(player);

        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        spider.setAdjacentEntities(new ArrayList<>());

        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        // Right
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 0));
        // Down
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 1));
        // Down
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 2));
        // Left
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 2));
        // Left
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 2));
        // Up ==
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 1));
        // Up
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(-1, 0));
        // Right
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        // Repeats 
        spider.move(null);
        assertEquals(spider.getPosition(), new Position(1, 0));
    }
}
