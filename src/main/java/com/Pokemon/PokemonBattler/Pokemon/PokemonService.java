package com.Pokemon.PokemonBattler.Pokemon;

import com.Pokemon.PokemonBattler.Moves.Move;
import com.Pokemon.PokemonBattler.Moves.MoveRepository;
import com.Pokemon.PokemonBattler.MovesList.MovesList;
import com.Pokemon.PokemonBattler.MovesList.MovesListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private MovesListRepository movesListRepository;

    @Autowired
    private MoveRepository moveRepository;

    public List<Pokemon> getPokemon() {
        return pokemonRepository.findAll();
    }

    public List<MovesList> getMoves() { return movesListRepository.findAll();}

    public void createPokemon(String name, String type, String username) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(name);
        pokemon.setType(type);
        pokemon.setTeam(username);
        pokemon.generateRandomStats();
        List<MovesList> allMoves = movesListRepository.findAll();
        while (pokemon.getMoveList().size() != 4) {
            int randomIndex = (int) Math.floor(Math.random() * allMoves.size());
            MovesList randomMove = allMoves.get(randomIndex);
            Move newMove = new Move(randomMove.getName(), randomMove.getType(), randomMove.getPower(), randomMove.getMaxPP(), randomMove.getAccuracy());
            if ((newMove.getType().equalsIgnoreCase(pokemon.getType()) || newMove.getType().equalsIgnoreCase("normal")) && pokemon.getMoveList().stream().noneMatch(p->p.getName().equalsIgnoreCase(newMove.getName()))) {
                pokemon.getMoveList().add(newMove);
                newMove.setPokemon(pokemon.getName());
                newMove.setUser(username);
                moveRepository.save(newMove);
            }
        }
        pokemonRepository.save(pokemon);
    }
}
