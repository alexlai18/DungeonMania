package dungeonmania.entity.staticentity;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.logic.Logic;
import dungeonmania.entity.staticentity.logic.LogicHandler;
import dungeonmania.interfaces.overlapinteract.ImmovableOverlapInteract;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;
import dungeonmania.util.Position;

public class SwitchDoor extends Door implements Logic {
    private LogicHandler logicHandler;

    public SwitchDoor(
        Position position, 
        int keyId, 
        List<Entity> adjacentEntities, 
        String id, 
        String logic
    ) {
        super(position, keyId, adjacentEntities, id);
        setType("switch_door");
        this.logicHandler = LogicHandler.logicFactory(logic);
    }

    @Override
    public void useActivationStatus() {
        if (this.logicHandler.determineActive(Entity.getAdjacentCircuits(this))) {
            setType("switch_door_open");
            setOverlapInteract(new TraversableOverlapInteract());
        } else if (this.isLocked()) {
            // only make immovable if not unlocked through other means (keys/sunstone)
            setType("switch_door");
            setOverlapInteract(new ImmovableOverlapInteract());
        }
    }
    
}
