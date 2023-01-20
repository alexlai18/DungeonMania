package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Key extends CollectableItem {
    private int inputKeyId;
    
    public Key(Position position, List<Entity> adjacentEntities, int id, String itemId) {
        super(position, adjacentEntities, itemId);
        this.inputKeyId = id;
        setType("key");
    }

    public int getInputKeyId() {
        return this.inputKeyId;
    }

    public void stopCollection() {
        setInventoryStatus(false);
    }
}
