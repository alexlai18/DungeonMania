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
import dungeonmania.interfaces.movement.MercenaryMovement;
import dungeonmania.interfaces.movement.ZombieToastMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AssassinTests {

    Config config = new Config("simple");

    @Test
    @DisplayName("Wall blocks movement of Assassin")
    public void testWallBlocksMovementOfAssassin() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Player player = new Player(new Position(100, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        Wall wall1 = new Wall(new Position(0, 1), null, "");
        Wall wall2  = new Wall(new Position(0, -1), null, "");
        Wall wall3 = new Wall(new Position(1, 0), null, "");
        Wall wall4 = new Wall(new Position(-1, 0), null, "");
        
        Assassin a = new Assassin(new Position(0, 0), null, dungeon, "");
        dungeon.setEntities(Arrays.asList(player, wall1, wall2, wall3, wall4, a));
        
        wall1.setAdjacentEntities(Arrays.asList(a));
        wall2.setAdjacentEntities(Arrays.asList(a));
        wall3.setAdjacentEntities(Arrays.asList(a));
        wall4.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(wall1, wall2, wall3, wall4));

        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Boulder blocks movement of Assassin")
    public void testBoulderBlocksMovementOfAssassin() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Player player = new Player(new Position(100, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        Boulder boulder1 = new Boulder(new Position(0, 1), null, "");
        Boulder boulder2  = new Boulder(new Position(0, -1), null, "");
        Boulder boulder3 = new Boulder(new Position(1, 0), null, "");
        Boulder boulder4 = new Boulder(new Position(-1, 0), null, "");
        
        Assassin a = new Assassin(new Position(0, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, boulder1, boulder2, boulder3, boulder4, a));

        boulder1.setAdjacentEntities(Arrays.asList(a));
        boulder2.setAdjacentEntities(Arrays.asList(a));
        boulder3.setAdjacentEntities(Arrays.asList(a));
        boulder4.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(boulder1, boulder2, boulder3, boulder4));
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Assassin Moves right towards player")
    public void testAssassinMoveRight() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        
        Assassin a = new Assassin(new Position(-1, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));
        
        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Assassin Moves left towards player")
    public void testAssassinMoveLeft() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);

        Player player = new Player(new Position(1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
    
        Assassin a = new Assassin(new Position(3, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(2, 0));
    }

    @Test
    @DisplayName("Assassin Moves down towards player")
    public void testAssassinMoveUp() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        
        Assassin a = new Assassin(new Position(0, -2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));
        
        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, -1));
    }

    @Test
    @DisplayName("Assassin Moves Up towards player")
    public void testAssassinMoveDown() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
    
        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 1));
    }

    // door 
    // exit 


    @Test
    @DisplayName("Assassin Moves around wall to Player")
    public void testAssassinMoveAroundWall() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Wall wall = new Wall(new Position(0, 1), null, "");
        

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(Arrays.asList(wall));
        wall.setAdjacentEntities(Arrays.asList(player));
    
        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a, wall));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(-1, 2));
        a.move(null);
        assertEquals(a.getPosition(), new Position(-1, 1));
    }


    @Test
    @DisplayName("Assassin Moves around Boulder to Player")
    public void testAssassinMoveAroundBoulder() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Boulder boulder = new Boulder(new Position(0, 1), null, "");
        

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(Arrays.asList(boulder));
        boulder.setAdjacentEntities(Arrays.asList(player));
    
        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a, boulder));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(-1, 2));
        a.move(null);
        assertEquals(a.getPosition(), new Position(-1, 1));
    }

    @Test
    @DisplayName("Assassin Moves through Spider to Player")
    public void testAssassinMoveAroundSpider() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        Spider spider = new Spider(new Position(0, 1), null, config, "", dungeon);
        Player player = new Player(new Position(0, -1), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(new ArrayList<>());
        spider.setAdjacentEntities(new ArrayList<>());

        spider.move(null);
        assertEquals(spider.getPosition(), new Position(0, 0));
        
        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");

        spider.setAdjacentEntities(Arrays.asList(player, a));

        dungeon.setEntities(Arrays.asList(player, a, spider));

        a.setAdjacentEntities(Arrays.asList(spider));
        a.move(null);
        assertEquals(new Position(0, 0), a.getPosition());
    }

    @Test
    @DisplayName("Assassin Moves through Zombie to Player")
    public void testAssassinMoveAroundZombie() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        ZombieToast zombie = new ZombieToast(new Position(0, 1), null, config, "", dungeon);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        player.setAdjacentEntities(Arrays.asList(zombie));
        zombie.setAdjacentEntities(Arrays.asList(player));        

        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a, zombie));

        a.setAdjacentEntities(Arrays.asList(zombie));
        a.move(null);
        assertEquals(new Position(0, 1), a.getPosition());
    }

    @Test
    @DisplayName("Assassin failed to be bribed by Player")
    public void testAssassinFailedBribed() {
        Dungeon dungeon = new Dungeon("zombies", "assassin_bribe_fail", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertDoesNotThrow(()->player.bribe(a));

        assertEquals(a.getBribed(), false);

        assertFalse(player.getInventoryList().contains(coin));
    }


    @Test
    @DisplayName("Assassin failed to be bribed by Player, Player can't try to bribe again")
    public void testAssassinBribeCantbeAttempted() {
        Dungeon dungeon = new Dungeon("assassin", "assassin_bribe_fail", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        Treasure coin1 = new Treasure(new Position(0, 6), null, "coin");
        player.collectEntity(coin);
        player.collectEntity(coin1);


        assertTrue(player.getInventoryList().contains(coin));
        assertTrue(player.getInventoryList().contains(coin1));
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertDoesNotThrow(()->player.bribe(a));

        assertEquals(a.getBribed(), false);

        assertEquals(1, player.getInventoryList().size());

        assertThrows(InvalidActionException.class, ()->player.bribe(a));
    }


    @Test
    @DisplayName("Assassin failed to be bribed by Player, takes 1 coin")
    public void testAssassinFailedBribed2Coins() {
        Dungeon dungeon = new Dungeon("assassin", "assassin_bribe_fail", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        Treasure coin1 = new Treasure(new Position(0, 6), null, "coin1");
        player.collectEntity(coin);
        player.collectEntity(coin1);

        assertTrue(player.getInventoryList().contains(coin));
        assertTrue(player.getInventoryList().contains(coin1));

        assertEquals(2, player.getInventoryList().size());
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertDoesNotThrow(()->player.bribe(a));

        assertEquals(a.getBribed(), false);

        assertEquals(1, player.getInventoryList().size());
    }


    @Test
    @DisplayName("Assassin gets bribed by Player")
    public void testAssassingetsBribed() {
        Dungeon dungeon = new Dungeon("zombies", "assassin_bribe", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertDoesNotThrow(()->player.bribe(a));

        assertEquals(a.getBribed(), true);

        assertFalse(player.getInventoryList().contains(coin));
    }

    @Test
    @DisplayName("Assassin is already Bribed")
    public void testAssassinAlreadyBribed() {
        Dungeon dungeon = new Dungeon("zombies", "assassin_bribe", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertDoesNotThrow(()->player.bribe(a));

        assertEquals(a.getBribed(), true);

        assertFalse(player.getInventoryList().contains(coin));

        assertThrows(InvalidActionException.class, ()->player.bribe(a));
    }

    @Test
    @DisplayName("Player Doesn't have enough treasure to bribe Assassin")
    public void testPlayerNotEnoughTreasureforAssassin() {
        Dungeon dungeon = new Dungeon("zombies", "assassin_bribe", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 1), null, dungeon, "");
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertThrows(InvalidActionException.class, ()->player.bribe(a));

        assertEquals(a.getBribed(), false);

    }


    @Test
    @DisplayName("Assassin Too Far Away")
    public void testAssassinTooFarAway() {
        Dungeon dungeon = new Dungeon("zombies", "simple", 1);
        
        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);

        Assassin a = new Assassin(new Position(0, 100), null, dungeon, "");
        Treasure coin = new Treasure(new Position(0, 5), null, "coin");
        player.collectEntity(coin);

        assertTrue(player.getInventoryList().contains(coin));
        
        player.setAdjacentEntities(Arrays.asList(a));
        a.setAdjacentEntities(Arrays.asList(player)); 

        dungeon.setEntities(Arrays.asList(player, a));
        
        assertThrows(InvalidActionException.class, ()->player.bribe(a));

        assertEquals(a.getBribed(), false);
                
    }

    @Test
    @DisplayName("Assassin runs up away from invincible player")
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
    
        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 3));
    }

    @Test
    @DisplayName("Assassin runs down away from invincible player")
    public void testAssassinRunsDownAway() {
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
    
        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 1));
    }

    @Test
    @DisplayName("Assassin runs left away from invincible player")
    public void testAssassinRunsLeftAway() {
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
    
        Assassin a = new Assassin(new Position(1, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 0));
    }
    
    @Test
    @DisplayName("Assassin runs right away from invincible player")
    public void testAssassinRunsRightAway() {
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
    
        Assassin a = new Assassin(new Position(3, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(4, 0));
    }


    @Test
    @DisplayName("Assassin shortest path")
    public void testShortestPathAssassin() {
        // _  _  _ 
        // M  W  _
        // _  _  _
        // _  W  X 
        // _  _  _
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 3), new ArrayList<>(), config, "player", dungeon);
        Wall wall1 = new Wall(new Position(1, 1), new ArrayList<>(), "w1");
        Wall wall2 = new Wall(new Position(1, 3), new ArrayList<>(), "w2");
        Assassin a = new Assassin(new Position(0,1), new ArrayList<>(), dungeon, "merc");

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(wall1, wall2, a));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.LEFT, null);
        assertEquals(a.getPosition(), new Position(0, 2));
        dungeon.movementTick(Direction.LEFT, null);
        assertEquals(a.getPosition(), new Position(1, 2));
        dungeon.movementTick(Direction.LEFT, null);
        assertEquals(a.getPosition(), new Position(2, 2));
    }

    @Test
    @DisplayName("Assassin accounts portals for shortest path")
    public void testAssassinPortalPath() {
        // _ _ _ _ _ _
        // P _ X W M P
        // _ _ _ _ _ _
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        Portal portal1 = new Portal(new Position(0, 0), new ArrayList<>(), "p1");
        Portal portal2 = new Portal(new Position(5, 0), portal1, new ArrayList<>(), "p2");
        portal1.setLinkedPortal(portal2);
        Wall wall1 = new Wall(new Position(3, 0), new ArrayList<>(), "w1");
        Assassin a = new Assassin(new Position(4, 0), new ArrayList<>(), dungeon, "merc");

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(portal1, portal2, wall1, a));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.RIGHT, null);

        assertEquals(a.getPosition(), new Position(1, 0));
    }

    @Test
    @DisplayName("Assassin accounts swamptile for shortest path")
    public void testAssassinSwampPath() {
        // _  _  _ 
        // M  S5 X
        // W  W  W 
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        Player player = new Player(new Position(2, 0), new ArrayList<>(), config, "player", dungeon);
        Wall wall1 = new Wall(new Position(0, 1), new ArrayList<>(), "w1");
        Wall wall2 = new Wall(new Position(1, 1), new ArrayList<>(), "w2");
        Wall wall3 = new Wall(new Position(2, 1), new ArrayList<>(), "w3");
        Assassin a = new Assassin(new Position(0, 0), new ArrayList<>(), dungeon, "merc");
        // give swamp tile 5 tick slow
        SwampTile swamptile = new SwampTile(new Position(1, 0), new ArrayList<>(), "id", 5);

        dungeon.setPlayer(player);
        dungeon.setEntities(Arrays.asList(wall1, wall2, wall3, a, swamptile));

        dungeon.updateAdjacentEntities();

        dungeon.movementTick(Direction.DOWN, null);

        assertEquals(new Position(0, -1), a.getPosition());
    }

    @Test
    @DisplayName("Assassin moves like a zombie if a player is invisible and out of range")
    public void testAssassinMovesLikeZombie() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);

        Player player = new Player(new Position(1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        // Player is now invisible
        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });
        player.setAdjacentEntities(new ArrayList<>());
    
        Assassin a = new Assassin(new Position(500, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.invisibilityEffects(dungeon);
        assertTrue(a.getMovement().getClass() == ZombieToastMovement.class);
    }


    @Test
    @DisplayName("Assassin Moves right towards invisible player")
    public void testAssassinMoveRightWithInvisibility() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);

        Player player = new Player(new Position(1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        // Player is now invisible
        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });

        player.setAdjacentEntities(new ArrayList<>());
        Assassin a = new Assassin(new Position(-1, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.invisibilityEffects(dungeon);
        assertTrue(a.getMovement().getClass() == MercenaryMovement.class);
        
        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Assassin Moves left towards invisible player")
    public void testAssassinMoveLeftWithInvisibility() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);

        Player player = new Player(new Position(-1, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        // Player is now invisible
        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });

        player.setAdjacentEntities(new ArrayList<>());
        Assassin a = new Assassin(new Position(1, 0), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.invisibilityEffects(dungeon);
        assertTrue(a.getMovement().getClass() == MercenaryMovement.class);
        
        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 0));
    }

    @Test
    @DisplayName("Assassin Moves down towards invisible player")
    public void testAssassinMoveDownWithInvisibility() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        // Player is now invisible
        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });

        player.setAdjacentEntities(new ArrayList<>());
        Assassin a = new Assassin(new Position(0, -2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.invisibilityEffects(dungeon);
        assertTrue(a.getMovement().getClass() == MercenaryMovement.class);
        
        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, -1));
    }

    @Test
    @DisplayName("Assassin Moves Up towards invisible player")
    public void testAssassinMoveUpWithInvisibility() {
        Dungeon dungeon = new Dungeon("empty", "M3_config", 1);

        Player player = new Player(new Position(0, 0), new ArrayList<>(), config, "", dungeon);
        dungeon.setPlayer(player);
        InvisibilityPotion invis = new InvisibilityPotion(null, null, config.getInvisibilityPotionDuration(), "invis");
        // Player is now invisible
        player.collectEntity(invis);
        assertDoesNotThrow(()-> {
            player.useItem("invis", dungeon);
        });

        player.setAdjacentEntities(new ArrayList<>());
        Assassin a = new Assassin(new Position(0, 2), null, dungeon, "");

        dungeon.setEntities(Arrays.asList(player, a));

        a.invisibilityEffects(dungeon);
        assertTrue(a.getMovement().getClass() == MercenaryMovement.class);
        
        a.setAdjacentEntities(new ArrayList<>());
        a.move(null);
        assertEquals(a.getPosition(), new Position(0, 1));
    }
}