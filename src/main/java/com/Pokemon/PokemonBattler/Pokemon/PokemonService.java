package com.Pokemon.PokemonBattler.Pokemon;

import com.Pokemon.PokemonBattler.Moves.Move;
import com.Pokemon.PokemonBattler.Moves.MoveRepository;
import com.Pokemon.PokemonBattler.MovesList.MovesList;
import com.Pokemon.PokemonBattler.MovesList.MovesListRepository;
import com.Pokemon.PokemonBattler.PokemonList.PokemonList;
import com.Pokemon.PokemonBattler.PokemonList.PokemonListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("ps")
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private PokemonListRepository pokemonListRepository;

    @Autowired
    private MovesListRepository movesListRepository;

    @Autowired
    private MoveRepository moveRepository;

    public List<Pokemon> getPokemon() {
        return pokemonRepository.findAll();
    }

    public List<MovesList> getMoves() { return movesListRepository.findAll();}

    public void assignMoves(Pokemon pokemon, String username, String team) {
        List<MovesList> allMoves = movesListRepository.findAll();
        while (pokemon.getMoveList().size() != 4) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(allMoves.size());
            MovesList randomMove = allMoves.get(randomIndex);
            Move newMove = new Move(randomMove.getName(), randomMove.getType(), randomMove.getPower(), randomMove.getMaxPP(), randomMove.getAccuracy());
            if ((newMove.getType().equalsIgnoreCase(pokemon.getType()) || newMove.getType().equalsIgnoreCase("normal")) && pokemon.getMoveList().stream().noneMatch(p->p.getName().equalsIgnoreCase(newMove.getName()))) {
                pokemon.getMoveList().add(newMove);
                newMove.setPokemon(pokemon.getName());
                newMove.setTeam(team);
                newMove.setUsername(username);
                moveRepository.save(newMove);
            }
        }
    }

    public void createPokemon(String name, String type, String username, boolean current) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(name);
        pokemon.setType(type);
        pokemon.setUsername(username);
        pokemon.setTeam("User");
        pokemon.setCurrent(current);
        pokemon.generateRandomStats();
        assignMoves(pokemon, username, "User");
        pokemonRepository.save(pokemon);
    }

    public void createComTeam(String username) {
        ArrayList<PokemonList> pokemonList = pokemonListRepository.findAll();
        int teamCount = 0;
        while (teamCount < 6) {
            List<Pokemon> comTeam = pokemonRepository.findPokemonByUsernameAndTeam(username, "Com");
            int randomIndex = (int) Math.floor(Math.random() * pokemonList.size());
            PokemonList randomPokemon = pokemonList.get(randomIndex);
            if (comTeam.stream().noneMatch(p -> p.getName() == randomPokemon.getName())) {
                if (teamCount == 0) {
                    Pokemon newPokemon = new Pokemon(randomPokemon.getName(), randomPokemon.getType(), username, "Com", true);
                    pokemonRepository.save(newPokemon);
                    assignMoves(newPokemon, username, "Com");
                } else {
                    Pokemon newPokemon = new Pokemon(randomPokemon.getName(), randomPokemon.getType(), username, "Com", false);
                    pokemonRepository.save(newPokemon);
                    assignMoves(newPokemon, username, "Com");
                }

                teamCount += 1;
            }
        }
    }

    public double checkEffectiveness(String attacker, String defender) {
        switch (attacker.toLowerCase()) {
            case "fire":
                switch (defender.toLowerCase()) {
                    case "fire":
                    case "water":
                        return 0.5;
                    case "grass":
                        return 2.0;
                    case "normal":
                    case "ground":
                    case "flying":
                    case "electric":
                        return 1.0;
                }
            case "water":
                switch (defender.toLowerCase()) {
                    case "fire":
                    case "ground":
                        return 2.0;
                    case "grass":
                    case "water":
                        return 0.5;
                    case "normal":
                    case "electric":
                    case "flying":
                        return 1.0;
                }
            case "grass":
                switch (defender.toLowerCase()) {
                    case "fire":
                    case "grass":
                    case "flying":
                        return 0.5;
                    case "water":
                    case "ground":
                        return 2.0;
                    case "normal":
                    case "electric":
                        return 1.0;
                }
            case "electric":
                switch (defender.toLowerCase()) {
                    case "electric":
                    case "grass":
                        return 0.5;
                    case "ground":
                        return 0.0;
                    case "flying":
                    case "water":
                        return 2.0;
                    case "fire":
                        return 1.0;
                }
            case "flying":
                switch (defender.toLowerCase()) {
                    case "electric":
                        return 0.5;
                    case "fire":
                    case "water":
                    case "ground":
                    case "flying":
                        return 1.0;
                    case "grass":
                        return 2.0;
                }
            case "ground":
                switch (defender.toLowerCase()) {
                    case "water":
                    case "ground":
                        return 1.0;
                    case "flying":
                        return 0;
                    case "grass":
                        return 0.5;
                    case "fire":
                    case "electric":
                        return 2.0;
                }
        }
        return 1.0;
    }

    public void damagePokemon(Pokemon attacker, Pokemon defender, Move move) {
        System.out.println(move.getName() + move.getTeam());
        double crit = 1.0;
        double stab = 1.0;
        if (Math.random() * 100 < 15) {
            crit = 2;
        }
        if (move.getType().equalsIgnoreCase(attacker.getType())) {
            stab = 1.5;
        }
        if (Math.random() * 100 < move.getAccuracy()) {
            defender.setHealth((int) (defender.getHealth() - ((12 * move.getPower() * attacker.getAttack() / defender.getDefense()) / 50) * checkEffectiveness(move.getType(), defender.getType()) * crit * stab));
        }
        move.setPP(Math.max(0, move.getPP() - 1));
        if (defender.getHealth() <= 0) {
            defender.setHealth(0);
            pokemonRepository.delete(defender);
            defender.setCurrent(false);
            pokemonRepository.save(defender);
            if (defender.getTeam().equalsIgnoreCase("Com")) {
                for (Pokemon comMon: pokemonRepository.findPokemonByUsernameAndTeam(defender.getUsername(), "Com")) {
                    if (comMon.getHealth() != 0) {
                        pokemonRepository.delete(comMon);
                        comMon.setCurrent(true);
                        pokemonRepository.save(comMon);
                        break;
                    }
                }
            }
        }
        Pokemon defenderDelete = pokemonRepository.findPokemonByNameAndUsernameAndTeam(defender.getName(), defender.getUsername(), defender.getTeam());
        pokemonRepository.delete(defenderDelete);
        moveRepository.delete(move);
        moveRepository.save(move);
        pokemonRepository.save(defender);
    }
    public void swapPokemon(Pokemon pokemon) {
        pokemonRepository.delete(pokemon);
        pokemon.setCurrent(true);
        pokemonRepository.save(pokemon);
    }

    public void gameOver(String username) {
        pokemonRepository.deleteAll(pokemonRepository.findPokemonByUsernameAndTeam(username, "User"));
        moveRepository.deleteAll(moveRepository.findMovesByUsernameAndTeam(username, "User"));
        pokemonRepository.deleteAll(pokemonRepository.findPokemonByUsernameAndTeam(username, "Com"));
        moveRepository.deleteAll(moveRepository.findMovesByUsernameAndTeam(username, "Com"));
    }
}
