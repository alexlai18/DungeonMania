package dungeonmania.entity.inventoryitem.collectableitem;

import java.util.Arrays;
import java.util.List;

import dungeonmania.entity.Entity;
import dungeonmania.interfaces.animation.Animation;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;

public class Treasure extends CollectableItem implements Animation {
    private static String[] sprites = {
        "sprite treasure",
        "sprite treasure_1",
        "sprite treasure_2",
        "sprite treasure_3",
        "sprite treasure_4",
        "sprite treasure_3",
        "sprite treasure_2",
        "sprite treasure_1"
    };
    private int animationTicker = 0;

    public Treasure(Position position, List<Entity> adjacentEntities, String itemId) {
        super(position, adjacentEntities, itemId);
        setType("treasure");
    }

    @Override
    public List<AnimationQueue> getAnimation() {
        List<AnimationQueue> animations = Arrays.asList(new AnimationQueue(
            "PostTick", 
            getId(), 
            Arrays.asList(sprites[animationTicker]), 
            false, 
            -1
        ));

        animationTicker++;
        animationTicker %= sprites.length;

        return animations;
    }
}
