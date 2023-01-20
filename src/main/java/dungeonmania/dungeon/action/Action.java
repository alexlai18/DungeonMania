package dungeonmania.dungeon.action;

import java.io.Serializable;

import dungeonmania.util.Direction;

public class Action implements Serializable {
    public static final String BUILD = "BUILD";
    public static final String INTERACT = "INTERACT";
    public static final String USE_ITEM = "USE_ITEM";
    public static final String MOVEMENT = "MOVEMENT";
    public static final String REWIND = "REWIND";
    
    private String type;
    private Direction direction;
    private String id;

    public Action(String type, String id) {
        this.type = type;
        this.direction = null;
        this.id = id;
    }

    public Action(String type, Direction direction) {
        this.type = type;
        this.direction = direction;
        this.id = null;
    }

    public Action(String type) {
        this.type = type;
        this.direction = null;
        this.id = null;
    }

    public String getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getId() {
        return id;
    }
}
