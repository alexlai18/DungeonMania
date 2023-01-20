package dungeonmania.interfaces.movement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.Portal;
import dungeonmania.entity.staticentity.SwampTile;
import dungeonmania.interfaces.overlapinteract.BattleOverlapInteract;
import dungeonmania.interfaces.overlapinteract.CollectableOverlapInteract;
import dungeonmania.interfaces.overlapinteract.SlowMovementOverlapInteract;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryMovement implements Movement, Serializable, UsesDungeon {
    private Dungeon dungeon;
    private Entity[][] gridEntities;
    private int rowMin;
    private int colMin;
    private int rowSize;
    private int colSize;
    private static Direction[] dir = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private Position playerPosRelGrid;

    public MercenaryMovement(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    private void setupGrid() {
        if (this.dungeon == null || this.dungeon.getEntities() == null) return;

        List<Entity> entityList = this.dungeon.getEntities();
        // add player to list as dungeon keeps them separate
        Player player = this.dungeon.getPlayer();
        if (player != null) {
            entityList.add(player);
        }

        // get maximum and minimum x (col) and y (row) coordinate as dungeon is infinite
        // grid should be one extra dimension on each side as it is traversable on the edge 
        // assumes that there will be at least one entity in the dungeon
        this.rowMin = entityList.stream().map(Entity::getPosition).map(Position::getY).min(Integer::compare).orElse(0) - 1;
        int rowMax = entityList.stream().map(Entity::getPosition).map(Position::getY).max(Integer::compare).orElse(0) + 1;
        this.colMin = entityList.stream().map(Entity::getPosition).map(Position::getX).min(Integer::compare).orElse(0) - 1;
        int colMax = entityList.stream().map(Entity::getPosition).map(Position::getX).max(Integer::compare).orElse(0) + 1;


        this.rowSize = Math.abs(rowMax - this.rowMin) + 1;
        this.colSize = Math.abs(colMax - this.colMin) + 1;
        this.gridEntities = new Entity[this.rowSize][this.colSize];

        // populate grid 
        entityList.stream().forEach(entity -> {
            int row = Math.abs(entity.getPosition().getY() - this.rowMin);
            int col = Math.abs(entity.getPosition().getX() - this.colMin);
            this.gridEntities[row][col] = entity;
            if (entity instanceof Player) {
                // record where the player is located relative to grid
                this.playerPosRelGrid = new Position(col, row);
            }
        });
    }

    private boolean isInGrid(int row, int col) {
        return row >= 0 && row < this.rowSize && col >= 0 && col < this.colSize;
    }

    private Direction reverseDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            case DOWN:
                return Direction.UP;
            case UP:
                return Direction.DOWN;
            default:
                return null;
        }
    }


    private Direction tracebackToFirstDirection(Position srcPosRelGrid, Map<Position, Map.Entry<Position, Direction>> pred) {
        // pred nodes have predecessor of a position key
        Map.Entry<Position, Direction> predPair = pred.get(this.playerPosRelGrid);
        // if no predecessor then there is no path to the player
        if (predPair == null) return null;
        Position prevPos = predPair.getKey();
        Direction moveDirection = predPair.getValue();
        
        while (!prevPos.equals(srcPosRelGrid)) {
            // no check needed for null as if predecessor to player pos exists it should exist for whole path
            predPair = pred.get(prevPos);
            prevPos = predPair.getKey();
            moveDirection = predPair.getValue();
        }
        // since prevPos == src and prevPair is the predecessor for the key which is the pos after predecessor, 
        // moveDirection is the direction from src to the first step 
        // reverse if invincible
        return this.dungeon.getPlayerInvincibility() ? reverseDirection(moveDirection) : moveDirection;
    }


    private Direction findMercenaryNextDirection(Position src) {
        // source parameter is absolute, must convert to make relative to grid
        Position srcPosRelGrid = new Position(Math.abs(this.colMin - src.getX()), Math.abs(this.rowMin - src.getY()));
        Map<Position, Map.Entry<Position, Direction>> pred = findShortestPathDijkstras(srcPosRelGrid);
        return tracebackToFirstDirection(srcPosRelGrid, pred);
    }

    // mercenary can travel to this grid cell without movement repecussions
    // TODO add moveCostFrom and moveCostTo method in overlap interact
    private boolean canMoveToNormal(int row, int col) {
        return this.gridEntities[row][col] == null
            || this.gridEntities[row][col].getOverlapInteract() instanceof TraversableOverlapInteract
            || this.gridEntities[row][col].getOverlapInteract() instanceof BattleOverlapInteract
            || this.gridEntities[row][col].getOverlapInteract() instanceof CollectableOverlapInteract
            || this.gridEntities[row][col].getOverlapInteract() instanceof SlowMovementOverlapInteract
            || this.gridEntities[row][col] instanceof Player;
    }

    private Double cost(Position from, Position to) {
        int fromRow = from.getY();
        int fromCol = from.getX();
        int toRow = to.getY();
        int toCol = to.getX();
        if (this.gridEntities[fromRow][fromCol] instanceof SwampTile) {
            SwampTile swampTile = (SwampTile) this.gridEntities[fromRow][fromCol];
            return swampTile.getMovementFactor() + Dungeon.COST_UNIT;
        }
        if (canMoveToNormal(toRow, toCol)) return Dungeon.COST_UNIT;
        return Double.MAX_VALUE;
    }

    private Map<Position, Map.Entry<Position, Direction>> findShortestPathDijkstras(Position srcPosRelGrid) {
        // dijkstras to find shortest path 
        Map<Position, Double> dist = new HashMap<>();
        Map<Position, Map.Entry<Position, Direction>> prev = new HashMap<>();

        // initialise dist and prev with all positions in grid
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < colSize; x++) {
                dist.put(new Position(x, y), Double.MAX_VALUE);
                prev.put(new Position(x, y), null);
            }
        }
        dist.put(srcPosRelGrid, 0.0);

        // pq to store position nodes with smallest distance prioritised
        PriorityQueue<Position> pq = new PriorityQueue<>(this.rowSize * this.colSize, (p1, p2) -> Double.compare(dist.get(p1), dist.get(p2)));

        // add all positions to pq
        pq.addAll(dist.keySet());

        // traverse 
        while (!pq.isEmpty()) {
            Position u = pq.remove();
            
            // record direction merc entered portal from that will determine the next node from linked portal
            // portals should be treated as 4 separate edges
            for (Direction d : dir) {
                Position v = u.translateBy(d);

                if (isInGrid(v.getY(), v.getX()) && this.gridEntities[v.getY()][v.getX()] instanceof Portal) {
                    // change teleport v to linked portal cardinal position
                    Portal portal = (Portal) this.gridEntities[v.getY()][v.getX()];
                    Portal linkedPortal = portal.getLinkedPortal();
                    v = linkedPortal.getPosition().translateBy(d);
                }

                if (isInGrid(v.getY(), v.getX()) 
                    && dist.get(u) != Double.MAX_VALUE 
                    && dist.get(u) + cost(u, v) < dist.get(v)) {
                    dist.put(v, dist.get(u) + cost(u, v));
                    // update pq as distance for v has changed
                    pq.remove(v);
                    pq.add(v);
                    prev.put(v, Map.entry(u, d));
                }
            }
        }
        return prev;
    }


    /**
     * @pre movementDirection is null
     */
    @Override
    public void move(Entity entity, Direction movementDirection) {
        if (entity == null) return;
        setupGrid();
        // if player is not present mercenary does not move
        if (playerPosRelGrid == null) return;
        // this direction will not overlap any current entities
        Direction shortestDirection = findMercenaryNextDirection(entity.getPosition());
        
        if (shortestDirection == null) return;
        List<Entity> overlappingEntities = entity.entityOverlap(shortestDirection);

        if (!overlappingEntities.isEmpty()) {
            for (Entity e : overlappingEntities) {
                e.overlapInteractBehaviour(entity, shortestDirection);
            }
        } else {
            entity.moveByDirection(shortestDirection);
        }
    }

	@Override
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
}
