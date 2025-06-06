package com.arena.network.message;

import com.arena.game.GameNameEnum;
import com.arena.player.ActionEnum;
import com.google.gson.Gson;

public class Message implements Comparable<Message> {
    private String uuid;
    private ActionEnum action;
    private GameNameEnum gameName;
    private long timestamp;
    private float posX;
    private float posZ;
    private float posY;
    private float rotationY;
    private long cooldownQStart;
    private long cooldownWStart;
    private long cooldownEStart;
    private long cooldownRStart;

    private long cooldownQEnd;
    private long cooldownWEnd;
    private long cooldownEEnd;
    private long cooldownREnd;

    // Constructeur vide nécessaire pour Gson
    public Message() {}

    public long getTimeStamp() {
        return timestamp;
    }


    public void setTimeStamp(long newTimestamp) {
        this.timestamp = newTimestamp;
    }

    public GameNameEnum getGameName() {
        return gameName;
    }
    public void setGameName(GameNameEnum gameName) {
        this.gameName = gameName;
    }

    public ActionEnum getAction() {
        return action;
    }
    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getX() {
        return posX;
    }
    public void setX(float x) {
        this.posX = x;
    }
    public float getZ() {
        return posZ;
    }
    public void setZ(float z) {
        this.posZ = z;
    }
    public float getY() {
        return posY;
    }
    public void setY(float y) {
        this.posY = y;
    }
    public float getRotationY() {
        return rotationY;
    }
    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }


    public long getCooldownQStart() {
        return cooldownQStart;
    }
    public void setCooldownQStart(long cooldownQStart) {
        this.cooldownQStart = cooldownQStart;
    }
    public long getCooldownWStart() {
        return cooldownWStart;
    }
    public void setCooldownWStart(long cooldownWStart) {
        this.cooldownWStart = cooldownWStart;
    }
    public long getCooldownEStart() {
        return cooldownEStart;
    }
    public void setCooldownEStart(long cooldownEStart) {
        this.cooldownEStart = cooldownEStart;
    }
    public long getCooldownRStart() {
        return cooldownRStart;
    }
    public void setCooldownRStart(long cooldownRStart) {
        this.cooldownRStart = cooldownRStart;
    }


    public long getCooldownQEnd() {
        return cooldownQEnd;
    }
    public void setCooldownQEnd(long cooldownQEnd) {
        this.cooldownQEnd = cooldownQEnd;
    }
    public long getCooldownWEnd() {
        return cooldownWEnd;
    }
    public void setCooldownWEnd(long cooldownWEnd) {
        this.cooldownWEnd = cooldownWEnd;
    }
    public long getCooldownEEnd() {
        return cooldownEEnd;
    }
    public void setCooldownEEnd(long cooldownEEnd) {
        this.cooldownEEnd = cooldownEEnd;
    }
    public long getCooldownREnd() {
        return cooldownREnd;
    }
    public void setCooldownREnd(long cooldownREnd) {
        this.cooldownREnd = cooldownREnd;
    }

    @Override
    public int compareTo(Message other) {
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
