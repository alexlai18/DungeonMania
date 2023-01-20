package dungeonmania.spawner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.staticentity.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawnerFunctionality extends Spawner {
    private ZombieToastSpawner zombieToastSpawner;
    private Dungeon dungeon;
    
    public ZombieToastSpawnerFunctionality(ZombieToastSpawner zombieToastSpawner, Dungeon dungeon, Config config) {
        super(config.getZombieSpawnRate(), dungeon);
        this.zombieToastSpawner = zombieToastSpawner;
    }

    public void spawn() {
        // exit if spawn rate is 0
        if (this.getSpawnRate() == 0) return;
        
        this.setCounter(this.getCounter() + 1);

        if (this.getCounter() == this.getSpawnRate()) {
            this.setCounter(0);
            
            // spawn zombie where there is no entity
            Position ztsPosition = zombieToastSpawner.getPosition();
            List<Position> cardinalAdjacentPositions = Arrays.asList(
                ztsPosition.translateBy(Direction.UP),
                ztsPosition.translateBy(Direction.RIGHT),
                ztsPosition.translateBy(Direction.DOWN),
                ztsPosition.translateBy(Direction.LEFT)
            );
            
            List<Position> adjacentEntityPositions = zombieToastSpawner.getAdjacentEntities()
                                                        .stream()
                                                        .map(Entity::getPosition)
                                                        .collect(Collectors.toList());

            List<Position> possibleSpawnLocations = cardinalAdjacentPositions
                                                        .stream()
                                                        .filter(pos -> adjacentEntityPositions.stream().noneMatch(adjEntPos -> adjEntPos.equals(pos)))
                                                        .collect(Collectors.toList());

            // if empty, there is no available adjacent spot to spawn
            if (possibleSpawnLocations.isEmpty()) {
                return;
            }

            // choose a random open spot
            Random random = new Random(System.currentTimeMillis());
            Position spawnLocation = possibleSpawnLocations.get(random.nextInt(possibleSpawnLocations.size()));

            // adjacent entity logic completed in dungeon
            this.getDungeon().addEntityAfterCreation("zombie_toast", spawnLocation);
        } 
    }
}
