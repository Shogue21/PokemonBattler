package com.Pokemon.PokemonBattler.Pokemon;

import com.Pokemon.PokemonBattler.Moves.Move;
import lombok.Data;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name = "assigned_pokemon")
public @Data class Pokemon implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
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
    private String team;

        private ArrayList<Move> moveList = new ArrayList<>();
        private ArrayList<Move> allMoves = getAllMoves();


        public Pokemon() {}

        public Pokemon(String initName, String initType) {
            name = initName;
            type = initType;
            this.generateRandomStats();
            this.assignRandomMoves();
        }

        public Pokemon(String loadedName, String loadedType, int loadedHealth, int loadedMaxHealth, int loadedAttack, int loadedDefense, int loadedSpeed){
            name = loadedName;
            type = loadedType;
            health = loadedHealth;
            maxHealth = loadedMaxHealth;
            attack = loadedAttack;
            defense = loadedDefense;
            speed = loadedSpeed;
            this.assignRandomMoves();
        }

        public void assignRandomMoves() {
            while (this.getMoveList().size() != 4) {
                int randomIndex = (int) Math.floor(Math.random() * allMoves.size());
                Move randomMove = allMoves.get(randomIndex);
                Move newMove = new Move(randomMove.getName(), randomMove.getType(), randomMove.getPower(), randomMove.getMaxPP(), randomMove.getAccuracy());
                if ((newMove.getType().equalsIgnoreCase(this.getType()) || newMove.getType().equalsIgnoreCase("normal")) && this.getMoveList().stream().noneMatch(p->p.getName().equalsIgnoreCase(newMove.getName()))) {
                    this.getMoveList().add(newMove);
                }
            }
        }

        public Move useRandomMove() {
            while (true) {
                int randomIndex = (int) Math.floor(Math.random() * moveList.size());
                Move randomMove = moveList.get(randomIndex);
                if (randomMove.getPP() != 0) {
                    System.out.printf("%s used %s!\n", this.name, randomMove.getName());
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
