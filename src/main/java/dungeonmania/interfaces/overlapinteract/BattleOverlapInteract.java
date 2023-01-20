package dungeonmania.interfaces.overlapinteract;

import java.io.Serializable;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.battles.Battle;
import dungeonmania.entity.inventoryitem.collectableitem.InvisibilityPotion;
import dungeonmania.entity.movingentity.Mercenary;
import dungeonmania.entity.movingentity.MovingEntity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;
import dungeonmania.util.Direction;

public class BattleOverlapInteract implements OverlapInteract, Serializable, UsesDungeon {
    private Dungeon dungeon;

    public BattleOverlapInteract(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    private boolean checkMerc(Entity entity) {
        if (entity instanceof Mercenary) {
            Mercenary merc = (Mercenary) entity;
            if (merc.getBribed() || merc.getMindControlled()) {
                // movement handled in player movement
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean overlapInteract(Entity entity, Entity incomingEntity, Direction movementDirection) {
        Player player = null;
        MovingEntity enemy = null;
        if (entity instanceof Player && incomingEntity instanceof Player) {
            if (entity.getType().equals("older_player")) {
                player = (Player) incomingEntity;
                enemy = (MovingEntity) entity;
            } else {
                player = (Player) entity;
                enemy = (MovingEntity) incomingEntity;
            }
            Player olderPlayer = (Player) enemy;
            if (olderPlayer.bypassPlayerFight() || player.bypassPlayerFight()) {
                incomingEntity.setPosition(entity.getPosition());
                return true;
            }
        } else if (entity instanceof MovingEntity && incomingEntity instanceof Player) {
            player = (Player) incomingEntity;
            enemy = (MovingEntity) entity;
        } else if (entity instanceof Player && incomingEntity instanceof MovingEntity) {
            player = (Player) entity;
            enemy = (MovingEntity) incomingEntity;
        }

        if (checkMerc(enemy)) return true;
        incomingEntity.setPosition(entity.getPosition());

        if (player != null && player.getCurrentPotion() instanceof InvisibilityPotion) {
            return true;
        } 

        if (player != null) {
            Battle battle = new Battle(player, enemy, dungeon);
            battle.battleTrigger();
        }

        return true;
    }
}
