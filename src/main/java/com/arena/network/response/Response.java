package com.arena.network.response;

import com.arena.game.GameNameEnum;
import com.arena.game.entity.LivingEntity;
import com.arena.player.ResponseEnum;
import com.google.gson.Gson;

import java.util.Collection;

/**
 * Represents a response sent over the network.
 * This class is used to encapsulate the data that is sent by the {@link com.arena.server.Server} to the {@link com.arena.player.Player} .
 */
public class Response implements Comparable<Response>{
    private String _uuid;
    private ResponseEnum _reponse;
    private GameNameEnum _gameName;
    private String _ability;
    private String _text;
    private String _notify;
    private long _timestamp;
    private Collection<LivingEntity> _livingEntities;

    /* Constructor for json */
    public Response() {
    }

    public GameNameEnum getGameName() {
        return _gameName;
    }

    public void setGameName(GameNameEnum gameName) {
        this._gameName = gameName;
    }

    public ResponseEnum getReponse() {
        return _reponse;
    }

    public void setResponse(ResponseEnum response) {
        this._reponse = response;
    }

    public String getUuid() {
        return _uuid;
    }

    public void setUuid(String uuid) {
        this._uuid = uuid;
    }

    public void setText(String text) {
        this._text = text;
    }

    public void setNotify(String notify) {
        this._notify = notify;
    }

    public void setTimestamp(long timestamp) {
        this._timestamp = timestamp;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    public void setLivingEntities(Collection<LivingEntity> livingEntities) {
        _livingEntities = livingEntities;
    }

    public Collection<LivingEntity> getLivingEntities() {
        return _livingEntities;
    }

    @Override
    public int compareTo(Response other) {
        return Long.compare(this._timestamp, other._timestamp);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Send to server.
     */
    public void Send() {
        _timestamp = System.currentTimeMillis();
        ResponseService.send(this, false);
    }

    /**
     * Send to server silently
     * @param silent whether to send the response silently or not.
     */
    public void Send(boolean silent) {
        _timestamp = System.currentTimeMillis();
        ResponseService.send(this, silent);
    }


    /**
     * Send to game.
     * @param gameName the name of the game as a {@link GameNameEnum}.
     */
    public void Send(GameNameEnum gameName) {
        _timestamp = System.currentTimeMillis();
        ResponseService.sendToGame(this, gameName, false);
    }

    /**
     * Send to game silently.
     * @param gameName the name of the game as a {@link GameNameEnum}.
     * @param silent whether to send the response silently or not.
     */
    public void Send(GameNameEnum gameName, boolean silent) {
        _timestamp = System.currentTimeMillis();
        ResponseService.sendToGame(this, gameName, silent);
    }

    /**
     * Send to uuid.
     * @param uuid the unique identifier of the player as a {@link String}.
     */
    public void Send(String uuid) {
        _timestamp = System.currentTimeMillis();
        ResponseService.sendToUuid(uuid, this, false);
    }

    /**
     * Send to uuid silently
     * @param uuid the unique identifier of the player as a {@link String}.
     * @param silent whether to send the response silently or not.
     */
    public void Send(String uuid, boolean silent) {
        _timestamp = System.currentTimeMillis();
        ResponseService.sendToUuid(uuid, this, silent);
    }
}
