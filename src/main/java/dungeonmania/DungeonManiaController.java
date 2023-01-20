package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.dungeon.*;
import dungeonmania.dungeon.action.Action;
import dungeonmania.Persistence.Persistence;
import dungeonmania.entity.movingentity.ZombieToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DungeonManiaController {

    /**
     * Holds a reference to the current dungeon.
     */
    private Dungeon currentDungeon;
    
    /**
     * Holds a number for the dungeon we're up to.
     */
    private int id = 1;

    /**
     * Holds the list of saved games.
     */
    private List<String> games = new ArrayList<String>();

    /**
     * Holds the list of previous dungeon states
     */
    private DungeonStateManager dungeonStateManager;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        // Check if a valid dungeon name is given
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException("Invalid dungeon name");
        }

        // Check if a valid config name is given
        checkValidConfigName(configName);

        currentDungeon = new Dungeon(dungeonName, configName, id);
        id += 1;
        this.dungeonStateManager = new DungeonStateManager(currentDungeon);
        dungeonStateManager.addDungeonState();

        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        checkValidItemArgument(itemUsedId);

        dungeonStateManager.addDungeonState();
        currentDungeon.itemTick(itemUsedId, dungeonStateManager.getOldNextActions());
        dungeonStateManager.clearOldNextActions();
        dungeonStateManager.addAction(new Action(Action.USE_ITEM, itemUsedId));

        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        dungeonStateManager.addDungeonState();
        currentDungeon.movementTick(movementDirection, dungeonStateManager.getOldNextActions());
        dungeonStateManager.handleTimeTravelPortal();
        dungeonStateManager.clearOldNextActions();
        dungeonStateManager.addAction(new Action(Action.MOVEMENT, movementDirection));
        
        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        checkValidBuildableArgument(buildable);

        currentDungeon.buildItem(buildable);
        dungeonStateManager.addAction(new Action(Action.BUILD, buildable));

        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        checkValidEntityArgument(entityId);

        currentDungeon.interactWith(entityId);
        dungeonStateManager.addAction(new Action(Action.INTERACT, entityId));

        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        Persistence.saveGame(name, allGames(), currentDungeon, dungeonStateManager);
        return currentDungeon.toDungeonResponse();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {

        // Check whether the given name is a valid saved game.
        if (!allGames().contains(name)) {
            throw new IllegalArgumentException("The game:" + name + ", does not exist.");
        }

        Map<String,Object> loaded = Persistence.loadGame(name);
        this.currentDungeon = (Dungeon) loaded.get("currentDungeon");
        this.dungeonStateManager = (DungeonStateManager) loaded.get("dungeonStateManager");
        return currentDungeon.toDungeonResponse();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        // Load in saved games list.    
        List<String> loadedGames = Persistence.loadGamesList();
        
        // Check if saved games list exist
        if (loadedGames != null) {
            games = loadedGames;
        }

        return games;
    }

    /**
     * /api/game/new/generate
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        // Check if a valid config name is given
        checkValidConfigName(configName);
        
        // Create randomized dungeon
        currentDungeon = new Dungeon(xStart, yStart, xEnd, yEnd, configName, id);
        id += 1;
        this.dungeonStateManager = new DungeonStateManager(currentDungeon);
        dungeonStateManager.addDungeonState();

        return currentDungeon.toDungeonResponse();
    }
    
    /**
     * /api/game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {

        dungeonStateManager.addAction(new Action(Action.REWIND));
        dungeonStateManager.rewind(ticks);
        
        return currentDungeon.toDungeonResponse();
    }

    /*
     * 
     * @param itemUsedId the ID of the item being used
     * @throws IllegalArgumentException when itemUsed is not a bomb, invinciblity_potion or 
     * invisbility_potion
     */
    private void checkValidItemArgument(String itemUsedId) throws IllegalArgumentException {
        if (itemUsedId.startsWith("bomb:") || itemUsedId.startsWith("invincibility_potion:")
            || itemUsedId.startsWith("invisibility_potion:")) {
                return;
            }
        throw new IllegalArgumentException("Trying to use " + itemUsedId + " which is invalid");
    }

    /**
     * 
     * @param buildable the ID of the item being built
     * @throws IllegalArgumentException when buildable is not a bow or shield
     */
    private void checkValidBuildableArgument(String buildable) throws IllegalArgumentException, InvalidActionException {
        if (buildable.equals("bow") || buildable.equals("shield") || buildable.equals("midnight_armour") || buildable.equals("sceptre")) {
            if (currentDungeon.getEntities().stream().anyMatch(ZombieToast.class :: isInstance) && buildable.equals("midnight_armour")) {
                throw new InvalidActionException("Trying to build " + buildable + " which is invalid");
            }
            return;
        }
        throw new IllegalArgumentException("Trying to build " + buildable + " which is invalid");
    }

    /**
     * 
     * @param entityID the ID of the entity being interacted with.
     * @throws IllegalArgumentException when entity isn't a valid ID.
     */
    private void checkValidEntityArgument(String entityID) throws IllegalArgumentException {
        if (entityID.startsWith("mercenary:") || entityID.startsWith("zombie_toast_spawner:") || entityID.startsWith("assassin:")) {
            return;
        }
        throw new IllegalArgumentException("Trying to interact with " + entityID + " which is invalid");
    }

    /**
     * 
     * @param config The name of the config we're trying to load.
     * @throws IllegalArgumentException when an invalid config name is given
     */
    private void checkValidConfigName(String config) throws IllegalArgumentException {
        if (!configs().contains(config)) {
            throw new IllegalArgumentException("Invalid config name");
        }
    }
}
