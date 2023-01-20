package dungeonmania.entity.staticentity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.logic.Circuit;
import dungeonmania.entity.staticentity.logic.Logic;
import dungeonmania.entity.staticentity.logic.LogicHandler;
import dungeonmania.util.Position;

public class LogicFloorSwitch extends FloorSwitch implements Logic {
    private LogicHandler logicHandler;

    public LogicFloorSwitch(
        Position position, 
        boolean triggered, 
        List<Entity> adjacentEntities, 
        String id,
        String logic, 
        int[] tickAsRef
    ) {
        super(position, triggered, adjacentEntities, id, tickAsRef);
        this.logicHandler = LogicHandler.logicFactory(logic);
    }
    
    @Override
    public boolean isActive() {
        if (logicHandler != null) return logicHandler.isActive() || this.isTriggered();
		return this.isTriggered();
    }

    @Override
    public void setActive(boolean active) {
        this.logicHandler.setActive(active);
        this.setTickLastActivated(this.getTickAsRef()[0]);
    }

    @Override
    public void useActivationStatus() {
        Set<Circuit> visitedCircuits = new HashSet<>();
        if (this.isTriggered() 
            || this.logicHandler.determineActive(Entity.getAdjacentCircuits(this))
        ) {
            setActive(true);
            this.activationSideEffects(true, visitedCircuits);
        } else {
            setActive(false);
            this.activationSideEffects(false, visitedCircuits);
        }
    }
}
