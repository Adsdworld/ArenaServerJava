package com.arena.game.entity.champion;

import com.arena.game.entity.EntityCollider;
import com.arena.game.entity.EntityNavMeshAgent;
import com.arena.game.entity.EntityRigidbody;
import com.arena.game.entity.LivingEntity;

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

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(false);
        this.rigidbody = rigidbody;

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.collider = collider;

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(true);
        this.navMeshAgent = navMeshAgent;
    }

    /*public void Q() {  }
    public void Z() {  attaque 2  }
    public void E() {  attaque 3  }
    public void R() {  attaque ultime  }*/
}
