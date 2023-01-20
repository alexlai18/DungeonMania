package dungeonmania.spawner;

import java.io.Serializable;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;

public abstract class Spawner implements Serializable, UsesDungeon {
    private int spawnRate;
    private int counter;
    private Dungeon dungeon;

    public Spawner(int spawnRate, Dungeon dungeon) {
        this.spawnRate = spawnRate;
        this.dungeon = dungeon;
        this.counter = 0;
    }
    
    public int getSpawnRate() {
        return spawnRate;
    }

    public int getCounter() {
        return counter;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Spawns entity into dungeon based on spawner rate
     */
    public abstract void spawn();
} 
