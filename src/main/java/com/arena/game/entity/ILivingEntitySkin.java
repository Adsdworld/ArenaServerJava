package com.arena.game.entity;

public interface ILivingEntitySkin {
    void lockSkinAnimation(boolean lock);
    boolean isSkinAnimationLocked();
    String getSkinAnimation();
    void setSkinAnimation(String animation);
    float getSkinAnimationBaseSpeed();
    void setSkinAnimationBaseSpeed(float speed);
    float getSkinAnimationSpeed();
    void setSkinAnimationSpeed(float speed);
    String getSkinAnimationForRunning();
    String getSkinAnimationForIdle();
    String getSkinAnimationForQ();
    String getSkinAnimationForW();
    String getSkinAnimationForE();
    String getSkinAnimationForR();
    String getSkinAnimationForDeath();
    String getSkinAnimationForDeathHold();
    String getSkinAnimationForSpawn();
    String getSkinAnimationForSpawnHold();
    long getSkinAnimationDurationForQ();
    long getSkinAnimationDurationForW();
    long getSkinAnimationDurationForE();
    long getSkinAnimationDurationForR();
    long getSkinAnimationDurationForDeath();
    long getSkinAnimationDurationForDeathHold();
    long getSkinAnimationDurationForSpawn();
    long getSkinAnimationDurationForSpawnHold();
}
