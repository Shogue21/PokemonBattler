package com.Pokemon.PokemonBattler;

import com.Pokemon.PokemonBattler.Pokemon.Pokemon;
import com.Pokemon.PokemonBattler.Pokemon.PokemonRepository;
import com.Pokemon.PokemonBattler.PokemonList.PokemonList;
import com.Pokemon.PokemonBattler.PokemonList.PokemonListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private PokemonListRepository pokemonListRepository;

    @Autowired
    private PokemonRepository pokemonRepository;


    @GetMapping("/")
    public String homePage(Model model) {
        List<PokemonList> pokemonList = pokemonListRepository.findAll();
        model.addAttribute("pokemonList", pokemonList);
        return "homePage";
    }

    @GetMapping("/team")
    public String teamPage(Model model) {
        List<Pokemon> userTeam = pokemonRepository.findAll();
        model.addAttribute("userTeam", userTeam);
        return "team";
    }
}
