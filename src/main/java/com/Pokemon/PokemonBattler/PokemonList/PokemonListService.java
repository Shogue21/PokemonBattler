package com.Pokemon.PokemonBattler.PokemonList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonListService {
    private final PokemonListRepository pokemonListRepository;

    @Autowired
    public PokemonListService(PokemonListRepository pokemonListRepository) {
        this.pokemonListRepository = pokemonListRepository;
    }

    public List<PokemonList> getPokemon() {
        return pokemonListRepository.findAll();
    }
}
