package dungeonmania.entity.staticentity.logic;

import java.util.List;

public class AndLogic extends LogicHandler {

    @Override
    public boolean determineActive(List<Circuit> adjacentCircuits) {
        if (adjacentCircuits == null || adjacentCircuits.isEmpty()) return false;

        return adjacentCircuits.size() >= 2 && adjacentCircuits.stream().allMatch(Circuit::isActive);
    }
    
}
