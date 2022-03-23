package com.example;

import com.example.domain.Category;
import com.example.domain.JoinRequestState;
import com.example.domain.MenuItem;
import com.example.domain.Role;
import com.example.dto.*;
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

    public void displayLogin(UserDTO user) {
        System.out.println("\nLogin successful.Your role(s): \n" );
        user.getRoles().forEach(r -> {
            System.out.println(r.toString());
        });
    }

     public void displayLoginFailure() {
         System.out.println("\nLogin failure, bye!");
     }

    public void displayPlayer(PlayerDTO player) {
        String data = "";

        if (player.getRoles().contains(Role.GROUP_ADMIN)) {
            data = getGroupAdminData(player);
        } else {
            data = getPlayerData(player);
        }
        System.out.println(data);
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

    public GameFormDTO displayNewGameForm () {
        GameFormDTO gameFormDTO = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the required fields to the new game:\n");
        try {
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
            gameFormDTO = new GameFormDTO(id,name,description,minimumAge,categories,min,max,from,to);
        } catch (Exception e) {}
        System.out.println("\nId:");

        return gameFormDTO;
    }

    public void displaySuccessGameAdding() {
        System.out.println("New game added successfully!\n");
    }

    public void displayOptionalGames(List<GameDTO> optionalGames) {
        if (optionalGames.size() > 0) {
            String result = "";
            int id = 1;
            for(GameDTO game : optionalGames) {
                result += id + ". " + ( game.getName() != null ? game.getName() + "\n": "" );
                id++;
            }
            result += id + ". Back to the menu\n\nPlease choose game: ";
            System.out.println(result);
        }
    }

    public void displayOptionalGroups(List<GroupDTO> optionalGroups) {
        String result = "";
        int id = 1;
        for (GroupDTO group : optionalGroups) {
            result += id + ". " + group.getName() + "\n";
            id++;
        }
        result += id + ". Back to the menu\nPlease choose group:";
        System.out.println(result);
    }

    public void displayGameList (List<GameDTO> games) {
        String result = "";
        for (GameDTO game : games) {
            result += "\n- id: " + ( game.getId() != null ? game.getId().toString() : "N/A")  +
                    "\n\tName: "
                    + ( game.getName() != null ? game.getName() : "N/A") +
                    "\n\tDescription: "
                    + ( game.getDescription() != null ? game.getDescription() : "N/A" ) +
                    "\n\tCategories: "
                    + ( game.getCategories() != null ? getGameCategory(game.getCategories()) : "" ) +
                    "\n\tMinimum age: "
                    + ( game.getMinimumAge()  != null ? game.getMinimumAge().toString() : "N/A" ) +
                    "\n\tNumber of players: "
                    + ( game.getNumberOfPlayers().getMin() != null ? game.getNumberOfPlayers().getMin().toString() + "min" : "N/A" )
                    + "-" + ( game.getNumberOfPlayers().getMax() != null ? game.getNumberOfPlayers().getMax().toString()  + "min" : "N/A" ) +
                    "\n\tPlay time: "
                    + ( game.getPlayTime().getFrom() != null ? game.getPlayTime().getFrom() : "N/A" )
                    + "-" + ( game.getPlayTime().getTo() != null ? game.getPlayTime().getTo().toString() : "N/A" );
        }
        System.out.println(result);
    }

    public void displayGameSelected() {
        System.out.println("Game has been added to your games collection.\n" );
    }

    public void displayGroupSelected() {
        System.out.println("Join request has been created.\n");
    }

    public MenuItem startMenu(PlayerDTO player) {
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

    public void displayJoinRequests (List<UserDTO> joinRequests) {
        if (joinRequests != null) {
            String result = "";
            int id = 1;
            for (UserDTO userDTO : joinRequests) {
                result += id + ". " + userDTO.getName() + "\n";
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
                "5. Quit application\n\nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        MenuItem menuItem = null;
        try {
            menuItem = MenuItem.from(selectedNumber);
        } catch (IllegalArgumentException e) {}
        return menuItem;
    }

    private MenuItem consoleWriteSuperUserMenu() {
        System.out.println("Here are the possible actions:  \n6.Create new game \nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        MenuItem menuItem = null;
        try {
            menuItem = MenuItem.from(selectedNumber);
        } catch (IllegalArgumentException e) {}
        return menuItem;
    }

    private String getPlayerData(PlayerDTO playerDTO) {
        String result = "";
        result = "\nHi " + ( playerDTO.getName() != null ? playerDTO.getName() : "N/A" ) + "!\n" +
                "\n Your games :\n" + ( getGameNames(playerDTO.getGameNames()) != null ? getGameNames(playerDTO.getGameNames()) : "N/A" ) +
                "\n Group membership: " + ( playerDTO.getGroupName() != null ? "\n- " + playerDTO.getGroupName() : "\nN/A" );
        return  result;
    }

    private String getGroupAdminData(PlayerDTO playerDTO) {
        String result = "";
        result = "\nHi " + ( playerDTO.getName() != null ? playerDTO.getName() : "\nN/A" ) + "!\n" +
                "\nYour group: " + ( playerDTO.getGroupName() != null ? "\n- " + playerDTO.getGroupName() : "\nN/A" ) +
                "\n\nGroup join requests: \n" + ( getJoinRequests(playerDTO.getJoinRequestedGroupName()) != null ? getJoinRequests(playerDTO.getJoinRequestedGroupName()) : "N/A" ) +
                "\n Your games :\n" +  ( getGameNames(playerDTO.getGameNames()) != null ? getGameNames(playerDTO.getGameNames()) : "N/A" )+
                "\n Group membership: " + ( playerDTO.getGroupName() != null ? "\n- " + playerDTO.getGroupName() : "\nN/A" );
        return  result;
    }

    private String getGameNames(List<String> gameNames) {
        String result = "";
        for (String name : gameNames) {
            result += "- " + name + "\n";
        }
        return  result;
    }

    private String getJoinRequests(List<String> joinRequests) {
        String result = "";
        for (String name : joinRequests) {
            result += "- " + name + "\n";
        }
        return  result;
    }

}
