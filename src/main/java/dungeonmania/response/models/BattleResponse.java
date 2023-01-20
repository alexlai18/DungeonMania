package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;

public final class BattleResponse {
    private final String enemy;
    private final double initialPlayerHealth;
    private final double initialEnemyHealth;
    private final List<RoundResponse> rounds;
    
    public BattleResponse(){
        this.initialPlayerHealth = 0;
        this.initialEnemyHealth = 0;
        this.enemy = "";
        this.rounds = new ArrayList<RoundResponse>();
    }

    public BattleResponse(String enemy, List<RoundResponse> rounds, double initialPlayerHealth, double initialEnemyHealth) {
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.enemy = enemy;
        this.rounds = rounds;
    }

    public final String getEnemy(){
        return enemy;
    }

    public final Double getInitialPlayerHealth(){
        return initialPlayerHealth;
    }

    public final Double getInitialEnemyHealth(){
        return initialEnemyHealth;
    }

    public final List<RoundResponse> getRounds(){
        return rounds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BattleResponse)) return false;

        BattleResponse other = (BattleResponse) obj;
        return other.getRounds().equals(this.rounds) && other.getInitialPlayerHealth().equals(this.initialPlayerHealth) 
            && other.getInitialEnemyHealth().equals(this.initialEnemyHealth) && other.getEnemy().equals(this.enemy);
    }
}
