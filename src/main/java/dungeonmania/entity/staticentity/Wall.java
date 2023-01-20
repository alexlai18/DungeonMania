package dungeonmania.entity.staticentity;

import dungeonmania.util.Position;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.interfaces.overlapinteract.ImmovableOverlapInteract;

public class Wall extends StaticEntity {
    
    public Wall(Position position, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new ImmovableOverlapInteract());
        setType("wall");
    }

}
