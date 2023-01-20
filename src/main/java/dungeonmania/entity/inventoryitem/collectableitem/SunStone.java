package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.List;

import dungeonmania.entity.Entity;

import dungeonmania.util.Position;

public class SunStone extends CollectableItem {
    public SunStone(Position position, List<Entity> adjacentEntities, String itemId) {
        super(position, adjacentEntities, itemId);
        super.setType("sun_stone");
    }
}
