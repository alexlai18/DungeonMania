package dungeonmania.interfaces.animation;

import java.util.List;

import dungeonmania.response.models.AnimationQueue;

public interface Animation {
    public abstract List<AnimationQueue> getAnimation();
}
