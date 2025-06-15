package com.arena.game.entity;

import com.arena.game.utils.Position;

public abstract class LivingEntityPos extends LivingEntityLock implements ILivingEntityPos {
    protected float rotationY, posX, posZ, posY, posXDesired, posZDesired, posYDesired;

    public LivingEntityPos(String id) {
        super(id);
    }

    @Override
    public float getPosXDesired() {return posXDesired;}

    @Override
    public void setPosXDesired(float x) {this.posXDesired = x;}

    @Override
    public float getPosZDesired() {return posZDesired;}

    @Override
    public void setPosZDesired(float z) {this.posZDesired = z;}

    @Override
    public float getPosYDesired() {return posYDesired;}

    @Override
    public void setPosYDesired(float y) {this.posYDesired = y;}

    @Override
    public float getPosX() {return posX;}

    @Override
    public void setPosX(float x) {this.posX = x;}

    @Override
    public float getPosZ() {return posZ;}

    @Override
    public void setPosZ(float z) {this.posZ = z;}

    @Override
    public float getPosY() {return posY;}

    @Override
    public void setPosY(float y) {this.posY = y;}

    @Override
    public void setRotationY(float rotationY) {this.rotationY = rotationY;}

    @Override
    public float getRotationY() {return rotationY;}

    /**
     * Sets the position of the entity based on a Position object.
     *
     * @param position the {@link Position} object containing the position and rotation data.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void setPos(Position position) {
        this.posX = position.pos.x;
        this.posY = position.pos.y;
        this.posZ = position.pos.z;
        this.rotationY = position.rotY;
    }
}
