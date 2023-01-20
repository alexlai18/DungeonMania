package dungeonmania.goals.simple_goals;

import java.io.Serializable;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.goals.Goal;


public class TreasureGoal implements Goal, Serializable {

    /**
     * Whether the Goal is completed.
     */
    private boolean isCompleted = false;

    /**
     * Number of treasure to get.
     */
    private final Integer numberofTreasureToGet;

    /**
     * 
     * @param config Configuration file sets the number of enemies to kill.
     */
    public TreasureGoal(Config config) {
        this.numberofTreasureToGet = config.getTreasureGoal();
    }

    /**
     * 
     * @return return "" if goal has been reached, ":treasure" otherwise.
     */
    public String evaluate(Dungeon currentDungeon) {
        int numTreasure = currentDungeon.getPlayer().getNumTreasure();
        if (numTreasure >= this.numberofTreasureToGet) {
            this.isCompleted = true;
            return "";
        }
        this.isCompleted = false;
        int numLeft = numberofTreasureToGet - numTreasure;
        return numLeft + ":treasure left";
    }

    public boolean onlyExitLeft() {
        return false;
    }

    public Integer getNumberofTreasureToGet() {
        return numberofTreasureToGet;
    }
    
    @Override
    public boolean isCompleted() {
        return isCompleted;
    }
}
