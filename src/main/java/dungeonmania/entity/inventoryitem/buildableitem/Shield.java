package dungeonmania.entity.inventoryitem.buildableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.Weapon;
import dungeonmania.util.Position;

public class Shield extends BuildableItem implements Weapon {
    private int defence;
    public Shield(Position position, List<Entity> adjacentEntities, int durability, int defence, String itemId) {
        super(position, adjacentEntities, durability, itemId);
        this.defence = defence;
        setType("shield");
    }

    public int getDefence() {
        return this.defence;
    }

    public void use() {
        decreaseDurability();
    }
}
