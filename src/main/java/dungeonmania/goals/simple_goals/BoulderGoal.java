package dungeonmania.goals.simple_goals;

import dungeonmania.goals.Goal;

import java.io.Serializable;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.staticentity.FloorSwitch;


public class BoulderGoal implements Goal, Serializable {
    
    /**
     * Whether the Goal is completed.
     */
    private boolean isCompleted = false;

    public BoulderGoal(Dungeon currentDungeon) {
        super();
    }

    /**
     * 
     * @return "" if goal has been reached, ":boulder" otherwise.
     */
    public String evaluate(Dungeon currentDungeon) {
        /*
         * Check that all boulders are on top of all switches i.e. all switches are triggered. 
         * IF so return an empty string otherwise return a string with :boulders.
         */
        if (currentDungeon.getEntities().stream().filter(FloorSwitch.class::isInstance).allMatch(e -> ((FloorSwitch) e).isTriggered())) {
            this.isCompleted = true;
            return "";
        }
        this.isCompleted = false;

        int numLeft = (int) currentDungeon.getEntities().stream()
            .filter(FloorSwitch.class::isInstance)
            .filter(e -> !((FloorSwitch) e).isTriggered())
            .count();
        return ":boulders" + "/" + numLeft + " :switch left";
    }

    public boolean onlyExitLeft() {
        return false;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }
}
