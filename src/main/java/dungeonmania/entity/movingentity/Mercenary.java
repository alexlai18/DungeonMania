package dungeonmania.entity.movingentity;

import dungeonmania.interfaces.ally.Ally;
import dungeonmania.interfaces.interact.Interact;
import dungeonmania.interfaces.movement.MercenaryMovement;
import dungeonmania.interfaces.movement.NoMovement;
import dungeonmania.interfaces.movement.ZombieToastMovement;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import java.util.List;
import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.buildableitem.Sceptre;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;


public class Mercenary extends MovingEntity implements Interact, PotionEffects, Ally {
    private int bribeRadius;
    private int bribeAmount;
    private boolean bribed = false;
    private boolean mindControlled = false;

    public Mercenary(Position position, List<Entity> adjacentEntities, Config config, Dungeon dungeon, String id) {
        super(position, adjacentEntities, config.getMercenaryAttack(), config.getMercenaryHealth(), id, dungeon);
        setMovement(new MercenaryMovement(dungeon));
        this.bribeRadius = config.getBribeRadius();
        this.bribeAmount = config.getBribeAmount();
        setType("mercenary");
    }

    public boolean getBribed() {
        return bribed;
    }

    public void setBribed(boolean bribed) {
        this.bribed = bribed;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public boolean getMindControlled() {
        return this.mindControlled;
    }

    public void setMindControlled(Boolean b) {
        this.mindControlled = b;
    }

    public void invincibilityEffects(Dungeon dungeon) {
        // handled in mercenary already
    }

    @Override
    public void interact(Player player) throws InvalidActionException {
        if (player.getInventoryList().stream().anyMatch(Sceptre.class :: isInstance)) {
            player.mindControl(this, true);
        } else {
            player.bribe(this);
        }
    }   
    
    @Override
    public void invisibilityEffects(Dungeon dungeon) {
        if (this.getMovement() instanceof NoMovement) return;
        if (dungeon.getPlayerInvisibility()) {
            setMovement(new ZombieToastMovement());
        } else {
            setMovementToOriginal(dungeon);
        }
    }


    @Override
    public EntityResponse getEntityResponse() {
        return bribed 
            ? new EntityResponse(getId(), getType(), getPosition(), false)
            : new EntityResponse(getId(), getType(), getPosition(), true);
    }

    @Override
    public void setMovementToOriginal(Dungeon dungeon) {
        setMovement(new MercenaryMovement(dungeon));
    }
}


