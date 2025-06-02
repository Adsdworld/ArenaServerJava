package com.arena.network.response;

import com.arena.game.GameNameEnum;
import com.arena.player.ActionEnum;
import com.arena.player.ResponseEnum;
import com.google.gson.Gson;

public class Response {
    private String _uuid;
    private ResponseEnum _reponse;
    private GameNameEnum _gameName;
    private String _ability;
    private String _text;
    private String _notify;

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public void Send() {
        ResponseService.getResponseSender().sendResponse(this);
    }

    public void Send(GameNameEnum gameName) {
        ResponseService.getResponseSender().sendGameResponse(this, gameName);
    }

    public void Send(String uuid) {
        ResponseService.getResponseSender().sendUuidResponse(uuid, this);
    }
}
