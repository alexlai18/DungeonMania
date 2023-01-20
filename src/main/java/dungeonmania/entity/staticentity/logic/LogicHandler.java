package dungeonmania.entity.staticentity.logic;

import java.io.Serializable;
import java.util.List;

public abstract class LogicHandler implements Serializable {
    private boolean active = false;

    public static LogicHandler logicFactory(String logic) {
        switch (logic) {
            case "and":
                return new AndLogic();
            case "or":
                return new OrLogic();
            case "xor":
                return new XorLogic();
            case "co_and":
                return new CoAndLogic();
            default:
                return null;
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract boolean determineActive(List<Circuit> adjacentCircuits);
    
}
