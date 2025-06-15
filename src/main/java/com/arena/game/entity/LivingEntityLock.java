package com.arena.game.entity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class LivingEntityLock extends Entity implements ILivingEntityLock {
    protected boolean entityLocked, skinAnimationLocked, entityCastLocked, entityMoveLocked;

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public LivingEntityLock(String id) {
        super(id);
    }

    @Override
    public void lockEntity(boolean locked) {this.entityLocked = locked;}

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

    @Override
    public void lockSkinAnimation(boolean lock) {this.skinAnimationLocked = lock;}

    @Override
    public boolean isSkinAnimationLocked() {return skinAnimationLocked;}

    /**
     * Locks the skin animation for a specified duration.
     *
     * @param ms the duration in milliseconds to lock the skin animation.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void lockSkinAnimation(long ms) {
        this.lockSkinAnimation(true);
        SCHEDULER.schedule(() ->
                {
                    this.lockSkinAnimation(false);
                },
                ms,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Locks the skin animation for a specified duration and runs an action after unlocking.
     *
     * @param ms the duration in milliseconds to lock the skin animation.
     * @param afterUnlock the action to run after unlocking the skin animation, can be null.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void lockSkinAnimation(long ms, Runnable afterUnlock) {
        this.lockSkinAnimation(true);
        SCHEDULER.schedule(() -> {
            this.lockSkinAnimation(false);
            if (afterUnlock != null) {
                afterUnlock.run();
            }
        }, ms, TimeUnit.MILLISECONDS);
    }
}
