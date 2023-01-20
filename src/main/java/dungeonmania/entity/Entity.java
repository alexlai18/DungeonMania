package dungeonmania.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entity.staticentity.logic.Circuit;
import dungeonmania.interfaces.interact.Interact;
import dungeonmania.interfaces.overlapinteract.OverlapInteract;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Entity implements Serializable {
    private Position position;
    private Interact interact;
    private OverlapInteract overlapInteract;
    // stores cardinally adjacent entities
    private List<Entity> adjacentEntities;
    private Position startingPosition;
    private String type;
    private String id;

    protected Entity(Position position, List<Entity> adjacentEntities, String id) {
        this.position = position;
        this.adjacentEntities = adjacentEntities;
        this.startingPosition = getPosition();
        this.id = id;
    }
    
    public void setAdjacentEntities(List<Entity> adjacentEntities) {
        this.adjacentEntities = adjacentEntities;
    }

    public void addAdjacentEntity(Entity adjacentEntity) {
        this.adjacentEntities.add(adjacentEntity);
    }

    public List<Entity> getAdjacentEntities() {
        return this.adjacentEntities;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public void setInteract(Interact interact, Position position) {
        this.interact = interact;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPosX() {
        return this.getPosition().getX();
    }

    public int getPosY() {
        return this.getPosition().getY();
    }


    public OverlapInteract getOverlapInteract() {
        return overlapInteract;
    }

    public void setOverlapInteract(OverlapInteract overlapInteract) {
        this.overlapInteract = overlapInteract;
    }

    public void moveByDirection(Direction direction) {
        this.position = this.position.translateBy(direction);
    }

    /**
     * determines and returns any adjacent entities that will overlap with this entity
     * @param movementDirection
     * @return Overlapping Entities
     */
    public List<Entity> entityOverlap(Direction movementDirection) {
        Position newPos = this.position.translateBy(movementDirection);
        List<Entity> overlappingEntities = new ArrayList<>();

        for (Entity e : this.adjacentEntities) {
            if (e.getPosition().equals(newPos)) {
                overlappingEntities.add(e);
            }
        }

        return overlappingEntities;
    }

    public boolean overlapInteractBehaviour(Entity incomingEntity, Direction movementDirection) {
        return this.overlapInteract.overlapInteract(this, incomingEntity, movementDirection);
    }

    public Interact getInteract() {
        return interact;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), false);
    }

    public static List<Circuit> getAdjacentCircuits(Entity e) {
        return e.getAdjacentEntities().stream()
            .filter(Circuit.class::isInstance).map(Circuit.class::cast)
            .collect(Collectors.toList());
    }
}
