package dungeonmania.entity.movingentity;

import dungeonmania.interfaces.movement.*;
import dungeonmania.util.Position;
import java.util.List;
import dungeonmania.entity.Entity;
import dungeonmania.dungeon.Dungeon;

public class Hydra extends MovingEntity implements PotionEffects {
    public Hydra(Position position, List<Entity> adjacentEntities, String id, Dungeon dungeon) {
        super(position, adjacentEntities, dungeon.getConfig().getHydraAttack(), dungeon.getConfig().getHydraHealth(), id, dungeon);
        setMovement(new ZombieToastMovement());
        setType("hydra");
    }

    @Override
    public void invincibilityEffects(Dungeon dungeon) {
        if (this.getMovement() instanceof NoMovement) return;
        if (dungeon.getPlayerInvincibility()) {
            setMovement(new MercenaryMovement(dungeon));
        } else {
            setMovementToOriginal(dungeon);
        }
    }


    @Override
    public void invisibilityEffects(Dungeon dungeon) {
        // does nothing
    }

    @Override
    public void setMovementToOriginal(Dungeon dungeon) {
        setMovement(new ZombieToastMovement());
    }

}