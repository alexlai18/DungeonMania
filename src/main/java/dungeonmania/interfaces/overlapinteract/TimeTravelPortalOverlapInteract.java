package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;

import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.util.Direction;

public class TimeTravelPortalOverlapInteract implements OverlapInteract, Serializable {

    @Override
    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        // TODO change older player to a type
        if (incomingEntity instanceof Player && !"older_player".equals(incomingEntity.getType())) {
            incomingEntity.setPosition(entity.getPosition());
            return true;
        }     
        return false;
    }
}
