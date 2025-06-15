package com.arena.game.entity;

public interface ILivingEntityLock {
    void lockEntity(boolean lock);
    boolean isLocked();

    void lockEntityCast(boolean lock);
    boolean isCastLocked();

    void lockEntityMove(boolean lock);
    boolean isMoveLocked();

    void lockSkinAnimation(boolean lock);
    boolean isSkinAnimationLocked();
}
