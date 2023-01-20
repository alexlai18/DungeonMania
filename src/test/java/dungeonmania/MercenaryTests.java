package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.entity.staticentity.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.interfaces.movement.ZombieToastMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryTests {

    Config config = new Config("simple");

    @Test
    @DisplayName("Wall blocks movement of Mercenary")
    public void testWallBlocksMovementOfMercenary() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Player player = new Player(new Position(100, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        Wall wall1 = new Wall(new Position(0, 1), null, "");
        Wall wall2  = new Wall(new Position(0, -1), null, "");
        Wall wall3 = new Wall(new Position(1, 0), null, "");
        Wall wall4 = new Wall(new Position(-1, 0), null, "");
        
        Mercenary merc = new Mercenary(new Position(0, 0), null, config, dungeon, "");
        dungeon.setEntities(Arrays.asList(player, wall1, wall2, wall3, wall4, merc));
        
        wall1.setAdjacentEntities(Arrays.asList(merc));
        wall2.setAdjacentEntities(Arrays.asList(merc));
        wall3.setAdjacentEntities(Arrays.asList(merc));
        wall4.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(wall1, wall2, wall3, wall4));

        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Boulder blocks movement of Mercenary")
    public void testBoulderBlocksMovementOfMercenary() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Player player = new Player(new Position(100, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        Boulder boulder1 = new Boulder(new Position(0, 1), null, "");
        Boulder boulder2  = new Boulder(new Position(0, -1), null, "");
        Boulder boulder3 = new Boulder(new Position(1, 0), null, "");
        Boulder boulder4 = new Boulder(new Position(-1, 0), null, "");
        
        Mercenary merc = new Mercenary(new Position(0, 0), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, boulder1, boulder2, boulder3, boulder4, merc));

        boulder1.setAdjacentEntities(Arrays.asList(merc));
        boulder2.setAdjacentEntities(Arrays.asList(merc));
        boulder3.setAdjacentEntities(Arrays.asList(merc));
        boulder4.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(boulder1, boulder2, boulder3, boulder4));
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Mercenary Moves right towards player")
    public void testMercenaryMoveRight() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        
        Mercenary merc = new Mercenary(new Position(-1, 0), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));
        
        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Mercenary Moves left towards player")
    public void testMercenaryMoveLeft() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);

        Player player = new Player(new Position(1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
    
        Mercenary merc = new Mercenary(new Position(3, 0), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(2, 0));
    }

    @Test
    @DisplayName("Mercenary Moves down towards player")
    public void testMercenaryMoveUp() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        
        Mercenary merc = new Mercenary(new Position(0, -2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));
        
        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, -1));
    }

    @Test
    @DisplayName("Mercenary Moves Up towards player")
    public void testMercenaryMoveDown() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
    
        Mercenary merc = new Mercenary(new Position(0, 2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 1));
    }

    // door 
    // exit 


    @Test
    @DisplayName("Mercenary Moves around wall to Player")
    public void testMercenaryMoveAroundWall() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Wall wall = new Wall(new Position(0, 1), null, "");
        

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(Arrays.asList(wall));
        wall.setAdjacentEntities(Arrays.asList(player));
    
        Mercenary merc = new Mercenary(new Position(0, 2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc, wall));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(-1, 2));
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(-1, 1));
    }


    @Test
    @DisplayName("Mercenary Moves around Boulder to Player")
    public void testMercenaryMoveAroundBoulder() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Boulder boulder = new Boulder(new Position(0, 1), null, "");
        

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(player));
    
        Mercenary merc = new Mercenary(new Position(0, 2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc, boulder));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(-1, 2));
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(-1, 1));
    }

    @Test
    @DisplayName("Mercenary Moves through Spider to Player")
    public void testMercenaryMoveAroundSpider() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, -1), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        spider.setAdjacentEntities(new ArrayList<>());

        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        
        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");

        spider.setAdjacentEntities(Arrays.asList(player, merc));

        dungeon.setEntities(Arrays.asList(player, merc, spider));

        merc.setAdjacentEntities(Arrays.asList(spider));
        merc.move(null);
        assertEquals(new Position(0, 0), merc.getPosition());
    }

    @Test
    @DisplayName("Mercenary Moves through Zombie to Player")
    public void testMercenaryMoveAroundZombie() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        ZombieToast zombie = new ZombieToast(new Position(0, 1), null, config, "", dungeon);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(Arrays.asList(zombie));
        zombie.setAdjacentEntities(Arrays.asList(player));        

        Mercenary merc = new Mercenary(new Position(0, 2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc, zombie));

        merc.setAdjacentEntities(Arrays.asList(zombie));
        merc.move(null);
        assertEquals(new Position(0, 1), merc.getPosition());
    }

    @Test
    @DisplayName("Mercenary gets bribed by Player")
    public void testMercenarygetsBribed() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, merc));
        
        assertDoesNotThrow(()->player.bribe(merc));

        assertEquals(merc.getBribed(), true);

        assertFalse(player.getInventoryList().contains(coin));
    }

    @Test
    @DisplayName("Mercenary is already Bribed")
    public void testMercenaryAlreadyBribed() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, merc));
        
        assertDoesNotThrow(()->player.bribe(merc));

        assertEquals(merc.getBribed(), true);

        assertFalse(player.getInventoryList().contains(coin));

        assertThrows(InvalidActionException.class, ()->player.bribe(merc));
    }

    @Test
    @DisplayName("Player Doesn't have enough treasure to bribe Mercenary")
    public void testPlayerNotEnoughTreasure() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Mercenary merc = new Mercenary(new Position(0, 1), null, config, dungeon, "");
        
        player.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, merc));
        
        assertThrows(InvalidActionException.class, ()->player.bribe(merc));

        assertEquals(merc.getBribed(), false);

    }


    @Test
    @DisplayName("Mercenary Too Far Away")
    public void testMercenaryTooFarAway() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Mercenary merc = new Mercenary(new Position(0, 100), null, config, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(merc));
        merc.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, merc));
        
        assertThrows(InvalidActionException.class, ()->player.bribe(merc));

        assertEquals(merc.getBribed(), false);
                
    }

    @Test
    @DisplayName("Mercenary runs up away from invincible player")
    public void testMercenaryRunsUpAway() {
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
    
        Mercenary merc = new Mercenary(new Position(0, 2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 3));
    }

    @Test
    @DisplayName("Mercenary runs down away from invincible player")
    public void testMercenaryRunsDownAway() {
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
    
        Mercenary merc = new Mercenary(new Position(0, 2), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Mercenary runs left away from invincible player")
    public void testMercenaryRunsLeftAway() {
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
    
        Mercenary merc = new Mercenary(new Position(1, 0), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Mercenary runs right away from invincible player")
    public void testMercenaryRunsRightAway() {
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
    
        Mercenary merc = new Mercenary(new Position(3, 0), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.setAdjacentEntities(new ArrayList<>());
        merc.move(null);
        assertEquals(merc.getPosition(), new Position(4, 0));
    }

    @Test
    @DisplayName("Mercenary moves like a zombie if a player is invisible")
    public void testMercenaryMovesLikeZombie() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);

        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        // Player is now invincible
        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });
        player.setAdjacentEntities(new ArrayList<>());
    
        Mercenary merc = new Mercenary(new Position(3, 0), null, config, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, merc));

        merc.invisibilityEffects(dungeon);
        assertTrue(merc.getMovement().getClass() == ZombieToastMovement.class);
    }

    @Test
    @DisplayName("Merc shortest path")
    public void testShortestPath() {
        // _  _  _ 
        // M  W  _
        // _  _  _
        // _  W  X 
        // _  _  _
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 3), new ArrayList<>(), config, "player", dungeon);
        Wall wall1 = new Wall(new Position(1, 1), new ArrayList<>(), "w1");
        Wall wall2 = new Wall(new Position(1, 3), new ArrayList<>(), "w2");
        Mercenary merc = new Mercenary(new Position(0,1), new ArrayList<>(), config, dungeon, "merc");

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(wall1, wall2, merc));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.LEFT, null);
        assertEquals(merc.getPosition(), new Position(0, 2));
        dungeon.movementTick(Direction.LEFT, null);
        assertEquals(merc.getPosition(), new Position(1, 2));
        dungeon.movementTick(Direction.LEFT, null);
        assertEquals(merc.getPosition(), new Position(2, 2));
    }

    @Test
    @DisplayName("Mercenary accounts portals for shortest path")
    public void testMercenaryPortalPath() {
        // _ _ _ _ _ _
        // P _ X W M P
        // _ _ _ _ _ _
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        Portal portal1 = new Portal(new Position(0, 0), new ArrayList<>(), "p1");
        Portal portal2 = new Portal(new Position(5, 0), portal1, new ArrayList<>(), "p2");
        portal1.setLinkedPortal(portal2);
        Wall wall1 = new Wall(new Position(3, 0), new ArrayList<>(), "w1");
        Mercenary merc = new Mercenary(new Position(4, 0), new ArrayList<>(), config, dungeon, "merc");

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(portal1, portal2, wall1, merc));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.RIGHT, null);

        assertEquals(merc.getPosition(), new Position(1, 0));
    }

    @Test
    @DisplayName("Mercenary accounts swamptile for shortest path")
    public void testMercenarySwampPath() {
        // _  _  _ 
        // M  S5 X
        // W  W  W 
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        Wall wall1 = new Wall(new Position(0, 1), new ArrayList<>(), "w1");
        Wall wall2 = new Wall(new Position(1, 1), new ArrayList<>(), "w2");
        Wall wall3 = new Wall(new Position(2, 1), new ArrayList<>(), "w3");
        Mercenary merc = new Mercenary(new Position(0, 0), new ArrayList<>(), config, dungeon, "merc");
        // give swamp tile 5 tick slow
        SwampTile swamptile = new SwampTile(new Position(1, 0), new ArrayList<>(), "id", 5);

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(wall1, wall2, wall3, merc, swamptile));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.DOWN, null);

        assertEquals(new Position(0, -1), merc.getPosition());
    }
}
