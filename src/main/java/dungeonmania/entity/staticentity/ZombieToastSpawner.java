package dungeonmania.entity.staticentity;

import dungeonmania.util.Position;
import dungeonmania.interfaces.interact.Interact;

import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.interfaces.overlapinteract.ImmovableOverlapInteract;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.spawner.Spawner;
import dungeonmania.spawner.ZombieToastSpawnerFunctionality;

public class ZombieToastSpawner extends StaticEntity implements Interact, UsesDungeon {
    private Spawner spawner;
    private Dungeon dungeon;
    
    public ZombieToastSpawner(Dungeon dungeon, Position position, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new ImmovableOverlapInteract());
        this.spawner = new ZombieToastSpawnerFunctionality(this, dungeon, dungeon.getConfig());
        this.dungeon = dungeon;
        setType("zombie_toast_spawner");
    }

    public void tick() {
        this.spawner.spawn();
    }

    public Spawner getSpawner() {
        return spawner;
    }

    @Override
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), true);
    }

    @Override
    public void interact(Player player) {
        if (player.hasWeapon() && Position.isAdjacent(player.getPosition(), this.getPosition())) {
            dungeon.removeEntity(this);
        }
    }
}
