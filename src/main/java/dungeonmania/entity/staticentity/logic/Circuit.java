package dungeonmania.entity.staticentity.logic;

import java.util.List;
import java.util.Set;

public interface Circuit {

    public abstract boolean isActive();

    public abstract void setActive(boolean active);

    public abstract int getTickLastActivated();

    public abstract List<Circuit> getAdjacentCircuits();

    public static void changeCircuitState(boolean isActive, Circuit root, Set<Circuit> visitedCircuits) {
        if (root == null) return;
        if (visitedCircuits.contains(root)) return;
        root.setActive(isActive);
        visitedCircuits.add(root);
        root.getAdjacentCircuits().stream()
            .forEach(c -> Circuit.changeCircuitState(isActive, c, visitedCircuits));
    }

}
