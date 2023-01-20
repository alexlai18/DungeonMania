package dungeonmania.entity.battles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.RoundResponse;

import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.movingentity.player.*;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.inventoryitem.InventoryItem;
import dungeonmania.entity.inventoryitem.Weapon;

public class Battle implements Serializable {
    private Player player;
    private double initialPlayerHealth;
    private double initialEnemyHealth;
    private MovingEntity enemy;
    private List<Rounds> rounds = new ArrayList<>();
    private Dungeon dungeon;

    public Battle(Player player, MovingEntity enemy, Dungeon dungeon) {
        this.player = player;
        this.initialPlayerHealth = player.getHealth();
        this.initialEnemyHealth = enemy.getHealth();
        this.enemy = enemy;
        this.rounds = new ArrayList<>();
        this.dungeon = dungeon;
    }

    public void battleTrigger() {
        for (InventoryItem item : getPlayerWeaponry()) {
            Weapon weapon = (Weapon) item;
            weapon.use();
        }

        if (enemy.getType().equals("older_player")) {
            while (dungeon.getOlderPlayer() != null && this.player != null) {
                Rounds round = new Rounds(this);
                round.setDungeon(dungeon);
                round.fight();
                addRound(round);
            }
        } else {
            while (dungeon.getEntities().contains(enemy) && dungeon.getPlayer() != null) {
                Rounds round = new Rounds(this);
                round.setDungeon(dungeon);
                round.fight();
                addRound(round);
            }
        }
        getPlayer().checkWeaponDurability();

        dungeon.addBattle(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public MovingEntity getEnemy() {
        return this.enemy;
    }

    public double getPlayerHealth() {
        return getPlayer().getHealth();
    }

    public void setPlayerHealth(double health) {
        getPlayer().setHealth(health);
    }
    
    public double getEnemyHealth() {
        return getEnemy().getHealth();
    }

    public void setEnemyHealth(double health) {
        getEnemy().setHealth(health);
    }

    public double getEnemyAttack() {
        return getEnemy().getAttack();
    }

    public double getEnemyDefence() {
        if (!(getEnemy() instanceof Player)) return 0;
        return ((Player) getEnemy()).getDefence();
    }

    public double getPlayerDefence() {
        return getPlayer().getDefence();
    }

    public List<InventoryItem> getPlayerWeaponry() {
        return getPlayer().getWeaponryList();
    }

    public double getPlayerAttack() {
        return getPlayer().getAttack();
    }

    public InventoryItem getPlayerPotion() {
        return player.getCurrentPotion();
    }

    public void addRound(Rounds round) {
        this.rounds.add(round);
    }

    public BattleResponse getBattleResponse() {
        return new BattleResponse(enemy.getType(), roundResponses(), initialPlayerHealth, initialEnemyHealth);
    }

    public List<RoundResponse> roundResponses() {
        List<RoundResponse> roundResponses = new ArrayList<>();
        for (Rounds round : rounds) {
            roundResponses.add(round.roundResponseCreate());
        }
       return roundResponses;
    }
}
