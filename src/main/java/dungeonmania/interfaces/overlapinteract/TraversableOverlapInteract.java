package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;

import dungeonmania.entity.Entity;
import dungeonmania.util.Direction;

/**
 * ImmovableEntity
 */
public class TraversableOverlapInteract implements OverlapInteract, Serializable {

    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        incomingEntity.setPosition(entity.getPosition());
        return true;
    }    
}
