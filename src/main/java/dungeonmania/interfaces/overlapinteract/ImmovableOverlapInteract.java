package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;

import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.Spider;
import dungeonmania.entity.staticentity.Boulder;
import dungeonmania.util.Direction;

/**
 * ImmovableEntity
 */
public class ImmovableOverlapInteract implements OverlapInteract, Serializable {

    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        // does not move incoming as the entity is immovable
       
        if (incomingEntity instanceof Spider && !(entity instanceof Boulder)) {
            OverlapInteract traversableOverlap = new TraversableOverlapInteract();
            return traversableOverlap.overlapInteract(entity, incomingEntity, movementDirection);
        }
        
        return false;
    }
}
