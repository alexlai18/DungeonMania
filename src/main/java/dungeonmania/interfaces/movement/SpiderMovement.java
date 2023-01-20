package dungeonmania.interfaces.movement;

import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.Boulder;
import dungeonmania.util.Direction;

import java.io.Serializable;
import java.util.List;

public class SpiderMovement implements Movement, Serializable {
    private boolean boulderHit = false; 

    @Override
    public void move(Entity entity, Direction movementDirection) {
        Direction direction;
        // Move right 
        if (entity.getPosition().getX() == entity.getStartingPosition().getX() && entity.getPosition().getY() == entity.getStartingPosition().getY() - 1 && boulderHit == false) {
            direction = Direction.RIGHT;
        // Move down
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() + 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() - 1 && boulderHit == false) {
            direction = Direction.DOWN;
        // Move down 
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() + 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() && boulderHit == false) {
            direction = Direction.DOWN;
        // Move left
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() + 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() + 1 && boulderHit == false) {
            direction = Direction.LEFT;
        // Move left
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() && entity.getPosition().getY() == entity.getStartingPosition().getY() + 1 && boulderHit == false) {
            direction = Direction.LEFT;
        // Move up 
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() - 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() + 1 && boulderHit == false) {
            direction = Direction.UP;
        // Move up
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() - 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() && boulderHit == false) {
            direction = Direction.UP;
        // Move right
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() - 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() - 1 && boulderHit == false) {
            direction = Direction.RIGHT;
        }
        // Move Left
        else if (entity.getPosition().getX() == entity.getStartingPosition().getX() && entity.getPosition().getY() == entity.getStartingPosition().getY() - 1 && boulderHit == true) {
            direction = Direction.LEFT;
        // Move Left
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() + 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() - 1 && boulderHit == true) {
            direction = Direction.LEFT;
        // Move Up
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() + 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() && boulderHit == true) {
            direction = Direction.UP;
        // Move Up
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() + 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() + 1 && boulderHit == true) {
            direction = Direction.UP;
        // Move Right
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() && entity.getPosition().getY() == entity.getStartingPosition().getY() + 1 && boulderHit == true) {
            direction = Direction.RIGHT;
        // Move Right
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() - 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() + 1 && boulderHit == true) {
            direction = Direction.RIGHT;
        // Move Down
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() - 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() && boulderHit == true) {
            direction = Direction.DOWN;
        // Move Down
        } else if (entity.getPosition().getX() == entity.getStartingPosition().getX() - 1 && entity.getPosition().getY() == entity.getStartingPosition().getY() - 1 && boulderHit == true) {
            direction = Direction.DOWN;
        // Don't move
        } else if (entity.getPosition() == entity.getStartingPosition() && boulderHit == true) {
            direction = Direction.UP;
            entity.moveByDirection(direction);
            direction = Direction.DOWN;
        // Move Up
        } else {
            direction = Direction.UP;
        }

        List<Entity> overlappingEntities = entity.entityOverlap(direction);
        if (!overlappingEntities.isEmpty()) {
            for (Entity e : overlappingEntities) {
                e.overlapInteractBehaviour(entity, direction);
                if (e instanceof Boulder) {
                    boulderHit = !boulderHit;
                }
            }
        } else {
            entity.moveByDirection(direction);
        }
    }
    

}
    
