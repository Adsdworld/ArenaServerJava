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
    private String uuid;
    private ResponseEnum response;
    private GameNameEnum gameName;
    private String text;
    private String notify;
    private long timestamp;
    private Collection<LivingEntity> livingEntities;

    /* Constructor for json */
    public Response() {
    }

    public GameNameEnum getGameName() {
        return gameName;
    }

    public void setGameName(GameNameEnum gameName) {
        this.gameName = gameName;
    }

    public ResponseEnum getResponse() {
        return response;
    }

    public void setResponse(ResponseEnum res) {
        this.response = res;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNotify(String notify1) {
        this.notify = notify1;
    }

    public String getNotify() {
        return notify;
    }

    public void setTimestamp(long timestamp1) {
        this.timestamp = timestamp1;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setLivingEntities(Collection<LivingEntity> entities) {
        livingEntities = entities;
    }

    public Collection<LivingEntity> getLivingEntities() {
        return livingEntities;
    }

    @Override
    public int compareTo(Response other) {
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Send to server.
     */
    public void send() {
        timestamp = System.currentTimeMillis();
        ResponseService.send(this, false);
    }

    /**
     * Send to server silently
     * @param silent whether to send the response silently or not.
     */
    public void send(boolean silent) {
        timestamp = System.currentTimeMillis();
        ResponseService.send(this, silent);
    }


    /**
     * Send to game.
     * @param game the name of the game as a {@link GameNameEnum}.
     */
    public void send(GameNameEnum game) {
        timestamp = System.currentTimeMillis();
        ResponseService.sendToGame(this, game, false);
    }

    /**
     * Send to game silently.
     * @param game the name of the game as a {@link GameNameEnum}.
     * @param silent whether to send the response silently or not.
     */
    public void send(GameNameEnum game, boolean silent) {
        timestamp = System.currentTimeMillis();
        ResponseService.sendToGame(this, game, silent);
    }

    /**
     * Send to uuid.
     * @param id the unique identifier of the player as a {@link String}.
     */
    public void send(String id) {
        timestamp = System.currentTimeMillis();
        ResponseService.sendToUuid(id, this, false);
    }

    /**
     * Send to uuid silently
     * @param id the unique identifier of the player as a {@link String}.
     * @param silent whether to send the response silently or not.
     */
    public void send(String id, boolean silent) {
        timestamp = System.currentTimeMillis();
        ResponseService.sendToUuid(id, this, silent);
    }
}
