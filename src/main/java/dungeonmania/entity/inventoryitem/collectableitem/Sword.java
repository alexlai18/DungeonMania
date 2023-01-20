package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.Weapon;
import dungeonmania.util.Position;

public class Sword extends CollectableItem implements Weapon {
    private int durability;
    private int damage;
    
    public Sword(Position position, List<Entity> adjacentEntities, int durability, int damage, String itemId) {
        super(position, adjacentEntities, itemId);
        this.damage = damage;
        this.durability = durability;
        setType("sword");
    }

    public void decreaseDurability() {
        durability -= 1;
    }

    public int getDurability() {
        return this.durability;
    }
    
    public int getDamage() {
        return this.damage;
    }

    public void use() {
        decreaseDurability();
    }
}
