package dungeonmania.Persistence;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonStateManager;

public class Persistence {
        /**
     * This method saves the current dungeon to a file under the given name.
     * @param name The name of the saved game.
     * @param games A list for all games currently saved.
     * @param currentDungeon The current dungeon we're saving.
     * @param dungeonStateManager list of dungeon states from its creation.
     */
    public static void saveGame(String name, List<String> games, Dungeon currentDungeon, DungeonStateManager dungeonStateManager) {
        try {
            // Creating the object to save the file to
            File saveFile = new File("./" + name + ".ser");
            saveFile.createNewFile();
            FileOutputStream pathToSaveGame = new FileOutputStream("./" + name + ".ser");
            ObjectOutputStream savedGame = new ObjectOutputStream(pathToSaveGame);
                
            // Serializing the dungeon and writing it to a file.
            // store the dungeon state manager as well through a map
            Map<String, Object> toSave = new HashMap<>();
            toSave.put("currentDungeon", currentDungeon);
            toSave.put("dungeonStateManager", dungeonStateManager);
            savedGame.writeObject(toSave);
                
            // Close objects
            savedGame.close();
            pathToSaveGame.close();

            addGameToList(name, games);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param name the name of the game we're trying to load.
     * @return The newly loaded dungeon
     */
    public static Map<String, Object> loadGame(String name) {
        try {
            // Creating the object to read the file from
            FileInputStream savedGamePath = new FileInputStream("./" + name + ".ser");
            ObjectInputStream savedGame = new ObjectInputStream(savedGamePath);
                
            // Reading the serialized dungeon/state manager.
            Map<String, Object> loaded = (Map<String, Object>) savedGame.readObject();
                
            // Close objects
            savedGame.close();
            savedGamePath.close();

            return loaded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method loads in the saved game list, if no list exists, we return null
     * @return null if no saved games exists, saved game list otherwise
     */
    public static List<String> loadGamesList() {
        try {
            // Creating the object to read the file from
            FileInputStream savedGamePath = new FileInputStream("./savedGamesList.ser");
            ObjectInputStream savedGame = new ObjectInputStream(savedGamePath);
                
            // Reading the serialized games list and loading it.
            List<String> games = (List<String>) savedGame.readObject();
                
            // Close objects
            savedGame.close();
            savedGamePath.close();

            return games;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This method adds a saved game to the list of saved games and saves this to a file.
     * @param name The name of the game we're saving to the list
     * @param games The list of saved games we're saving to.
     */
    private static void addGameToList(String name, List<String> games) {
        // Add game to saved games.
        if (!games.contains(name)) {
            games.add(name);
        }    

        try {
            // Creating the object to save the games list to
            File savedGamesList = new File("./savedGamesList.ser");
            savedGamesList.createNewFile();
            FileOutputStream pathToSaveGame = new FileOutputStream("./savedGamesList.ser");
            ObjectOutputStream savedGame = new ObjectOutputStream(pathToSaveGame);
                
            // Serializing the dungeon and writing it to a file.
            savedGame.writeObject(games);
                
            // Close objects
            savedGame.close();
            pathToSaveGame.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
