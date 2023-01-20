package dungeonmania.goals.composite_goals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;

public class AndGoal extends CompositeGoal implements Goal {
    
    /**
     * Whether the Goal is completed.
     */
    private boolean isCompleted = false;
    
    public AndGoal(Dungeon currentDungeon, JSONObject goalJsonObject) {
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
     * @param currentDungeon the current state of the dungeon.
     */
    public String evaluate(Dungeon currentDungeon) {        

        // iterate through all goals and return string state of goals
        // as soon as a goal comes up as false.
        for (Goal g : getSubGoals()) {
            if (!g.evaluate(currentDungeon).equals("")) {
                this.isCompleted = false;
                return this.stringGoalConstructor(currentDungeon);
            }
        }

        // If we haven't found any goal with an incomplete condition, return an empty string
        // as all goals must be completed.
        this.isCompleted = true;
        return "";
    }
    
    private String stringGoalConstructor(Dungeon currentDungeon) {
        // AND Goal string
        String goalString = "";

        // Iterate through goals, if any are not completed, add them to show state.
        for (Goal g : getSubGoals()) {
            if (!g.evaluate(currentDungeon).equals("")) {
                goalString += String.format("(%s) AND ", g.evaluate(currentDungeon));
            }
        }

        // Remove the last " AND " and return it
        return goalString.substring(0, goalString.length() - 5);
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }
}
