package com.example;

import com.example.domain.JoinRequestState;
import com.example.domain.MenuItem;
import com.example.domain.Credentials;
import com.example.domain.Role;
import com.example.entity.Group;
import com.example.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;


@EnableTransactionManagement
@SpringBootApplication
@RequiredArgsConstructor
public class GameClubApplication implements CommandLineRunner {

    private final GameClubService gameClubService;
    private final ConsoleView consoleView;

    public static void main(String[] args) {
        SpringApplication.run(GameClubApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Credentials credentials = consoleView.readCredentials();
        if (credentials != null) {
            com.example.entity.Player player = gameClubService.authenticate(credentials);
            if (player != null) {
                consoleView.displayLogin(player);
                Group group;
                if (player.getRoles().contains(Role.GROUP_ADMIN)) {
                    group = gameClubService.getGroupForAdmin(player);
                    List<String> joinRequestNames = gameClubService.getJoinRequestPlayerNames();
                    consoleView.displayAdmin(player, group, joinRequestNames);
                } else {
                    group = gameClubService.getGroupForPlayer(player);
                    consoleView.displayPlayer(player, group);
                }
                showMainMenu(player);
            } else {
                consoleView.displayLoginFailure();
            }
        }

    }

    private void showMainMenu(Player player) {
        boolean showMenu = false;
        do {
            MenuItem menuItem = consoleView.startMenu(player);
            consoleView.displayMenuPoint(menuItem);
            showMenu = selectedMenuPointStart(menuItem);
        } while(showMenu);
    }

    private boolean selectedMenuPointStart(MenuItem menuItem) {
        boolean isBackToTheMenu = false;
        switch (menuItem.getId()) {
            case 1:
                consoleView.displayGameList(gameClubService.getGameList());
                isBackToTheMenu = true;
                break;
            case 2:
                consoleView.displayOptionalGames(gameClubService.getAllOptionalGames());
                boolean isSuccess = gameClubService.addNewGame(consoleView.readingSelectedMenuNumber());
                if (isSuccess) {
                    consoleView.displayGameSelected();
                }
                isBackToTheMenu = true;
                break;
            case 3:
                consoleView.displayJoinableGroups(gameClubService.getJoinableGroups());
                isSuccess = gameClubService.addJoinRequest(consoleView.readingSelectedMenuNumber());
                if (isSuccess) {
                    consoleView.displayGroupSelected();
                }
                isBackToTheMenu = true;
                break;
            case 4:
                consoleView.displayJoinRequests(gameClubService.getJoinRequestPlayerNames());
                JoinRequestState state = gameClubService.processSelectedJoinRequests(consoleView.readingSelectedJoinRequest());
                consoleView.displayJoinRequestState(state);
                isBackToTheMenu = true;
                break;
            case 5:
                gameClubService.quitApplication();
                break;
            case 6:
                isSuccess = gameClubService.addNewGame(consoleView.displayNewGameForm());
                if (isSuccess) {
                    consoleView.displaySuccessGameAdding();
                }
                isBackToTheMenu = true;
                break;
        }
        return isBackToTheMenu;
    }
}
