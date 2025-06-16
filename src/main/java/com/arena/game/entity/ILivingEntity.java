package com.arena.game.entity;

public interface ILivingEntity {
    int getHealth();
    void heal(int amount);
    int getMaxHealth();
    int setMaxHealth(int maxHealth);
    void takeDamage(int amount);
    void setAttackable(boolean attackable);
    boolean isAttackable();

    float getMoveSpeed();
    void setMoveSpeed(float moveSpeed);

    boolean isMoving();
    void setMoving(boolean moving);
    boolean hasArrived();
    void setHasArrived(boolean hasArrived);

    int getTeam();
    void setTeam(int team);

    String getName();

    /**
     * To make die an entity
     * Code logic specific to make the entity die after taking damage and having 0 health.
     *
     * @implNote
     * @author A.SALLIER
     * @date 2025-06-16
     */
    void die();
}
