package dungeonmania.interfaces.movement;

import dungeonmania.entity.Entity;
import dungeonmania.interfaces.overlapinteract.SlowMovementOverlapInteract;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;
import dungeonmania.util.Direction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class ZombieToastMovement implements Movement, Serializable {
    
    @Override
    public void move(Entity entity, Direction movementDirection) {
        Random random = new Random(System.currentTimeMillis());
        List<Direction> dirs = new LinkedList<>(Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
        
        Direction randomDirection = dirs.get(random.nextInt(dirs.size()));
        // shows whether zombie moved through traversable entity
        boolean moved = false;
        // avoid overlapping with uninteractable entity
        List<Entity> overlappingEntities = entity.entityOverlap(randomDirection);
        while (!overlappingEntities.isEmpty() && !dirs.isEmpty()) {
            for (Entity e : overlappingEntities) {
                if ((e.getOverlapInteract() instanceof TraversableOverlapInteract
                || e.getOverlapInteract() instanceof SlowMovementOverlapInteract)
                && e.overlapInteractBehaviour(entity, randomDirection)) {
                moved = true;
                }
            }

            // random direction worked hence no other movement required
            if (moved) return;

            // remove the direction that causes overlap with uninteractable object
            dirs.remove(randomDirection);
            if (!dirs.isEmpty()) {
                randomDirection = dirs.get(random.nextInt(dirs.size()));
                
                // reassign overlappingEntities
                overlappingEntities = entity.entityOverlap(randomDirection);
            }

        }

        // cannot move without overlapping another uninteractable entity
        if (dirs.isEmpty()) return;
            
        entity.moveByDirection(randomDirection);
    }
}
