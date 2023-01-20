package dungeonmania.entity.staticentity.logic;

import java.util.List;

public class XorLogic extends LogicHandler {

    @Override
    public boolean determineActive(List<Circuit> adjacentCircuits) {
        if (adjacentCircuits == null || adjacentCircuits.isEmpty()) return false;
        return adjacentCircuits.stream().filter(Circuit::isActive).count() == 1;
    }
    
}
