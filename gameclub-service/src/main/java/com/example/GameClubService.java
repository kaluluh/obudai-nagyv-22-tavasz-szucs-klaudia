package com.example;

import com.example.domain.Category;
import com.example.domain.JoinRequestState;
import com.example.domain.Limits;
import com.example.domain.Role;
import com.example.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameClubService {

    private final DataStore dataStore;

    @Getter
    public List<PlayerDTO> players;

    @Getter
    private List<GameDTO> games;

    @Getter
    private List<GroupDTO> groups;

    @Getter
    private List<JoinRequestDTO> joinRequests;

    @Getter
    @Setter
    private CredentialsDTO credentialsDTO;

    @Getter
    @Setter
    private UserDataDTO userDataDTO;


    @PostConstruct
    public void loadData() {
        players = dataStore.readPlayers();
        games = dataStore.readGames();
        groups = dataStore.readGroups();
        joinRequests = dataStore.readJoinRequests();
    }

    public void initialize() {
        System.out.println("Welcome to the Board Game Club Application\n\nPlease log in.\n");
        readCredentials();
        authenticate(this.credentialsDTO);
        findUserData();
        consoleWriteUserData();
        startMenu(this.userDataDTO);
    }

    private void readCredentials() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Login name: ");
            String name = reader.readLine();
            System.out.println("\nPassword: ");
            String password = reader.readLine();
            if (name != null && password != null){
                credentialsDTO = new CredentialsDTO(name,password);
            }

        } catch (Exception e) {
            System.out.println("Invalid input format! Please try again. Error message: " + e.getMessage());
        }
    }

    private void authenticate(CredentialsDTO credentialsDTO) {
        PlayerDTO playerDTO = players.stream().filter(p -> p.getLoginName().equals(credentialsDTO.userName) && p.getPassword().equals(credentialsDTO.password)).findFirst().orElse(null);
        if (playerDTO != null) {
            credentialsDTO.setRoles(playerDTO.getRoles());
            System.out.println("\nLogin successful.Your role: " );
            credentialsDTO.getRoles().forEach(c -> {
                System.out.println(c.toString());
            });

        } else {
            System.out.println("\nLogin failure, bye!");
        }
    }

    private void findUserData() {
        PlayerDTO playerDTO = players.stream().filter(p -> p.getLoginName().equals(credentialsDTO.userName) && p.getPassword().equals(credentialsDTO.password)).findFirst().orElse(null);
        this.userDataDTO = new UserDataDTO();
        userDataDTO.setRoles(playerDTO.getRoles());

        if (userDataDTO.getRoles().contains(Role.PLAYER) || userDataDTO.getRoles().contains(Role.SUPERUSER)) {
            findPLayer(playerDTO);
        }

        if (userDataDTO.getRoles().contains(Role.GROUP_ADMIN)) {
            findJoinRequests();
        }
    }

    private void findPLayer(PlayerDTO playerDTO) {
        userDataDTO.setGameIds(playerDTO.getGames());
        userDataDTO.setId(playerDTO.getId());
        userDataDTO.setName(playerDTO.getName());

        games.forEach( g -> {
            userDataDTO.getGameIds().forEach( u -> {
                if (g.getId() == u) {
                    userDataDTO.getGames().add(g);
                }
            });
        });

        groups.forEach( g -> {
            g.getMembers().forEach( m -> {
                if (m == userDataDTO.getId()){
                    userDataDTO.setGroupName(g.getName());
                    userDataDTO.setGroupId(g.getId());
                }
            });
        });
    }

    private void findJoinRequests() {
         List<JoinRequestDTO> joinRequestList = joinRequests.stream().filter(j ->
                 j.getGroupId() ==  userDataDTO.getGroupId() && j.getState() == JoinRequestState.REQUESTED).collect(Collectors.toList());
         if (joinRequestList != null){
             joinRequestList.forEach( j -> {
                 players.forEach(p -> {
                     if (j.getUserId() == p.getId()) {
                         userDataDTO.addPlayerName(p.getName());
                         userDataDTO.addPlayerJoinRequest(p);
                     }
                 });
             });
         }
     }

     private void consoleWriteUserData() {
       String data = "";

       if (this.userDataDTO.getRoles().contains(Role.GROUP_ADMIN)) {
           data = getGroupAdminData(this.userDataDTO);
       } else {
           data = getPlayerData(this.userDataDTO);
       }
       System.out.println(data);
    }

    private String getPlayerData(UserDataDTO userDataDTO) {
        String result = "";
        result = "\nHi " + ( userDataDTO.getName() != null ? userDataDTO.getName() : "N/A" ) + "!\n" +
                "\n Your games :\n" + ( getGameNames(userDataDTO.getGameNames()) != null ? getGameNames(userDataDTO.getGameNames()) : "N/A" ) +
                "\n Group membership: " + ( userDataDTO.getGroupName() != null ? "\n- " + userDataDTO.getGroupName() : "\nN/A" );
        return  result;
    }

    private String getGroupAdminData(UserDataDTO userDataDTO) {
        String result = "";
        result = "\nHi " + ( userDataDTO.getName() != null ? userDataDTO.getName() : "\nN/A" ) + "!\n" +
                "\nYour group: " + ( userDataDTO.getGroupName() != null ? "\n- " + userDataDTO.getGroupName() : "\nN/A" ) +
                "\n\nGroup join requests: \n" + ( getJoinRequests(userDataDTO.getJoinRequestedGroupName()) != null ? getJoinRequests(userDataDTO.getJoinRequestedGroupName()) : "N/A" ) +
                "\n Your games :\n" +  ( getGameNames(userDataDTO.getGameNames()) != null ? getGameNames(userDataDTO.getGameNames()) : "N/A" )+
                "\n Group membership: " + ( userDataDTO.getGroupName() != null ? "\n- " + userDataDTO.getGroupName() : "\nN/A" );
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

    private void startMenu(UserDataDTO userDataDTO) {

        if (userDataDTO.getRoles().contains(Role.GROUP_ADMIN) && userDataDTO.getRoles().contains(Role.PLAYER)) {
            consoleWriteGroupAdminMenu();
        } else if (userDataDTO.getRoles().contains(Role.PLAYER) && userDataDTO.getRoles().stream().count() == 1) {
            consoleWritePlayerMenu();
        } else if (userDataDTO.getRoles().contains(Role.SUPERUSER)) {
            consoleWriteSuperUserMenu();
        }
    }

    private void consoleWriteGroupAdminMenu() {
        System.out.println("Here are the possible actions:  \n1. View list of all games \n2. Add my game " +
                "\n3. Create Join request\n4. Handle Join request\n5. Quit application\n\nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        selectedMenuPointStart(selectedNumber);
    }

    private void consoleWritePlayerMenu() {
        System.out.println("Here are the possible actions:  \n1. View list of all games \n2. Add my game " +
                "5. Quit application\n\nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        selectedMenuPointStart(selectedNumber);
    }

    private void consoleWriteSuperUserMenu() {
        System.out.println("Here are the possible actions:  \n6.Create new game \nPlease choose an item:");
        int selectedNumber = readingSelectedMenuNumber();
        selectedMenuPointStart(selectedNumber);
    }

     private int readingSelectedMenuNumber() {

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

    private void selectedMenuPointStart(int selected) {
        switch (selected) {
            case 1:
                System.out.println("\n\nViewing list of all games:\n");
                viewingGameList();
                break;
            case 2:
                System.out.println("Please choose a game from the following list add:\n");
                choosingGame(this.userDataDTO);
                break;
            case 3:
                System.out.println("Please choose the group you would like to join:\n");
                createJoinRequest();
                break;
            case 4:
                System.out.println("List of players who would like to join your group. Please select player number and (A)ccept or (R)eject:\n");
                processingMembership();
                break;
            case 5:
                System.out.println("Quiting from the application");
                dataStore.writeResultToJSON(this.players,this.games,this.groups,this.joinRequests);
                break;
            case 6:
                System.out.println("Creating a new game\n");
                addNewGame();
                break;
        }
    }

    private void viewingGameList () {
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
        backToTheMenu();
    }

    private String getGameCategory (List<Category> categories) {
        String result = "";
        for (Category category : categories) {
            result += category.toString() + " ";
        }
        return result;
    }

    private void choosingGame(UserDataDTO userDataDTO) {
        List<GameDTO> optionalGames = new ArrayList<>(games);
        optionalGames.removeAll(userDataDTO.getGames());

         String result = "";
         int id = 1;
         for(GameDTO game : optionalGames) {
             result += id + ". " + ( game.getName() != null ? game.getName() + "\n": "" );
             id++;
         }
         result += id + ". Back to the menu\n\nPlease choose game: ";
         System.out.println(result);

         int selectedNumber = readingSelectedMenuNumber();

         if (selectedNumber > optionalGames.size()) {
             startMenu(userDataDTO);
         } else {
             GameDTO selectedGame = optionalGames.get(selectedNumber - 1);
             userDataDTO.getGameIds().add(selectedGame.getId());
             userDataDTO.getGameNames().add(selectedGame.getName());
             System.out.println("Game has been added to your games collection. " );
             backToTheMenu();
         }
    }

    private void backToTheMenu() {
        System.out.println("\nPlease press a button to navigate back to the menu.\n");
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.nextLine();
        if(input != null) {
            startMenu(userDataDTO);
        }
    }

    private void createJoinRequest() {
        List<GroupDTO> optionalGroups = new ArrayList<>(groups);
        GroupDTO existedGroup = optionalGroups.stream().filter(g -> g.getId() == userDataDTO.getGroupId()).findFirst().orElse(null);
        String result = "";
        if (existedGroup != null) {
            optionalGroups.remove(existedGroup);
        }
        int id = 1;
        for (GroupDTO group : optionalGroups) {
            result += id + ". " + group.getName() + "\n";
            id++;
        }
        result += id + ". Back to the menu\nPlease choose group:";
        System.out.println(result);

        int selectedNumber = readingSelectedMenuNumber();

        if (selectedNumber > optionalGroups.size()) {
            startMenu(this.userDataDTO);
        } else {
            GroupDTO selectedGroup = optionalGroups.get(selectedNumber - 1);
            joinRequests.add(new JoinRequestDTO(userDataDTO.getId(),selectedGroup.getId(),JoinRequestState.REQUESTED));
            System.out.println("Join request has been created.");
            backToTheMenu();
        }
    }

    private void processingMembership() {
        String result = "";
        int id = 1;
        int index = 0;
        for (PlayerDTO playerDTO : userDataDTO.getJoinRequests()) {
            result += id + ". " + playerDTO.getName() + "\n";
            id++;
        }
        result += id + ". Back to the menu.\n\n Please answer:";
        System.out.println(result);

        String selectedJoinRequest = readingSelectedJoinRequest();
        if (selectedJoinRequest.contains("A") || selectedJoinRequest.contains("R")) {
            index =  Integer.parseInt(selectedJoinRequest.substring(0,selectedJoinRequest.length() - 1));
        } else {
            index =  Integer.parseInt(selectedJoinRequest);
        }
        if ( index > userDataDTO.getJoinRequests().size()) {
            startMenu(userDataDTO);
        } else {
            if(selectedJoinRequest.contains("A")) {
                PlayerDTO selectedPlayer  = userDataDTO.getJoinRequests().get(index - 1);
                joinRequests.add(new JoinRequestDTO(selectedPlayer.getId(),userDataDTO.getGroupId(),JoinRequestState.ACCEPTED));
                System.out.println("Player has joined your group.");
                backToTheMenu();
            } else if (selectedJoinRequest.contains("R")) {
                PlayerDTO selectedPlayer  = userDataDTO.getJoinRequests().get(index - 1);
                joinRequests.add(new JoinRequestDTO(selectedPlayer.getId(),userDataDTO.getGroupId(),JoinRequestState.REJECTED));
                System.out.println("Player has been rejected to join your group.");
                backToTheMenu();
            }
        }

    }

    private String readingSelectedJoinRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String result = "";
        try {
            result = reader.readLine();
        } catch (Exception e) { result += e.getMessage();}
        return result;
    }

    private void addNewGame() {
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

            List<Category> categoriesList = new ArrayList<>();
            games.add(new GameDTO(id,name,description,minimumAge,categoriesList,
                    new LimitsDTO(min,max), new Limits(from,to)));

            System.out.println("New game added successfully!\n");
            backToTheMenu();

        } catch (Exception e) { }
    }

}
