package dungeonmania.entity.staticentity;

import dungeonmania.util.Position;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;

public class Exit extends StaticEntity {
    
    public Exit(Position position, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new TraversableOverlapInteract());
        setType("exit");
    }
}
