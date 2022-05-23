package com.example.controller;

import com.example.GameClubService;
import com.example.MetaData;
import com.example.dto.EventFormDTO;
import com.example.dto.GameFormDTO;
import com.example.dto.JoinRequestsDTO;
import com.example.entity.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@AllArgsConstructor
public class GameClubController {

    private GameClubService gameClubService;


    @GetMapping("/")
    public String showHome(Model model){
        gameClubService.findCurrentPlayerAndGroup();
        List<Game> games = gameClubService.getGameList();
        model.addAttribute("player", MetaData.currentPlayer);
        model.addAttribute("games", games);
        return "user-details";
    }

    @GetMapping("group-membership")
    public String showGroups(Model model) {
        List<Group> joinableGroups = gameClubService.getJoinableGroups();
        model.addAttribute("currentGroup",  MetaData.currentPlayerGroup);
        model.addAttribute("joinableGroups", joinableGroups);
        model.addAttribute("currentPlayer", MetaData.currentPlayer);
//        Boolean bool = MetaData.currentPlayerGroup.getJoinRequests().contains(MetaData.currentPlayer.getId());
        return "group-membership";
    }

    @GetMapping("games-list")
    public String showGameDetails(Model model) {
        List<Game> currentGames = MetaData.currentPlayer.getGames();
        List<Game> optionalGames = gameClubService.getAllOptionalGames();
        model.addAttribute("currentGames", currentGames);
        model.addAttribute("optionalGames", optionalGames);
        return "games-list";
    }


    @GetMapping ("group-details/{groupId}")
    public String showGroupDetails(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("currentPlayer", MetaData.currentPlayer);
        Group group = gameClubService.getGroupById(groupId);
        model.addAttribute("group", group);
        return "group-details";
    }

    @GetMapping ("event-details/{groupId}")
    public String showEventDetails(@PathVariable("groupId") Long groupId, Model model) {
        List<Event> events = gameClubService.getEvents(groupId);

        if(!model.containsAttribute("eventForm")) {
            model.addAttribute("eventForm", new EventFormDTO());
        }
        model.addAttribute("currentPlayer", MetaData.currentPlayer);
        model.addAttribute("events", events);
        return "event-details";
    }

    @GetMapping("my-group")
    public String showGroupJoinRequests(Model model) {
        model.addAttribute("currentPlayer", MetaData.currentPlayer);
        List<JoinRequestsDTO> joinRequestsDTOS = gameClubService.getJoinRequestsPlayerNames(MetaData.currentPlayerGroup.getJoinRequests());
        model.addAttribute("joinRequests", joinRequestsDTOS);
        model.addAttribute("group", MetaData.currentPlayerGroup);
        return "my-group";
    }

    @RequestMapping(value = "/add-game-to-list", method = RequestMethod.POST)
    public String addGame(@RequestParam("gameId") String gameId) {
        gameClubService.addNewGame(gameId);
        return "redirect:/";
    }

    @RequestMapping(value = "/attend-group", method = RequestMethod.POST)
    public String attendGroup(@RequestParam("playerId") String playerId, @RequestParam("groupId") String groupId) {
        gameClubService.attendGroup(Long.valueOf(playerId),Long.valueOf(groupId));
        return "redirect:/group-membership";
    }

    @RequestMapping(value = "/attend-event", method = RequestMethod.POST)
    public String attendEvent(@RequestParam("playerId") String playerId, @RequestParam("eventId") String eventId) {
        gameClubService.attendEvent(Long.valueOf(playerId),Long.valueOf(eventId));
        return "redirect:/my-group";
    }

    @RequestMapping(value = "/handle-join-requests", method = RequestMethod.POST)
    public String handleJoinRequests(@RequestParam("playerId") String playerId, @RequestParam("isAccepted") boolean isAccepted, @RequestParam("groupId") String groupId) {
        JoinRequestsDTO joinRequestsDTO = new JoinRequestsDTO(null,Long.valueOf(playerId),Long.valueOf(groupId),isAccepted);
        JoinRequest joinRequest = joinRequestsDTO.toDomainObject();
        gameClubService.processJoinRequest(joinRequest);
        return "redirect:/my-group";
    }

    @PostMapping("/save-event")
    public String saveEvent(@Valid @ModelAttribute("eventForm")  EventFormDTO eventForm,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        Event event = eventForm.toDomainObject();
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.eventForm", result);
            redirectAttributes.addFlashAttribute("eventForm", eventForm);
            return "redirect:/event-details/" + MetaData.currentPlayerGroup.getId();
        }
        else {
            gameClubService.saveEvent(event);
            return "redirect:/event-details/" + MetaData.currentPlayerGroup.getId();
        }
    }

    @GetMapping("/add-game")
    public String showAddGameForm(Model model) {
        model.addAttribute("gameDTO",new GameFormDTO());
        return "add-game";
    }

    @PostMapping("/save-game")
    public String saveEvent(@Valid @ModelAttribute("gameDTO") GameFormDTO gameDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "add-game";
        } else {
            Game game = gameDTO.toDomainObject();
            gameClubService.saveGame(game);
            return "redirect:/";
        }
    }
}
