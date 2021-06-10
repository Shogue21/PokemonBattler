package com.Pokemon.PokemonBattler.PokemonList;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pokemon")
@Data public class PokemonList implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String type;
}
