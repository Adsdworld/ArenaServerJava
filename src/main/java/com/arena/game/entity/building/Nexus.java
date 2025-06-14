package com.arena.game.entity.building;

import com.arena.game.Game;
import com.arena.game.entity.*;
import com.arena.network.response.Response;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.arena.utils.Vector3f;

public class Nexus extends LivingEntity {
    private String skinAnimationIdle = "Idle1_Base";

    private final String skinAnimationForSpawnHold = "Nexus_spawn_hold.anm";
    private final long skinAnimationDurationForSpawnHold = 166;

    private final String skinAnimationForSpawn = "Nexus_spawn.anm";
    private final long skinAnimationDurationForSpawn = 5166;

    private final String skinAnimationForDeath = "Death";
    private final long skinAnimationDurationForDeath = 7999;

    private final String skinAnimationForDeathHold = "Nexus_spawn_hold.anm";
    private final long skinAnimationDurationForDeathHold = 166;


    public Nexus(String id, int team) {
        super(id, 2000, team, "Nexus");

        this.setAttackDamage(0);

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(true);
        this.setRigidbody(rigidbody);

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.setCollider(collider);

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(false);
        this.setNavMeshAgent(navMeshAgent);

        EntityTransform transform = new EntityTransform();
        transform.setScale(26f);
        this.setTransform(transform);


        this.setSkinScale(0.0015f);
        this.setSkinPos(new Vector3f(0.0f, 0.578f, 0.0f));
        this.setSkinAnimationSpeed(0.5f);
        this.setSkinAnimation(getSkinAnimationForSpawnHold());
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
    public long getSkinAnimationDurationForDeath() {
        return (long) (this.getSkinAnimationBaseSpeed()/this.getSkinAnimationSpeed()) * skinAnimationDurationForDeath;
    }

    @Override
    public String getSkinAnimationForDeath() {return skinAnimationForDeath;}

    @Override
    public long getSkinAnimationDurationForDeathHold() {return (long) (this.getSkinAnimationBaseSpeed()/this.getSkinAnimationSpeed()) * skinAnimationDurationForDeathHold;}

    @Override
    public String getSkinAnimationForDeathHold() {return skinAnimationForDeathHold;}


    @Override
    public String getSkinAnimationForIdle() {
        return skinAnimationIdle;
    }

    @Override
    public void die() {
        Server server = Server.getInstance();

        Game game = server.getGameOfEntity(this);

        if (game == null) {
            Logger.failure("Game not found for entity: " + this.getId());
            return;
        }

        this.setSkinAnimation(getSkinAnimationForDeath());
        this.LockSkinAnimation(getSkinAnimationDurationForDeath(), () -> {
            this.setSkinAnimation(getSkinAnimationForDeathHold());
        });

        this.setAttackable(false);

        Response response = new Response();
        response.setResponse(ResponseEnum.Info);
        if (this.getTeam() == 1) {
            response.setNotify("The Red Nexus has been destroyed! Blue Team wins!");
        } else {
            response.setNotify("The Blue Nexus has been destroyed! Red Team wins!");
        }
        response.Send(game.getGameNameEnum());
    }
}
