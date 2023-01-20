package dungeonmania.entity.battles;

import dungeonmania.entity.inventoryitem.InventoryItem;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.collectableitem.InvincibilityPotion;
import dungeonmania.entity.movingentity.Hydra;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.response.models.ItemResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rounds implements Serializable {
    private Battle battle;
    private Dungeon dungeon; 
    private double initialPlayerHealth;
    private double initialEnemyHealth;
    private double finalPlayerHealth;
    private double finalEnemyHealth;


    public Rounds(Battle battle) {
        this.initialPlayerHealth = battle.getPlayerHealth();
        this.initialEnemyHealth = battle.getEnemyHealth();
        this.battle = battle;
    }

    public void fight() {
        // TODO: avoid using instanceof and have a fight override for moving entity classes polymorphism
        if (battle.getPlayer().getCurrentPotion() instanceof InvincibilityPotion) {
            battle.setEnemyHealth(0);
        } else if (battle.getEnemy() instanceof Hydra) {
            Random rand = new Random();
            if (rand.nextDouble() <= dungeon.getConfig().getHydraHealthIncreaseRate()) {
                battle.setEnemyHealth(battle.getEnemyHealth() + dungeon.getConfig().getHydraHealthIncreaseAmount());
                battle.setPlayerHealth(battle.getPlayerHealth() - ((battle.getEnemyAttack() - battle.getPlayerDefence()) / 10));
            } else {
                battle.setEnemyHealth(battle.getEnemyHealth() - (battle.getPlayerAttack() / 5));
                battle.setPlayerHealth(battle.getPlayerHealth() - ((battle.getEnemyAttack() - battle.getPlayerDefence()) / 10));
            }
        } else if (battle.getEnemy() instanceof Player) {
            battle.setEnemyHealth(battle.getEnemyHealth() - ((battle.getPlayerAttack() - battle.getEnemyDefence()) / 10));
            battle.setPlayerHealth(battle.getPlayerHealth() - ((battle.getEnemyAttack() - battle.getPlayerDefence()) / 10));
        } else {
            battle.setEnemyHealth(battle.getEnemyHealth() - (battle.getPlayerAttack() / 5));
            battle.setPlayerHealth(battle.getPlayerHealth() - ((battle.getEnemyAttack() - battle.getPlayerDefence()) / 10));
        }

        if (battle.getPlayerHealth() <= 0) {
            removePlayer(battle.getPlayer());
        }

        if (battle.getEnemyHealth() <= 0) {
            if (battle.getEnemy() instanceof Player) {
                removePlayer((Player) battle.getEnemy());
            }
            dungeon.removeEntity(battle.getEnemy());
            battle.getPlayer().addKill();
        }

        this.finalPlayerHealth = battle.getPlayerHealth();
        this.finalEnemyHealth = battle.getEnemyHealth();
    }

    public RoundResponse roundResponseCreate() {
        return new RoundResponse(finalPlayerHealth - initialPlayerHealth, finalEnemyHealth - initialEnemyHealth, makeWeaponList(battle.getPlayerWeaponry()));
    }

    public List<ItemResponse> makeWeaponList(List<InventoryItem> weaponry) {
        List<ItemResponse> items = new ArrayList<>();
        for (InventoryItem weapon : weaponry) {
            items.add(itemResponseCreate(weapon));
        }

        if (battle.getPlayerPotion() != null) {
            items.add(itemResponseCreate(battle.getPlayerPotion()));
        }

        return items;
    }

    public ItemResponse itemResponseCreate(InventoryItem item) {
        return new ItemResponse(item.getId(), item.getType());
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    private void removePlayer(Player player) {
        if (player.getType().equals("older_player")) {
            dungeon.setOldPlayer(null);
        } else {
            dungeon.setPlayer(null);
        }
    }
}