package dungeonmania.interfaces.movement;

import dungeonmania.entity.Entity;
import dungeonmania.util.Direction;

public interface Movement {
    public abstract void move(Entity entity, Direction movementDirection);
}
