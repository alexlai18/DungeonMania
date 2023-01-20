package dungeonmania.dungeon;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.config.Config;
import dungeonmania.dungeon.action.Action;
import dungeonmania.entity.Entity;
import dungeonmania.entity.battles.Battle;
import dungeonmania.entity.inventoryitem.*;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.*;
import dungeonmania.entity.movingentity.player.Player;
import dungeonmania.entity.staticentity.*;
import dungeonmania.entity.staticentity.logic.Logic;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;
import dungeonmania.goals.simple_goals.ExitGoal;
import dungeonmania.interfaces.ally.Ally;
import dungeonmania.interfaces.animation.Animation;
import dungeonmania.interfaces.movement.ControlledMovingEntity;
import dungeonmania.interfaces.movement.MercenaryMovement;
import dungeonmania.interfaces.overlapinteract.BattleOverlapInteract;
import dungeonmania.interfaces.usesdungeon.UsesDungeon;
import dungeonmania.response.models.*;
import dungeonmania.spawner.*;
import dungeonmania.util.*;

/**
 * Dungeon
 */
public class Dungeon implements Serializable {
    private static final String PLAYER = "player";
    private static final String WALL = "wall";
    private static final String EXIT = "exit";
    private static final String BOULDER = "boulder";
    private static final String FLOOR_SWITCH = "switch";
    private static final String DOOR = "door";
    private static final String PORTAL = "portal";
    private static final String ZOMBIE_TOAST_SPAWNER = "zombie_toast_spawner";
    private static final String SPIDER = "spider";
    private static final String ZOMBIE_TOAST = "zombie_toast";
    private static final String MERCENARY = "mercenary";
    private static final String ASSASSIN = "assassin";
    private static final String HYDRA = "hydra";
    private static final String TREASURE = "treasure";
    private static final String KEY = "key";
    private static final String INVINCIBILITY_POTION = "invincibility_potion";
    private static final String INVISIBILITY_POTION = "invisibility_potion";
    private static final String WOOD = "wood";
    private static final String ARROWS = "arrow";
    private static final String BOMB = "bomb";
    private static final String SWORD = "sword";
    private static final String SUN_STONE = "sun_stone";
    private static final String SWAMP_TILE = "swamp_tile";
    private static final String TIME_TURNER = "time_turner";
    private static final String TIME_TRAVEL_PORTAL = "time_travelling_portal";
    private static final String LIGHT_BULB_OFF = "light_bulb_off";
    private static final String WIRE = "wire";
    private static final String SWITCH_DOOR = "switch_door";

    public static final int MAZE_OFFSET = 50;

    public static final Double COST_UNIT = 1.0;

    
    /**
     * The unique identifier for the dungeon
     */
    private final String id;

    /**
     * The name of the dungeon map being used
     */
    private String name;

    /**
     * Reference to the object for the player
     */
    private Player player;

    /**
     * List of all entities in the dungeon.
     */
    private List<Entity> entities = new ArrayList<>();

    /**
     * Hashmap of portals in the dungeon and their colours.
     */
    private Map<String, Portal> portalColourMap = new HashMap<>();

    /**
     * The goal for the dungeon that has to be completed.
     */
    private Goal goal;

    /**
     * List of battles that has occured in total in the game so far
     */
    private List<Battle> battles;

    /**
     * The configuration file for the dungeon.
     */
    private final Config config;
    private Spawner spawner;

    /**
     * A hashmap for the id number we're upto for a given entity type.
     */
    private HashMap<String, Integer> ids = new HashMap<>();

    /**
     * stores tick as reference so can be modified in places other than dungeon
     */
    private int[] tick = {0};

    /**
     * Time travel attributes
     */
    private Player oldPlayer;

    /**
     * The dungeon constructor for default loading
     * @param dungeonName The name of the dungeon map we're using.
     * @param configName The name of the configuration file we're using.
     * @param id The ID of the Dungeon.
     */
    public Dungeon(String dungeonName, String configName, int id) {
        config = new Config(configName);
        setName(dungeonName);
        initalizeIds();
        loadEntities();
        loadGoal();
        setEntities(entities);
        setBattles(new ArrayList<>());
        this.spawner = new SpiderSpawnerFunctionality(this, config);
        this.id = "Dungeon:" + id;
    }

    /**
     * The dungeon constructor for random generation.
     * @param xStart The starting x position.
     * @param yStart The starting y position.
     * @param xEnd The ending x position.
     * @param yEnd The ending y position.
     * @param configName The name of the configuration file we're using.
     * @param id The ID of the Dungeon.
     */
    public Dungeon(int xStart, int yStart, int xEnd, int yEnd, String configName, int id) {
        config = new Config(configName);
        setName("Random Dungeon " + id);
        initalizeIds();
        setGoal(new ExitGoal(null));
        loadRandomDungeon(xStart, yStart, xEnd, yEnd);
        setEntities(entities);
        setBattles(new ArrayList<>());
        this.spawner = new SpiderSpawnerFunctionality(this, config);
        this.id = "Dungeon:" + id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
    public Config getConfig() {
        return config;
    }

    public List<Battle> getBattles() {
        return battles;
    }

    public int getCurrTick() {
        return tick[0];
    }

    public int[] getCurrTickAsRef() {
        return this.tick;
    }

    public void incCurrTick() {
        this.tick[0] = tick[0] + 1;
    }
    
    public List<Entity> getEntities() {
        return this.entities.stream().filter(e -> e.getPosition() != null).collect(Collectors.toList());
    }
    
    public Goal getGoal() {
        return goal;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOlderPlayer() {
        return oldPlayer;
    }

    public int getKills() {
        if (getPlayer() != null) {
            return getPlayer().getKills();
        }
        return -1;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setBattles(List<Battle> battles) {
        this.battles = battles;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
        updateAdjacentEntities();
    }

    public void addBattle(Battle battle) {
        getBattles().add(battle);
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
        updateAdjacentEntities();
    }

    public void removeEntity(Entity entity) {
        if (entity.getType().equals("older_player")) {
            setOldPlayer(null);
        } else {
            this.entities.remove(entity);
        }
        updateAdjacentEntities();
    }

    public void addEntityAfterCreation(String name, Position position) {
        if (!ids.containsKey(name)) return;
        ids.put(name, ids.get(name) + 1);
        String entityId = name + ":" + ids.get(name);

        switch(name) {
            case ZOMBIE_TOAST:
                addEntity(new ZombieToast(position, new ArrayList<>(), config, entityId, this));
                return;
            case SPIDER:
                addEntity(new Spider(position, new ArrayList<>(), config, entityId, this));
                return;
            case PLAYER:
                ids.put(PLAYER, ids.get(PLAYER) + 1);    
                setPlayer(new Player(position, null, config, PLAYER + ":" + ids.get(PLAYER), this));    
                return;
            case WALL:
                ids.put(WALL, ids.get(WALL) + 1);
                addEntity(new Wall(position, null, WALL + ":" + ids.get(WALL)));
            case EXIT:
                ids.put(EXIT, ids.get(EXIT) + 1);
                addEntity(new Exit(position, null, EXIT + ":" + ids.get(EXIT)));
            default:
                return;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateAdjacentEntities();
    }

    public void setOldPlayer(Player oldPlayer) {
        this.oldPlayer = oldPlayer;
        updateAdjacentEntities();
    }

    public Boolean getPlayerInvincibility() {
        if (player == null) return false;

        if (player.getCurrentPotion() == null) {
            return false;
        } else if (player.getCurrentPotion() instanceof InvincibilityPotion) {
            return true;
        }
        return false;
    }

    public boolean getPlayerInvisibility() {
        if (player == null) return false;

        if (player.getCurrentPotion() == null) {
            return false;
        } else if (player.getCurrentPotion() instanceof InvisibilityPotion) {
            return true;
        }
        return false;
    }
   
    /**
     * goes through dungeon and updates the adjacent entities for all entities
     */
    public void updateAdjacentEntities() {
        if (this.entities == null) return;

        // make temp list to include player
        List<Entity> allEntitiesIncludingPlayer = new ArrayList<>(this.entities);

        // filter out nulls
        allEntitiesIncludingPlayer.add(this.player);
        allEntitiesIncludingPlayer.add(this.oldPlayer);
        allEntitiesIncludingPlayer = allEntitiesIncludingPlayer.stream().filter(Objects::nonNull).collect(Collectors.toList());
        
        if (allEntitiesIncludingPlayer.isEmpty()) return;
        
        // stores all entities at a certain position key
        Map<Position, List<Entity>> posDict = new HashMap<>();
        allEntitiesIncludingPlayer.stream().forEach(e -> {
            Position ePos = e.getPosition();
            if (posDict.containsKey(ePos)) {
                posDict.get(ePos).add(e);
            } else {
                posDict.put(ePos, new ArrayList<>(Arrays.asList(e)));
            }
        });

        // go through entities and store the cardinally adjacent entities
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT));
        allEntitiesIncludingPlayer.stream().forEach(e -> {
            // reset state of adjacent entities for each entity as new state
            e.setAdjacentEntities(new ArrayList<>());

            Position ePos = e.getPosition();
            if (ePos == null) return;

            // populate adjacent
            directions.stream().forEach(direction -> {
                Position cardinallyAdjacentPos = ePos.translateBy(direction);
                List<Entity> adjacentEntitiesAtOnePos = posDict.get(cardinallyAdjacentPos);
                if (adjacentEntitiesAtOnePos != null) {
                    adjacentEntitiesAtOnePos.stream().forEach(e::addAdjacentEntity);
                }
            });
        });
    }

    /**
     * Loads goals into the dungeon on constructor.
     */
    private void loadGoal() {
        JSONObject dungeonGoal;
        try {
            dungeonGoal = new JSONObject(FileLoader.loadResourceFile("/dungeons/" + getName() + ".json"));
            JSONObject goalJsonObject = dungeonGoal.getJSONObject("goal-condition");
            setGoal(GoalFactory.createGoal(goalJsonObject.getString("goal"), goalJsonObject, this, null)); 
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    private void loadEntities() {
        try {
            JSONObject dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/" + getName() + ".json"));
            JSONArray entitiesJsonArray = new JSONArray(dungeon.getJSONArray("entities"));
            List<Ally> allies = new ArrayList<>();
            for (int i = 0; i < entitiesJsonArray.length(); i++) {
                JSONObject entityJsonObject = entitiesJsonArray.getJSONObject(i);
                Entity newEntity = entityFactory(entityJsonObject);
                if (newEntity != null) {
                    this.addEntity(newEntity);
                    if (newEntity instanceof Ally) {
                        allies.add((Ally) newEntity);
                    } 
                }
            }
            if (player != null) {
                ((ControlledMovingEntity) player.getMovement()).setAllies(allies);
            }

            Portal.setColours(this.portalColourMap);
            
            updateAdjacentEntities();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Initalize all IDs for all entities to 0 at the start.
     */
    private void initalizeIds() {
        ids.put(PLAYER, 0);
        ids.put(WALL, 0);
        ids.put(EXIT, 0);
        ids.put(BOULDER, 0);
        ids.put(FLOOR_SWITCH, 0);
        ids.put(DOOR, 0);
        ids.put(PORTAL, 0);
        ids.put(ZOMBIE_TOAST_SPAWNER, 0);
        ids.put(SPIDER, 0);
        ids.put(ZOMBIE_TOAST, 0);
        ids.put(MERCENARY, 0);
        ids.put(ASSASSIN, 0);
        ids.put(HYDRA, 0);
        ids.put(TREASURE, 0);
        ids.put(KEY, 0);
        ids.put(INVINCIBILITY_POTION, 0);
        ids.put(INVISIBILITY_POTION, 0);
        ids.put(WOOD, 0);
        ids.put(ARROWS, 0);
        ids.put(BOMB, 0);
        ids.put(SWORD, 0);
        ids.put(SUN_STONE, 0);
        ids.put(SWAMP_TILE, 0);
        ids.put(TIME_TURNER, 0);
        ids.put(TIME_TRAVEL_PORTAL, 0);
        ids.put(LIGHT_BULB_OFF, 0);
        ids.put(WIRE, 0);
        ids.put(SWITCH_DOOR, 0);
    }

    /**
     * Creates a Dungeon Response object out of current state of Dungeon and returns it.
     * @return DungeonResponse object with the status of the dungeon.
     */
    public DungeonResponse toDungeonResponse() {
        List<EntityResponse> entitiesResponse = new ArrayList<>();
        List<ItemResponse> inventoryResponses = new ArrayList<>();
        List<AnimationQueue> animations = new ArrayList<>();
        List<String> buildables = new ArrayList<>();  
        entities.stream().forEach(e -> {
            if (e.getPosition() == null) return;
            entitiesResponse.add(e.getEntityResponse());
            if (e instanceof Animation) animations.addAll(((Animation) e).getAnimation());
        });

        if (player != null) {
            entitiesResponse.add(player.getEntityResponse());
            inventoryResponses = player.getItemListResponse();
            buildables = player.getBuildables();
            animations.addAll(player.getAnimation());
        }
        
        if (oldPlayer != null) {
            entitiesResponse.add(oldPlayer.getEntityResponse());
            animations.addAll(oldPlayer.getAnimation());
        }

        List<BattleResponse> battleResponses = new ArrayList<>();
        battles.stream().forEach(b -> battleResponses.add(b.getBattleResponse()));
        return new DungeonResponse(id, name, entitiesResponse, inventoryResponses, battleResponses, buildables, getGoal().evaluate(this), animations);
    }

    private void commonTick(List<Action> oldPlayerActions) {
        List<Entity> entityCopy = new ArrayList<>(this.getEntities());
        // change movement based on swamp tiles
        entityCopy.stream().filter(SwampTile.class::isInstance).forEach(st -> {
            ((SwampTile) st).slowedMovementTick(this);
        });

        // move entities
        entityCopy.stream().filter(MovingEntity.class::isInstance).forEach(e -> {
            ((MovingEntity) e).move(null);
            updateAdjacentEntities();
        });

        // spawn entities
        this.spawner.spawn();
        List<ZombieToastSpawner> ztsList = new ArrayList<>(this.entities.stream().filter(ZombieToastSpawner.class::isInstance).map(ZombieToastSpawner.class::cast).collect(Collectors.toList()));
        ztsList.forEach(ZombieToastSpawner::tick);

        updateAdjacentEntities();
        this.incCurrTick();

        Logic.activateEntities(this);

        // tick old player if existing
        if (this.oldPlayer != null) {
            try {
                this.oldPlayerTick(oldPlayerActions);
            } catch (IllegalArgumentException | InvalidActionException e1) {
                e1.printStackTrace();
            }
        }
        
        if (player != null) {
            Potion currentPotion = player.getCurrentPotion();
            if (currentPotion != null) {
                currentPotion.checkPotionStatus(player);
            }
        }
    }

    /**
     * Carries out the movement tick when calling tick(Direction movementDirection) in 
     * controller.
     * @param movementDirection the direction of movement for the player
     * @param oldPlayerActions actions the old player must take if exists
     */
    public void movementTick(Direction movementDirection, List<Action> oldPlayerActions) {   
        Potion currentPotion = player.getCurrentPotion();
        if (currentPotion != null) {
            currentPotion.use();
        }

        player.useSceptre(this, entities);

        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invincibilityEffects(this));
        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invisibilityEffects(this));

        // Everything moves
        player.move(movementDirection);
        updateAdjacentEntities();
        this.commonTick(oldPlayerActions);
    }

    public void itemTick(String itemUsed, List<Action> oldPlayerActions) throws InvalidActionException, IllegalArgumentException {
        Potion currentPotion = player.getCurrentPotion();
        if (currentPotion != null) {
            currentPotion.use();
        }

        player.useSceptre(this, entities);

        player.useItem(itemUsed, this);
        
        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invincibilityEffects(this));
        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invisibilityEffects(this));

        this.commonTick(oldPlayerActions);
    }

    /**
     * Builds the item given appropriate inputs and if the player has enough resources.
     * @param buildable the ID for the item trying to be built.
     * @throws InvalidActionException If the player does not have sufficient items to craft the buildable 
     */
    public void buildItem(String buildable) throws InvalidActionException {
        if (player.canCraft(buildable)) {
            InventoryItem weapon = player.buildEntity(buildable);
            if (!buildable.equals("sceptre")) {
                player.equipWeapon(weapon);   
            }
        } else {
            throw new InvalidActionException("You don't have the resources to build " + buildable + "!");
        }
    }

    public void interactWith(String entityId) throws InvalidActionException {
        Entity interactingWith = getEntityFromId(entityId);
        if (interactingWith instanceof Mercenary) {
            Mercenary merc = (Mercenary) interactingWith;
            merc.interact(getPlayer());
        } else if (interactingWith instanceof ZombieToastSpawner) {
            ZombieToastSpawner zts = (ZombieToastSpawner) interactingWith;
            zts.interact(getPlayer());
        } else if (interactingWith instanceof Assassin) {
            Assassin ass = (Assassin) interactingWith;
            ass.interact(getPlayer());
        }
    }

    /**
     * Carries out the movement tick when calling tick(Direction movementDirection) in 
     * controller by the OLD PLAYER.
     * @param movementDirection the direction of movement for the OLD player
     */
    public void oldMovementTick(Direction movementDirection) {        
        Potion currentPotion = oldPlayer.getCurrentPotion();
        if (currentPotion != null) {
            currentPotion.checkPotionStatus(oldPlayer);
            currentPotion.use();
        }

        oldPlayer.useSceptre(this, entities);

        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invincibilityEffects(this));
        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invisibilityEffects(this));

        // Everything moves
        oldPlayer.move(movementDirection);
        updateAdjacentEntities();
    }

    public void oldItemTick(String itemUsed) throws InvalidActionException, IllegalArgumentException {
        Potion currentPotion = oldPlayer.getCurrentPotion();
        if (currentPotion != null) {
            currentPotion.checkPotionStatus(oldPlayer);
            currentPotion.use();
        }

        oldPlayer.useSceptre(this, entities);

        oldPlayer.oldUseItem(itemUsed, this);
        
        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invincibilityEffects(this));
        this.getEntities().stream().filter(PotionEffects.class :: isInstance).forEach(e -> ((PotionEffects) e).invisibilityEffects(this));
    }

    /**
     * Builds the item given appropriate inputs and if the player has enough resources.
     * @param buildable the ID for the item trying to be built.
     * @throws InvalidActionException If the player does not have sufficient items to craft the buildable 
     */
    public void oldBuildItem(String buildable) throws InvalidActionException {
        if (Boolean.TRUE.equals(oldPlayer.canCraft(buildable))) {
            InventoryItem weapon = oldPlayer.buildEntity(buildable);
            if (buildable.equals("sceptre")) {
                oldPlayer.equipWeapon(weapon);   
            }
        } else {
            throw new InvalidActionException("You don't have the resources to build " + buildable + "!");
        }
    }

    public void oldInteractWith(String entityId) throws InvalidActionException {
        Entity interactingWith = getOldEntityFromId(entityId);
        if (interactingWith instanceof Mercenary) {
            Mercenary merc = (Mercenary) interactingWith;
            merc.interact(oldPlayer);
        } else if (interactingWith instanceof ZombieToastSpawner) {
            ZombieToastSpawner zts = (ZombieToastSpawner) interactingWith;
            zts.setDungeon(this);
            zts.interact(oldPlayer);
        }
    }

    public void rewind(Dungeon prevDungeon)  {
        this.oldPlayer = prevDungeon.getPlayer();
        oldPlayer.makeOld();
        this.setEntities(prevDungeon.getEntities());

        // change dungeon references
        player.setOverlapInteract(new BattleOverlapInteract(this));
        oldPlayer.setOverlapInteract(new BattleOverlapInteract(this));
        this.getEntities().stream()
            .filter(e -> e instanceof Bomb)
            .map(e -> (Bomb) e)
            .forEach(b -> b.setDungeon(this));
        this.getEntities().stream()
            .filter(e -> e.getOverlapInteract() instanceof BattleOverlapInteract)
            .map(e -> (BattleOverlapInteract) e.getOverlapInteract())
            .forEach(o -> o.setDungeon(this));
        this.getEntities().stream()
            .filter(e -> e instanceof MovingEntity)
            .map(m -> ((MovingEntity) m).getMovement())
            .filter(m -> m instanceof MercenaryMovement)
            .map(m -> (MercenaryMovement) m)
            .map(m -> (UsesDungeon) m)
            .forEach(m -> m.setDungeon(this));
        this.getEntities().stream()
            .filter(e -> e instanceof ZombieToastSpawner)
            .map(s -> ((ZombieToastSpawner) s).getSpawner())
            .map(s -> (UsesDungeon) s)
            .forEach(s -> s.setDungeon(this));
    }

    public boolean isPlayerOnTimeTravelPortal() {
        List<Position> timeTravelPortalPositions = this.entities.stream().filter(TimeTravelPortal.class::isInstance).map(Entity::getPosition).collect(Collectors.toList());
        if (this.getPlayer() == null) return false;
        Position playerPosition = this.getPlayer().getPosition();
        return timeTravelPortalPositions.stream().anyMatch(playerPosition::equals);
    }

    public void oldPlayerTick(List<Action> actions) throws InvalidActionException, IllegalArgumentException {
        for (Action action : actions) {
            switch (action.getType()) {
                case Action.MOVEMENT: {
                    oldMovementTick(action.getDirection());
                    break;
                }
                case Action.BUILD: {
                    oldBuildItem(action.getId());
                    break;
                }
                case Action.USE_ITEM: {
                    oldItemTick(action.getId());
                    break;
                }
                case Action.INTERACT: {
                    oldInteractWith(action.getId());
                    break;
                }
                case Action.REWIND: {
                    removeEntity(oldPlayer);
                    break;
                }
                default: break;
            }
        }
    }

    /**
     * Given an entity ID we return the entity associated with it or we return null.
     * @param entityId The ID of the entity we're trying to find
     * @return the entity object associated with that id. Return null when we don't get valid input 
     * or don't want to add an entity to the overall list.
     */
    private Entity getEntityFromId(String entityId) {
        return entities.stream()
        .filter(e -> e.getId().equals(entityId))
        .findFirst()
        .orElse(null);
    }

    private Entity getOldEntityFromId(String entityId) {
        return entities.stream()
        .filter(e -> e.getId().startsWith(entityId))
        .findFirst()
        .orElse(null);
    }

    /**
     * Given the JSON Object for an entity we create it and return it.
     * @param entityJsonObject The JSON Object for the entity.
     * @return An object with the entity
     */
    private Entity entityFactory(JSONObject entityJsonObject) {
        String type = entityJsonObject.getString("type");
        int x = entityJsonObject.getInt("x");
        int y = entityJsonObject.getInt("y");
        Position pos = new Position(x, y);
        switch(type) {
            case PLAYER:
                ids.put(PLAYER, ids.get(PLAYER) + 1);    
                setPlayer(new Player(pos, null, config, PLAYER + ":" + ids.get(PLAYER), this));    
                return null;
            case ZOMBIE_TOAST:
                ids.put(ZOMBIE_TOAST, ids.get(ZOMBIE_TOAST) + 1);
                return new ZombieToast(pos, null, config, ZOMBIE_TOAST + ":" + ids.get(ZOMBIE_TOAST), this);
            case ZOMBIE_TOAST_SPAWNER:
                ids.put(ZOMBIE_TOAST_SPAWNER, ids.get(ZOMBIE_TOAST_SPAWNER) + 1);
                return new ZombieToastSpawner(this, pos, null, ZOMBIE_TOAST_SPAWNER + ":" + ids.get(ZOMBIE_TOAST_SPAWNER));
            case MERCENARY:
                ids.put(MERCENARY, ids.get(MERCENARY) + 1);
                return new Mercenary(pos, null, config, this, MERCENARY + ":" + ids.get(MERCENARY));
            case ASSASSIN:
                ids.put(ASSASSIN, ids.get(ASSASSIN) + 1);
                return new Assassin(pos, null, this, ASSASSIN + ":" + ids.get(ASSASSIN));
            case HYDRA:
                ids.put(HYDRA, ids.get(HYDRA) + 1);
                return new Hydra(pos, null,  HYDRA + ":" + ids.get(HYDRA), this);
            case SPIDER:
                ids.put(SPIDER, ids.get(SPIDER) + 1);
                return new Spider(pos, null, config, SPIDER + ":" + ids.get(SPIDER), this);
            case TREASURE:
                ids.put(TREASURE, ids.get(TREASURE) + 1);
                return new Treasure(pos, null, TREASURE + ":" + ids.get(TREASURE));
            case ARROWS:
                ids.put(ARROWS, ids.get(ARROWS) + 1);
                return new Arrows(pos, null, ARROWS + ":" + ids.get(ARROWS));
            case WOOD:
                ids.put(WOOD, ids.get(WOOD) + 1);
                return new Wood(pos, null, WOOD + ":" + ids.get(WOOD));
            case KEY:
                ids.put(KEY, ids.get(KEY) + 1);
                return new Key(pos, null, entityJsonObject.getInt(KEY), KEY + ":" + ids.get(KEY)); 
            case INVINCIBILITY_POTION:
                ids.put(INVINCIBILITY_POTION, ids.get(INVINCIBILITY_POTION) + 1);
                return new InvincibilityPotion(pos, null, config.getInvincibilityPotionDuration(), INVINCIBILITY_POTION + ":" + ids.get(INVINCIBILITY_POTION)); 
            case INVISIBILITY_POTION:
                ids.put(INVISIBILITY_POTION, ids.get(INVISIBILITY_POTION) + 1);
                return new InvisibilityPotion(pos, null, config.getInvisibilityPotionDuration(), INVISIBILITY_POTION + ":" + ids.get(INVISIBILITY_POTION)); 
            case SWORD:
                ids.put(SWORD, ids.get(SWORD) + 1);
                return new Sword(pos, null, config.getSwordDurability(), config.getSwordAttack(), SWORD + ":" + ids.get(SWORD)); 
            case BOMB:
                ids.put(BOMB, ids.get(BOMB) + 1);
                try {
                    String bLogic = entityJsonObject.getString("logic");
                    return new LogicBomb(pos, null, config.getBombRadius(), BOMB + ":" + ids.get(BOMB), this, bLogic); 
                } catch (JSONException e) {
                    return new Bomb(pos, null, config.getBombRadius(), BOMB + ":" + ids.get(BOMB), this); 
                }
            case WALL:
                ids.put(WALL, ids.get(WALL) + 1);
                return new Wall(pos, null, WALL + ":" + ids.get(WALL));
            case BOULDER:
                ids.put(BOULDER, ids.get(BOULDER) + 1);
                return new Boulder(pos, null, BOULDER + ":" + ids.get(BOULDER));
            case EXIT:
                ids.put(EXIT, ids.get(EXIT) + 1);
                return new Exit(pos, null, EXIT + ":" + ids.get(EXIT));
            case FLOOR_SWITCH:
                ids.put(FLOOR_SWITCH, ids.get(FLOOR_SWITCH) + 1);
                try {
                    String fsLogic = entityJsonObject.getString("logic");
                    return new LogicFloorSwitch(pos, false, null, FLOOR_SWITCH + ":" + ids.get(FLOOR_SWITCH), fsLogic, this.getCurrTickAsRef());
                } catch (JSONException e) {
                    return new FloorSwitch(pos, false, null, FLOOR_SWITCH + ":" + ids.get(FLOOR_SWITCH), this.getCurrTickAsRef());
                }
            case DOOR:
                ids.put(DOOR, ids.get(DOOR) + 1);
                return new Door(pos, entityJsonObject.getInt(KEY), null, DOOR + ":" + ids.get(DOOR));
            case PORTAL:
                ids.put(PORTAL, ids.get(PORTAL) + 1);
                String colour = entityJsonObject.getString("colour");
                if (!portalColourMap.containsKey(colour)) {
                    Portal portal = new Portal(pos, null, PORTAL + ":" + ids.get(PORTAL));
                    portalColourMap.put(colour, portal);
                    return portal;
                } else {
                    Portal linkedPortal = portalColourMap.get(colour);
                    Portal portal = new Portal(pos, linkedPortal, null, PORTAL + ":" + ids.get(PORTAL));
                    linkedPortal.setLinkedPortal(portal);
                    return portal;
                }
            case SUN_STONE:
                ids.put(SUN_STONE, ids.get(SUN_STONE) + 1);
                return new SunStone(pos, null, SUN_STONE + ":" + ids.get(SUN_STONE));
            case SWAMP_TILE:
                ids.put(SWAMP_TILE, ids.get(SWAMP_TILE) + 1);
                int movementFactor = entityJsonObject.getInt("movement_factor");
                return new SwampTile(pos, null, SWAMP_TILE + ":" + ids.get(SWAMP_TILE), movementFactor);
            case TIME_TURNER:
                ids.put(TIME_TURNER, ids.get(TIME_TURNER) + 1);
                return new TimeTurner(pos, null, TIME_TURNER + ":" + ids.get(TIME_TURNER));
            case TIME_TRAVEL_PORTAL:
                ids.put(TIME_TRAVEL_PORTAL, ids.get(TIME_TRAVEL_PORTAL) + 1);
                return new TimeTravelPortal(pos, null, TIME_TRAVEL_PORTAL + ":" + ids.get(TIME_TRAVEL_PORTAL), this);
            case LIGHT_BULB_OFF:
                String lbLogic = entityJsonObject.getString("logic");
                ids.put(LIGHT_BULB_OFF, ids.get(LIGHT_BULB_OFF) + 1);
                return new LightBulb(pos, null, LIGHT_BULB_OFF + ":" + ids.get(LIGHT_BULB_OFF), lbLogic);
            case WIRE:
                ids.put(WIRE, ids.get(WIRE) + 1);
                return new Wire(pos, null, WIRE + ":" + ids.get(WIRE), this.getCurrTickAsRef());
            case SWITCH_DOOR:
                String sdLogic = entityJsonObject.getString("logic");
                ids.put(SWITCH_DOOR, ids.get(SWITCH_DOOR) + 1);
                return new SwitchDoor(pos, entityJsonObject.getInt(KEY), null, SWITCH_DOOR + ":" + ids.get(SWITCH_DOOR), sdLogic);
            default:
                return null;
        }
    }

    /**
     * Based on the given randomized prim's algorithm, we create a dungeon map and load it in.
     * @param xStart The starting x position of the randomly created dungeon
     * @param yStart The starting y position of the randomly created dungeon
     * @param xEnd The ending x position of the randomly created dungeon
     * @param yEnd The ending y position of the randomly created dungeon
     */
    private void loadRandomDungeon(int xStart, int yStart, int xEnd, int yEnd) {
        // Initalize maze
        boolean[][] maze = DungeonGenerator.generatePrimsDungeon(xStart, yStart, xEnd, yEnd); 

        // Iterate through the maze and add a wall for all false.
        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                if (!maze[x + MAZE_OFFSET][y + MAZE_OFFSET] && (!(x == xEnd - 1 && y == yEnd))) {
                    addEntityAfterCreation(WALL, new Position(x, y));
                }
            }
        }

        // Add an exit at the end.
        addEntityAfterCreation(EXIT, new Position(xEnd, yEnd));

        // Put the player at the start.    
        addEntityAfterCreation(PLAYER, new Position(xStart, yStart));

        // Put vertical walls around maze
        for (int y = yStart; y <= yEnd; y++) {
            addEntityAfterCreation(WALL, new Position(xStart - 1, y));            
            addEntityAfterCreation(WALL, new Position(xEnd + 1, y));            
        }

        // Put horizontal walls around maze
        for (int x = xStart; x <= xEnd; x++) {
            addEntityAfterCreation(WALL, new Position(x, yStart - 1));            
            addEntityAfterCreation(WALL, new Position(x, yEnd + 1));   
        }
    }
}
