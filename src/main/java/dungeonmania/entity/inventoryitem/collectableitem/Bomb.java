package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.UsableItem;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.FloorSwitch;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;
import dungeonmania.util.Position;

public class Bomb extends CollectableItem implements UsableItem, UsesDungeon {
    private int bombRadius;
    private Boolean hasBeenCollected;
    private Dungeon dungeon;

    public Bomb(
        Position position, 
        List<Entity> adjacentEntities, 
        int bombRadius, 
        String itemId, 
        Dungeon dungeon
    ) {
        super(position, adjacentEntities, itemId);
        this.bombRadius = bombRadius;
        this.hasBeenCollected = false;
        this.dungeon = dungeon;
        setType("bomb");
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public Boolean collectItem() {
        if (Boolean.FALSE.equals(hasBeenCollected)) {
            super.collectItem();
            this.hasBeenCollected = true;
            return true;
        }
        return false;
    }

    protected void detonate() {
        int xCoord = this.getPosX();
        int yCoord = this.getPosY();

        int lowLimitX = xCoord - bombRadius;
        int highLimitX = xCoord + bombRadius;
        int lowLimitY = yCoord - bombRadius;
        int highLimitY = yCoord + bombRadius;
        
        List<Entity> entityCopy = new ArrayList<>(this.dungeon.getEntities());
        entityCopy.stream().forEach(entity -> {
            // It destroys all entities in a radius
            int entityX = entity.getPosX();
            int entityY = entity.getPosY();
            if (entityX <= highLimitX && entityX >= lowLimitX && entityY <= highLimitY && entityY >= lowLimitY) {
                this.dungeon.removeEntity(entity);
            }
        });
        this.setPosition(null);
    }

    public void floorSwitchTriggered() {
        detonate();
    }

    public void activateItem(Player player, Dungeon dungeon) {
        this.setPosition(player.getPosition());
        this.setAdjacentEntities(player.getAdjacentEntities());
        this.getAdjacentEntities().stream().forEach(e -> {
            if (e instanceof FloorSwitch) {
                FloorSwitch f = (FloorSwitch) e;
                if (f.isActive()) {
                    detonate();
                    if (this.bombRadius == 0) {
                        f.removeBomb(this);
                    }
                    return;
                } 
                f.addBomb(this);
            }
        });
    }
}
