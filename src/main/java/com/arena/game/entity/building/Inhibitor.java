package com.arena.game.entity.building;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.network.response.Response;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Vector3f;

public class Inhibitor extends LivingEntity {
    private final String skinAnimationIdle = "Idle_Normal1";

    private final String skinAnimationForSpawnHold = "Idle_Hold";
    private final long skinAnimationDurationForSpawnHold = 333;

    private final String skinAnimationForSpawn = "SRUAP_ChaosInhibitor_spawn.anm";
    private final long skinAnimationDurationForSpawn = 1166;

    private final String skinAnimationForDeath = "Death_Base";
    private final long skinAnimationDurationForDeath = 8333;

    private final String skinAnimationForDeathHold = "Idle_Hold";
    private final long skinAnimationDurationForDeathHold = 333;


    public Inhibitor(String id, int team) {
        super(id, 1200, team, "Inhibitor");

        this.setSkinScale(0.0029f);
        this.setSkinPos(new Vector3f(0.0f, 0.515f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForSpawnHold());
        this.setSkinAnimationSpeed(0.1f);

        Building building = new Building(17f);

        this.setRigidbody(building.getRigidbody());

        this.setCollider(building.getCollider());

        this.setNavMeshAgent(building.getNavMeshAgent());

        this.setTransform(building.getTransform());
    }

    @Override
    public String getSkinAnimationForIdle() {
        return skinAnimationIdle;
    }

    @Override
    public long getSkinAnimationDurationForSpawnHold() {return (long) (this.getSkinAnimationBaseSpeed()/this.getSkinAnimationSpeed()) * skinAnimationDurationForSpawnHold;}

    @Override
    public String getSkinAnimationForSpawnHold() {return skinAnimationForSpawnHold;}

    @Override
    public long getSkinAnimationDurationForSpawn() {return (long) (this.getSkinAnimationBaseSpeed()/this.getSkinAnimationSpeed()) * skinAnimationDurationForSpawn;}

    @Override
    public String getSkinAnimationForSpawn() {return skinAnimationForSpawn;}

    @Override
    public long getSkinAnimationDurationForDeath() {return (long) (this.getSkinAnimationBaseSpeed()/this.getSkinAnimationSpeed()) * skinAnimationDurationForDeath;}

    @Override
    public String getSkinAnimationForDeath() {return skinAnimationForDeath;}

    @Override
    public long getSkinAnimationDurationForDeathHold() {return (long) (this.getSkinAnimationBaseSpeed()/this.getSkinAnimationSpeed()) * skinAnimationDurationForDeathHold;}

    @Override
    public String getSkinAnimationForDeathHold() {return skinAnimationForDeathHold;}

    @Override
    public void die() {
        Server server = Server.getInstance();

        Game game = server.getGameOfEntity(this);

        this.setSkinAnimation(getSkinAnimationForDeathHold());

        Response response = new Response();
        response.setResponse(ResponseEnum.Info);
        response.setNotify("Inhibitor " + this.getGeneralId() + " has been destroyed.");
        response.Send(game.getGameNameEnum());
    }
}
