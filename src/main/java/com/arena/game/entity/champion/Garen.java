package com.arena.game.entity.champion;

import com.arena.game.entity.*;
import com.arena.utils.Vector3f;

public class Garen extends LivingEntity {
    public Garen(String id, int team) {
        super(id, 600, team, "Garen"); // maxHealth
        this.armor = 30;
        this.attackDamage = 60;
        this.moveSpeed = 10.03f;

        this.cooldownQMs = 1000;
        this.cooldownWMs = 5000;
        this.cooldownEMs = 20000;
        this.cooldownRMs = 60000;

        this.setSkinScale(0.005f);
        this.setSkinPos(new Vector3f(0.0f, 0.0f, 0.0f));
        this.setSkinAnimation("Idle1_Base");

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(false);
        this.rigidbody = rigidbody;

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.collider = collider;

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(true);
        this.navMeshAgent = navMeshAgent;

        EntityTransform transform = new EntityTransform();
        transform.setScale(5f);
        this.setTransform(transform);
    }

    /*public void Q() {  }
    public void Z() {  attaque 2  }
    public void E() {  attaque 3  }
    public void R() {  attaque ultime  }*/
}
