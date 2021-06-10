package com.Pokemon.PokemonBattler.MovesList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovesListService {
    private final MovesListRepository movesListRepository;

    @Autowired
    public MovesListService(MovesListRepository movesListRepository) {
        this.movesListRepository = movesListRepository;
    }

    public List<MovesList> getAllMoves() {
        return movesListRepository.findAll();
    }
}
