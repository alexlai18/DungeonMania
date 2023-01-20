package dungeonmania.entity.movingentity;

import dungeonmania.interfaces.movement.*;
import dungeonmania.util.Position;
import java.util.List;
import dungeonmania.entity.Entity;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;

public class ZombieToast extends MovingEntity implements PotionEffects {
    public ZombieToast(Position position, List<Entity> adjacentEntities, Config config, String id, Dungeon dungeon) {
        super(position, adjacentEntities, config.getZombieAttack(), config.getZombieHealth(), id, dungeon);
        setMovement(new ZombieToastMovement());
        setType("zombie_toast");
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
