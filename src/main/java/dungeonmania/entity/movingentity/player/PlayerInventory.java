package dungeonmania.entity.movingentity.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.config.Config;
import dungeonmania.entity.inventoryitem.*;
import dungeonmania.entity.inventoryitem.buildableitem.*;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.exceptions.InvalidActionException;



public class PlayerInventory implements Serializable {
    private List<InventoryItem> inventory = new ArrayList<>();
    private Config config;
    private int bowCount = 0;
    private int shieldCount = 0;
    private int armourCount = 0;
    private int sceptreCount = 0;
    
    public PlayerInventory (Config config) {
        // This just stores the entities
        this.config = config;
    }

    public List<InventoryItem> getInventory() {
        return this.inventory;
    }

    public void addToInventory(InventoryItem item) {
        if (item instanceof Key && inventory.stream().anyMatch(Key.class :: isInstance)) {
            Key k = (Key) item;
            k.stopCollection();
            return;
        }
        item.setPosition(null);
        inventory.add(item);
    }

    public void removeFromInventory(InventoryItem item) {
        inventory.remove(item);
    }

    public void checkDurability() {
        List<InventoryItem> inventoryCopy = new ArrayList<>(inventory);

        for (InventoryItem entity : inventoryCopy) {
            if (entity instanceof Weapon) {
                Weapon weapon = (Weapon) entity;
                if (weapon.getDurability() == 0) {
                    this.removeFromInventory(entity);
                }
            }
        }
    }

    public InventoryItem buildEntity(String type, PlayerWeaponry weaponry) throws InvalidActionException {
        if (type.equals("bow") && canCraft(type)) {
            InventoryItem bow = craftBow();
            addToInventory(bow);
            return bow;
        } else if (type.equals("shield") && canCraft(type)) {
            InventoryItem shield = craftShield(inventory.stream().anyMatch(Treasure.class :: isInstance) || inventory.stream().anyMatch(SunStone.class :: isInstance));
            addToInventory(shield);
            return shield;
        } else if (type.equals("midnight_armour") && canCraft(type)) {
            InventoryItem armour = craftArmour(weaponry);
            addToInventory(armour);
            return armour;
        } else if (type.equals("sceptre") && canCraft(type)) {
            InventoryItem sceptre = craftSceptre(weaponry);
            addToInventory(sceptre);
            return sceptre;
        } else {
            throw new InvalidActionException("You don't have the resources!");
        }
    }

    public Boolean canCraft(String type) {
        if (type.equals("bow")) {
            int arrows = (int) inventory.stream().filter(Arrows.class :: isInstance).count();
            Boolean containsWood = inventory.stream().anyMatch(Wood.class :: isInstance);
            return arrows >= 3 && containsWood;
        } else if (type.equals("shield")) {
            int wood = (int) inventory.stream().filter(Wood.class :: isInstance).count();
            Boolean containsTreasure = inventory.stream().anyMatch(Treasure.class :: isInstance);
            Boolean containsKey = inventory.stream().anyMatch(Key.class :: isInstance);
            return wood >= 2 && (containsKey || containsTreasure);
        } else if (type.equals("midnight_armour")) {
            Boolean containsSword = inventory.stream().anyMatch(Sword.class :: isInstance);
            Boolean containsStone = inventory.stream().anyMatch(SunStone.class :: isInstance);
            return containsStone && containsSword;
        }  else if (type.equals("sceptre")) {
            Boolean containsWood = inventory.stream().anyMatch(Wood.class :: isInstance);
            int arrows = (int) inventory.stream().filter(Arrows.class :: isInstance).count();
            Boolean containsKey = inventory.stream().anyMatch(Key.class :: isInstance);
            Boolean containsTreasure = inventory.stream().anyMatch(Treasure.class :: isInstance);
            Boolean containsStone = inventory.stream().anyMatch(SunStone.class :: isInstance);
            return (arrows >= 2 || containsWood) && (containsKey || containsTreasure) && containsStone;
        } else {
            return false;
        }
    }

    public InventoryItem craftBow() {
        int wood = 0;
        int arrows = 0;
        List<InventoryItem> inventoryCopy = new ArrayList<>(inventory);
        for (InventoryItem entity : inventoryCopy) {
            if (wood == 1 && arrows == 3) {
                break;
            }
            
            if (entity instanceof Wood && wood < 1) {
                wood += 1;
                this.removeFromInventory(entity);
            }

            if (entity instanceof Arrows && arrows < 3) {
                arrows += 1;
                this.removeFromInventory(entity);
            }
        }

        bowCount += 1;
        return new Bow(null, null, config.getBowDurability(), "bow:" + bowCount);
    }

    public InventoryItem craftShield(Boolean b) {
        int wood = 0;
        int metal = 0;
        // Check if it has sunstone
        List<InventoryItem> inventoryCopy = new ArrayList<>(inventory);
        for (InventoryItem entity : inventoryCopy) {
            if (wood == 2 && metal == 1) {
                break;
            }

            if (entity instanceof Wood && wood < 2) {
                wood += 1;
                this.removeFromInventory(entity);
            }

            if (entity instanceof Key && !b) {
                metal += 1;
                this.removeFromInventory(entity);
            }

            if ((entity instanceof Treasure || entity instanceof SunStone) && b) {
                metal += 1;
                this.removeFromInventory(entity);
            }
        }
           
        shieldCount += 1;
        return new Shield(null, null, config.getShieldDurability(), config.getShieldDefence(), "shield:" + shieldCount);
    }

    public InventoryItem craftArmour(PlayerWeaponry weaponry) {
        int sword = 0;
        int sunstone = 0;
        // Check if it has sunstone
        List<InventoryItem> inventoryCopy = new ArrayList<>(inventory);
        for (InventoryItem entity : inventoryCopy) {
            if (sword == 1 && sunstone == 1) {
                break;
            }

            if (entity instanceof SunStone && sunstone < 2) {
                sunstone += 1;
                this.removeFromInventory(entity);
            }

            if (entity instanceof Sword) {
                sword += 1;
                this.removeFromInventory(entity);
                weaponry.removeWeapon(entity);
            }
        }
           
        armourCount += 1;
        return new MidnightArmour(null, null, config.getMidnightArmourAttack(), config.getMidnightArmourDefence(), "midnight_armour:" + armourCount);
    }

    public InventoryItem craftSceptre(PlayerWeaponry weaponry) {
        // Uses wood before arrows, Treasure before key
        int sunstone = 0;
        List<InventoryItem> inventoryCopy = new ArrayList<>(inventory);
        if (inventory.stream().anyMatch(Wood.class :: isInstance)) {
            int wood = 0;
            if (inventory.stream().anyMatch(Treasure.class :: isInstance)) {
                int treasure = 0;
                for (InventoryItem entity : inventoryCopy) {
                    if (treasure == 1 && wood == 1 && sunstone == 1) {
                        break;
                    }

                    if (entity instanceof SunStone && sunstone < 1) {
                        sunstone += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Wood && wood < 1) {
                        wood += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Treasure && treasure < 1) {
                        treasure += 1;
                        this.removeFromInventory(entity);
                    }
                }
            } else {
                int key = 0;
                for (InventoryItem entity : inventoryCopy) {
                    if (key == 1 && wood == 1 && sunstone == 1) {
                        break;
                    }

                    if (entity instanceof SunStone && sunstone < 1) {
                        sunstone += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Wood && wood < 1) {
                        wood += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Key && key < 1) {
                        key += 1;
                        this.removeFromInventory(entity);
                    }
                }
            }
        } else {
            int arrow = 0;
            if (inventory.stream().anyMatch(Treasure.class :: isInstance)) {
                int treasure = 0;
                for (InventoryItem entity : inventoryCopy) {
                    if (treasure == 1 && arrow == 2 && sunstone == 1) {
                        break;
                    }

                    if (entity instanceof SunStone && sunstone < 1) {
                        sunstone += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Arrows && arrow < 2) {
                        arrow += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Treasure && treasure < 1) {
                        treasure += 1;
                        this.removeFromInventory(entity);
                    }
                }
            } else {
                int key = 0;
                for (InventoryItem entity : inventoryCopy) {
                    if (key == 1 && arrow == 2 && sunstone == 1) {
                        break;
                    }

                    if (entity instanceof SunStone && sunstone < 1) {
                        sunstone += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Arrows && arrow < 2) {
                        arrow += 1;
                        this.removeFromInventory(entity);
                    }

                    if (entity instanceof Key && key < 1) {
                        key += 1;
                        this.removeFromInventory(entity);
                    }
                }
            }
        }
        sceptreCount += 1;
        return new Sceptre(null, null, config.getMindControlDuration(), "sceptre:" + sceptreCount);
    }


    public List<Key> getKeyList() {
        List<Key> keyList = new ArrayList<>();
        for (InventoryItem item : inventory) {
            if (item instanceof Key) {
                keyList.add((Key)item);
            }
        }
        return keyList;
    }

    public int numTreasure() {
        return ((int) this.inventory.stream().filter(item -> item instanceof Treasure).count());
    }

    public int numSunStone() {
        return ((int) this.inventory.stream().filter(item -> item instanceof SunStone).count());
    }

    public void removeTreasure(int bribeAmount) {
        for (int i = 0; i < bribeAmount; i++) {
            for (int j = 0; j < this.inventory.size(); j++) {
                if (this.inventory.get(j) instanceof Treasure) {
                    this.removeFromInventory(this.inventory.get(j));
                    break;
                }
            }
        }
    }

    public InventoryItem findItem(String itemId) throws IllegalArgumentException, InvalidActionException {
        for (InventoryItem item : inventory) {
            if (item.getId().equals(itemId)) {
                if (!(item instanceof Bomb) && !(item instanceof Potion)) {
                    throw new IllegalArgumentException("You cannot use this item");
                }
                return item;
            }
        }
        throw new InvalidActionException("This item does not exist!");
    }

    public InventoryItem findOldItem(String itemId) throws IllegalArgumentException, InvalidActionException {
        for (InventoryItem item : inventory) {
            if (item.getId().startsWith(itemId)) {
                if (!(item instanceof Bomb) && !(item instanceof Potion)) {
                    throw new IllegalArgumentException("You cannot use this item");
                }
                return item;
            }
        }
        throw new InvalidActionException("This item does not exist!");
    }
}
