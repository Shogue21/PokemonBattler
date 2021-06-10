package com.Pokemon.PokemonBattler.Moves;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "assigned_moves")
public @Data class Move implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private int power;
    @Column(nullable = false)
    private int PP;
    @Column(nullable = false)
    private int MaxPP;
    @Column(nullable = false)
    private int accuracy;
    @Column(nullable = false)
    private String pokemon;

    public Move() {}

    public Move(String initName, String initType, int initPower, int initPP, int initAcc) {
        name = initName;
        type = initType;
        power = initPower;
        MaxPP = initPP;
        PP = MaxPP;
        accuracy = initAcc;
    }
    public Move(String loadedName, String loadedType, int loadedPower, int loadedPP, int loadedMaxPP, int loadedAcc) {
        name = loadedName;
        type = loadedType;
        power = loadedPower;
        MaxPP = loadedMaxPP;
        PP = loadedPP;
        accuracy = loadedAcc;
    }
}

