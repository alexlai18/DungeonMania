package dungeonmania.interfaces.interact;

import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.exceptions.InvalidActionException;

public interface Interact {
    public abstract void interact(Player player) throws InvalidActionException;
}