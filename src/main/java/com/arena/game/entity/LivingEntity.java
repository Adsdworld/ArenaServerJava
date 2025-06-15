package com.arena.game.entity;

import com.arena.game.Game;
import com.arena.game.entity.building.Inhibitor;
import com.arena.game.entity.building.Nexus;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;

import java.util.Collection;

public abstract class LivingEntity extends LivingEntitySkin implements ILivingEntity {
    protected int health, maxHealth;
    protected boolean moving, hasArrived, attackable;
    protected float moveSpeed;
    protected String name;
    /* Team 1 = Blue Team, Team 2 = Red Team */
    protected int team;
    protected EntityCollider collider;
    protected EntityNavMeshAgent navMeshAgent;
    protected EntityRigidbody rigidbody;
    protected EntityTransform transform;

    protected Collection<String> nextObjective;



    public LivingEntity(String id, int maxHealth, int team, String name) {
        super(id);
        this.setMaxHealth(maxHealth);
        this.heal(maxHealth);
        this.setTeam(team);
        this.setName(name);
        this.setSkinAnimationBaseSpeed(1f);
        this.setSkinAnimationSpeed(1f);
        this.setAttackable(false);
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
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        LivingEntity that = (LivingEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public int getHealth() {return health;}

    @Override
    public void heal(int amount) {
        this.health = Math.min(this.health + amount, maxHealth);
    }

    @Override
    public int getMaxHealth() {return maxHealth;}

    @Override
    public int setMaxHealth(int maxHealth1) {
        this.maxHealth = maxHealth1;
        return this.maxHealth;
    }

    @Override
    public void takeDamage(int amount) {
        this.health = Math.max(0, this.health - amount);
        if (this.health == 0) {
            this.setSkinAnimation(this.getSkinAnimationForDeath());
            if (this instanceof Nexus || this instanceof Inhibitor) {
                this.setAttackable(false);
            }

            if (this.getNextObjective() != null) {
                Server server = Server.getInstance();
                Game game = server.getGameOfEntity(this);

                for (String nextObjectiveGeneralId : this.getNextObjective()) {
                    LivingEntity nextEntity = game.getLivingEntityByGeneralId(nextObjectiveGeneralId);
                    if (nextEntity != null) {
                        nextEntity.setAttackable(true);
                    }
                }
            }
            this.lockSkinAnimation(this.getSkinAnimationDurationForDeath(), this::die);
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

    public void setNextObjective(Collection<String> nextObjective) {
        this.nextObjective = nextObjective;
    }

    public Collection<String> getNextObjective() {
        return nextObjective;
    }

    @Override
    public void die() {
        Logger.game(this.getName() + " (ID: " + this.getId() + ") has died.", Server.getInstance().getGameOfEntity(this).getGameNameEnum());
    }

    /**
     * Spawns the player at their team spawn location.
     *
     * @implNote This method sets the player's position to the designated spawn point based on their team.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void spawnAtTeamSpawn() {
        switch (this.getTeam()) {
            case 1:
                this.setPos(EntityPositions.BLUE_SPAWN);
                break;
            case 2:
                this.setPos(EntityPositions.RED_SPAWN);
                break;
            default:
                this.setPos(EntityPositions.CENTER_SPAWN);
                Logger.warn("Team not specified for player " + this.getId() + ", defaulting to CENTER_SPAWN.");
                break;
        }
    }

    @Override
    public float getMoveSpeed() {return moveSpeed;}

    @Override
    public void setMoveSpeed(float moveSpeed) {this.moveSpeed = moveSpeed;}

    @Override
    public boolean isMoving() {return moving;}

    @Override
    public void setMoving(boolean moving) {this.moving = moving;}

    @Override
    public boolean hasArrived() {return hasArrived;}

    @Override
    public void setHasArrived(boolean hasArrived) {this.hasArrived = hasArrived;}

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

    private void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the LivingEntity's position and state based on the provided LivingEntity.
     * This method is typically called to synchronize the state of the entity with the server.
     *
     * @param livingEntity The LivingEntity whose state will be used to update this entity.
     * @implNote update this entity's position, rotation, movement state, and desired position according to locks.
     * @author A.SALLIER
     * @date 2025-06-15
     */
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
                if (!isSkinAnimationLocked() && this.isMoving()) {
                    this.setSkinAnimation(this.getSkinAnimationForRunning());
                } else if (!isSkinAnimationLocked()){
                    this.setSkinAnimation(this.getSkinAnimationForIdle());
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
