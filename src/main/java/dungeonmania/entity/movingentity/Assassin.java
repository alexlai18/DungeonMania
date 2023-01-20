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
import dungeonmania.dungeon.Dungeon;


public class Assassin extends MovingEntity implements Interact, PotionEffects, Ally {
    private int bribeRadius;
    private int bribeAmount;
    private int reconRadius;
    private double bribeFailRate; 
    private boolean bribed = false;
    private boolean mindControlled = false;

    public Assassin(Position position, List<Entity> adjacentEntities, Dungeon dungeon, String id) {
        super(position, adjacentEntities, dungeon.getConfig().getAssassinAttack(), dungeon.getConfig().getAssassinHealth(), id, dungeon);
        setMovement(new MercenaryMovement(dungeon));
        this.bribeRadius = dungeon.getConfig().getBribeRadius();
        this.bribeAmount = dungeon.getConfig().getAssassinBribeAmount();
        this.reconRadius = dungeon.getConfig().getAssassinReconRadius();
        this.bribeFailRate = dungeon.getConfig().getAssassinBribeFailRate();
        setType("assassin");
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

    public int getReconRadius() {
        return reconRadius;
    }

    public double getBribeFailRate() {
        return bribeFailRate;
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

        int lowLimitX = dungeon.getPlayer().getPosition().getX() - this.getReconRadius();
        int highLimitX = dungeon.getPlayer().getPosition().getX() + this.getReconRadius();
        int lowLimitY = dungeon.getPlayer().getPosition().getY() - this.getReconRadius();
        int highLimitY = dungeon.getPlayer().getPosition().getY() + this.getReconRadius();
        if (this.getMovement() instanceof NoMovement) return;
        
        if (dungeon.getPlayerInvisibility()) {
            if (this.getPosition().getX() >= lowLimitX && this.getPosition().getX() <= highLimitX && this.getPosition().getY() >= lowLimitY && this.getPosition().getY() <= highLimitY) {
                setMovementToOriginal(dungeon);
            } else {
                setMovement(new ZombieToastMovement());
            }
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


