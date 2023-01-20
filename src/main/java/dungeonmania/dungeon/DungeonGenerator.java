package dungeonmania.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.util.Position;

public class DungeonGenerator {

    public static final int MAZE_OFFSET = 50;

    /**
     * Randomly generates a dungeon based on the given algorithm and returns it as a 2d boolean array.
     * @param xStart The starting x position of the randomly created dungeon
     * @param yStart The starting y position of the randomly created dungeon
     * @param xEnd The ending x position of the randomly created dungeon
     * @param yEnd The ending y position of the randomly created dungeon
     * @return a 2 dimensional boolean maze showing the state of the randomly generated dungeon
     */
    public static boolean[][] generatePrimsDungeon(int xStart, int yStart, int xEnd, int yEnd) {
        boolean[][] maze = new boolean[102][102]; 
        
        // Set start to empty
        maze[xStart + MAZE_OFFSET][yStart + MAZE_OFFSET] = true;

        // Create a list for options
        List<Position>options = new ArrayList<Position>();

        // Add all neighbours of start that are of distance two away.
        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                // Check if current position is a wall and it's of distance two away
                if (!maze[x + MAZE_OFFSET][y + MAZE_OFFSET]) {
                    if (((xStart + 2 == x) && (yStart == y)) || ((xStart - 2 == x) && (yStart == y)) 
                        || ((xStart == x) && (yStart + 2 == y)) || ((xStart == x) && (yStart - 2 == y))) {
                            options.add(new Position(x, y));
                    } 
                }
            }
        }

        // Create random variable for use
        Random rand = new Random();

        while (!options.isEmpty()) {

            // let next = remove random from options
            Position next = options.get(rand.nextInt(options.size()));

            // Create a list for neighbours
            List<Position>neighbours = new ArrayList<Position>();

            // Add all neighbours of next that are of distance two away and empty. 
            for (int x = xStart; x <= xEnd; x++) {
                for (int y = yStart; y <= yEnd; y++) {
                    // Check if current position is empty and it's of distance two away
                    if (maze[x + MAZE_OFFSET][y + MAZE_OFFSET]) {
                        if (((next.getX() + 2 == x) && (next.getY() == y)) || ((next.getX() - 2 == x) && (next.getY() == y)) 
                            || ((next.getX() == x) && (next.getY() + 2 == y)) || ((next.getX() == x) && (next.getY() - 2 == y))) {
                                neighbours.add(new Position(x, y));
                        } 
                    }
                }
            }

            if (!neighbours.isEmpty()) {
                // let neighbour = random from neighbours
                Position neighbour = neighbours.get(rand.nextInt(neighbours.size()));
                
                // maze[ next ] = empty (i.e. true)
                maze[next.getX() + MAZE_OFFSET][next.getY() + MAZE_OFFSET] = true;

                // maze[ neighbour ] = empty (i.e. true)
                maze[neighbour.getX() + MAZE_OFFSET][neighbour.getY() + MAZE_OFFSET] = true;

                // maze[ position inbetween next and neighbour ] = empty (i.e. true)
                if (neighbour.getX() > next.getX()) {
                    maze[next.getX() + 1 + MAZE_OFFSET][next.getY() + MAZE_OFFSET] = true;
                } else if (neighbour.getX() < next.getX()) {
                    maze[next.getX() - 1 + MAZE_OFFSET][next.getY() + MAZE_OFFSET] = true;
                } else if (neighbour.getY() > next.getY()) {
                    maze[next.getX() + MAZE_OFFSET][next.getY() + 1 + MAZE_OFFSET] = true;
                } else if (neighbour.getY() < next.getY()) {
                    maze[next.getX() + MAZE_OFFSET][next.getY() - 1 + MAZE_OFFSET] = true;
                }
                
            }

            // Add all neighbours of next that are of distance two away and walls. 
            for (int x = xStart; x <= xEnd; x++) {
                for (int y = yStart; y <= yEnd; y++) {
                    // Check if current position is a wall and it's of distance two away
                    if (!maze[x + MAZE_OFFSET][y + MAZE_OFFSET]) {
                        if (((next.getX() + 2 == x) && (next.getY() == y)) || ((next.getX() - 2 == x) && (next.getY() == y)) 
                            || ((next.getX() == x) && (next.getY() + 2 == y)) || ((next.getX() == x) && (next.getY() - 2 == y))) {
                                options.add(new Position(x, y));
                        } 
                    }
                }
            }

            options.remove(next);
        }
        
        // at the end there is still a case where our end position isn't connected to the map
        // this will make it consistently have a pathway between the two.
        if (!maze[xEnd + MAZE_OFFSET][yEnd + MAZE_OFFSET]) {
            // Set the end to empty
            maze[xEnd + MAZE_OFFSET][yEnd + MAZE_OFFSET] = true;

            // Create a list for neighbours
            List<Position>neighbours = new ArrayList<Position>();

            // Add all neighbours of next that are of distance two away and empty. 
            for (int x = xStart; x <= xEnd; x++) {
                for (int y = yStart; y <= yEnd; y++) {
                    // Check if current position is a wall and it's of distance two away
                    if (maze[x + MAZE_OFFSET][y + MAZE_OFFSET]) {
                        if (((xEnd + 1 == x) && (yEnd == y)) || ((xEnd - 1 == x) && (yEnd == y)) 
                            || ((xEnd == x) && (yEnd + 1 == y)) || ((xEnd == x) && (yEnd - 1 == y))) {
                                options.add(new Position(x, y));
                        } 
                    }
                }
            }

            // if there are no cells in neighbours that are empty:
            if (neighbours.size() != 0 && neighbours.stream().allMatch(n -> (maze[n.getX() + MAZE_OFFSET][n.getY() + MAZE_OFFSET] == false))) {
                // let neighbour = random from neighbours
                Position neighbour = neighbours.get(rand.nextInt(neighbours.size()));
                
                // maze[ neighbour ] = empty (i.e. true)
                maze[neighbour.getX() + MAZE_OFFSET][neighbour.getY() + MAZE_OFFSET] = true;
                
            }
        }
        return maze;
    }
    
}
