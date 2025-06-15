package com.arena.game.entity;

import com.arena.game.zone.Zone;
import com.arena.game.zone.ZoneCircle;

public abstract class LivingEntityCast extends LivingEntityPos implements ILivingEntityCast {
    protected long cooldownQStart, cooldownWStart, cooldownEStart, cooldownRStart, cooldownQEnd, cooldownWEnd, cooldownEEnd, cooldownREnd, cooldownQMs, cooldownWMs, cooldownEMs, cooldownRMs;

    public LivingEntityCast(String id) {
        super(id);
    }

    @Override
    public void setCooldownQStart(long cooldownQStart) {
        this.cooldownQStart = cooldownQStart;
    }

    @Override
    public long getCooldownQStart() {
        return cooldownQStart;
    }

    @Override
    public void setCooldownWStart(long cooldownWStart) {
        this.cooldownWStart = cooldownWStart;
    }

    @Override
    public long getCooldownWStart() {
        return cooldownWStart;
    }

    @Override
    public void setCooldownEStart(long cooldownEStart) {
        this.cooldownEStart = cooldownEStart;
    }

    @Override
    public long getCooldownEStart() {
        return cooldownEStart;
    }

    @Override
    public void setCooldownRStart(long cooldownRStart) {
        this.cooldownRStart = cooldownRStart;
    }

    @Override
    public long getCooldownRStart() {
        return cooldownRStart;
    }

    @Override
    public void setCooldownQEnd(long cooldownQEnd) {
        this.cooldownQEnd = cooldownQEnd;
    }

    @Override
    public long getCooldownQEnd() {
        return cooldownQEnd;
    }

    @Override
    public void setCooldownWEnd(long cooldownWEnd) {
        this.cooldownWEnd = cooldownWEnd;
    }

    @Override
    public long getCooldownWEnd() {
        return cooldownWEnd;
    }

    @Override
    public void setCooldownEEnd(long cooldownEEnd) {
        this.cooldownEEnd = cooldownEEnd;
    }

    @Override
    public long getCooldownEEnd() {
        return cooldownEEnd;
    }

    @Override
    public void setCooldownREnd(long cooldownREnd) {
        this.cooldownREnd = cooldownREnd;
    }

    @Override
    public long getCooldownREnd() {
        return cooldownREnd;
    }

    @Override
    public void setCooldownQMs(long cooldownQMs) {
        this.cooldownQMs = cooldownQMs;
    }

    @Override
    public long getCooldownQMs() {
        return cooldownQMs;
    }

    @Override
    public void setCooldownWMs(long cooldownWMs) {
        this.cooldownWMs = cooldownWMs;
    }

    @Override
    public long getCooldownWMs() {
        return cooldownWMs;
    }

    @Override
    public void setCooldownEMs(long cooldownEMs) {
        this.cooldownEMs = cooldownEMs;
    }

    @Override
    public long getCooldownEMs() {
        return cooldownEMs;
    }

    @Override
    public void setCooldownRMs(long cooldownRMs) {
        this.cooldownRMs = cooldownRMs;
    }

    @Override
    public long getCooldownRMs() {
        return cooldownRMs;
    }

    @Override
    public int getQTotalDamage() {return 0;}

    @Override
    public int getWTotalShield() {return 0;}

    @Override
    public int getETotalDamage() {return 0;}

    @Override
    public int getRTotalDamage() {return 0;}

    @Override
    public Zone getQZone() {
        return new ZoneCircle(0);
    }

    @Override
    public Zone getWZone() {return new ZoneCircle(0);}

    @Override
    public Zone getEZone() {
        return new ZoneCircle(0);
    }

    @Override
    public Zone getRZone() {
        return new ZoneCircle(0);
    }

    @Override
    public void useQ() {}

    @Override
    public void useW() {}

    @Override
    public void useE() {}

    @Override
    public void useR() {}
}
