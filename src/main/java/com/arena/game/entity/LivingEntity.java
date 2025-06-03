package com.arena.game.entity;

public abstract class LivingEntity extends Entity implements ILiving {
    protected int health, maxHealth;
    protected int armor, magicResist, attackDamage, abilityPower;
    protected float moveSpeed;
    protected boolean moving;
    protected float posX, posZ, posXDesired, posZDesired;
    protected String name;

    protected int team;

    public LivingEntity(String id, int maxHealth, int team, String name) {
        super(id);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.team = team;
        this.name = name;
    }

    @Override public int getHealth() { return health; }
    @Override public void heal(int amount) {
        this.health = Math.min(this.health + amount, maxHealth);
    }
    @Override public int getMaxHealth() { return maxHealth; }
    @Override public int setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        return this.maxHealth;
    }
    @Override public void takeDamage(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    @Override public int getArmor() { return armor; }
    @Override public int setArmor(int armor) {
        this.armor = armor; return this.armor;
    }

    @Override public int getMagicResist() { return magicResist; }
    @Override public int setMagicResist(int magicResist) {
        this.magicResist = magicResist; return this.magicResist;
    }

    @Override public int getAttackDamage() { return attackDamage; }
    @Override public int setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage; return this.attackDamage;
    }

    @Override public int getAbilityPower() { return abilityPower; }
    @Override public int setAbilityPower(int abilityPower) {
        this.abilityPower = abilityPower; return this.abilityPower;
    }

    @Override public float getMoveSpeed() { return moveSpeed; }
    @Override public boolean isMoving() { return moving; }
    @Override public void setMoving(boolean moving) { this.moving = moving; }

    @Override public float getPosXDesired() { return posXDesired; }
    @Override public void setPosXDesired(float x) { this.posXDesired = x; }
    @Override public float getPosZDesired() { return posZDesired; }
    @Override public void setPosZDesired(float z) { this.posZDesired = z; }

    @Override public float getPosX() { return posX; }
    @Override public void setPosX(float x) { this.posX = x; }
    @Override public float getPosZ() { return posZ; }
    @Override public void setPosZ(float z) { this.posZ = z; }

    @Override
    public int getTeam() {
        return team;
    }
    @Override
    public void setTeam(int team) {
        this.team = team;
    }

    @Override
    public String getName() {
        return name;
    }
}
