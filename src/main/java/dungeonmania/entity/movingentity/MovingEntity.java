package dungeonmania.entity.movingentity;

import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.interfaces.movement.Movement;
import dungeonmania.interfaces.overlapinteract.BattleOverlapInteract;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
    private Movement movement;
    private double health;
    private double attack;

    public MovingEntity(Position position, List<Entity> adjacentEntities, int attack, int health, String id, Dungeon dungeon) {
        super(position, adjacentEntities, id);
        this.health = health;
        this.attack = attack;
        setOverlapInteract(new BattleOverlapInteract(dungeon));
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public Movement getMovement() {
        return movement; 
    }

    public double getAttack() {
        return attack;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public void move(Direction movementDirection) {
        this.movement.move(this, movementDirection);
    }

    public abstract void setMovementToOriginal(Dungeon dungeon);
}
