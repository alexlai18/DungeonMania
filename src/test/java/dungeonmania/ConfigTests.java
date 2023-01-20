package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.config.Config;

/**
 * Tests that ensure that the config class pulls in configuration data
 * accurately and appropriately for ALL configurations.
 */
public class ConfigTests {

    @Test
    @DisplayName("Test bomb_radius_2.json loading for config")
    public void testBombRadius2() {
        Config newConfig = new Config("bomb_radius_2");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 2);
        assertEquals(newConfig.getBowDurability(), 1);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 1);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 1);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 1);
        assertEquals(newConfig.getSpiderAttack(), 1);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 10);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 1);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 1);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 1);

    }

    @Test
    @DisplayName("Test bribe_amount_3.json loading for config")
    public void testBribeAmount3() {
        Config newConfig = new Config("bribe_amount_3");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 2);
        assertEquals(newConfig.getBowDurability(), 1);
        assertEquals(newConfig.getBribeAmount(), 3);
        assertEquals(newConfig.getBribeRadius(), 3);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 1);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 1);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 1);
        assertEquals(newConfig.getSpiderAttack(), 1);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 10);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 1);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 1);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 1);
    }

    @Test
    @DisplayName("Test bribe_radius_1.json loading for config")
    public void testBribeRadius1() {
        Config newConfig = new Config("bribe_radius_1");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 2);
        assertEquals(newConfig.getBowDurability(), 1);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 3);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 1);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 1);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 1);
        assertEquals(newConfig.getSpiderAttack(), 1);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 10);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 1);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 1);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 1);
    }

    @Test
    @DisplayName("Test no_spider_spawning.json loading for config")
    public void testNoSpiderSpawning() {
        Config newConfig = new Config("no_spider_spawning");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 1);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 1);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 1);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 1);
        assertEquals(newConfig.getSpiderAttack(), 1);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 1);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 1);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 1);
    }

    @Test
    @DisplayName("Test no_zombie_spawning.json loading for config")
    public void testNoZombieSpawning() {
        Config newConfig = new Config("no_zombie_spawning");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 1);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 1);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 1);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 1);
        assertEquals(newConfig.getSpiderAttack(), 1);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 10);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 1);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 1);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
    }

    @Test
    @DisplayName("Test simple.json loading for config")
    public void testSimple() {
        Config newConfig = new Config("simple");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 1);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 1);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 1);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 1);
        assertEquals(newConfig.getSpiderAttack(), 1);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 10);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 1);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 1);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 10);
    }


    @Test
    @DisplayName("Test M3_config.json loading for config")
    public void testM3() {
        Config newConfig = new Config("M3_config");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 2);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 5);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 10);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 2);
        assertEquals(newConfig.getSpiderAttack(), 5);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 2);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 5);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
        assertEquals(newConfig.getAssassinAttack(), 10);
        assertEquals(newConfig.getAssassinBribeAmount(),1);
        assertEquals(newConfig.getAssassinBribeFailRate(), 0.3);
        assertEquals(newConfig.getAssassinHealth(), 10);
        assertEquals(newConfig.getAssassinReconRadius(), 5);
        assertEquals(newConfig.getHydraAttack(), 10);
        assertEquals(newConfig.getHydraHealth(), 10);
        assertEquals(newConfig.getHydraHealthIncreaseRate(), 0.5);
        assertEquals(newConfig.getHydraHealthIncreaseAmount(), 1);
        assertEquals(newConfig.getMindControlDuration(), 3);
        assertEquals(newConfig.getMidnightArmourAttack(), 2);
        assertEquals(newConfig.getMidnightArmourDefence(), 2);
        
    }

    @Test
    @DisplayName("Test M3_op_merc.json loading for config")
    public void testM3OpMerc() {
        Config newConfig = new Config("M3_op_merc");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 2);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 0);
        assertEquals(newConfig.getMercenaryHealth(), 10000);
        assertEquals(newConfig.getPlayerAttack(), 10);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 2);
        assertEquals(newConfig.getSpiderAttack(), 5);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 2);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 5);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
        assertEquals(newConfig.getAssassinAttack(), 10);
        assertEquals(newConfig.getAssassinBribeAmount(),1);
        assertEquals(newConfig.getAssassinBribeFailRate(), 0.3);
        assertEquals(newConfig.getAssassinHealth(), 10);
        assertEquals(newConfig.getAssassinReconRadius(), 5);
        assertEquals(newConfig.getHydraAttack(), 10);
        assertEquals(newConfig.getHydraHealth(), 10);
        assertEquals(newConfig.getHydraHealthIncreaseRate(), 0.5);
        assertEquals(newConfig.getHydraHealthIncreaseAmount(), 1);
        assertEquals(newConfig.getMindControlDuration(), 3);
        assertEquals(newConfig.getMidnightArmourAttack(), 2);
        assertEquals(newConfig.getMidnightArmourDefence(), 2);
    }

    @Test
    @DisplayName("Test health_decrease.json loading for config")
    public void testHealthDecrease() {
        Config newConfig = new Config("health_decrease");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 2);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 5);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 10);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 2);
        assertEquals(newConfig.getSpiderAttack(), 5);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 2);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 5);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
        assertEquals(newConfig.getAssassinAttack(), 10);
        assertEquals(newConfig.getAssassinBribeAmount(),1);
        assertEquals(newConfig.getAssassinBribeFailRate(), 0.3);
        assertEquals(newConfig.getAssassinHealth(), 10);
        assertEquals(newConfig.getAssassinReconRadius(), 5);
        assertEquals(newConfig.getHydraAttack(), 10);
        assertEquals(newConfig.getHydraHealth(), 10);
        assertEquals(newConfig.getHydraHealthIncreaseRate(), 0);
        assertEquals(newConfig.getHydraHealthIncreaseAmount(), 1);
        assertEquals(newConfig.getMindControlDuration(), 3);
        assertEquals(newConfig.getMidnightArmourAttack(), 2);
        assertEquals(newConfig.getMidnightArmourDefence(), 2);
    }

    @Test
    @DisplayName("Test health_increase.json loading for config")
    public void testHealthIncrease() {
        Config newConfig = new Config("health_increase");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 2);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 5);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 10);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 2);
        assertEquals(newConfig.getSpiderAttack(), 5);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 2);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 5);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
        assertEquals(newConfig.getAssassinAttack(), 10);
        assertEquals(newConfig.getAssassinBribeAmount(),1);
        assertEquals(newConfig.getAssassinBribeFailRate(), 0.3);
        assertEquals(newConfig.getAssassinHealth(), 10);
        assertEquals(newConfig.getAssassinReconRadius(), 5);
        assertEquals(newConfig.getHydraAttack(), 10);
        assertEquals(newConfig.getHydraHealth(), 10);
        assertEquals(newConfig.getHydraHealthIncreaseRate(), 1);
        assertEquals(newConfig.getHydraHealthIncreaseAmount(), 1);
        assertEquals(newConfig.getMindControlDuration(), 3);
        assertEquals(newConfig.getMidnightArmourAttack(), 2);
        assertEquals(newConfig.getMidnightArmourDefence(), 2);
    }

    @Test
    @DisplayName("Test assassin_bribe.json loading for config")
    public void testAssassinBribe() {
        Config newConfig = new Config("assassin_bribe");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 2);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 5);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 10);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 2);
        assertEquals(newConfig.getSpiderAttack(), 5);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 2);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 5);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
        assertEquals(newConfig.getAssassinAttack(), 10);
        assertEquals(newConfig.getAssassinBribeAmount(),1);
        assertEquals(newConfig.getAssassinBribeFailRate(), 0);
        assertEquals(newConfig.getAssassinHealth(), 10);
        assertEquals(newConfig.getAssassinReconRadius(), 5);
        assertEquals(newConfig.getHydraAttack(), 10);
        assertEquals(newConfig.getHydraHealth(), 10);
        assertEquals(newConfig.getHydraHealthIncreaseRate(), 0);
        assertEquals(newConfig.getHydraHealthIncreaseAmount(), 1);
        assertEquals(newConfig.getMindControlDuration(), 3);
        assertEquals(newConfig.getMidnightArmourAttack(), 2);
        assertEquals(newConfig.getMidnightArmourDefence(), 2);
    }

    @Test
    @DisplayName("Test assassin_bribe_fail.json loading for config")
    public void testAssassinBribeFail() {
        Config newConfig = new Config("assassin_bribe_fail");
        // assert all fields in config are as expected
        assertEquals(newConfig.getAllyAttack(), 3);
        assertEquals(newConfig.getAllyDefence(), 3);
        assertEquals(newConfig.getBombRadius(), 1);
        assertEquals(newConfig.getBowDurability(), 2);
        assertEquals(newConfig.getBribeAmount(), 1);
        assertEquals(newConfig.getBribeRadius(), 1);
        assertEquals(newConfig.getEnemyGoal(), 1);
        assertEquals(newConfig.getInvincibilityPotionDuration(), 1);
        assertEquals(newConfig.getInvisibilityPotionDuration(), 1);
        assertEquals(newConfig.getMercenaryAttack(), 5);
        assertEquals(newConfig.getMercenaryHealth(), 5);
        assertEquals(newConfig.getPlayerAttack(), 10);
        assertEquals(newConfig.getPlayerHealth(), 10);
        assertEquals(newConfig.getShieldDefence(), 1);
        assertEquals(newConfig.getShieldDurability(), 2);
        assertEquals(newConfig.getSpiderAttack(), 5);
        assertEquals(newConfig.getSpiderHealth(), 5);
        assertEquals(newConfig.getSpiderSpawnRate(), 0);
        assertEquals(newConfig.getSwordAttack(), 2);
        assertEquals(newConfig.getSwordDurability(), 2);
        assertEquals(newConfig.getTreasureGoal(), 1);
        assertEquals(newConfig.getZombieAttack(), 5);
        assertEquals(newConfig.getZombieHealth(), 5);
        assertEquals(newConfig.getZombieSpawnRate(), 0);
        assertEquals(newConfig.getAssassinAttack(), 10);
        assertEquals(newConfig.getAssassinBribeAmount(),1);
        assertEquals(newConfig.getAssassinBribeFailRate(), 1);
        assertEquals(newConfig.getAssassinHealth(), 10);
        assertEquals(newConfig.getAssassinReconRadius(), 5);
        assertEquals(newConfig.getHydraAttack(), 10);
        assertEquals(newConfig.getHydraHealth(), 10);
        assertEquals(newConfig.getHydraHealthIncreaseRate(), 0);
        assertEquals(newConfig.getHydraHealthIncreaseAmount(), 1);
        assertEquals(newConfig.getMindControlDuration(), 3);
        assertEquals(newConfig.getMidnightArmourAttack(), 2);
        assertEquals(newConfig.getMidnightArmourDefence(), 2);
    }

    
}
