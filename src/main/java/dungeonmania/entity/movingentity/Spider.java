package dungeonmania.entity.movingentity;

import dungeonmania.interfaces.movement.SpiderMovement;
import dungeonmania.util.Position;
import java.util.List;
import dungeonmania.entity.Entity;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;


public class Spider extends MovingEntity {
    public Spider(Position position, List<Entity> adjacentEntities, Config config, String id, Dungeon dungeon) {
        super(position, adjacentEntities, config.getSpiderAttack(), config.getSpiderHealth(), id, dungeon);
        setMovement(new SpiderMovement());
        setType("spider");
    }

    @Override
    public void setMovementToOriginal(Dungeon dungeon) {
        setMovement(new SpiderMovement());
    }
}
    

