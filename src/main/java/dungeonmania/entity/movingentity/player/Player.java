package dungeonmania.entity.movingentity.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dungeonmania.config.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.interfaces.movement.NoMovement;
import dungeonmania.interfaces.animation.Animation;
import dungeonmania.interfaces.movement.ControlledMovingEntity;
import dungeonmania.interfaces.movement.MercenaryMovement;
import dungeonmania.interfaces.overlapinteract.TraversableOverlapInteract;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.*;
import dungeonmania.entity.Entity;
import dungeonmania.entity.inventoryitem.*;
import dungeonmania.entity.inventoryitem.buildableitem.*;
import dungeonmania.entity.inventoryitem.collectableitem.*;
import dungeonmania.entity.movingentity.*;
import dungeonmania.exceptions.*;


public class Player extends MovingEntity implements Animation {
    private PlayerInventory inventory;
    private PlayerWeaponry weaponryEquipped;
    private List<Potion> potionQueue = new ArrayList<>();
    private Potion currentPotion;
    private double defence;
    private int killCounter;
    private boolean canBeBribed; 

    public Player(Position position, List<Entity> adjacentEntities, Config config, String id, Dungeon dungeon) {
        super(position, adjacentEntities, config.getPlayerAttack(), config.getPlayerHealth(), id, dungeon);
        setMovement(new ControlledMovingEntity());
        this.inventory = new PlayerInventory(config);
        this.weaponryEquipped = new PlayerWeaponry(config);
        this.defence = 0;
        this.currentPotion = null;
        this.killCounter = 0;
        this.canBeBribed = true; 
        setType("player");
    }

    public List<ItemResponse> getItemListResponse() {
        List<ItemResponse> itemList = new ArrayList<>();
        for (InventoryItem item : getInventoryList()) {
            itemList.add(new ItemResponse(item.getId(), item.getType()));
        }
        return itemList;
    }

    public int getKills() {
        return this.killCounter;
    }

    public boolean getCanBeBribed() {
        return this.canBeBribed;
    }

    public void setCanBeBribed(boolean canBeBribed) {
        this.canBeBribed = canBeBribed;
    }

    public void addKill() {
        this.killCounter += 1;
    }

    public void bribe(MovingEntity entity) throws InvalidActionException {

        if (entity instanceof Mercenary) {
            mercenaryBribe((Mercenary) entity);
        } else {
            assassinBribe((Assassin) entity);
        }
    }

    public void mercenaryBribe(Mercenary mercenary) throws InvalidActionException {
        if (mercenary.getBribed()) {
            throw new InvalidActionException("Mercenary is already bribed");
        }

        if (getPlayerInventory().numTreasure() < mercenary.getBribeAmount()) {
            throw new InvalidActionException("Not enough treasure to bribe");
        } 

        int lowLimitX = this.getPosition().getX() - mercenary.getBribeRadius();
        int highLimitX = this.getPosition().getX() + mercenary.getBribeRadius();
        int lowLimitY = this.getPosition().getY() - mercenary.getBribeRadius();
        int highLimitY = this.getPosition().getY() + mercenary.getBribeRadius();

        if (mercenary.getPosition().getX() >= lowLimitX && mercenary.getPosition().getX() <= highLimitX
                && mercenary.getPosition().getY() >= lowLimitY && mercenary.getPosition().getY() <= highLimitY) {
            mercenary.setBribed(true);
            getPlayerInventory().removeTreasure(mercenary.getBribeAmount());
            mercenary.setOverlapInteract(new TraversableOverlapInteract());
            mercenary.setMovement(new NoMovement());
        } else {
            throw new InvalidActionException("Mercenary is too far away");
        }
    }

    public void assassinBribe(Assassin assassin) throws InvalidActionException {
        if (assassin.getBribed()) {
            throw new InvalidActionException("Assassin is already bribed");
        }

        if (getPlayerInventory().numTreasure() < assassin.getBribeAmount()) {
            throw new InvalidActionException("Not enough treasure to bribe");
        } 

        int lowLimitX = this.getPosition().getX() - assassin.getBribeRadius();
        int highLimitX = this.getPosition().getX() + assassin.getBribeRadius();
        int lowLimitY = this.getPosition().getY() - assassin.getBribeRadius();
        int highLimitY = this.getPosition().getY() + assassin.getBribeRadius();

        if (assassin.getPosition().getX() >= lowLimitX && assassin.getPosition().getX() <= highLimitX
                && assassin.getPosition().getY() >= lowLimitY && assassin.getPosition().getY() <= highLimitY) {


            if (!getCanBeBribed()) {
                throw new InvalidActionException("Assassin can't be bribed");
            }
            
            Random rand = new Random(); 
            if (rand.nextDouble() <= assassin.getBribeFailRate()) {
                getPlayerInventory().removeTreasure(assassin.getBribeAmount());
                setCanBeBribed(false);
            } else {
                assassin.setBribed(true);
                getPlayerInventory().removeTreasure(assassin.getBribeAmount());
                assassin.setOverlapInteract(new TraversableOverlapInteract());
                assassin.setMovement(new NoMovement());
            }

        } else {
            throw new InvalidActionException("Assassin is too far away");
        }
    }

    public void mindControl(MovingEntity merc, boolean b) {
        // Make sure to do for assassin too
        if (merc == null) {
            // The enemy which is mind controlled does not exist anymore
            return;
        }

        for (InventoryItem item : getInventoryList()) {
            if (item instanceof Sceptre) {
                ((Sceptre) item).activate();
            }
        }
        if (merc instanceof Mercenary) {
            ((Mercenary) merc).setMindControlled(b);
        } else if (merc instanceof Assassin) {
            ((Assassin) merc).setMindControlled(b);
        }
    }

    public void useSceptre(Dungeon dungeon, List<Entity> entities) {
        // TODO: Have to add assassin here
        List<InventoryItem> inventoryCopy = new ArrayList<>(getInventoryList());
        for (InventoryItem item : inventoryCopy) {
            if (item instanceof Sceptre) {
                Sceptre sceptre = (Sceptre) item;
                if (!sceptre.getActivated()) {
                    return;
                }

                MovingEntity merc = null; 
                for (Entity entity : entities) {
                    if (entity instanceof Mercenary && ((Mercenary) entity).getMindControlled()) {
                        merc = (Mercenary) entity;
                    } else if (entity instanceof Assassin && ((Assassin) entity).getMindControlled()) {
                        merc = (Assassin) entity;
                    }
                }

                if (merc == null) {
                    if (sceptre.getDuration() > 0) {
                        sceptre.use();
                    } else {
                        itemUsed(sceptre);
                    }
                    return;
                }

                // Item is removed from inventory if its duration is 0
                if (sceptre.getDuration() > 0) {
                    sceptre.use();
                    mindControl(merc, true);
                    merc.setMovement(new NoMovement());
                } else {
                    mindControl(merc, false);
                    merc.setMovement(new MercenaryMovement(dungeon));
                    itemUsed(sceptre);
                }
                return;
            }
        }
    }

    public void collectEntity(InventoryItem item) {
        if (!item.collectItem()) {
            return;
        }

        getPlayerInventory().addToInventory(item);

        if (item instanceof Bow || item instanceof Shield || item instanceof Sword) {
            equipWeapon(item);
        }
        setAttack(getWeaponryEquipped().calculateAttack());
        setDefence(getWeaponryEquipped().calculateDefence());
    }

    public void itemUsed(InventoryItem item) {
        getPlayerInventory().removeFromInventory(item);
    }

    public PlayerInventory getPlayerInventory() {
        return this.inventory;
    }

    public List<InventoryItem> getInventoryList() {
        return getPlayerInventory().getInventory();
    }

    public PlayerWeaponry getWeaponryEquipped() {
        return this.weaponryEquipped;
    }

    public List<InventoryItem> getWeaponryList() {
        return getWeaponryEquipped().getWeaponry();
    }

    public boolean hasWeapon() {
        return getWeaponryEquipped().hasWeapons();
    }

    public void equipWeapon(InventoryItem weaponry) {
        getWeaponryEquipped().addWeapon(weaponry);
        setAttack(getWeaponryEquipped().calculateAttack());
        setDefence(getWeaponryEquipped().calculateDefence());
    }

    public InventoryItem buildEntity(String type) throws InvalidActionException {
        return getPlayerInventory().buildEntity(type, getWeaponryEquipped());
    }

    public Boolean canCraft(String type) {
        return getPlayerInventory().canCraft(type);
    }

    public double getDefence() {
        return this.defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void checkWeaponDurability() {
        getWeaponryEquipped().checkDurability();
        getPlayerInventory().checkDurability();
        setAttack(getWeaponryEquipped().getAttack());
        setDefence(getWeaponryEquipped().getDefence());
    }

    public List<Key> getKeys() {
        return getPlayerInventory().getKeyList();
    }

    public void useItem(String itemUsedId, Dungeon dungeon) throws IllegalArgumentException, InvalidActionException {
        InventoryItem item = getPlayerInventory().findItem(itemUsedId);
        UsableItem usable = (UsableItem) item;
        usable.activateItem(this, dungeon);
        itemUsed(item);
    }

    public void oldUseItem(String itemUsedId, Dungeon dungeon) throws IllegalArgumentException, InvalidActionException {
        InventoryItem item = getPlayerInventory().findOldItem(itemUsedId);
        UsableItem usable = (UsableItem) item;
        usable.activateItem(this, dungeon);
        itemUsed(item);
    }

    public Potion getCurrentPotion() {
        return this.currentPotion;
    }

    public List<Potion> getPotionQueue() {
        return this.potionQueue;
    }

    public void setCurrentPotion(Potion potion) {
        this.currentPotion = potion;
    }

    public void updatePotionQueue() {
        potionQueue.remove(0);
    }

    public int getNumTreasure() {
        return this.inventory.numTreasure() + this.inventory.numSunStone();
    }

    public List<String> getBuildables() {
        List<String> buildables = new ArrayList<>();
        if (canCraft("bow")) {
            buildables.add("bow");
        }
        if (canCraft("shield")) {
            buildables.add("shield");
        }
        if (canCraft("midnight_armour")) {
            buildables.add("midnight_armour");
        }
        if (canCraft("sceptre")) {
            buildables.add("sceptre");
        }
        return buildables;
    }

    @Override
    public void setMovementToOriginal(Dungeon dungeon) {
        setMovement(new ControlledMovingEntity());
    }

    public void makeOld() {
        setType("older_player");
        ((ControlledMovingEntity) this.getMovement()).setAllies(new ArrayList<>());
    }

    public boolean bypassPlayerFight() {
        return getInventoryList().stream().anyMatch(e -> (e instanceof MidnightArmour || e instanceof SunStone))
            || getCurrentPotion() instanceof InvisibilityPotion;
    }

    @Override
    public List<AnimationQueue> getAnimation() {
        List<AnimationQueue> animations = new ArrayList<>();
        
        if (getCurrentPotion() instanceof InvincibilityPotion) {
            if (getType().equals("player")) {
                animations.add(new AnimationQueue("PostTick", getId(), Arrays.asList("sprite player_invincible"), false, -1));
            } else {
                animations.add(new AnimationQueue("PostTick", getId(), Arrays.asList("sprite older_player_invincible"), false, -1));
            }
        } else if (getCurrentPotion() instanceof InvisibilityPotion) {
            if (getType().equals("player")) {
                animations.add(new AnimationQueue("PostTick", getId(), Arrays.asList("sprite player_invisible"), false, -1));
            } else {
                animations.add(new AnimationQueue("PostTick", getId(), Arrays.asList("sprite older_player_invisible"), false, -1));
            }
        }

        return animations;
    }
}
