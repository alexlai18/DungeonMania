package dungeonmania.entity.staticentity;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.logic.Logic;
import dungeonmania.entity.staticentity.logic.LogicHandler;
import dungeonmania.interfaces.overlapinteract.ImmovableOverlapInteract;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements Logic {
    private LogicHandler logicHandler;

    public LightBulb(
        Position position, 
        List<Entity> adjacentEntities, 
        String id, 
        String logic
    ) {
        super(position, adjacentEntities, id);
        this.logicHandler = LogicHandler.logicFactory(logic);
        setType("light_bulb_off");
        setOverlapInteract(new ImmovableOverlapInteract());
    }

    @Override
    public void useActivationStatus() {
        if (this.logicHandler.determineActive(Entity.getAdjacentCircuits(this))) {
            setType("light_bulb_on");
        } else {
            setType("light_bulb_off");
        }
    }
    
}
