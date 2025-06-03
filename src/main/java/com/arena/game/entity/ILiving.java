package com.arena.game.entity;

public interface ILiving {
    int getHealth();
    void heal(int amount);
    int getMaxHealth();
    int setMaxHealth(int maxHealth);
    void takeDamage(int amount);


    int getArmor();
    int setArmor(int armor);
    int getMagicResist();
    int setMagicResist(int magicResist);
    int getAttackDamage();
    int setAttackDamage(int attackDamage);
    int getAbilityPower();
    int setAbilityPower(int abilityPower);


    float getMoveSpeed();

    boolean isMoving();
    void setMoving(boolean moving);
    float getPosXDesired();
    void setPosXDesired(float x);
    float getPosZDesired();
    void setPosZDesired(float z);

    float getPosX();
    void setPosX(float x);
    float getPosZ();
    void setPosZ(float z);

    int getTeam();
    void setTeam(int team);
}
