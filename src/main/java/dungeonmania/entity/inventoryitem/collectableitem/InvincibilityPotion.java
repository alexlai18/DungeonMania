package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    public InvincibilityPotion(Position position, List<Entity> adjacentEntities, int potionDurability, String itemId) {
        super(position, adjacentEntities, potionDurability, itemId);
        setType("invincibility_potion");
    }
}
