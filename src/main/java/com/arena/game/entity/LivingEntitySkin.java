package com.arena.game.entity;

import com.arena.utils.Vector3f;

public abstract class LivingEntitySkin extends LivingEntityCast implements ILivingEntitySkin {
    protected float skinAnimationSpeed, skinAnimationBaseSpeed, posSkinX, posSkinZ, posSkinY, skinScale;
    protected String skinAnimation;

    public LivingEntitySkin(String id) {
        super(id);
    }

    @Override public String getSkinAnimation() { return skinAnimation; }
    @Override public void setSkinAnimation(String animation) { this.skinAnimation = animation;}
    @Override public float getSkinAnimationBaseSpeed() { return skinAnimationBaseSpeed; }
    @Override public void setSkinAnimationBaseSpeed(float speed) {this.skinAnimationBaseSpeed = speed;}
    @Override public float getSkinAnimationSpeed() { return skinAnimationSpeed; }
    @Override public void setSkinAnimationSpeed(float skinAnimationSpeed) { this.skinAnimationSpeed = skinAnimationSpeed; }
    @Override public String getSkinAnimationForRunning() {
        return "None";
    }
    @Override public String getSkinAnimationForIdle() {
        return "None";
    }
    @Override public String getSkinAnimationForQ() {
        return "None";
    }
    @Override public String getSkinAnimationForW() {
        return "None";
    }
    @Override public String getSkinAnimationForE() {
        return "None";
    }
    @Override public String getSkinAnimationForR() {
        return "None";
    }
    @Override public String getSkinAnimationForDeath() {
        return "None";
    }
    @Override public String getSkinAnimationForSpawnHold() {return "None";}
    @Override public String getSkinAnimationForSpawn() {return "None";}
    @Override public String getSkinAnimationForDeathHold() {return "None";}
    @Override public long getSkinAnimationDurationForQ() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForW() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForE() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForR() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForDeath() {
        return 0;
    }
    @Override public long getSkinAnimationDurationForSpawnHold() {return 0;}
    @Override public long getSkinAnimationDurationForSpawn() {return 0;}
    @Override public long getSkinAnimationDurationForDeathHold() {return 0;}

    @Override
    public float getPosSkinX() {return posSkinX;}

    @Override
    public void setPosSkinX(float x) {this.posSkinX = x;}

    @Override
    public float getPosSkinZ() {return posSkinZ;}

    @Override
    public void setPosSkinZ(float z) {this.posSkinZ = z;}

    @Override
    public float getPosSkinY() {return posSkinY;}

    @Override
    public void setPosSkinY(float y) {this.posSkinY = y;}

    @Override
    public float getSkinScale() {return skinScale;}

    @Override
    public void setSkinScale(float skinScale) {this.skinScale = skinScale;}

    /**
     * Set the position of the skin using a {@link Vector3f} .
     *
     * @param vector3f the position vector containing x, y, z coordinates.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void setSkinPos(Vector3f vector3f) {
        this.posSkinX = vector3f.x;
        this.posSkinY = vector3f.y;
        this.posSkinZ = vector3f.z;
    }
}
