package dungeonmania.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class SpiderSpawnerFunctionality extends Spawner {
    // spawn spider where there is no entity in a 25 x 25 coordinate box centered around (0,0)
    private static final int BOX_X_START = -12;
    private static final int BOX_X_END = 12;
    private static final int BOX_Y_START = -12;
    private static final int BOX_Y_END = 12;

    public SpiderSpawnerFunctionality(Dungeon dungeon, Config config) {
        super(config.getSpiderSpawnRate(), dungeon);
    }

    @Override
    public void spawn() {
        // exit if spawn rate is 0
        if (this.getSpawnRate() == 0) return;

        this.setCounter(this.getCounter() + 1);

        if (this.getCounter() == this.getSpawnRate()) {
            this.setCounter(0);

            List<Position> allPositionsInBox = new ArrayList<>();
            for (int y = BOX_Y_START; y <= BOX_Y_END; y++) {
                for (int x = BOX_X_START; x <= BOX_X_END; x++) {
                    allPositionsInBox.add(new Position(x, y));
                }
            }

            List<Position> occupiedPositions = this.getDungeon().getEntities().stream().map(Entity::getPosition).collect(Collectors.toList());

            List<Position> possibleSpawnLocations = allPositionsInBox.stream().filter(pos -> !occupiedPositions.contains(pos)).collect(Collectors.toList());

            // if empty, there is no available adjacent spot to spawn
            if (possibleSpawnLocations.isEmpty()) {
                return;
            }

            // choose a random open spot
            Random random = new Random(System.currentTimeMillis());
            Position spawnLocation = possibleSpawnLocations.get(random.nextInt(possibleSpawnLocations.size()));

            // adjacent entity logic completed in dungeon
            this.getDungeon().addEntityAfterCreation("spider", spawnLocation);
        }
    }
    
}
