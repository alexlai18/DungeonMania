package dungeonmania.entity.staticentity;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.collectableitem.Bomb;
import dungeonmania.entity.inventoryitem.collectableitem.LogicBomb;
import dungeonmania.entity.staticentity.logic.Circuit;
import dungeonmania.interfaces.overlapinteract.ImmovableOverlapInteract;
import dungeonmania.interfaces.overlapinteract.PushableOverlapInteract;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;

public class FloorSwitch extends StaticEntity implements Circuit {
    private boolean triggered;
    private List<Bomb> bombs = new ArrayList<>();
    private int[] tickAsRef;
    private int tickLastActivated = -1;

	public FloorSwitch(
        Position position, 
        boolean triggered, 
        List<Entity> adjacentEntities, 
        String id, 
        int[] tickAsRef
    ) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new TraversableOverlapInteract());
        this.triggered = triggered;
        setType("switch");
        this.tickAsRef = tickAsRef;
    }

    public void addBomb(Bomb b) {
        this.bombs.add(b);
    }

    public void removeBomb(Bomb b) {
        this.bombs.remove(b);
    }

    public boolean isTriggered() {
        return this.triggered;
	}

    public int[] getTickAsRef() {
        return tickAsRef;
    }

    public void activationSideEffects(boolean isActive, Set<Circuit> visitedCircuits) {
        // avoid triggering logic bombs
        // as done after movement done instead
        if (isActive) {
            bombs.stream()
                .filter(b -> !(b instanceof LogicBomb))
                .forEach(Bomb::floorSwitchTriggered);
        }
        
        // set active/deactive for wires
        this.getAdjacentEntities().stream()
            .filter(Circuit.class::isInstance)
            .map(Circuit.class::cast)
            .forEach(c -> Circuit.changeCircuitState(isActive, c, visitedCircuits));
    }   

    @Override
    public boolean overlapInteractBehaviour(Entity incomingEntity, Direction movementDirection) {
        if (isTriggered()) {
            List<Entity> nextToSwitch = this.entityOverlap(movementDirection);
            if (nextToSwitch.stream()
                .anyMatch(e -> e.getOverlapInteract() instanceof PushableOverlapInteract 
                    || e.getOverlapInteract() instanceof ImmovableOverlapInteract)) {
                return false;
            }
        }
        this.triggered = incomingEntity.getOverlapInteract() instanceof PushableOverlapInteract;
        Set<Circuit> visitedCircuits = new HashSet<>();
        if (isTriggered()) {
            this.tickLastActivated = this.tickAsRef[0];
            this.activationSideEffects(true, visitedCircuits);
        } else {
            this.activationSideEffects(false, visitedCircuits);
        }

        return super.overlapInteractBehaviour(incomingEntity, movementDirection);
    }

    @Override
    public boolean isActive() {
		return this.triggered;
    }

    @Override
    public void setActive(boolean active) {
        this.tickLastActivated = this.tickAsRef[0];
    }

    @Override
    public int getTickLastActivated() {
        return this.tickLastActivated;
    }

    @Override
    public List<Circuit> getAdjacentCircuits() {
        return Entity.getAdjacentCircuits(this);
    }

    public void setTickLastActivated(int tickLastActivated) {
        this.tickLastActivated = tickLastActivated;
    }

}
