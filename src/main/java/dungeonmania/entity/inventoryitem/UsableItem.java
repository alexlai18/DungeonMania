package dungeonmania.entity.inventoryitem;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.movingentity.player.Player;

public interface UsableItem {
    public void activateItem(Player player, Dungeon dungeon);
}
