package com.Pokemon.PokemonBattler.Moves;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveService {
    private final MoveRepository moveRepository;

    public MoveService(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    public List<Move> getMoves() {
        return moveRepository.findAll();
    }
}
