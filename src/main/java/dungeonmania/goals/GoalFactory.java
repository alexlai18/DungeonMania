package dungeonmania.goals;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.goals.composite_goals.AndGoal;
import dungeonmania.goals.composite_goals.OrGoal;
import dungeonmania.goals.simple_goals.BoulderGoal;
import dungeonmania.goals.simple_goals.EnemyGoal;
import dungeonmania.goals.simple_goals.ExitGoal;
import dungeonmania.goals.simple_goals.TreasureGoal;

public final class GoalFactory {
    private GoalFactory() {
        super();
    }

    public static Goal createGoal(String goalType, JSONObject goalObject, Dungeon currentDungeon, Goal container) {
        switch(goalType) {
            case "enemies":
                return new EnemyGoal(currentDungeon.getConfig());
            case "boulders":
                return new BoulderGoal(currentDungeon);
            case "treasure":
                return new TreasureGoal(currentDungeon.getConfig());
            case "exit":
                return new ExitGoal(container);
            case "AND":
                return new AndGoal(currentDungeon, goalObject);
            case "OR":
                return new OrGoal(currentDungeon, goalObject);
        }
        return null;
    }
}
