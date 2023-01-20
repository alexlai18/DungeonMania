package dungeonmania.entity.staticentity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.MovingEntity;
import dungeonmania.interfaces.movement.NoMovement;
import dungeonmania.interfaces.overlapinteract.SlowMovementOverlapInteract;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;
    private Map<MovingEntity, Integer> slowedEntities = new HashMap<>();

    public SwampTile(Position position, List<Entity> adjacentEntities, String id, int movementFactor) {
        super(position, adjacentEntities, id);
        this.movementFactor = movementFactor;
        setOverlapInteract(new SlowMovementOverlapInteract(this));
        setType("swamp_tile");
    }

    public int getMovementFactor() {
        return movementFactor;
    }

    public void addSlowedEntity(MovingEntity slowedEntity) {
        slowedEntities.put(slowedEntity, 0);
        slowedEntity.setMovement(new NoMovement());
    }

    public void removeSlowedEntity(MovingEntity slowedEntity, Dungeon dungeon) {
        slowedEntity.setMovementToOriginal(dungeon);
        slowedEntities.remove(slowedEntity);
    }

    public void slowedMovementTick(Dungeon dungeon) {
        Set<MovingEntity> entityList = new HashSet<>(slowedEntities.keySet());
        entityList.stream().forEach(e -> {
            int ticksStayed = slowedEntities.get(e);
            if (ticksStayed >= this.movementFactor) {
                // remove from observer and revert movement
                this.removeSlowedEntity(e, dungeon);
            } else {
                // increase count in tile
                slowedEntities.put(e, ticksStayed + 1);
            }
        });
    }

}
