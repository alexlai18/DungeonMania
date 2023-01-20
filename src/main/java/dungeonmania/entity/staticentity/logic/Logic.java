package dungeonmania.entity.staticentity.logic;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.staticentity.FloorSwitch;
import dungeonmania.entity.staticentity.LogicFloorSwitch;

public interface Logic {

    public abstract void useActivationStatus();

    public static void activateEntities(Dungeon dungeon) {
        // activate/deactivate logical floor switches and their wires
        dungeon.getEntities().stream()
            .filter(LogicFloorSwitch.class::isInstance)
            .map(LogicFloorSwitch.class::cast)
            .forEach(LogicFloorSwitch::useActivationStatus);

        // go through other logical entities and activate them
        dungeon.getEntities().stream()
            .filter(e -> !(e instanceof FloorSwitch))
            .filter(Logic.class::isInstance)
            .map(Logic.class::cast)
            .forEach(Logic::useActivationStatus);
    }
}
