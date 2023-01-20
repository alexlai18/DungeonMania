package dungeonmania.entity.staticentity;

import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.interfaces.overlapinteract.TimeTravelPortalOverlapInteract;
import dungeonmania.util.Position;

public class TimeTravelPortal extends StaticEntity {
    public TimeTravelPortal(Position position, List<Entity> adjacentEntities, String id, Dungeon dungeon) {
        super(position, adjacentEntities, id);
        setOverlapInteract(new TimeTravelPortalOverlapInteract());
        setType("time_travelling_portal");
    }
}
