package com.arena.game.entity.building;

import com.arena.game.Game;
import com.arena.game.entity.*;
import com.arena.game.entity.champion.Garen;
import com.arena.network.response.Response;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.arena.utils.Position;
import com.arena.utils.Vector3f;
import com.arena.utils.json.JsonService;

import java.util.ArrayList;

public class Tower extends LivingEntity {
    private String skinAnimationIdle = "None";

    public Tower(String id, int team) {
        super(id, 300, team, "Tower");

        this.setAttackDamage(100);

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
        transform.setScale(13f);
        this.setTransform(transform);

        this.setSkinScale(0.004f);
        this.setSkinPos(new Vector3f(0.0f, -0.65f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());
    }

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

        int newEntityTeam = 0;
        String entityId = this.getGeneralId();
        int entityTeam = this.getTeam();

        game.removeEntity(this);


        Position position;
        switch (entityTeam) {
            case 1:
                position = EntityPositions.BLUE_TOWERS.get(entityId);
                break;
            case 2:
                position = EntityPositions.RED_TOWERS.get(entityId);
                break;
            default:
                position = EntityPositions.BLUE_TOWERS.get(entityId);
                break;
        }

        TowerDead towerDead = new TowerDead(entityId + "_DEAD", newEntityTeam);
        towerDead.setPos(position);

        game.addEntity(towerDead);
    }
}
