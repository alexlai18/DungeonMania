package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.InventoryItem;
import dungeonmania.interfaces.overlapinteract.*;
import dungeonmania.util.Position;

public abstract class CollectableItem extends InventoryItem {
    public CollectableItem(Position position, List<Entity> adjacentEntities, String itemId) {
        super(position, adjacentEntities, false, itemId);
        setOverlapInteract(new CollectableOverlapInteract());
    }
}
