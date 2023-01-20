package dungeonmania.entity.inventoryitem.buildableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Sceptre extends BuildableItem {
    private int durability;
    private boolean activated;

    public Sceptre(Position position, List<Entity> adjacentEntities, int durability, String itemId) {
        super(position, adjacentEntities, durability, itemId);
        this.durability = durability;
        this.activated = false;
        setType("sceptre");
    }

    public void use() {
        durability -= 1;
    }

    public int getDuration() {
        return this.durability;
    }

    public void activate() {
        this.activated = true;
    }

    public boolean getActivated() {
        return this.activated;
    }
}
