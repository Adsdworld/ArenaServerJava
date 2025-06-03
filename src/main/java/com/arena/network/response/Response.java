package com.arena.network.response;

import com.arena.game.GameNameEnum;
import com.arena.game.entity.LivingEntity;
import com.arena.player.ActionEnum;
import com.arena.player.ResponseEnum;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Response {
    private String _uuid;   // Unique id
    private ResponseEnum _reponse;
    private GameNameEnum _gameName;
    private String _ability;
    private String _text;
    private String _notify;
    private ArrayList<LivingEntity> _livingEntities;

    // Constructeur vide nécessaire pour Gson
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

    public ArrayList<LivingEntity> setLivingEntities(ArrayList<LivingEntity> livingEntities) {
        return _livingEntities = livingEntities;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * send to server.
     */
    public void Send() {
        ResponseService.send(this, false);
    }

    /**
     * send to server silently
     * @param silent
     */
    public void Send(boolean silent) {
        ResponseService.send(this, silent);
    }


    /**
     * send to game.
     * @param gameName
     */
    public void Send(GameNameEnum gameName) {
        ResponseService.sendToGame(this, gameName, false);
    }

    /**
     * send to game silently.
     * @param gameName
     */
    public void Send(GameNameEnum gameName, boolean silent) {
        ResponseService.sendToGame(this, gameName, silent);
    }

    /**
     * send to uuid.
     * @param uuid
     */
    public void Send(String uuid) {
        ResponseService.sendToUuid(uuid, this, false);
    }

    /**
     * send to uuid silently
     * @param uuid
     */
    public void Send(String uuid, boolean silent) {
        ResponseService.sendToUuid(uuid, this, silent);
    }
}
