package com.arena.game.entity;

public abstract class LivingEntitySkin extends Entity implements ILivingEntitySkin {
    protected float skinAnimationSpeed, skinAnimationBaseSpeed;
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
}
