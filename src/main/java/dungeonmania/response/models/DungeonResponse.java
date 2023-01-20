package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;

public final class DungeonResponse {
    private final String dungeonId;
    private final String dungeonName;
    private final List<EntityResponse> entities;
    private final List<ItemResponse> inventory;
    private final List<BattleResponse> battles;
    private final List<String> buildables;
    private final String goals;
    private final List<AnimationQueue> animations;

    public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
            List<ItemResponse> inventory, List<BattleResponse> battles, List<String> buildables, String goals) {
        this(dungeonId, dungeonName, entities, inventory, battles, buildables, goals, new ArrayList<>());
    }

    public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
            List<ItemResponse> inventory, List<BattleResponse> battles, List<String> buildables, String goals,
            List<AnimationQueue> animations) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.entities = entities;
        this.inventory = inventory;
        this.battles = battles;
        this.buildables = buildables;
        this.goals = goals;
        this.animations = animations;
    }

    public List<AnimationQueue> getAnimations() {
        return animations;
    }

    public final String getDungeonName() {
        return dungeonName;
    }

    public final List<ItemResponse> getInventory() {
        return inventory;
    }

    public final List<BattleResponse> getBattles(){
        return battles;
    }

    public final List<String> getBuildables() {
        return buildables;
    }

    public final String getGoals() {
        return goals;
    }

    public final String getDungeonId() {
        return dungeonId;
    }

    public final List<EntityResponse> getEntities() {
        return entities;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DungeonResponse other = (DungeonResponse) obj;

        return dungeonId.equals(other.getDungeonId())
            && dungeonName.equals(other.getDungeonName())
            && this.entities.size() == other.getEntities().size()
            && this.entities.containsAll(other.getEntities())
            && other.getEntities().containsAll(this.entities)
            && other.getInventory().size() == this.inventory.size()
            && other.getInventory().containsAll(this.inventory)
            && this.inventory.containsAll(other.getInventory())
            && other.getBattles().size() == this.battles.size()
            && other.getBattles().containsAll(this.battles)
            && this.battles.containsAll(other.getBattles())
            && other.getBuildables().size() == this.buildables.size()
            && other.getBuildables().containsAll(this.buildables)
            && this.buildables.containsAll(other.getBuildables())
            && other.getGoals().equals(this.goals);
    }
}
