package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.movingentity.player.Player;

import dungeonmania.util.Position;

public class PlayerTests {
    @Test
    @DisplayName("Test player health and attack properly stored")
    public void testHealthandAttack() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bomb_radius_2");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        
        assertEquals(player.getAttack(), 1);
        assertEquals(player.getHealth(), 10);
    }

    @Test
    @DisplayName("Test player position is properly stored")
    public void testPosition() {
        Dungeon dungeon = new Dungeon("empty", "bomb_radius_2", 1);
        Config newConfig = new Config("bribe_amount_3");
        Player player = new Player(new Position(0, 0), new ArrayList<>(), newConfig, "", dungeon);
        
        assertEquals(player.getPosition(), new Position(0, 0));
    }
}
