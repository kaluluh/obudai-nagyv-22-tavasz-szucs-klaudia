package com.example;

import com.example.domain.*;
import com.example.entity.Game;
import com.example.entity.Group;
import com.example.entity.Player;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

@Component
public class ConsoleView {

    public Credentials readCredentials() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Credentials credentials = null;
        try {
            System.out.println("Login name: ");
            String name = reader.readLine();
            System.out.println("\nPassword: ");
            String password = reader.readLine();
            if (name != null && password != null){
                credentials = new Credentials(name,password);
            }

        } catch (Exception e) {
            System.out.println("Invalid input format! Please try again. Error message: " + e.getMessage());
        }
        return credentials;
    }

    public void displayLogin(com.example.entity.User user) {
        System.out.println("\nLogin successful.Your role(s): \n" );
        user.getRoles().forEach(r -> {
            System.out.println(r.toString());
        });
    }

    public void displayLoginFailure() {
        System.out.println("\nLogin failure, bye!");
    }

    public void displayAdmin(Player player, Group playerGroup, List<String> joinRequestNames) {
       System.out.println(getGroupAdminData(player, playerGroup, joinRequestNames));
    }

    public void displayPlayer(com.example.entity.Player player, com.example.entity.Group playerGroup) {
       System.out.println(getPlayerData(player, playerGroup.getName()));
    }

    public void displayMenuPoint (MenuItem menuItem) {
        switch (menuItem.getId()) {
            case 1:
                System.out.println("\n\nViewing list of all games:\n");
                break;
            case 2:
                System.out.println("Please choose a game from the following list add:\n");
                break;
            case 3:
                System.out.println("Please choose the group you would like to join:\n");
                break;
            case 4:
                System.out.println("List of players who would like to join your group. Please select player number and (A)ccept or (R)eject:\n");
                break;
            case 5:
                System.out.println("Quiting from the application");
                break;
            case 6:
                System.out.println("Creating a new game\n");
                break;
        }
    }

    public GameForm displayNewGameForm () {
        GameForm gameForm = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the required fields to the new game:\n");
        try {
            System.out.println("\nId:");
            long id = Long.parseLong(reader.readLine());
            System.out.println("\nName:");
            String name = reader.readLine();
            System.out.println("\nDescription:");
            String description = reader.readLine();
            System.out.println("\nMinimum age:");
            int minimumAge = Integer.parseInt(reader.readLine());
            System.out.println("\nCategories:");
            String categories = reader.readLine();
            System.out.println("\nNumber of players:\nMinimum:");
            int min = Integer.parseInt(reader.readLine());
            System.out.println("\nMaximum:");
            int max = Integer.parseInt(reader.readLine());
            System.out.println("\nPlaytime:\nFrom:");
            int from = Integer.parseInt(reader.readLine());
            System.out.println("\nTo:");
            int to = Integer.parseInt(reader.readLine());
            gameForm = new GameForm(id,name,description,minimumAge,categories,min,max,from,to);
        } catch (Exception e) {}

        return gameForm;
    }

    public void displaySuccessGameAdding() {
        System.out.println("New game added successfully!\n");
    }

    public void displayOptionalGames(List<com.example.entity.Game> optionalGames) {
        String result = "";
        int id = 1;
        if (optionalGames.size() > 0) {
            for(com.example.entity.Game game : optionalGames) {
                result += id + ". " + ( game.getName() != null ? game.getName() + "\n": "" );
                id++;
            }
        } else {
            System.out.println("No available game selectable.\n");
        }
        result += id + ". Back to the menu\n\nPlease choose an item: ";
        System.out.println(result);
    }

    public void displayJoinableGroups(List<com.example.entity.Group> optionalGroups) {
        String result = "";
        int id = 1;
        for (com.example.entity.Group group : optionalGroups) {
            result += id + ". " + group.getName() + "\n";
            id++;
        }
        result += id + ". Back to the menu\nPlease choose group:";
        System.out.println(result);
    }

    public void displayGameList(List<com.example.entity.Game> games) {
        String result = "";
        for (com.example.entity.Game game : games) {
            result += "\n- id: " + ( game.getId() != null ? game.getId().toString() : "N/A")  +
                    "\n\tName: "
                    + ( game.getName() != null ? game.getName() : "N/A") +
                    "\n\tDescription: "
                    + ( game.getDescription() != null ? game.getDescription() : "N/A" ) +
                    "\n\tCategories: "
                    + ( game.getCategories() != null ? getGameCategory(game.getCategories()) : "" ) +
                    "\n\tMinimum age: "
                    + ( game.getMinimumAge() != 0 ? game.getMinimumAge() : "N/A" ) +
                    "\n\tNumber of players: "
                    + ( game.getNumberOfPlayers().getMinimum() != null ? game.getNumberOfPlayers().getMinimum().toString() + "min" : "N/A" )
                    + "-" + ( game.getNumberOfPlayers().getMaximum() != null ? game.getNumberOfPlayers().getMaximum().toString()  + "min" : "N/A" ) +
                    "\n\tPlay time: "
                    + ( game.getPlayTime().getMinimum() != null ? game.getPlayTime().getMinimum() : "N/A" )
                    + "-" + ( game.getPlayTime().getMaximum() != null ? game.getPlayTime().getClass().toString() : "N/A" );
        }
        System.out.println(result);
    }

    public void displayGameSelected() {
        System.out.println("Game has been added to your games collection.\n" );
    }

    public void displayGroupSelected() {
        System.out.println("Join request has been created.\n");
    }

    public MenuItem startMenu(com.example.entity.Player player) {
        MenuItem menuItem = null;
        if (player.getRoles().contains(Role.GROUP_ADMIN) && player.getRoles().contains(Role.PLAYER)) {
            menuItem = getMenuItem(x -> consoleWriteGroupAdminMenu());
        } else if (player.getRoles().contains(Role.PLAYER) && player.getRoles().stream().count() == 1) {
            menuItem = getMenuItem(x -> consoleWritePlayerMenu());
        } else if (player.getRoles().contains(Role.SUPERUSER)) {
            menuItem = getMenuItem(x -> consoleWriteSuperUserMenu());
        }
        return menuItem;
    }

    public int readingSelectedMenuNumber() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean invalid = true;
        int selectedNumber = 0;

        do {
            try {
                selectedNumber = Integer.parseInt(reader.readLine());
                invalid = false;
            } catch(NumberFormatException e) {
                System.out.print("Please enter a valid integer");
            } catch(IOException e) {
                e.printStackTrace();
            }
        } while(invalid);

        return selectedNumber;
    }

    public void displayJoinRequestState(JoinRequestState state) {
        if (state == JoinRequestState.ACCEPTED) {
            System.out.println("Player has joined your group.");
        } else if (state == JoinRequestState.REJECTED) {
            System.out.println("Player has been rejected to join your group.");
        }
    }

    public String readingSelectedJoinRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String result = "";
        try {
            result = reader.readLine();
        } catch (Exception e) { result += e.getMessage();}
        return result;
    }

    public void displayJoinRequests(List<String> joinRequestPlayerNames) {
        if (joinRequestPlayerNames != null) {
            String result = "";
            int id = 1;
            for (String playerName : joinRequestPlayerNames) {
                result += id + ". " + playerName + "\n";
                id++;
            }
            result += id + ". Back to the menu.\n\n Please answer:";
            System.out.println(result);
        }
    }

    private MenuItem getMenuItem(Function<Void, MenuItem> function) {
        MenuItem result;
        do {
            result = function.apply(null);
        } while (result == null);
        return result;
    }

    private String getGameCategory (List<Category> categories) {
        String result = "";
        for (Category category : categories) {
            result += category.toString() + " ";
        }
        return result;
    }

    private MenuItem consoleWriteGroupAdminMenu() {
        System.out.println("Here are the possible actions:  \n1. View list of all games \n2. Add my game " +
                "\n3. Create Join request\n4. Handle Join request\n5. Quit application\n\nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        MenuItem menuItem = null;
        try {
            menuItem = MenuItem.from(selectedNumber);
        } catch (IllegalArgumentException e) {}
        return menuItem;
    }
    private MenuItem consoleWritePlayerMenu() {
        System.out.println("Here are the possible actions:  \n1. View list of all games \n2. Add my game " +
                "\n5. Quit application\n\nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        MenuItem menuItem = null;
        try {
            menuItem = MenuItem.from(selectedNumber);
        } catch (IllegalArgumentException e) {}
        return menuItem;
    }

    private MenuItem consoleWriteSuperUserMenu() {
        System.out.println("Here are the possible actions:  \n5. Quit application\n6.Create new game \nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        MenuItem menuItem = null;
        try {
            menuItem = MenuItem.from(selectedNumber);
        } catch (IllegalArgumentException e) {}
        return menuItem;
    }

    private String getPlayerData(com.example.entity.Player player, String groupName) {
        String result = "";
        result = "\nHi " + (player.getName() != null ? player.getName() : "N/A") + "!\n" +
                "\n Your games :\n" + (getGameNames(player.getGames()) != null ? getGameNames(player.getGames()) : "N/A") +
                "\n Group membership: " + (groupName != null ? "\n- " + groupName : "\nN/A");
        return  result;
    }

    private String getGroupAdminData(com.example.entity.Player player, com.example.entity.Group playerGroup, List<String> joinRequestPlayerNames) {
        String result = "";
        result = "\nHi " + (player.getName() != null ? player.getName() : "\nN/A") + "!\n" +
                "\nGroup membership: " + (playerGroup != null ? "\n- " + playerGroup.getName() : "\nN/A" ) +
                "\n\nGroup join requests: \n" + (joinRequestPlayerNames != null ? getJoinRequests(joinRequestPlayerNames) : "N/A" ) +
                "\n Your games :\n" +  ( getGameNames(player.getGames()) != null ? getGameNames(player.getGames()) : "N/A" );
        return  result;
    }

    private String getGameNames(List<com.example.entity.Game> games) {
        String result = "";
        for (com.example.entity.Game game : games) {
            result += "- " + game.getName() + "\n";
        }
        return  result;
    }

    private String getJoinRequests(List<String> joinRequestPlayerNames) {
        String result = "";
        for (String playerName : joinRequestPlayerNames) {
            result += "- " + playerName + "\n";
        }
        return  result;
    }

}
