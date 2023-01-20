package dungeonmania.entity.inventoryitem.buildableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.InventoryItem;
import dungeonmania.util.Position;

public class BuildableItem extends InventoryItem {
    private int durability;

    public BuildableItem(Position position, List<Entity> adjacentEntities, int durability, String itemId) {
        super(position, adjacentEntities, true, itemId);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public void decreaseDurability() {
        durability -= 1;
    }
}
