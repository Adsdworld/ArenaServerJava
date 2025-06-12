package com.arena.game.entity.champion;

import com.arena.game.entity.*;
import com.arena.utils.Vector3f;

public class Garen extends LivingEntity {
    private String skinAnimationIdle = "Idle1_Base";
    private String skinAnimationRun = "Run";



    public Garen(String id, int team) {
        super(id, 600, team, "Garen"); // maxHealth
        this.setArmor(30);
        this.setAttackDamage(60);
        this.setMoveSpeed(12f);

        this.setCooldownQMs(1200);
        this.setCooldownWMs(5000);
        this.setCooldownEMs(20000);
        this.setCooldownRMs(60000);

        this.setSkinScale(0.007f);
        this.setSkinPos(new Vector3f(0.0f, 0.0f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(false);
        this.setRigidbody(rigidbody);

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.setCollider(collider);

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(true);
        this.setNavMeshAgent(navMeshAgent);

        EntityTransform transform = new EntityTransform();
        transform.setScale(5f);
        this.setTransform(transform);
    }

    @Override
    public String getSkinAnimationForRunning() {
        return skinAnimationRun;
    }
    @Override
    public String getSkinAnimationForIdle() {
        return skinAnimationIdle;
    }
    @Override
    public String getSkinAnimationForQ() {
        return "Spell1";
    }
    @Override
    public String getSkinAnimationForW() {
        return "Dance_Base";
    }
    @Override
    public String getSkinAnimationForE() {
        return "Spell3_90";
    }
    @Override
    public String getSkinAnimationForR() {
        return "Spell4_Base";
    }

    @Override
    public long getSkinAnimationDurationForQ(){
        return 1000;
    }
    @Override
    public long getSkinAnimationDurationForW(){
        return 2666;
    }
    @Override
    public long getSkinAnimationDurationForE(){
        return 3000;
    }
    @Override
    public long getSkinAnimationDurationForR() {
        return 1566;
    }
}
