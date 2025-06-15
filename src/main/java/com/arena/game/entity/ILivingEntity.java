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

    void die();


}
