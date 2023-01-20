package dungeonmania.entity.staticentity;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.Optional;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.collectableitem.Key;
import dungeonmania.entity.inventoryitem.collectableitem.SunStone;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.interfaces.overlapinteract.ImmovableOverlapInteract;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;

public class Door extends StaticEntity {
    private int keyId;
    private boolean locked = true;
    
    public Door(Position position, int keyId, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new ImmovableOverlapInteract());
        this.keyId = keyId;
        setType("door");
    }
    
    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean overlapInteractBehaviour(Entity incomingEntity, Direction movementDirection) {
        // set overlap to traversable if player has matching key
        if (incomingEntity instanceof Player) {
            Player player = (Player) incomingEntity;
            List<Key> keys = player.getKeys();
            Optional<Key> matchingKey = keys.stream().filter(key -> key.getInputKeyId() == this.keyId).findFirst();
            
            if (player.getInventoryList().stream().anyMatch(SunStone.class :: isInstance)) {
                this.setOverlapInteract(new TraversableOverlapInteract());
                this.setType("door_open");
                locked = false;
            } else if (matchingKey.orElse(null) != null) {
                player.itemUsed(matchingKey.get());
                this.setOverlapInteract(new TraversableOverlapInteract());
                this.setType("door_open");
                locked = false;
            }
        }
        
        return super.overlapInteractBehaviour(incomingEntity, movementDirection);
    }
}
