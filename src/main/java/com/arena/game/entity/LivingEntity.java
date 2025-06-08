package com.arena.game.entity;

import com.arena.game.entity.champion.Garen;
import com.arena.utils.Position;
import com.arena.utils.Vector3f;

public abstract class LivingEntity extends Entity implements ILiving {
    protected int health, maxHealth;
    protected int armor, magicResist, attackDamage, abilityPower;
    protected boolean moving;
    protected float moveSpeed, rotationY, posX, posZ, posY, posXDesired, posZDesired, posYDesired;
    protected String name;
    protected int team;
    protected long cooldownQStart, cooldownWStart, cooldownEStart, cooldownRStart, cooldownQEnd, cooldownWEnd, cooldownEEnd, cooldownREnd, cooldownQMs, cooldownWMs, cooldownEMs, cooldownRMs;

    public LivingEntity(String id, int maxHealth, int team, String name) {
        super(id);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.team = team;
        this.name = name;
    }

    public void setPos(Vector3f vector3f) {
        this.posX = vector3f.x;
        this.posY = vector3f.y;
        this.posZ = vector3f.z;
    }

    public void setPos(Position position) {
        this.posX = position.pos.x;
        this.posY = position.pos.y;
        this.posZ = position.pos.z;
        this.rotationY = position.rotY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LivingEntity that = (LivingEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
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
    @Override public float getPosYDesired() { return posYDesired; }
    @Override public void setPosYDesired(float y) { this.posYDesired = y; }

    @Override public float getPosX() { return posX; }
    @Override public void setPosX(float x) { this.posX = x; }
    @Override public float getPosZ() { return posZ; }
    @Override public void setPosZ(float z) { this.posZ = z; }
    @Override public float getPosY() { return posY; }
    @Override public void setPosY(float y) { this.posY = y; }

    @Override public void setRotationY(float rotationY) { this.rotationY = rotationY; }
    @Override public float getRotationY() { return rotationY; }

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

    @Override public void setCooldownQStart(long cooldownQStart) {
        this.cooldownQStart = cooldownQStart;
    }
    @Override public long getCooldownQStart() {
        return cooldownQStart;
    }
    @Override public void setCooldownWStart(long cooldownWStart) {
        this.cooldownWStart = cooldownWStart;
    }
    @Override public long getCooldownWStart() {
        return cooldownWStart;
    }
    @Override public void setCooldownEStart(long cooldownEStart) {
        this.cooldownEStart = cooldownEStart;
    }
    @Override public long getCooldownEStart() {
        return cooldownEStart;
    }
    @Override public void setCooldownRStart(long cooldownRStart) {
        this.cooldownRStart = cooldownRStart;
    }
    @Override public long getCooldownRStart() {
        return cooldownRStart;
    }
    @Override public void setCooldownQEnd(long cooldownQEnd) {
        this.cooldownQEnd = cooldownQEnd;
    }
    @Override public long getCooldownQEnd() {
        return cooldownQEnd;
    }
    @Override public void setCooldownWEnd(long cooldownWEnd) {
        this.cooldownWEnd = cooldownWEnd;
    }
    @Override public long getCooldownWEnd() {
        return cooldownWEnd;
    }
    @Override public void setCooldownEEnd(long cooldownEEnd) {
        this.cooldownEEnd = cooldownEEnd;
    }
    @Override public long getCooldownEEnd() {
        return cooldownEEnd;
    }
    @Override public void setCooldownREnd(long cooldownREnd) {
        this.cooldownREnd = cooldownREnd;
    }
    @Override public long getCooldownREnd() {
        return cooldownREnd;
    }
    @Override public void setCooldownQMs(long cooldownQMs) {
        this.cooldownQMs = cooldownQMs;
    }
    @Override public long getCooldownQMs() {
        return cooldownQMs;
    }
    @Override public void setCooldownWMs(long cooldownWMs) {
        this.cooldownWMs = cooldownWMs;
    }
    @Override public long getCooldownWMs() {
        return cooldownWMs;
    }
    @Override public void setCooldownEMs(long cooldownEMs) {
        this.cooldownEMs = cooldownEMs;
    }
    @Override public long getCooldownEMs() {
        return cooldownEMs;
    }
    @Override public void setCooldownRMs(long cooldownRMs) {
        this.cooldownRMs = cooldownRMs;
    }
    @Override public long getCooldownRMs() {
        return cooldownRMs;
    }
}
