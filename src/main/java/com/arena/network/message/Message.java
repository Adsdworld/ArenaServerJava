package com.arena.network.message;

import com.arena.game.GameNameEnum;
import com.arena.game.entity.LivingEntity;
import com.arena.player.ActionEnum;
import com.google.gson.Gson;

/**
 * Represents a message sent over the network.
 * This class is used to encapsulate the data that is sent by the {@link com.arena.player.Player} to the {@link com.arena.server.Server} .
 */
public class Message implements Comparable<Message> {
    private String uuid;
    private ActionEnum action;
    private GameNameEnum gameName;
    private long timestamp;

    private LivingEntity livingEntity;

    /* Constructor for json */
    public Message() {}

    public long getTimeStamp() {return timestamp;}
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

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }
    public void setLivingEntity(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
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
