package com.Pokemon.PokemonBattler;

import com.Pokemon.PokemonBattler.Moves.Move;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public String homePage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }

        return "homePage";
    }

    @GetMapping("/create_team")
    public String createPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }
        if (! pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User").isEmpty()) {
            return "team_already_exists";
        }
        List<PokemonList> pokemonList = pokemonListRepository.findAll();
        pokemonList.sort(Comparator.comparing(PokemonList :: getType));
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
        pokemonService.createPokemon(p1.getName(), p1.getType(), username, true);
        PokemonList p2 = pokemonListRepository.findPokemonByName(team.pokemon2.getName());
        pokemonService.createPokemon(p2.getName(), p2.getType(), username, false);
        PokemonList p3 = pokemonListRepository.findPokemonByName(team.pokemon3.getName());
        pokemonService.createPokemon(p3.getName(), p3.getType(), username, false);
        PokemonList p4 = pokemonListRepository.findPokemonByName(team.pokemon4.getName());
        pokemonService.createPokemon(p4.getName(), p4.getType(), username, false);
        PokemonList p5 = pokemonListRepository.findPokemonByName(team.pokemon5.getName());
        pokemonService.createPokemon(p5.getName(), p5.getType(), username, false);
        PokemonList p6 = pokemonListRepository.findPokemonByName(team.pokemon6.getName());
        pokemonService.createPokemon(p6.getName(), p6.getType(), username, false);
        pokemonService.createComTeam(username);

        return "redirect:/play";
    }

    @GetMapping("/play")
    public String playPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User").isEmpty()) {
            return "redirect:/create_team";
        }
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }
        Pokemon currentUser = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        ArrayList<Move> userMoves = moveRepository.findMovesByPokemonAndUsernameAndTeam(currentUser.getName(), currentUser.getUsername(), currentUser.getTeam());
        userMoves.sort(Comparator.comparing(Move::getName));
        currentUser.setMoveList(userMoves);
        Pokemon currentCom = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        currentCom.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(currentCom.getName(), currentCom.getUsername(), currentCom.getTeam()));
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        List<Pokemon> comTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "Com");
        model.addAttribute("userMon", currentUser);
        model.addAttribute("comMon", currentCom);
        model.addAttribute("userTeam", userTeam);
        model.addAttribute("comTeam", comTeam);
        return "play";
    }

    @RequestMapping(value = "/fight", method = RequestMethod.POST, params = "move1")
    public String fightMove1(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        List<Pokemon> comTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "Com");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        ArrayList<Move> userMoves = moveRepository.findMovesByPokemonAndUsernameAndTeam(userMon.getName(), userMon.getUsername(), "User");
        userMoves.sort(Comparator.comparing(Move::getName));
        userMon.setMoveList(userMoves);
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), "Com"));
        System.out.println(userMon.getName());
        System.out.println(comMon.getName());
        Move attackingMove = userMon.getMoveList().get(0);
        if (userMon.getSpeed() > comMon.getSpeed()) {
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
        } else {
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/fight", method = RequestMethod.POST, params = "move2")
    public String fightMove2(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        List<Pokemon> comTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "Com");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        ArrayList<Move> userMoves = moveRepository.findMovesByPokemonAndUsernameAndTeam(userMon.getName(), userMon.getUsername(), userMon.getTeam());
        userMoves.sort(Comparator.comparing(Move::getName));
        userMon.setMoveList(userMoves);
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        Move attackingMove = userMon.getMoveList().get(1);
        if (userMon.getSpeed() > comMon.getSpeed()) {
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
        } else {
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/fight", method = RequestMethod.POST, params = "move3")
    public String fightMove3(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        List<Pokemon> comTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "Com");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        ArrayList<Move> userMoves = moveRepository.findMovesByPokemonAndUsernameAndTeam(userMon.getName(), userMon.getUsername(), userMon.getTeam());
        userMoves.sort(Comparator.comparing(Move::getName));
        userMon.setMoveList(userMoves);
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        Move attackingMove = userMon.getMoveList().get(2);
        if (userMon.getSpeed() > comMon.getSpeed()) {
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
        } else {
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/fight", method = RequestMethod.POST, params = "move4")
    public String fight(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        List<Pokemon> comTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "Com");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        ArrayList<Move> userMoves = moveRepository.findMovesByPokemonAndUsernameAndTeam(userMon.getName(), userMon.getUsername(), userMon.getTeam());
        userMoves.sort(Comparator.comparing(Move::getName));
        userMon.setMoveList(userMoves);
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        Move attackingMove = userMon.getMoveList().get(3);
        if (userMon.getSpeed() > comMon.getSpeed()) {
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
        } else {
            pokemonService.damagePokemon(comMon, userMon, comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            pokemonService.damagePokemon(userMon, comMon, attackingMove);
            if (comMon.getHealth() == 0) {
                if (comTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/player_win";
                }
                return "redirect:/play";
            }
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/swapMon", method = RequestMethod.POST, params = "p1")
    public String swapMon1(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        if (userTeam.get(0).getHealth() == 0) {
            return "swap_failure";
        } else {
            pokemonService.swapPokemon(userTeam.get(0));
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/swapMon", method = RequestMethod.POST, params = "p2")
    public String swapMon2(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        if (userTeam.get(1).getHealth() == 0) {
            return "swap_failure";
        } else {
            pokemonService.swapPokemon(userTeam.get(1));
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/swapMon", method = RequestMethod.POST, params = "p3")
    public String swapMon3(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        if (userTeam.get(2).getHealth() == 0) {
            return "swap_failure";
        } else {
            pokemonService.swapPokemon(userTeam.get(2));
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/swapMon", method = RequestMethod.POST, params = "p4")
    public String swapMon4(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        if (userTeam.get(3).getHealth() == 0) {
            return "swap_failure";
        } else {
            pokemonService.swapPokemon(userTeam.get(3));
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/swapMon", method = RequestMethod.POST, params = "p5")
    public String swapMon5(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        if (userTeam.get(4).getHealth() == 0) {
            return "swap_failure";
        } else {
            pokemonService.swapPokemon(userTeam.get(4));
        }
        return "redirect:/play";
    }

    @RequestMapping(value = "/swapMon", method = RequestMethod.POST, params = "p6")
    public String swapMon6(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        if (userTeam.get(5).getHealth() == 0) {
            return "swap_failure";
        } else {
            pokemonService.swapPokemon(userTeam.get(5));
        }
        return "redirect:/play";
    }

    @GetMapping("/swap")
    public String swap(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        model.addAttribute("userTeam", userTeam);
        return "swap";
    }

    @GetMapping("/switch_turn")
    public String switchTurn(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        model.addAttribute("userTeam", userTeam);
        return "switch_turn";
    }

    @RequestMapping(value = "/switchMon", method = RequestMethod.POST, params = "p1")
    public String switchMon1(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        if (userTeam.get(0).getHealth() == 0 || userTeam.get(0) == userMon) {
            return "switch_failure";
        } else {
            pokemonRepository.delete(userMon);
            userMon.setCurrent(false);
            pokemonRepository.save(userMon);
            pokemonService.swapPokemon(userTeam.get(0));
            pokemonService.damagePokemon(comMon, userTeam.get(0), comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            return "redirect:/play";
        }
    }

    @RequestMapping(value = "/switchMon", method = RequestMethod.POST, params = "p2")
    public String switchMon2(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        if (userTeam.get(1).getHealth() == 0 || userTeam.get(1) == userMon) {
            return "switch_failure";
        } else {
            pokemonRepository.delete(userMon);
            userMon.setCurrent(false);
            pokemonRepository.save(userMon);
            pokemonService.swapPokemon(userTeam.get(1));
            pokemonService.damagePokemon(comMon, userTeam.get(1), comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            return "redirect:/play";
        }
    }

    @RequestMapping(value = "/switchMon", method = RequestMethod.POST, params = "p3")
    public String switchMon3(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        if (userTeam.get(2).getHealth() == 0 || userTeam.get(2) == userMon) {
            return "switch_failure";
        } else {
            pokemonRepository.delete(userMon);
            userMon.setCurrent(false);
            pokemonRepository.save(userMon);
            pokemonService.swapPokemon(userTeam.get(2));
            pokemonService.damagePokemon(comMon, userTeam.get(2), comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            return "redirect:/play";
        }
    }

    @RequestMapping(value = "/switchMon", method = RequestMethod.POST, params = "p4")
    public String switchMon4(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        if (userTeam.get(3).getHealth() == 0 || userTeam.get(3) == userMon) {
            return "switch_failure";
        } else {
            pokemonRepository.delete(userMon);
            userMon.setCurrent(false);
            pokemonRepository.save(userMon);
            pokemonService.swapPokemon(userTeam.get(3));
            pokemonService.damagePokemon(comMon, userTeam.get(3), comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            return "redirect:/play";
        }
    }

    @RequestMapping(value = "/switchMon", method = RequestMethod.POST, params = "p5")
    public String switchMon5(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        if (userTeam.get(4).getHealth() == 0 || userTeam.get(4) == userMon) {
            return "switch_failure";
        } else {
            pokemonRepository.delete(userMon);
            userMon.setCurrent(false);
            pokemonRepository.save(userMon);
            pokemonService.swapPokemon(userTeam.get(4));
            pokemonService.damagePokemon(comMon, userTeam.get(4), comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            return "redirect:/play";
        }
    }

    @RequestMapping(value = "/switchMon", method = RequestMethod.POST, params = "p6")
    public String switchMon6(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(userDetails.getUsername(), "User");
        Pokemon userMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "User");
        Pokemon comMon = pokemonRepository.findPokemonByCurrentAndUsernameAndTeam(true, userDetails.getUsername(), "Com");
        comMon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(comMon.getName(), comMon.getUsername(), comMon.getTeam()));
        if (userTeam.get(5).getHealth() == 0 || userTeam.get(5) == userMon) {
            return "switch_failure";
        } else {
            pokemonRepository.delete(userMon);
            userMon.setCurrent(false);
            pokemonRepository.save(userMon);
            pokemonService.swapPokemon(userTeam.get(5));
            pokemonService.damagePokemon(comMon, userTeam.get(5), comMon.useRandomMove());
            if (userMon.getHealth() == 0) {
                if (userTeam.stream().noneMatch(h -> h.getHealth() > 0)) {
                    pokemonService.gameOver(userDetails.getUsername());
                    return "redirect:/com_win";
                }
                return "redirect:/swap";
            }
            return "redirect:/play";
        }
    }

    @GetMapping("/team")
    public String teamPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }
        String username = userDetails.getUsername();
        List<Pokemon> userTeam = pokemonRepository.findPokemonByUsernameAndTeam(username, "User");
        model.addAttribute("userTeam", userTeam);
        return "team";
    }

    @GetMapping("/team_detail")
    public String teamDetailPage(@RequestParam String pokemon, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }
        String username = userDetails.getUsername();

     Pokemon searchedPokemon = pokemonRepository.findPokemonByNameAndUsernameAndTeam(pokemon, username, "User");
     searchedPokemon.setMoveList(moveRepository.findMovesByPokemonAndUsernameAndTeam(searchedPokemon.getName(), username, "User"));
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

    @GetMapping("/com_win")
    public String com_win(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }
        return "com_win";
    }

    @GetMapping("/player_win")
    public String player_win(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            String username = userDetails.getFullName();
            model.addAttribute("name", username);
        }
        return "player_win";
    }
}
