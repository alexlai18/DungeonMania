package dungeonmania.dungeon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import dungeonmania.dungeon.action.Action;

public class DungeonStateManager implements Serializable {

    private List<Dungeon> dungeonList;
    private List<List<Action>> actionList;
    private Dungeon currentDungeon;
    private int currentTick;
    
    public DungeonStateManager(Dungeon currentDungeon) {
        this.dungeonList = new ArrayList<>();
        this.actionList = new ArrayList<>();
        this.currentDungeon = currentDungeon;
        this.currentTick = 0;
    }

    public Dungeon getDungeonStateFromTicks(int tick) {
        return this.dungeonList.get(this.currentTick + 1 - tick);
    }

    public void addDungeonState() {
        try {
            String dungeonString = dungeonObjectToString(this.currentDungeon);
            Dungeon dungeonCopy = (Dungeon) dungeonObjectFromString(dungeonString);
            // change the ids of everything inside dungeon copy
            if (dungeonCopy.getPlayer() != null) {
                dungeonCopy.getPlayer().setId(dungeonCopy.getPlayer().getId() + ":tick:" + (this.currentTick));
            }
            dungeonCopy.getEntities().stream().forEach(e -> e.setId(e.getId() + ":tick:" + (this.currentTick)));
            this.dungeonList.add(dungeonCopy);
            this.actionList.add(new ArrayList<>());
            if (dungeonList.size() > 1) this.currentTick++;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // initial state is at 0 index so will have size 1 already
    // so must add one before the size as tick 1 at index 1
    public void addAction(Action action) {
        this.actionList.get(this.currentTick).add(action);
    }

    public List<Action> getOldNextActions() {
        return this.actionList.get(this.currentTick);
    }

    public void clearOldNextActions() {
        this.actionList.get(this.currentTick).clear();
    }

    private static String dungeonObjectToString(Serializable dungeon) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOutput);
        output.writeObject(dungeon);
        output.close();
        return Base64.getEncoder().encodeToString(byteOutput.toByteArray()); 
    }

    private static Object dungeonObjectFromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
   }

    public void rewind(int ticks) throws IllegalArgumentException {
        if (ticks <= 0 || ticks > this.currentTick) {
            throw new IllegalArgumentException("Invalid number of ticks specified.");
        }
        currentDungeon.rewind(this.getDungeonStateFromTicks(ticks));
        currentTick -= ticks;
    }

    public void handleTimeTravelPortal() {
        if (this.currentDungeon.isPlayerOnTimeTravelPortal()) {
            this.addAction(new Action(Action.REWIND));
            if (this.currentTick >= 30) {
                this.rewind(30);
            } else {
                this.rewind(this.currentTick);
            }
        }
    }
    
}