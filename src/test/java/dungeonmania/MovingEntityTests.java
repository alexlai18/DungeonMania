package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.movingentity.Hydra;
import dungeonmania.entity.movingentity.Mercenary;
import dungeonmania.entity.movingentity.Spider;
import dungeonmania.entity.movingentity.ZombieToast;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.interfaces.movement.ControlledMovingEntity;
import dungeonmania.interfaces.movement.MercenaryMovement;
import dungeonmania.interfaces.movement.NoMovement;
import dungeonmania.interfaces.movement.SpiderMovement;
import dungeonmania.interfaces.movement.ZombieToastMovement;
import dungeonmania.util.Position;

public class MovingEntityTests {
    Config config = new Config("simple");

    @Test
    @DisplayName("Test set movement to original")
    public void testSetMovementOriginal() {
        Dungeon dungeon = new Dungeon("empty", "simple", 1);
        // change movement of all moving entities
        Player p = new Player(new Position(0, 0), null, config, "id", dungeon);
        Hydra h = new Hydra(new Position(1, 0), null, "id", dungeon);
        Mercenary m = new Mercenary(new Position(0, 1), null, config, dungeon, "id");
        Spider s = new Spider(new Position(0, -1), null, config, "id", dungeon);
        ZombieToast z = new ZombieToast(new Position(0, 2), null, config, "id", dungeon);

        p.setMovement(new NoMovement());
        h.setMovement(new NoMovement());
        m.setMovement(new NoMovement());
        s.setMovement(new NoMovement());
        z.setMovement(new NoMovement());

        p.setMovementToOriginal(dungeon);
        h.setMovementToOriginal(dungeon);
        m.setMovementToOriginal(dungeon);
        s.setMovementToOriginal(dungeon);
        z.setMovementToOriginal(dungeon);

        assertTrue(p.getMovement() instanceof ControlledMovingEntity);
        assertTrue(h.getMovement() instanceof ZombieToastMovement);
        assertTrue(m.getMovement() instanceof MercenaryMovement);
        assertTrue(s.getMovement() instanceof SpiderMovement);
        assertTrue(z.getMovement() instanceof ZombieToastMovement);
    }
}

