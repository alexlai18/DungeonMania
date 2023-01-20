package dungeonmania.entity.inventoryitem.buildableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.Weapon;
import dungeonmania.util.Position;

public class Bow extends BuildableItem implements Weapon {
    private int multiplier;

    public Bow(Position position, List<Entity> adjacentEntities, int durability, String itemId) {
        super(position, adjacentEntities, durability, itemId);
        this.multiplier = 2;
        setType("bow");
    }

    public int getMultiplier() {
        return this.multiplier;
    }

    public void use() {
        decreaseDurability();
    }
}
