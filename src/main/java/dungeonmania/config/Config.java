package dungeonmania.config;

import java.io.IOException;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.util.FileLoader;

public class Config implements Serializable {

    /**
     * Attack bonus each ally gives to the player.
     */
    private int allyAttack = 0;

    /**
     * Reduction in effect of enemy attack damage each ally gives to the player.
     */
    private int allyDefence = 0;

    /**
     * Blast radius of bomb.
     */
    private int bombRadius = 0;

    /**
     * The number of battles that the bow lasts for.
     */
    private int bowDurability = 0;

    /**
     * Amount of gold required to bribe a mercenary.
     */
    private int bribeAmount = 0;

    /**
     * Radius in which a mercenary can be bribed.
     */
    private int bribeRadius = 0;
    
    /**
     * At least x enemies must be killed to complete the enemy goal
     */
    private int enemyGoal = 0;

    /**
     * The effects of the potion only last for x ticks.
     */
    private int invincibilityPotionDuration = 0;

    /**
     * The effects of the potion only last for x ticks.
     */
    private int invisibilityPotionDuration = 0;

    /**
     * Attack damage of the mercenary.
     */
    private int mercenaryAttack = 0;

    /**
     * Health of the mercenary.
     */
    private int mercenaryHealth = 0;

    /**
     * Attack damage of the character.
     */
    private int playerAttack = 0;

    /**
     * Health of the character.
     */
    private int playerHealth = 0;

    /**
     * The reduction in the effect of the attack damage of the enemy as a result of the shield.
     */
    private int shieldDefence = 0;

    /**
     * The number of battles that the shield lasts for.
     */
    private int shieldDurability = 0;

    /**
     * Attack damage of the spider.
     */
    private int spiderAttack = 0;

    /**
     * Health of the spider.
     */
    private int spiderHealth = 0;

    /**
     * Spiders spawn every x ticks, starting from the x'th tick. 
     * Spawn rate of 0 means that spiders will never spawn in the game.
     */
    private int spiderSpawnRate = 0;

    /**
     * Amount of damage added to a players' attack damage when they use a sword in battle.
     */
    private int swordAttack = 0;

    /**
     * The number of battles that the sword lasts for.
     */
    private int swordDurability = 0;

    /**
     * At least x treasure must be collected to complete the treasure goal
     */
    private int treasureGoal = 0;

    /**
     * Attack damage of the zombie toast.
     */
    private int zombieAttack = 0;

    /**
     * Health of the zombie toast.
     */
    private int zombieHealth = 0;

    /**
     * Zombies spawn every x ticks from each spawner, starting from the x'th tick.
     * Spawn rate of 0 means that zombies will never spawn in the game.
     */
    private int zombieSpawnRate = 0;
    /*
     * Attack damage of the assassin.
     */
    private int assassinAttack = 0;

    /*
     * The amount of gold required to perform an attampt to bribe an assassin.
     */
    private int assassinBribeAmount = 0; 
    
    /*
     * The chance that the bribe on an assassin will fail. The value of this field should be always inclusively between 0 and 1.
     */
    private double assassinBribeFailRate = 0; 

    /*
     * Health of the assassin.
     */
    private int assassinHealth = 0; 

    /*
     * The radius within which an assassin can see and move towards the player even when they are invisible.
     */
    private int assassinReconRadius = 0;

    /*
     * Attack damage of the hydra.
     */
    private int hydraAttack = 0;

    /*
     * Health of the hydra.
     */
    private int hydraHealth = 0;

    /*
     * The chance that the health of a Hydra increases when it gets attacked. The value of this field should be always inclusively between 0 and 1.
     */
    private double hydraHealthIncreaseRate = 0; 

    /*
     * The increment on the health of a Hydra increases when it gets attacked.
     */
    private int hydraHealthIncreaseAmount = 0; 

    /*
     * The amount of time mind controlling via a sceptre lasts for.
     */
    private int mindControlDuration = 0; 

    /*
     * Attack bonus wearing midnight armour gives to the player
     */
    private int midnightArmourAttack = 0; 

    /*
     * Defence bonus wearing midnight armour gives to the player.
     */
    private int midnightArmourDefence = 0; 

    /**
     * 
     * @param configName The name of the configuration file we're using.
     */
    public Config(String configName) {
        try {
            JSONObject config = new JSONObject(FileLoader.loadResourceFile("/configs/" + configName + ".json"));
            this.allyAttack = config.getInt("ally_attack");
            this.allyDefence = config.getInt("ally_defence");
            this.bombRadius = config.getInt("bomb_radius");
            this.bowDurability = config.getInt("bow_durability");
            this.bribeAmount = config.getInt("bribe_amount");
            this.bribeRadius = config.getInt("bribe_radius");
            this.enemyGoal = config.getInt("enemy_goal");
            this.invincibilityPotionDuration = config.getInt("invincibility_potion_duration");
            this.invisibilityPotionDuration = config.getInt("invisibility_potion_duration");
            this.mercenaryAttack = config.getInt("mercenary_attack");
            this.mercenaryHealth = config.getInt("mercenary_health");
            this.playerAttack = config.getInt("player_attack");
            this.playerHealth = config.getInt("player_health");
            this.shieldDefence = config.getInt("shield_defence");
            this.shieldDurability = config.getInt("shield_durability");
            this.spiderAttack = config.getInt("spider_attack");
            this.spiderHealth = config.getInt("spider_health");
            this.spiderSpawnRate = config.getInt("spider_spawn_rate");
            this.swordAttack = config.getInt("sword_attack");
            this.swordDurability = config.getInt("sword_durability");
            this.treasureGoal = config.getInt("treasure_goal");
            this.zombieAttack = config.getInt("zombie_attack");
            this.zombieHealth = config.getInt("zombie_health");
            this.zombieSpawnRate = config.getInt("zombie_spawn_rate");
            this.assassinAttack = config.getInt("assassin_attack");
            this.assassinBribeAmount = config.getInt("assassin_bribe_amount");
            this.assassinBribeFailRate = config.getDouble("assassin_bribe_fail_rate");
            this.assassinHealth = config.getInt("assassin_health");
            this.assassinReconRadius = config.getInt("assassin_recon_radius");
            this.hydraAttack = config.getInt("hydra_attack");
            this.hydraHealth = config.getInt("hydra_health");
            this.hydraHealthIncreaseRate = config.getDouble("hydra_health_increase_rate");
            this.hydraHealthIncreaseAmount = config.getInt("hydra_health_increase_amount");
            this.mindControlDuration = config.getInt("mind_control_duration");
            this.midnightArmourAttack = config.getInt("midnight_armour_attack");
            this.midnightArmourDefence = config.getInt("midnight_armour_defence");

        } catch (JSONException e) {
            // handle exception       
        } catch (IOException e) {
            // handle exception
        }
    }
    
    public int getAllyAttack() {
        return allyAttack;
    }

    public int getAllyDefence() {
        return allyDefence;
    }
    
    public int getBombRadius() {
        return bombRadius;
    }

    public int getBowDurability() {
        return bowDurability;
    }
    
    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public int getEnemyGoal() {
        return enemyGoal;
    }

    public int getInvincibilityPotionDuration() {
        return invincibilityPotionDuration;
    }

    public int getInvisibilityPotionDuration() {
        return invisibilityPotionDuration;
    }

    public int getMercenaryAttack() {
        return mercenaryAttack;
    }

    public int getMercenaryHealth() {
        return mercenaryHealth;
    }

    public int getPlayerAttack() {
        return playerAttack;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getShieldDefence() {
        return shieldDefence;
    }
    
    public int getShieldDurability() {
        return shieldDurability;
    }

    public int getSpiderAttack() {
        return spiderAttack;
    }

    public int getSpiderHealth() {
        return spiderHealth;
    }

    public int getSpiderSpawnRate() {
        return spiderSpawnRate;
    }

    public int getSwordAttack() {
        return swordAttack;
    }

    public int getSwordDurability() {
        return swordDurability;
    }

    public int getTreasureGoal() {
        return treasureGoal;
    }

    public int getZombieAttack() {
        return zombieAttack;
    }

    public int getZombieHealth() {
        return zombieHealth;
    }

    public int getZombieSpawnRate() {
        return zombieSpawnRate;
    }

    public int getAssassinAttack() {
        return assassinAttack;
    }

    public int getAssassinBribeAmount() {
        return assassinBribeAmount;
    }

    public double getAssassinBribeFailRate() {
        return assassinBribeFailRate;
    }

    public int getAssassinHealth() {
        return assassinHealth;
    }

    public int getAssassinReconRadius() {
        return assassinReconRadius;
    }

    public int getHydraAttack() {
        return hydraAttack;
    }

    public int getHydraHealth() {
        return hydraHealth;
    }

    public double getHydraHealthIncreaseRate() {
        return hydraHealthIncreaseRate;
    }

    public int getHydraHealthIncreaseAmount() {
        return hydraHealthIncreaseAmount;
    }

    public int getMindControlDuration() {
        return mindControlDuration;
    }

    public int getMidnightArmourAttack() {
        return midnightArmourAttack;
    }

    public int getMidnightArmourDefence() {
        return midnightArmourDefence;
    }

}
