package com.arena.game.entity;

public interface ILivingEntityPos {
    float getPosX();
    void setPosX(float x);
    float getPosZ();
    void setPosZ(float z);
    float getPosY();
    void setPosY(float y);

    float getPosXDesired();
    void setPosXDesired(float x);
    float getPosZDesired();
    void setPosZDesired(float z);
    float getPosYDesired();
    void setPosYDesired(float y);

    void setRotationY(float rotation);
    float getRotationY();
}
