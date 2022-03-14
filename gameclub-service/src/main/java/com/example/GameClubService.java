package com.example;

import com.example.domain.JoinRequest;
import com.example.domain.JoinRequestState;
import com.example.domain.Role;
import com.example.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
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

        if (userDataDTO.getRoles().contains(Role.PLAYER)) {
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
                    userDataDTO.addGameName(g.getName());
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
}
