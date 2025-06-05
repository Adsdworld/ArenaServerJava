package com.arena.network.message;

import com.arena.game.GameNameEnum;
import com.arena.player.ActionEnum;
import com.google.gson.Gson;

public class Message implements Comparable<Message> {
    private String _uuid;
    private ActionEnum _action;
    private GameNameEnum _gameName;
    private String _ability;
    private long _timestamp;

    // Constructeur vide nécessaire pour Gson
    public Message() {}

    public long getTimeStamp() {
        return _timestamp;
    }

    public void setTimeStamp(long timestamp) {
        this._timestamp = timestamp;
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
