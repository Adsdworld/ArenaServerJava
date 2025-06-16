package com.arena.game.entity;

public interface ILivingEntityLock {
    /**
     * To lock the entity.
     * Maximum lock possibility.
     *
     * @param lock true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    void lockEntity(boolean lock);

    /**
     * isLocked
     *
     * @return true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    boolean isLocked();

    /**
     * To lock cast abilities
     *
     * @param lock true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    void lockEntityCast(boolean lock);

    /**
     * isCastLocked
     *
     * @return true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    boolean isCastLocked();

    /**
     * To lock move
     *
     * @param lock true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    void lockEntityMove(boolean lock);

    /**
     * isMoveLocked
     *
     * @return true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    boolean isMoveLocked();

    /**
     * To lock skin animation
     *
     * @param lock true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    void lockSkinAnimation(boolean lock);

    /**
     * isSkinAnimationLocked
     *
     * @return true or false
     * @author A.SALLIER
     * @date 2025-06-16
     */
    boolean isSkinAnimationLocked();
}
