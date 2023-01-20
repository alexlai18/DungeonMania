package dungeonmania.entity.staticentity;

import dungeonmania.util.Position;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.interfaces.overlapinteract.PushableOverlapInteract;

public class Boulder extends StaticEntity {
    public Boulder(Position position, List<Entity> adjacentEntities, String id) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new PushableOverlapInteract());
        setType("boulder");
    }
}
