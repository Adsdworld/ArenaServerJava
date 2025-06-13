package com.arena.game.entity;

import com.arena.game.zone.Zone;

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
    int getQTotalDamage();
    int getWTotalShield();
    int getETotalDamage();
    int getRTotalDamage();

    Zone getQZone();
    Zone getWZone();
    Zone getEZone();
    Zone getRZone();

    void useQ();
    void useW();
    void useE();
    void useR();

    float getMoveSpeed();
    void setMoveSpeed(float moveSpeed);

    boolean isMoving();
    void setMoving(boolean moving);
    boolean hasArrived();
    void setHasArrived(boolean hasArrived);
    float getPosXDesired();
    void setPosXDesired(float x);
    float getPosZDesired();
    void setPosZDesired(float z);
    float getPosYDesired();
    void setPosYDesired(float y);

    float getPosX();
    void setPosX(float x);
    float getPosZ();
    void setPosZ(float z);
    float getPosY();
    void setPosY(float y);

    float getPosSkinX();
    void setPosSkinX(float x);
    float getPosSkinZ();
    void setPosSkinZ(float z);
    float getPosSkinY();
    void setPosSkinY(float y);

    float getSkinScale();
    void setSkinScale(float scale);

    void setRotationY(float rotation);
    float getRotationY();

    int getTeam();
    void setTeam(int team);

    String getName();

    void lockSkinAnimation(boolean lock);
    boolean isSkinAnimationLocked();
    String getSkinAnimation();
    void setSkinAnimation(String animation);
    float getSkinAnimationSpeed();
    void setSkinAnimationSpeed(float speed);
    String getSkinAnimationForRunning();
    String getSkinAnimationForIdle();
    String getSkinAnimationForQ();
    String getSkinAnimationForW();
    String getSkinAnimationForE();
    String getSkinAnimationForR();
    String getSkinAnimationForDeath();
    long getSkinAnimationDurationForQ();
    long getSkinAnimationDurationForW();
    long getSkinAnimationDurationForE();
    long getSkinAnimationDurationForR();
    long getSkinAnimationDurationForDeath();

    void die();

    void setCooldownQStart(long cooldownQStart);
    long getCooldownQStart();
    void setCooldownWStart(long cooldownWStart);
    long getCooldownWStart();
    void setCooldownEStart(long cooldownEStart);
    long getCooldownEStart();
    void setCooldownRStart(long cooldownRStart);
    long getCooldownRStart();
    void setCooldownQEnd(long cooldownQEnd);
    long getCooldownQEnd();
    void setCooldownWEnd(long cooldownWEnd);
    long getCooldownWEnd();
    void setCooldownEEnd(long cooldownEEnd);
    long getCooldownEEnd();
    void setCooldownREnd(long cooldownREnd);
    long getCooldownREnd();

    void setCooldownQMs(long cooldownQEnd);
    long getCooldownQMs();
    void setCooldownWMs(long cooldownWEnd);
    long getCooldownWMs();
    void setCooldownEMs(long cooldownEEnd);
    long getCooldownEMs();
    void setCooldownRMs(long cooldownREnd);
    long getCooldownRMs();
}
