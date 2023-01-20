package dungeonmania.interfaces.movement;

import java.io.Serializable;

import dungeonmania.entity.Entity;
import dungeonmania.util.Direction;

/**
 * BribedMercenaryMovement
 */
public class NoMovement implements Movement, Serializable {
    @Override
    public void move(Entity entity, Direction movementDirection) {
        // do nothing
    }
}