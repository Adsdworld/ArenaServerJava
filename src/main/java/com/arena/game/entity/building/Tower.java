package com.arena.game.entity.building;

import com.arena.game.Game;
import com.arena.game.entity.EntityPositions;
import com.arena.game.entity.LivingEntity;
import com.arena.network.response.Response;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;
import com.arena.game.utils.Position;
import com.arena.utils.Vector3f;

public class Tower extends LivingEntity {

    public Tower(String id, int team) {
        super(id, 50, team, "Tower");

        Building building = new Building(13f);

        this.setRigidbody(building.getRigidbody());

        this.setCollider(building.getCollider());

        this.setNavMeshAgent(building.getNavMeshAgent());

        this.setTransform(building.getTransform());

        this.setSkinScale(0.004f);
        this.setSkinPos(new Vector3f(0.0f, -0.65f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());
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
            case 2:
                position = EntityPositions.RED_TOWERS.get(entityId).getPosition();
                break;
            default:
                position = EntityPositions.BLUE_TOWERS.get(entityId).getPosition();
                break;
        }

        TowerDead towerDead = new TowerDead(entityId + "_DEAD", newEntityTeam);
        towerDead.setPos(position);

        game.addEntity(towerDead);


        Response response = new Response();
        response.setResponse(ResponseEnum.Info);
        response.setNotify("Tower " + this.getGeneralId() + " has been destroyed.");
        response.send(game.getGameNameEnum());
    }
}
