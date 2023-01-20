package dungeonmania.goals.simple_goals;

import java.io.Serializable;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.staticentity.*;
import dungeonmania.goals.Goal;

public class EnemyGoal implements Goal, Serializable {
    
    /**
     * Whether the Goal is completed.
     */
    private boolean isCompleted = false;
    
    /**
     * Number of Enemies To Kill.
     */
    private final Integer numberOfEnemiesToKill;

    /**
     * 
     * @param config Configuration file sets the number of enemies to kill.
     */
    public EnemyGoal(Config config) {
        this.numberOfEnemiesToKill = config.getEnemyGoal();
    }

    /**
     * 
     * @return True if goal has been reached, false otherwise.
     */
    public String evaluate(Dungeon currentDungeon) {
        boolean noSpawners = !currentDungeon.getEntities().stream().anyMatch(e -> e instanceof ZombieToastSpawner);
        boolean numKilled = currentDungeon.getKills() >= numberOfEnemiesToKill;
        if (noSpawners && numKilled) {
            this.isCompleted = true;
            return "";
        }
        this.isCompleted = false;

        int numLeft = (int) currentDungeon.getEntities().stream()
            .filter(ZombieToastSpawner.class::isInstance)
            .count()
            +
            (numberOfEnemiesToKill - currentDungeon.getKills());
        return numLeft + ":enemies kills left";
    }

    public boolean onlyExitLeft() {
        return false;
    }

    public Integer getNumberOfEnemiesToKill() {
        return numberOfEnemiesToKill;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }
}
