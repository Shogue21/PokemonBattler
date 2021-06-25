package com.Pokemon.PokemonBattler;

import com.Pokemon.PokemonBattler.Moves.MoveRepository;
import com.Pokemon.PokemonBattler.Pokemon.Pokemon;
import com.Pokemon.PokemonBattler.Pokemon.PokemonRepository;
import com.Pokemon.PokemonBattler.Pokemon.PokemonService;
import com.Pokemon.PokemonBattler.PokemonList.PokemonList;
import com.Pokemon.PokemonBattler.PokemonList.PokemonListRepository;
import com.Pokemon.PokemonBattler.PokemonTeam.PokemonTeam;
import com.Pokemon.PokemonBattler.Users.CustomUserDetails;
import com.Pokemon.PokemonBattler.Users.User;
import com.Pokemon.PokemonBattler.Users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private PokemonListRepository pokemonListRepository;

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/")
    public String homePage(Model model) {

        return "homePage";
    }

    @GetMapping("/create_team")
    public String createPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (! pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User").isEmpty()) {
            return "team_already_exists";
        }
        List<PokemonList> pokemonList = pokemonListRepository.findAll();
        Pokemon pokemon1 = new Pokemon();
        Pokemon pokemon2 = new Pokemon();
        Pokemon pokemon3 = new Pokemon();
        Pokemon pokemon4 = new Pokemon();
        Pokemon pokemon5 = new Pokemon();
        Pokemon pokemon6 = new Pokemon();
        PokemonTeam team = new PokemonTeam(pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6);
        model.addAttribute("team", team);
        model.addAttribute("pokemonList", pokemonList);
        return "create_team";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createTeam (@ModelAttribute("team") PokemonTeam team, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();
        PokemonList p1 = pokemonListRepository.findPokemonByName(team.pokemon1.getName());
        pokemonService.createPokemon(p1.getName(), p1.getType(), username);
        PokemonList p2 = pokemonListRepository.findPokemonByName(team.pokemon2.getName());
        pokemonService.createPokemon(p2.getName(), p2.getType(), username);
        PokemonList p3 = pokemonListRepository.findPokemonByName(team.pokemon3.getName());
        pokemonService.createPokemon(p3.getName(), p3.getType(), username);
        PokemonList p4 = pokemonListRepository.findPokemonByName(team.pokemon4.getName());
        pokemonService.createPokemon(p4.getName(), p4.getType(), username);
        PokemonList p5 = pokemonListRepository.findPokemonByName(team.pokemon5.getName());
        pokemonService.createPokemon(p5.getName(), p5.getType(), username);
        PokemonList p6 = pokemonListRepository.findPokemonByName(team.pokemon6.getName());
        pokemonService.createPokemon(p6.getName(), p6.getType(), username);
        pokemonService.createComTeam(username);

        return "redirect:/play";
    }

    @GetMapping("/play")
    public String playPage(Model model) {
        List<PokemonList> pokemonList = pokemonListRepository.findAll();
        model.addAttribute("pokemonList", pokemonList);
        return "play";
    }

    @GetMapping("/team")
    public String teamPage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(username, "User");
        model.addAttribute("userTeam", userTeam);
        return "team";
    }

    @GetMapping("/team_detail")
    public String teamDetailPage(@RequestParam String pokemon, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();

     Pokemon searchedPokemon = pokemonRepository.findPokemonByNameAndTeam(pokemon, username);
     searchedPokemon.setMoveList(moveRepository.findMovesByPokemonAndTeam(searchedPokemon.getName(), username));
     model.addAttribute("pokemon", searchedPokemon);
        return "team_detail";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register_success";
    }
}
