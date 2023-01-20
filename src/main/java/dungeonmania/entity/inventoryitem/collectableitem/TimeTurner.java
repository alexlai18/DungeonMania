package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class TimeTurner extends CollectableItem {
    public TimeTurner(Position position, List<Entity> adjacentEntities, String itemId) {
        super(position, adjacentEntities, itemId);
        setType("time_turner");
    }
}
