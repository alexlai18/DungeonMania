package dungeonmania.entity.movingentity.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.config.Config;
import dungeonmania.entity.inventoryitem.InventoryItem;
import dungeonmania.entity.inventoryitem.Weapon;
import dungeonmania.entity.inventoryitem.buildableitem.*;
import dungeonmania.entity.inventoryitem.collectableitem.*;


public class PlayerWeaponry implements Serializable {
    private List<InventoryItem> weaponryEquipped = new ArrayList<>();
    private Config config;
    private int attack;
    private int defence;
    
    public PlayerWeaponry(Config config) {
        this.config = config;
        this.attack = config.getPlayerAttack();
        this.defence = 0;
    }

    public List<InventoryItem> getWeaponry() {
        return this.weaponryEquipped;
    }

    public boolean hasWeapons() {
        return !this.weaponryEquipped.isEmpty();
    }
    
    public void addWeapon(InventoryItem wep) {
        weaponryEquipped.add(wep);
        setAttack(calculateAttack());
        setDefence(calculateDefence());
    }

    public void removeWeapon(InventoryItem wep) {
        weaponryEquipped.remove(wep);
        setAttack(calculateAttack());
        setDefence(calculateDefence());
    }

    public void checkDurability() {
        List<InventoryItem> inventoryCopy = new ArrayList<>(weaponryEquipped);
        for (InventoryItem entity : inventoryCopy) {
            Weapon weapon = (Weapon) entity;
            if (weapon.getDurability() == 0) {
                this.removeWeapon(entity);
            }
        }
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefence() {
        return this.defence;
    }

    public int calculateAttack() {
        int newAttack = config.getPlayerAttack();

        InventoryItem sword = weaponryEquipped.stream().filter(Sword.class :: isInstance).findAny().orElse(null);
        int swordAmount = (int) weaponryEquipped.stream().filter(Sword.class :: isInstance).count();
        
        InventoryItem bow = weaponryEquipped.stream().filter(Bow.class :: isInstance).findAny().orElse(null);
        int bowAmount = (int) weaponryEquipped.stream().filter(Bow.class :: isInstance).count();
        
        InventoryItem midnightArmour = weaponryEquipped.stream().filter(MidnightArmour.class :: isInstance).findAny().orElse(null);
        int armourAmount = (int) weaponryEquipped.stream().filter(MidnightArmour.class :: isInstance).count();

        // After checking instance we can safely cast these types
        if (sword != null) {
            newAttack += (((Sword) sword).getDamage() * swordAmount);
        }

        if (midnightArmour != null) {
            newAttack += (((MidnightArmour) midnightArmour).getDamage() * armourAmount);
        }

        if (bow != null) {
            newAttack *= (((Bow) bow).getMultiplier() * bowAmount);
        }
        return newAttack;
    }

    public int calculateDefence() {
        int newDefence = 0;
        InventoryItem shield = weaponryEquipped.stream().filter(Shield.class :: isInstance).findAny().orElse(null);
        int shieldAmount = (int) weaponryEquipped.stream().filter(Shield.class :: isInstance).count();

        InventoryItem midnightArmour = weaponryEquipped.stream().filter(MidnightArmour.class :: isInstance).findAny().orElse(null);
        int midnight_armour = (int) weaponryEquipped.stream().filter(MidnightArmour.class :: isInstance).count();

        if (weaponryEquipped.stream().anyMatch(Shield.class :: isInstance) && shield != null) {
            newDefence += (((Shield) shield).getDefence() * shieldAmount);
        }

        if (midnightArmour != null) {
            newDefence += (((MidnightArmour) midnightArmour).getDefence() * midnight_armour);
        }

        return newDefence;
    }
}
