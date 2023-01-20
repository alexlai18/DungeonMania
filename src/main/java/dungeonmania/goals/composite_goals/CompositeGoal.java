package dungeonmania.goals.composite_goals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.goals.Goal;

public abstract class CompositeGoal implements Serializable {
    
    /**
     * List of all goals inside this composite goal.
     */
    private List<Goal> subGoals = new ArrayList<Goal>();

    public CompositeGoal() {
        super();
    }

    public List<Goal> getSubGoals() {
        return subGoals;
    }
    
    public void setSubGoals(List<Goal> subGoals) {
        this.subGoals = subGoals;
    }

    public boolean onlyExitLeft() {
        for (Goal g : getSubGoals()) {
            if (!g.onlyExitLeft() && !g.isCompleted()) {
                return false;
            }
        }
        return true;
    }

}
