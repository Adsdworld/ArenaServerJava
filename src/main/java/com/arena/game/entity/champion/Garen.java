package com.arena.game.entity.champion;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.game.zone.Zone;
import com.arena.game.zone.ZoneCircle;
import com.arena.game.zone.ZoneCone;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Vector3f;

/**
 * Garen is a champion in the game with specific abilities and animations.
 * This class extends LivingEntity and implements the champion's abilities.
 * It includes methods for handling abilities Q, W, E, and R, as well as death and respawn logic.
 */
public class Garen extends LivingEntity {
    private String skinAnimationForIdle = "Idle1_Base";
    private String skinAnimationForRun = "Run";
    private String skinAnimationForQ = "Spell1";
    private long skinAnimationDurationForQ = 1000;
    private String skinAnimationForW = "Dance_Base";
    private long skinAnimationDurationForW = 2666;
    private String skinAnimationForE = "Spell3_90";
    private long skinAnimationDurationForE = 3000;
    private String skinAnimationForR = "Spell4_Base";
    private long skinAnimationDurationForR = 1566;
    private String skinAnimationForDeath = "Death";
    private long skinAnimationDurationForDeath = 4300;

    private int qDamage = 50;
    private int wShield = 300;
    private int eDamage = 100;
    private int rDamage = 250;

    /**
     * Constructor for Garen.
     *
     * @param id   the unique identifier for the Garen entity.
     * @param team the team number (1 or 2) that Garen belongs to.
     */
    public Garen(String id, int team) {
        super(id, 600, team, "Garen"); // maxHealth
        this.setMoveSpeed(12f);

        this.setAttackable(true);

        this.setCooldownQMs(1200);
        this.setCooldownWMs(5000);
        this.setCooldownEMs(20000);
        this.setCooldownRMs(60000);

        this.setSkinScale(0.007f);
        this.setSkinPos(new Vector3f(0.0f, -1.04f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());

        Champion champion = new Champion(5f);

        this.setRigidbody(champion.getRigidbody());

        this.setCollider(champion.getCollider());

        this.setNavMeshAgent(champion.getNavMeshAgent());

        this.setTransform(champion.getTransform());
    }

    @Override
    public String getSkinAnimationForRunning() {
        return skinAnimationForRun;
    }

    @Override
    public String getSkinAnimationForIdle() {
        return skinAnimationForIdle;
    }

    @Override
    public String getSkinAnimationForQ() {
        return skinAnimationForQ;
    }

    @Override
    public String getSkinAnimationForW() {
        return skinAnimationForW;
    }

    @Override
    public String getSkinAnimationForE() {
        return skinAnimationForE;
    }

    @Override
    public String getSkinAnimationForR() {
        return skinAnimationForR;
    }

    @Override
    public String getSkinAnimationForDeath() {
        return skinAnimationForDeath;
    }

    @Override
    public long getSkinAnimationDurationForQ() {return (long) (this.getSkinAnimationBaseSpeed() / this.getSkinAnimationSpeed()) * skinAnimationDurationForQ;}

    @Override
    public long getSkinAnimationDurationForW() {
        return (long) (this.getSkinAnimationBaseSpeed() / this.getSkinAnimationSpeed()) * skinAnimationDurationForW;
    }

    @Override
    public long getSkinAnimationDurationForE() {
        return (long) (this.getSkinAnimationBaseSpeed() / this.getSkinAnimationSpeed()) * skinAnimationDurationForE;
    }

    @Override
    public long getSkinAnimationDurationForR() {
        return (long) (this.getSkinAnimationBaseSpeed() / this.getSkinAnimationSpeed()) * skinAnimationDurationForR;
    }

    @Override
    public long getSkinAnimationDurationForDeath() {
        return (long) (this.getSkinAnimationBaseSpeed() / this.getSkinAnimationSpeed()) * skinAnimationDurationForDeath;
    }

    @Override
    public int getQTotalDamage() {
        return qDamage; // Q deals 120% of AD as damage
    }

    @Override
    public int getWTotalShield() {
        return wShield; // W does not deal damage, it provides a shield
    }

    @Override
    public int getETotalDamage() {
        return eDamage; // E deals 150% of AD as damage
    }

    @Override
    public int getRTotalDamage() {
        return rDamage; // R deals 250% of AD as damage
    }

    @Override
    public Zone getQZone() {
        return new ZoneCone(15, 120);
    }

    @Override
    public void useQ() {
        Server server = Server.getInstance();

        Player player = new Player(this.getId());

        Game game = server.getGameOfPlayer(player);
        if (game != null) {
            game.dealDamageToEnnemies(this, this.getQZone(), this.getQTotalDamage());
        }
    }

    @Override
    public void useW() {
        Server server = Server.getInstance();

        Player player = new Player(this.getId());

        Game game = server.getGameOfPlayer(player);
        if (game != null) {
            game.healSelf(this);
        }
    }

    @Override
    public Zone getEZone() {
        return new ZoneCircle(15);
    }

    @Override
    public void useE() {
        Server server = Server.getInstance();

        Player player = new Player(this.getId());

        Game game = server.getGameOfPlayer(player);
        if (game != null) {
            game.dealDamageToEnnemies(this, this.getEZone(), this.getETotalDamage());
        }
    }

    @Override
    public Zone getRZone() {
        return new ZoneCircle(20);
    }

    @Override
    public void useR() {
        Server server = Server.getInstance();

        Player player = new Player(this.getId());

        Game game = server.getGameOfPlayer(player);
        if (game != null) {
            game.dealDamageToEnnemies(this, this.getRZone(), this.getRTotalDamage());
        }
    }

    @Override
    public void die() {
        this.lockEntity(true);

        Server server = Server.getInstance();

        Player player = new Player(this.getId());
        Game game = server.getGameOfPlayer(player);
        if (game != null) {
            int entityTeam = this.getTeam();

            game.removeEntity(this);

            Garen garen = new Garen(player.getUuid(), entityTeam);
            garen.spawnAtTeamSpawn();
            game.addEntity(garen);

            Response response = new Response();
            response.setGameName(game.getGameNameEnum());
            response.setResponse(ResponseEnum.Info);
            response.setNotify(this.getName() + " " + this.getId() + " have respawn.");
            response.send(game.getGameNameEnum());

            /* Clear the Unity game of the player */
            game.clearUnityGame(player);

            /* Subscribe player to the game */
            server.subscribePlayerToGame(player, game);

            /* Assign the new entity to the player */
            game.yourEntityIs(player.getUuid());
        }
    }
}
