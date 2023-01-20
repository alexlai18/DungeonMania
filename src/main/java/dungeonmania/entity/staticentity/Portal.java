package dungeonmania.entity.staticentity;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.Map;

import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.*;
import dungeonmania.interfaces.overlapinteract.*;

public class Portal extends StaticEntity {
    private Portal linkedPortal;
    
    public Portal(Position position, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        this.linkedPortal = null;
        setOverlapInteract(new TraversableOverlapInteract());
        setType("portal");
    }

    public Portal(Position position, Portal linkedPortal, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        this.linkedPortal = linkedPortal;
        setOverlapInteract(new TraversableOverlapInteract());
        setType("portal");
    }

    public Portal getLinkedPortal() {
        return linkedPortal;
    }

    public void setLinkedPortal(Portal portal) {
        this.linkedPortal = portal;
    }

    @Override
    public boolean overlapInteractBehaviour(Entity incomingEntity, Direction movementDirection) {
        if (incomingEntity instanceof ZombieToast || incomingEntity instanceof Hydra) {
            OverlapInteract immovableOverlap = new ImmovableOverlapInteract();
            return immovableOverlap.overlapInteract(this, incomingEntity, movementDirection);
        }else if (incomingEntity instanceof Spider) {
            OverlapInteract traversableOverlap = new TraversableOverlapInteract();
            return traversableOverlap.overlapInteract(this, incomingEntity, movementDirection);
        }

        
        List<Entity> entitiesCardinalOtherPortal = linkedPortal.entityOverlap(movementDirection);
        Position origPos = incomingEntity.getPosition();
        incomingEntity.setPosition(linkedPortal.getPosition());
        if (!entitiesCardinalOtherPortal.isEmpty()) {
            for (Entity e : entitiesCardinalOtherPortal) {
                boolean canMove = e.overlapInteractBehaviour(incomingEntity, movementDirection);
                if (!canMove) {
                    incomingEntity.setPosition(origPos);
                    return false;
                }
            }
        } else {
            incomingEntity.setPosition(linkedPortal.getPosition().translateBy(movementDirection));
        }
        return true;
    }

    /**
     * sets colours of portal for frontend, assumes every portal in map has a linked one
     * @param portalColourMap
     */
    public static void setColours(Map<String, Portal> portalColourMap) {
        for (Map.Entry<String, Portal> pair : portalColourMap.entrySet()) {
            String colour = pair.getKey();
            Portal portal1 = pair.getValue();
            Portal portal2 = portal1.getLinkedPortal();
            switch (colour) {
                case "BLUE":
                    portal1.setType("portal_blue");
                    portal2.setType("portal_blue");
                    break;
                case "RED":
                    portal1.setType("portal_red");
                    portal2.setType("portal_red");
                    break;
                case "YELLOW":
                    portal1.setType("portal_yellow");
                    portal2.setType("portal_yellow");
                    break;
                case "GREY":
                    portal1.setType("portal_grey");
                    portal2.setType("portal_grey");
                    break;
                case "GREEN":
                    portal1.setType("portal_green");
                    portal2.setType("portal_green");
                    break;
                default:
                    portal1.setType("portal");
                    portal2.setType("portal");
                    break;
            }
        }
    }
}
