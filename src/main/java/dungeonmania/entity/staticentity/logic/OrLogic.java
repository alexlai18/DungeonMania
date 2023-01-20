package dungeonmania.entity.staticentity.logic;

import java.util.List;

public class OrLogic extends LogicHandler {

    @Override
    public boolean determineActive(List<Circuit> adjacentCircuits) {
        if (adjacentCircuits == null || adjacentCircuits.isEmpty()) return false;
        return adjacentCircuits.stream().anyMatch(Circuit::isActive);
    }
    
}
