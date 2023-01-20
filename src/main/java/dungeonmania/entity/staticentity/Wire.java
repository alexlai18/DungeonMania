package dungeonmania.entity.staticentity;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.logic.Circuit;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;
import dungeonmania.util.Position;

public class Wire extends StaticEntity implements Circuit {
    private boolean active = false;
    private int[] tickAsRef;
    private int tickLastActivated = -1;

    public Wire(
        Position position, 
        List<Entity> adjacentEntities, 
        String id, 
        int[] tickAsRef
    ) {
        super(position, adjacentEntities, id);
        this.tickAsRef = tickAsRef;
        setType("wire");
        setOverlapInteract(new TraversableOverlapInteract());
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
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

}
