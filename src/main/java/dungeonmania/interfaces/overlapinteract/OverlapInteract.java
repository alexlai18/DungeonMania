package dungeonmania.interfaces.overlapinteract;

import dungeonmania.entity.Entity;
import dungeonmania.util.Direction;

public interface OverlapInteract {
    /**
     * determines behaviour of incoming/this entity when it tries to overlap
     * with this entity, the incoming has not moved yet
     * @param entity
     * @param incomingEntity
     * @param movementDirection
     * @return true if incomingEntity can move false otherwise
     */
    public abstract boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection);
}
