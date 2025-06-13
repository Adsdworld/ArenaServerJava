package com.arena.game.entity;

import com.arena.game.zone.Zone;
import com.arena.game.zone.ZoneCircle;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.arena.game.utils.Position;
import com.arena.utils.Vector3f;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.arena.game.entity.EntityPositions.*;

public abstract class LivingEntity extends Entity implements ILiving {
    protected int health, maxHealth;
    protected int armor, magicResist, attackDamage, abilityPower;
    protected boolean moving, hasArrived, skinAnimationLocked, attackable, entityLocked, entityCastLocked, entityMoveLocked;
    protected float moveSpeed, rotationY, posX, posZ, posY, posSkinX, posSkinZ, posSkinY, skinScale, posXDesired, posZDesired, posYDesired, skinAnimationSpeed=1.0f;
    protected String name, skinAnimation, nextObjective;
    /* Team 1 = Blue Team, Team 2 = Red Team */
    protected int team;
    protected long cooldownQStart, cooldownWStart, cooldownEStart, cooldownRStart, cooldownQEnd, cooldownWEnd, cooldownEEnd, cooldownREnd, cooldownQMs, cooldownWMs, cooldownEMs, cooldownRMs;
    protected EntityCollider collider;
    protected EntityNavMeshAgent navMeshAgent;
    protected EntityRigidbody rigidbody;
    protected EntityTransform transform;

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


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

    public void setSkinPos(Vector3f vector3f) {
        this.posSkinX = vector3f.x;
        this.posSkinY = vector3f.y;
        this.posSkinZ = vector3f.z;
    }

    public void setSkinPos(Position position) {
        this.posSkinX = position.pos.x;
        this.posSkinY = position.pos.y;
        this.posSkinZ = position.pos.z;
    }

    public void setRigidbody(EntityRigidbody rigidbody) {
        this.rigidbody = rigidbody;
    }
    public EntityRigidbody getRigidbody() {
        return rigidbody;
    }
    public void setCollider(EntityCollider collider) {
        this.collider = collider;
    }
    public EntityCollider getCollider() {
        return collider;
    }
    public void setNavMeshAgent(EntityNavMeshAgent navMeshAgent) {
        this.navMeshAgent = navMeshAgent;
    }
    public EntityNavMeshAgent getNavMeshAgent() {
        return navMeshAgent;
    }
    public void setTransform(EntityTransform transform) {
        this.transform = transform;
    }
    public EntityTransform getTransform() {
        return transform;
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

    @Override
    public void lockEntity(boolean locked) {
        this.entityLocked = locked;
    }

    @Override
    public boolean isLocked() {
        return entityLocked;
    }

    @Override
    public void lockEntityCast(boolean locked) {
        this.entityCastLocked = locked;
    }

    @Override
    public boolean isCastLocked() {
        return entityCastLocked;
    }

    @Override
    public void lockEntityMove(boolean locked) {
        this.entityMoveLocked = locked;
    }
    @Override
    public boolean isMoveLocked() {
        return entityMoveLocked;
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
        if (this.health == 0) {
            this.setSkinAnimation(this.getSkinAnimationForDeath());
            this.LockSkinAnimation(this.getSkinAnimationDurationForDeath(), this::die);
        }
    }

    @Override
    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    @Override
    public boolean isAttackable() {
        return attackable;
    }

    public void setNextObjective(String nextObjective) {
        this.nextObjective = nextObjective;
    }
    public String getNextObjective() {
        return nextObjective;
    }

    @Override
    public void die() {
        Logger.game(this.getName() + " (ID: " + this.getId() + ") has died.", Server.getInstance().getGameOfEntity(this).getGameNameEnum());
    }

    public void spawnAtTeamSpawn() {
        switch (this.getTeam()) {
            case 1:
                this.setPos(BLUE_SPAWN);
                break;
            case 2:
                this.setPos(RED_SPAWN);
                break;
            default:
                this.setPos(CENTER_SPAWN);
                Logger.warn("Team not specified for player " + this.getId() + ", defaulting to CENTER_SPAWN.");
                break;
        }
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

    @Override public int getQTotalDamage() { return 0; }
    @Override public int getWTotalShield() { return 0; }
    @Override public int getETotalDamage() { return 0; }
    @Override public int getRTotalDamage() { return 0; }

    @Override public Zone getQZone() {
        return new ZoneCircle(0);
    }
    @Override public Zone getWZone() {
        return new ZoneCircle(0);
    }
    @Override public Zone getEZone() {
        return new ZoneCircle(0);
    }
    @Override public Zone getRZone() {
        return new ZoneCircle(0);
    }

    @Override public void useQ() {}
    @Override public void useW() {}
    @Override public void useE() {}
    @Override public void useR() {}

    @Override public float getMoveSpeed() { return moveSpeed; }
    @Override public void setMoveSpeed(float moveSpeed) { this.moveSpeed = moveSpeed; }
    @Override public boolean isMoving() { return moving; }
    @Override public void setMoving(boolean moving) { this.moving = moving; }
    @Override public boolean hasArrived() { return hasArrived; }
    @Override public void setHasArrived(boolean hasArrived) { this.hasArrived = hasArrived; }

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

    @Override public float getPosSkinX() { return posSkinX; }
    @Override public void setPosSkinX(float x) { this.posSkinX = x; }
    @Override public float getPosSkinZ() { return posSkinZ; }
    @Override public void setPosSkinZ(float z) { this.posSkinZ = z; }
    @Override public float getPosSkinY() { return posSkinY; }
    @Override public void setPosSkinY(float y) { this.posSkinY = y; }

    @Override public void lockSkinAnimation(boolean lock) { this.skinAnimationLocked = lock; }
    @Override public boolean isSkinAnimationLocked() { return skinAnimationLocked; }
    @Override public String getSkinAnimation() { return skinAnimation; }
    @Override public void setSkinAnimation(String animation) { this.skinAnimation = animation; }
    @Override public float getSkinAnimationSpeed() { return skinAnimationSpeed; }
    @Override public void setSkinAnimationSpeed(float skinAnimationSpeed) { this.skinAnimationSpeed = skinAnimationSpeed; }
    @Override public String getSkinAnimationForRunning() {
        return "None";
    }
    @Override public String getSkinAnimationForIdle() {
        return "None";
    }
    @Override public String getSkinAnimationForQ() {
        return "None";
    }
    @Override public String getSkinAnimationForW() {
        return "None";
    }
    @Override public String getSkinAnimationForE() {
        return "None";
    }
    @Override public String getSkinAnimationForR() {
        return "None";
    }
    @Override public String getSkinAnimationForDeath() {
        return "None";
    }
    @Override public long getSkinAnimationDurationForQ() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForW() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForE() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForR() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForDeath() {
        return 0;
    }

    public void LockSkinAnimation(long ms) {
        this.lockSkinAnimation(true);
        scheduler.schedule(() ->
                {
                    this.lockSkinAnimation(false);
                },
                ms,
                TimeUnit.MILLISECONDS);
    }
    public void LockSkinAnimation(long ms, Runnable afterUnlock) {
        this.lockSkinAnimation(true);
        scheduler.schedule(() -> {
            this.lockSkinAnimation(false);
            if (afterUnlock != null) {
                afterUnlock.run();
            }
        }, ms, TimeUnit.MILLISECONDS);
    }


    @Override public float getSkinScale() { return skinScale; }
    @Override public void setSkinScale(float skinScale) { this.skinScale = skinScale; }

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

    public void update(LivingEntity livingEntity) {
        if (!isLocked()) {
            /* We trust player position
             * The server have no access to world physics
             * */
            if (!isMoveLocked()) {
                this.setPosX(livingEntity.getPosX());
                this.setPosY(livingEntity.getPosY());
                this.setPosZ(livingEntity.getPosZ());
                this.setRotationY(livingEntity.getRotationY());

                this.setMoving(livingEntity.isMoving());
                if (!isSkinAnimationLocked()) {
                    if (this.isMoving()) {
                        this.setSkinAnimation(this.getSkinAnimationForRunning());
                    } else {
                        this.setSkinAnimation(this.getSkinAnimationForIdle());
                    }
                }
                this.setHasArrived(livingEntity.hasArrived());

                this.setPosXDesired(livingEntity.getPosXDesired());
                this.setPosYDesired(livingEntity.getPosYDesired());
                this.setPosZDesired(livingEntity.getPosZDesired());
            } else {
                this.setPosXDesired(livingEntity.getPosX());
                this.setPosYDesired(livingEntity.getPosY());
                this.setPosZDesired(livingEntity.getPosZ());
            }
        }
    }
}
