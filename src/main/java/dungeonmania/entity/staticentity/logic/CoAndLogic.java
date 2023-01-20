package dungeonmania.entity.staticentity.logic;

import java.util.List;

public class CoAndLogic extends LogicHandler {

    @Override
    public boolean determineActive(List<Circuit> adjacentCircuits) {
        if (adjacentCircuits == null || adjacentCircuits.isEmpty() || adjacentCircuits.size() < 2) return false;
        // at least 2 values, none can be negative
        int tickCheck = adjacentCircuits.get(0).getTickLastActivated();
        return adjacentCircuits.stream().allMatch(
            c -> c.isActive() 
                && c.getTickLastActivated() >= 0 
                && c.getTickLastActivated() == tickCheck
        );
    }
    
}
