package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.goals.Goal;
import dungeonmania.goals.composite_goals.AndGoal;
import dungeonmania.goals.composite_goals.OrGoal;
import dungeonmania.goals.simple_goals.BoulderGoal;
import dungeonmania.goals.simple_goals.EnemyGoal;
import dungeonmania.goals.simple_goals.ExitGoal;
import dungeonmania.goals.simple_goals.TreasureGoal;
import dungeonmania.util.FileLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests that make sure goals are imported correctrly.
 */
public class GoalTests {
    @Test
    @DisplayName("Test 2_doors.json loading for goals")
    public void test2Doors() {
        Dungeon currentDungeon = new Dungeon("2_doors", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test advanced.json loading for goals")
    public void testAdvanced() {
        Dungeon currentDungeon = new Dungeon("advanced", "simple", 1);
        
        JSONObject dungeon;
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/advanced.json"));
            JSONObject goalJsonObject = dungeon.getJSONObject("goal-condition");
            Goal newGoal = new AndGoal(currentDungeon, goalJsonObject);
            assertTrue(
                newGoal.evaluate(currentDungeon).contains(":enemies")
                && newGoal.evaluate(currentDungeon).contains("AND")
                && newGoal.evaluate(currentDungeon).contains(":treasure")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    @DisplayName("Test bombs.json loading for goals")
    public void testBombs() {
        Dungeon currentDungeon = new Dungeon("bombs", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test boulders.json loading for goals")
    public void testBoulders() {
        Dungeon currentDungeon = new Dungeon("boulders", "simple", 1);
        Goal newGoal = new BoulderGoal(currentDungeon);

        assertTrue(newGoal.evaluate(currentDungeon).contains(":boulders"));
    }

    @Test
    @DisplayName("Test build_bow.json loading for goals")
    public void testBuildBow() {
        Dungeon currentDungeon = new Dungeon("build_bow", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test build_shield.json loading for goals")
    public void testBuildShield() {
        Dungeon currentDungeon = new Dungeon("build_shield", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test exit_goal_order.json loading for goals")
    public void testExitGoalOrder() {
        Dungeon currentDungeon = new Dungeon("exit_goal_order", "simple", 1);
        
        JSONObject dungeon;
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/exit_goal_order.json"));
            JSONObject goalJsonObject = dungeon.getJSONObject("goal-condition");
            Goal newGoal = new AndGoal(currentDungeon, goalJsonObject);
            assertTrue(
                newGoal.evaluate(currentDungeon).contains(":boulders")
                && newGoal.evaluate(currentDungeon).contains("AND")
                && newGoal.evaluate(currentDungeon).contains(":exit")
                && newGoal.evaluate(currentDungeon).contains(":treasure")
            );

        } catch (JSONException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    @DisplayName("Test maze.json loading for goals")
    public void testMaze() {
        Dungeon currentDungeon = new Dungeon("maze", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test mercenary.json loading for goals")
    public void testMercenary() {
        Dungeon currentDungeon = new Dungeon("mercenary", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test portals_advanced.json loading for goals")
    public void testPortalsAdvanced() {
        Dungeon currentDungeon = new Dungeon("portals_advanced", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test portals.json loading for goals")
    public void testPortals() {
        Dungeon currentDungeon = new Dungeon("portals", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test zombies.json loading for goals")
    public void testZombies() {
        Dungeon currentDungeon = new Dungeon("zombies", "simple", 1);
        Goal newGoal = new ExitGoal(null);

        assertTrue(newGoal.evaluate(currentDungeon).equals(":exit"));
    }

    @Test
    @DisplayName("Test advanced.json loading for goals but with OR")
    public void testAdvancedOR() {
        Dungeon currentDungeon = new Dungeon("advanced_or", "simple", 1);
        
        JSONObject dungeon;
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/advanced_or.json"));
            JSONObject goalJsonObject = dungeon.getJSONObject("goal-condition");
            Goal newGoal = new OrGoal(currentDungeon, goalJsonObject);
            assertTrue(
                newGoal.evaluate(currentDungeon).contains(":enemies")
                && newGoal.evaluate(currentDungeon).contains("OR")
                && newGoal.evaluate(currentDungeon).contains(":treasure")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    @DisplayName("Test mercenary.json loading for goals but with a treasure goal")
    public void testMercenaryTreasure() {
        Dungeon currentDungeon = new Dungeon("mercenary_treasure_goal", "simple", 1);
        TreasureGoal newGoal = new TreasureGoal(currentDungeon.getConfig());

        assertTrue(newGoal.evaluate(currentDungeon).contains(":treasure"));
        assertEquals(1, newGoal.getNumberofTreasureToGet());
    }

    @Test
    @DisplayName("Test zombies.json loading for goals but with an enemy goal")
    public void testZombiesEnemy() {
        Dungeon currentDungeon = new Dungeon("zombies_enemy_goal", "simple", 1);
        EnemyGoal newGoal = new EnemyGoal(currentDungeon.getConfig());

        assertTrue(newGoal.evaluate(currentDungeon).contains(":enemies"));
        assertEquals(1, newGoal.getNumberOfEnemiesToKill());
    }
}
