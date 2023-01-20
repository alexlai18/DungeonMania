package dungeonmania.goals;

import dungeonmania.dungeon.Dungeon;

public interface Goal {
    
    /**
     * Evaluates the goal's status
     * @param currentDungeon the Dungeon we're currently in
     * @param currentGoalStatus the current status of the goals
     * @return A string with the goal's status based on spec.
     */
    public abstract String evaluate(Dungeon currentDungeon);

    /**
     * Returns whether a goal and subgoals has only exits left.
     * @param currentDungeon the Dungeon we're currently in
     * @return whether there is only an exit left in true or false format.
     */
    public abstract boolean onlyExitLeft();
    
    /**
     * Returns whether a goal and subgoals has only exits left.
     * whether the goal is completed.
     * @return whether there is only an exit left in true or false format.
     */
    public abstract boolean isCompleted();
}
