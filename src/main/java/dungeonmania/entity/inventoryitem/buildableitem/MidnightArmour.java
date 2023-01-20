package dungeonmania.entity.inventoryitem.buildableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.Weapon;
import dungeonmania.util.Position;

public class MidnightArmour extends BuildableItem implements Weapon {
    private int damage;
    private int defence;

    public MidnightArmour(Position position, List<Entity> adjacentEntities, int damage, int defence, String itemId) {
        super(position, adjacentEntities, (int) Double.POSITIVE_INFINITY, itemId);
        this.damage = damage;
        this.defence = defence;
        setType("midnight_armour");
    }

    public int getDamage() {
        return this.damage;
    }

    public int getDefence() {
        return this.defence;
    }

    public void use() {
        // Does nothing because it lasts forever
    }
}
