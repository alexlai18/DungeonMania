package dungeonmania.entity.staticentity;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {

    protected StaticEntity(Position position, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
    }
}
