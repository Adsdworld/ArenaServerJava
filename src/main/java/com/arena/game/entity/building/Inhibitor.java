package com.arena.game.entity.building;

import com.arena.game.Game;
import com.arena.game.entity.*;
import com.arena.server.Server;
import com.arena.utils.Logger;
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
        transform.setScale(17f);
        this.setTransform(transform);
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

        if (game == null) {
            Logger.failure("Game not found for entity: " + this.getId());
            return;
        }

        this.setSkinAnimation(getSkinAnimationForDeath());

        this.setAttackable(false);
        for (String nextObjectiveGeneralId : this.getNextObjective()) {
            LivingEntity nextObjective = game.getLivingEntityByGeneralId(nextObjectiveGeneralId);
            if (nextObjective != null) {
                nextObjective.setAttackable(true);
            }
        }
    }
}
