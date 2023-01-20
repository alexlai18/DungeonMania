package dungeonmania.interfaces.movement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.interfaces.ally.Ally;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ControlledMovingEntity implements Movement, Serializable {
    private List<Ally> allies = new ArrayList<>();

    public void setAllies(List<Ally> allies) {
        this.allies = allies;
    }
    
    @Override
    public void move(Entity entity, Direction movementDirection) {
        Position originalPos = entity.getPosition();
        List<Entity> overlappingEntities = entity.entityOverlap(movementDirection);
        List<Boolean> moveBehaviour = new ArrayList<>();
        if (!overlappingEntities.isEmpty()) {
            for (Entity e : overlappingEntities) {
                moveBehaviour.add(e.overlapInteractBehaviour(entity, movementDirection));
            }
            int numTrue = (int) moveBehaviour.stream().filter(b -> b).count();
            int numFalse = (int) moveBehaviour.stream().filter(b -> !b).count();
            if (numTrue >= 1 && numFalse >= 1) {
                // player moved into immovable entity, so reverse the movement
                Direction revDirection;
                if (movementDirection == Direction.UP) {
                    revDirection = Direction.DOWN;
                } else if (movementDirection == Direction.DOWN) {
                    revDirection = Direction.UP;
                } else if (movementDirection == Direction.LEFT) {
                    revDirection = Direction.RIGHT;
                } else {
                    revDirection = Direction.LEFT;
                }
                entity.moveByDirection(revDirection);
            } else if (numTrue > 0 && numFalse == 0) {
                allies.stream().filter(Ally::getBribed).forEach(m -> ((Entity) m).setPosition(originalPos));
                allies.stream().filter(Ally::getMindControlled).forEach(m -> ((Entity) m).setPosition(originalPos));
            }
        } else {
            entity.moveByDirection(movementDirection);
            allies.stream().filter(Ally::getBribed).forEach(m -> ((Entity) m).setPosition(originalPos));
            allies.stream().filter(Ally::getMindControlled).forEach(m -> ((Entity) m).setPosition(originalPos));
        }
    }
}
