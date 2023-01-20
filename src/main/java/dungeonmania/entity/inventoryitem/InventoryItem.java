package dungeonmania.entity.inventoryitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class InventoryItem extends Entity {
    private Boolean inInventory;

    // Constructor for InventoryEntity
    public InventoryItem(Position position, List<Entity> adjacentEntities, Boolean inInventory, String id) {
        super(position, adjacentEntities, id);
        this.inInventory = inInventory;
    }

    public Boolean getInventoryStatus() {
        return this.inInventory;
    }

    public void setInventoryStatus(Boolean b) {
        this.inInventory = b;
    }

    public Boolean collectItem() {
        setInventoryStatus(true);
        return true;
    }
}