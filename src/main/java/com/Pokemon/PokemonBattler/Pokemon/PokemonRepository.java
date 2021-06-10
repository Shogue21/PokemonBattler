package com.Pokemon.PokemonBattler.Pokemon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serial;
import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Serial>{
}
