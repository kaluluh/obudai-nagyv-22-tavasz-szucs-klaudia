package com.example.controller;

import com.example.GameClubService;
import com.example.domain.Credentials;
import com.example.entity.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@AllArgsConstructor
public class GameClubController {

    private GameClubService gameClubService;

    @RequestMapping("/")
    public String showHome(Model model){
        Credentials credentials = new Credentials("kaluluh","asd-123");
        Player player = gameClubService.authenticate(credentials);
        model.addAttribute("player", player);
        return "user-details";
    }

    @GetMapping("user-details")
    public String showUserDetails(Model model) {
        Credentials credentials = new Credentials("kaluluh","asd-123");
        Player player = gameClubService.authenticate(credentials);
        model.addAttribute("player", player);
        return "user-details";
    }

    @GetMapping("group-details")
    public String showGroupDetails(Model model) {
        return "group-details";
    }

    @GetMapping("games-list")
    public String showGameDetails(Model model) {
        return "games-list";
    }
}
