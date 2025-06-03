package com.arena.game.entity.champion;

import com.arena.game.entity.LivingEntity;

public class Garen extends LivingEntity {
    public Garen(String id, int team) {
        super(id, 600, team, "Garen"); // maxHealth
        this.armor = 30;
        this.attackDamage = 60;
        this.moveSpeed = 3.5f;
    }

    /*public void Q() {  }
    public void Z() {  attaque 2  }
    public void E() {  attaque 3  }
    public void R() {  attaque ultime  }*/
}
