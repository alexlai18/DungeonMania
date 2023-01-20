package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;

import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.MovingEntity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.SwampTile;
import dungeonmania.interfaces.ally.Ally;
import dungeonmania.util.Direction;

public class SlowMovementOverlapInteract implements OverlapInteract, Serializable {
    private SwampTile swampTile;

    public SlowMovementOverlapInteract(SwampTile swampTile) {
        this.swampTile = swampTile;
    }

    private boolean canBeSlowed(Entity entity) {
        if (!(entity instanceof MovingEntity)) return false;
        if (entity instanceof Player) return false;
        if (entity instanceof Ally) return !((Ally) entity).getBribed();
        return true;
    }

    @Override
    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        // add swamp tile observers
        if (canBeSlowed(incomingEntity)) {
            swampTile.addSlowedEntity((MovingEntity) incomingEntity);
        }

        // Use logic from traversable overlap interact
        OverlapInteract traversableOverlap = new TraversableOverlapInteract();
        return traversableOverlap.overlapInteract(entity, incomingEntity, movementDirection);
    }
    
}
