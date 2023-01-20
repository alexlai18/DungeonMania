package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.logic.Logic;
import dungeonmania.entity.staticentity.logic.LogicHandler;
import dungeonmania.util.Position;

public class LogicBomb extends Bomb implements Logic {
    private LogicHandler logicHandler;
    
    public LogicBomb(
        Position position, 
        List<Entity> adjacentEntities, 
        int bombRadius, 
        String itemId, 
        Dungeon dungeon,
        String logic
    ) {
        super(position, adjacentEntities, bombRadius, itemId, dungeon);
        this.logicHandler = LogicHandler.logicFactory(logic);
    }

    @Override
    public void useActivationStatus() {
        if (this.logicHandler.determineActive(Entity.getAdjacentCircuits(this))) {
            this.detonate();
        }
    }
}
