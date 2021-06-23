package com.Pokemon.PokemonBattler.MovesList;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "moves")
@Data public class MovesList {

    public MovesList() {}

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
}
