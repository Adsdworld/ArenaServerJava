package com.arena.network.message;

import com.arena.game.GameNameEnum;
import com.arena.player.ActionEnum;
import com.google.gson.Gson;

import java.util.Date;

public class Message implements Comparable<Message> {
    private String _uuid;
    private ActionEnum _action;
    private GameNameEnum _gameName;
    private String _ability;
    private Float _x;
    private Float _z;
    private long _timestamp;

    // Constructeur vide nécessaire pour Gson
    public Message() {}

    public long getTimeStamp() {
        return _timestamp;
    }

    public GameNameEnum getGameName() {
        return _gameName;
    }
    public void setGameName(GameNameEnum gameName) {
        this._gameName = gameName;
    }

    public ActionEnum getAction() {
        return _action;
    }
    public void setAction(ActionEnum action) {
        this._action = action;
    }

    public String getUuid() {
        return _uuid;
    }
    public void setUuid(String uuid) {
        this._uuid = uuid;
    }

    @Override
    public int compareTo(Message other) {
        return Long.compare(this._timestamp, other._timestamp);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
