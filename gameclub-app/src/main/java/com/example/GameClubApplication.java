package com.example;

import com.example.domain.JoinRequestState;
import com.example.domain.MenuItem;
import com.example.domain.Player;
import com.example.domain.User;
import com.example.domain.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
            User user = gameClubService.authenticate(credentials);
            if (user != null) {
                consoleView.displayLogin(user);
                Player player = gameClubService.findUserData(user);
                if (player != null) {
                    consoleView.displayPlayer(player);
                    showMainMenu(player);
                }
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
                consoleView.displayOptionalGroups(gameClubService.getOptionalGroups());
                isSuccess = gameClubService.addJoinRequest(consoleView.readingSelectedMenuNumber());
                if (isSuccess) {
                    consoleView.displayGroupSelected();
                }
                isBackToTheMenu = true;
                break;
            case 4:
                consoleView.displayJoinRequests(gameClubService.getJoinRequests());
                JoinRequestState state = gameClubService.proccessSelectedJoinRequests(consoleView.readingSelectedJoinRequest());
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
