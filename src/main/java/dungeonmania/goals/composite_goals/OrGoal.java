package dungeonmania.goals.composite_goals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;

public class OrGoal extends CompositeGoal implements Goal {
    
    /**
     * Whether the Goal is completed.
     */
    private boolean isCompleted = false;

    public OrGoal(Dungeon currentDungeon, JSONObject goalJsonObject) {
        super();
        List<Goal> currentGoals = new ArrayList<Goal>();
        JSONArray goalsJsonArray = new JSONArray(goalJsonObject.getJSONArray("subgoals"));

        for (int i = 0; i < goalsJsonArray.length(); i++) {
            currentGoals.add(GoalFactory.createGoal(goalsJsonArray.getJSONObject(i).getString("goal"), goalsJsonArray.getJSONObject(i), currentDungeon, this));
        }

        setSubGoals(currentGoals);
    }

    /**
     * @return string showing status of goals.
     * @param currentDungeon showing the state of the current dungeon.
     */
    @Override
    public String evaluate(Dungeon currentDungeon) {
        // Iterate through goals and return an empty string if
        // a goal is true.
        for (Goal g : getSubGoals()) {
            if (g.evaluate(currentDungeon).equals("")) {
                this.isCompleted = true;
                return "";
            }
        }

        this.isCompleted = false;
        return this.stringGoalConstructor(currentDungeon);
    }

    private String stringGoalConstructor(Dungeon currentDungeon) {
        // Or Goal string
        // No need for an if statement inside to check whether a single goal is true
        // since if any goal was true we'd already return an empty string.
        String goalString = "";
        for (Goal g : getSubGoals()) {
            goalString += String.format("(%s) OR ", g.evaluate(currentDungeon));
        }
        
        // Remove the last " OR " and return it
        return goalString.substring(0, goalString.length() - 4);
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public boolean onlyExitLeft() {
        for (Goal g : getSubGoals()) {
            if (g.onlyExitLeft()) {
                return true;
            }
        }
        return false;
    }
}
