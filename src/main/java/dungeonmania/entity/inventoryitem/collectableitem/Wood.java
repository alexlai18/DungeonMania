package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Wood extends CollectableItem {
    public Wood(Position position, List<Entity> adjacentEntities, String itemId) {
        super(position, adjacentEntities, itemId);
        setType("wood");
    }
}
