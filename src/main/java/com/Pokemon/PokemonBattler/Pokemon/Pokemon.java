package com.Pokemon.PokemonBattler.Pokemon;

import com.Pokemon.PokemonBattler.Moves.Move;
import lombok.Data;

import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name = "assigned_pokemon", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "username", "team"})})
public @Data class Pokemon implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private int health;
    @Column(nullable = false)
    private int maxHealth;
    @Column(nullable = false)
    private int attack;
    @Column(nullable = false)
    private int defense;
    @Column(nullable = false)
    private int speed;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String team;
    @Column(nullable = false)
    private boolean current;

    @Transient
    private ArrayList<Move> moveList = new ArrayList<>();

        public Pokemon() {}

        public Pokemon(String initName, String initType, String initUsername, String initTeam, boolean initCurrent) {
            name = initName;
            type = initType;
            username = initUsername;
            team = initTeam;
            current = initCurrent;
            this.generateRandomStats();
        }

        public Pokemon(String loadedName, String loadedType, int loadedHealth, int loadedMaxHealth, int loadedAttack, int loadedDefense, int loadedSpeed){
            name = loadedName;
            type = loadedType;
            health = loadedHealth;
            maxHealth = loadedMaxHealth;
            attack = loadedAttack;
            defense = loadedDefense;
            speed = loadedSpeed;
        }

        public Move useRandomMove() {
            while (true) {
                int randomIndex = (int) Math.floor(Math.random() * moveList.size());
                Move randomMove = moveList.get(randomIndex);
                if (randomMove.getPP() != 0) {
                    return randomMove;
                } else if (moveList.stream().allMatch(p->p.getPP()==0)) {
                    return new Move("Struggle", "Normal", 20, 1000, 100);
                }
            }
        }

        public void generateRandomStats() {
            maxHealth = (int) Math.max(50, (Math.random() * (100 - 50) + 50));
            health = maxHealth;
            attack = (int) Math.max(20, (Math.random() * (40 - 20) + 20));
            defense = (int) Math.max(20, (Math.random() * (40 - 20) + 20));
            speed = (int) Math.max(20, (Math.random() * (40 - 20) + 20));
        }
    }
