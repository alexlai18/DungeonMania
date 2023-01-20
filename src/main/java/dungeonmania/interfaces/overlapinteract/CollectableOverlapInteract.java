package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.collectableitem.CollectableItem;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.util.Direction;

public class CollectableOverlapInteract implements OverlapInteract, Serializable {
    @Override
    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        if (!(entity instanceof CollectableItem)) return false;
        if (incomingEntity instanceof Player) {
            Player player = (Player) incomingEntity;
            CollectableItem collectableItem = (CollectableItem) entity;
            incomingEntity.setPosition(entity.getPosition());
            player.collectEntity(collectableItem);
            return true;
        } 

        incomingEntity.setPosition(entity.getPosition());
        return true;
    }
}