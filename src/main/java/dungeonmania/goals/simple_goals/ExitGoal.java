package dungeonmania.goals.simple_goals;

import dungeonmania.goals.Goal;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.Exit;


public class ExitGoal implements Goal, Serializable {

    /**
     * Whether the Goal is completed.
     */
    private boolean isCompleted = false;
    
    /**
     * The super-goal holding this goal.
     * Null if no super-goal
     */
    private Goal container;

    public ExitGoal(Goal container) {
        super();
        this.container = container;
    }

    /**
     * 
     * @return Empty string if goal has been reached, false otherwise.
     */
    public String evaluate(Dungeon currentDungeon) {
        /*
         * check only exit left from our container goal.
         * If null it means that we don't have a container and this is the only goal.
         * If yes continue and evaluate otherwise return false.
         */
        if (container == null || container.onlyExitLeft()) {
            List<Entity> exits = currentDungeon.getEntities().stream().filter(e -> e instanceof Exit).collect(Collectors.toList());
            Player player = currentDungeon.getPlayer();
            if (player == null) return ":exit";
            if (exits.stream().anyMatch(exit -> exit.getPosition().equals(player.getPosition()))) {
                this.isCompleted = true;
                return "";
            } 
        }
        this.isCompleted = false;
        return ":exit";
    }

    public boolean onlyExitLeft() {
        return true;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }
}
