package com.arena.game.entity;

import com.arena.game.zone.Zone;

public interface ILivingEntityCast {
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
