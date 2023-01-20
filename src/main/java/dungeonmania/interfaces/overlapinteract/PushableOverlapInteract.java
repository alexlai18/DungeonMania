package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;
import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.util.Direction;

public class PushableOverlapInteract implements OverlapInteract, Serializable {

    @Override
    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        // only players can move boulders
        if (!(incomingEntity instanceof Player)) {
            OverlapInteract immovableOverlap = new ImmovableOverlapInteract();
            return immovableOverlap.overlapInteract(entity, incomingEntity, movementDirection);
        }
        
        // entity is pushable
        List<Entity> entitiesNextToPushable = entity.entityOverlap(movementDirection);
        boolean canMove = true;
        Entity temp = null;
        for (Entity entityNextToPushable : entitiesNextToPushable) {
            if (entityNextToPushable.getOverlapInteract() instanceof PushableOverlapInteract
                || entityNextToPushable.getOverlapInteract() instanceof ImmovableOverlapInteract) {
                // if the entity is next to an immovable entity or pushable entity
                canMove = false;
                break;
            } 
            
            temp = entityNextToPushable;
        }
        
        if (!canMove) {
            return false;
        }

        // move incoming
        incomingEntity.setPosition(entity.getPosition());
        
        if (temp == null) {
            // move pushable entity
            entity.moveByDirection(movementDirection);
        } else {
            // call overlap for entity the boulder was pushed into
            temp.overlapInteractBehaviour(entity, movementDirection);
        }

        return true;
    }
}
