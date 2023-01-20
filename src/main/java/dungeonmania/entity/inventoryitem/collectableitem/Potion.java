package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.UsableItem;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.util.Position;

public class Potion extends CollectableItem implements UsableItem {
    private int timeLeft;

    public Potion(Position position, List<Entity> adjacentEntities, int potionDurability, String itemId) {
        super(position, adjacentEntities, itemId);
        this.timeLeft = potionDurability;
    }

    public void use() {
        timeLeft -= 1;
    }

    public int getTimeLeft() {
        return this.timeLeft;
    }

    public void checkPotionStatus(Player player) {
        if (timeLeft == 0) {
            if (player.getPotionQueue().size() > 0) {
                player.setCurrentPotion(player.getPotionQueue().get(0));
                player.updatePotionQueue();
            } else {
                player.setCurrentPotion(null);
            }
        }
    }

    public void activateItem(Player player, Dungeon dungeon) {
        if (player.getCurrentPotion() == null) {
            player.setCurrentPotion(this);
        } else {
            player.getPotionQueue().add(this);
        }
    }
}
