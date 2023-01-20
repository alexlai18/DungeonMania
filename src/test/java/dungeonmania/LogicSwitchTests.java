package dungeonmania;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.Key;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.Boulder;
import dungeonmania.entity.staticentity.FloorSwitch;
import dungeonmania.entity.staticentity.LightBulb;
import dungeonmania.entity.staticentity.LogicFloorSwitch;
import dungeonmania.entity.staticentity.SwitchDoor;
import dungeonmania.entity.staticentity.Wire;
import dungeonmania.entity.staticentity.logic.AndLogic;
import dungeonmania.entity.staticentity.logic.Circuit;
import dungeonmania.entity.staticentity.logic.CoAndLogic;
import dungeonmania.entity.staticentity.logic.LogicHandler;
import dungeonmania.entity.staticentity.logic.OrLogic;
import dungeonmania.entity.staticentity.logic.XorLogic;
import dungeonmania.response.models.DungeonResponse;

import static dungeonmania.TestUtils.*;

public class LogicSwitchTests {
    /**
     * P E
     * B F SD K (or SD) - testSwitchDoorOpens
     * 
     *       B
     *       F
     * B F W L (and L ) - testAnd
     * 
     *       F 
     * B F W LF L (or LF) / (xor L) - testOr
     * 
     * B F W LB (xor) - testXor
     * 
     *     W W
     * B F W L (co and) - testCoAnd
     * 
     */
    
    @Test
    @DisplayName("switch door opens through floor switch trigger")
    public void testSwitchDoorOpens() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("logic", "simple");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse actRes = dmc.tick(Direction.UP);

        assertEquals(new Position(2, 1), getPlayer(actRes).get().getPosition());
        
        dmc.tick(Direction.LEFT);
        actRes = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(1, 1), getPlayer(actRes).get().getPosition());
    }

    @Test
    @DisplayName("light bulb logical and triggers with 2 active circuits")
    public void testAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("logic", "simple");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse actRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, getEntitiesStream(actRes, "light_bulb_on").count());
    }

    @Test
    @DisplayName("light bulb logical xor triggers with activated switch logical or")
    public void testOr() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("logic", "simple");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse actRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, getEntitiesStream(actRes, "light_bulb_on").count());
        
        DungeonResponse nextRes = dmc.tick(Direction.RIGHT);
        assertEquals(3, getEntitiesStream(nextRes, "light_bulb_off").count());
    }

    @Test
    @DisplayName("bomb logical xor triggers with 1 active circuit")
    public void testXor() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("logic", "simple");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse actRes = dmc.tick(Direction.RIGHT);

        // destroy itself and wire next to it
        assertEquals(0, getEntitiesStream(actRes, "bomb").count());
        assertEquals(5, getEntitiesStream(actRes, "wire").count());
    }

    @Test
    @DisplayName("light bulb logical co_and triggers with 2 active circuits on same tick")
    public void testCoAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("logic", "simple");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse actRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, getEntitiesStream(actRes, "light_bulb_on").count());
    }

    @Test
    @DisplayName("co and floor switch")
    public void testCoAndFloorSwitch() {
        Config c = new Config("simple");
        Dungeon d = new Dungeon("empty", "simple", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, new ArrayList<>(),"id", new int[] {0});
        Wire w1 = new Wire(new Position(1, 1), new ArrayList<>(), "id", new int[] {0});
        Wire w3 = new Wire(new Position(0, 1), new ArrayList<>(), "id", new int[] {0});
        LightBulb l = new LightBulb(new Position(1, 0), new ArrayList<>(), "id", "co_and");
        Boulder b1 = new Boulder(new Position(-1, 0), new ArrayList<>(), "id");

        Player p = new Player(new Position(-2, 0), new ArrayList<>(), c, "id", d);
        d.setPlayer(p);
        d.setEntities(Arrays.asList(fs, w1, w3, l, b1));
        d.updateAdjacentEntities();

        d.movementTick(Direction.RIGHT, null);
        assertEquals(b1.getPosition(), fs.getPosition());
        assertEquals("light_bulb_on", l.getType());
    }

    @Test
    @DisplayName("Logic triggered by boulder")
    public void testLogicSwitchBoulder() {
        Config c = new Config("simple");
        Dungeon d = new Dungeon("empty", "simple", 1);
        LogicFloorSwitch fs = new LogicFloorSwitch(new Position(0, 0), false, new ArrayList<>(),"id", "and", new int[] {0});
        LightBulb l = new LightBulb(new Position(1, 0), new ArrayList<>(), "id", "or");
        Boulder b1 = new Boulder(new Position(-1, 0), new ArrayList<>(), "id");

        Player p = new Player(new Position(-2, 0), new ArrayList<>(), c, "id", d);
        d.setPlayer(p);
        d.setEntities(Arrays.asList(fs, l, b1));
        d.updateAdjacentEntities();

        d.movementTick(Direction.RIGHT, null);
        assertEquals(b1.getPosition(), fs.getPosition());
        assertEquals("light_bulb_on", l.getType());
    }

    @Test
    @DisplayName("Open switch door by key")
    public void testKeySwitchDoor() {
        Config c = new Config("simple");
        Dungeon d = new Dungeon("empty", "simple", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, new ArrayList<>(),"id", new int[] {0});
        Boulder b1 = new Boulder(new Position(-1, 0), new ArrayList<>(), "id");
        SwitchDoor sd = new SwitchDoor(new Position(1, 0), 1, null, "id", "or");
        Key k = new Key(new Position(2, 0), null, 1, "ID");

        Player p = new Player(new Position(3, 0), new ArrayList<>(), c, "id", d);
        d.setPlayer(p);
        d.setEntities(new ArrayList<>(Arrays.asList(fs, sd, k, b1)));
        d.updateAdjacentEntities();

        d.movementTick(Direction.LEFT, null);
        d.movementTick(Direction.LEFT, null);
        d.movementTick(Direction.DOWN, null);
        d.movementTick(Direction.LEFT, null);
        d.movementTick(Direction.LEFT, null);
        d.movementTick(Direction.LEFT, null);
        d.movementTick(Direction.UP, null);
        d.movementTick(Direction.RIGHT, null);
        assertEquals(b1.getPosition(), fs.getPosition());
        d.movementTick(Direction.DOWN, null);
        d.movementTick(Direction.RIGHT, null);
        d.movementTick(Direction.UP, null);
        d.movementTick(Direction.RIGHT, null);
        assertEquals(sd.getPosition(), p.getPosition());
    }

    @Test
    @DisplayName("logic error checks") 
    public void testLogicChecks() {
        LogicHandler lhx = new XorLogic();
        LogicHandler lho = new OrLogic();
        LogicHandler lha = new AndLogic();
        LogicHandler lhc = new CoAndLogic();
        assertFalse(lha.determineActive(null));
        assertFalse(lhx.determineActive(null));
        assertFalse(lho.determineActive(null));
        assertFalse(lhc.determineActive(null));
        List<Circuit> circuits = new ArrayList<>();
        assertFalse(lhx.determineActive(circuits));
        assertFalse(lho.determineActive(circuits));
        assertFalse(lha.determineActive(circuits));
        assertFalse(lhc.determineActive(circuits));
        circuits.add(new FloorSwitch(new Position(0, 0), false, null, "id", new int[] {0}));
        assertFalse(lhc.determineActive(circuits));
        Circuit.changeCircuitState(false, null, null);
    }

    @Test
    @DisplayName("co and not trigger simulate initial state")
    public void testCoAndBadTriggerInitial() {
        Config c = new Config("simple");
        Dungeon d = new Dungeon("empty", "simple", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, new ArrayList<>(),"id", new int[] {-1});
        Wire w1 = new Wire(new Position(1, 1), new ArrayList<>(), "id", new int[] {-1});
        Wire w3 = new Wire(new Position(0, 1), new ArrayList<>(), "id", new int[] {-1});
        LightBulb l = new LightBulb(new Position(1, 0), new ArrayList<>(), "id", "co_and");
        Boulder b1 = new Boulder(new Position(-1, 0), new ArrayList<>(), "id");

        Player p = new Player(new Position(-2, 0), new ArrayList<>(), c, "id", d);
        d.setPlayer(p);
        d.setEntities(Arrays.asList(fs, w1, w3, l, b1));
        d.updateAdjacentEntities();

        d.movementTick(Direction.RIGHT, null);
        assertEquals(b1.getPosition(), fs.getPosition());
        assertEquals("light_bulb_off", l.getType());
    }

    @Test
    @DisplayName("co and not trigger not same tick")
    public void testCoAndBadTriggerDiffTick() {
        Config c = new Config("simple");
        Dungeon d = new Dungeon("empty", "simple", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, new ArrayList<>(),"id", d.getCurrTickAsRef());
        FloorSwitch fs1 = new FloorSwitch(new Position(1, 1), false, new ArrayList<>(),"id", d.getCurrTickAsRef());
        LightBulb l = new LightBulb(new Position(1, 0), new ArrayList<>(), "id", "co_and");
        Boulder b1 = new Boulder(new Position(-1, 0), new ArrayList<>(), "id");
        Boulder b2 = new Boulder(new Position(1, 2), new ArrayList<>(), "id");

        Player p = new Player(new Position(-2, 0), new ArrayList<>(), c, "id", d);
        d.setPlayer(p);
        d.setEntities(Arrays.asList(fs, fs1, b2, l, b1));
        d.updateAdjacentEntities();

        d.movementTick(Direction.RIGHT, null);
        assertEquals(b1.getPosition(), fs.getPosition());
        d.movementTick(Direction.DOWN, null);
        d.movementTick(Direction.DOWN, null);
        d.movementTick(Direction.DOWN, null);
        d.movementTick(Direction.RIGHT, null);
        d.movementTick(Direction.RIGHT, null);
        d.movementTick(Direction.UP, null);
        assertEquals("light_bulb_off", l.getType());
    }

    @Test
    @DisplayName("uncognised logic is null")
    public void testUnrecognisedLogic() {
        assertEquals(null, LogicHandler.logicFactory("blah"));
    }

    @Test
    @DisplayName("and not trigger >= two switches")
    public void testAndBadTrigger() {
        Config c = new Config("simple");
        Dungeon d = new Dungeon("empty", "simple", 1);
        FloorSwitch fs = new FloorSwitch(new Position(0, 0), false, new ArrayList<>(),"id", d.getCurrTickAsRef());
        FloorSwitch fs1 = new FloorSwitch(new Position(1, 1), false, new ArrayList<>(),"id", d.getCurrTickAsRef());
        LightBulb l = new LightBulb(new Position(1, 0), new ArrayList<>(), "id", "co_and");
        Boulder b1 = new Boulder(new Position(-1, 0), new ArrayList<>(), "id");
        Boulder b2 = new Boulder(new Position(1, 2), new ArrayList<>(), "id");

        Player p = new Player(new Position(-2, 0), new ArrayList<>(), c, "id", d);
        d.setPlayer(p);
        d.setEntities(Arrays.asList(fs, fs1, b2, l, b1));
        d.updateAdjacentEntities();

        d.movementTick(Direction.RIGHT, null);
        assertEquals(b1.getPosition(), fs.getPosition());
        assertEquals("light_bulb_off", l.getType());
    }

}
