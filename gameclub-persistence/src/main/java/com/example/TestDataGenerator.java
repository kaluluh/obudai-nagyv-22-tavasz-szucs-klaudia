package com.example;

import com.example.domain.Category;
import com.example.domain.JoinRequestState;
import com.example.domain.Role;
import com.example.entity.*;
import com.example.repository.GameRepository;
import com.example.repository.GroupRepository;
import com.example.repository.JoinRequestRepository;
import com.example.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Component
@Transactional
public class TestDataGenerator {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private JoinRequestRepository joinRequestRepository;

    @PostConstruct
    public void createData() {
        createGameTestData();
        createPlayerTestData();
        createGroupTestData();
        createJoinRequest();
    }

    public void createGameTestData () {
        Game catanTelepesei = createGame("Catan Telepesei", "A Catan telepesei a legtöbb társasjáték\n" +
                        "rajongónak az első lépés ami túlmutat a gyermekkori klasszikus dobok és lépek játékokon. " +
                        "A játék célja Catan szigetén megszerezni az uralmat...",10,
                Arrays.asList(Category.STRATEGY),new Limits(60,120),new Limits(3,4));
        gameRepository.save(catanTelepesei);

        Game pandemic = createGame("Pandemic", "Megvan benned a képesség és a bátorság ahhoz," +
                        "hogy megmentsd az emberiséget ? Az izgalmas stratégiai játékban egy járványelhárító csapat " +
                        "szakképzett tagjaként feladatod, hogy felfedezd a halálos járvány ellenszérumát, még mielőtt " +
                        "az világszerte elterjedne...",8,
                Arrays.asList(Category.STRATEGY),new Limits(45,50),new Limits(2,4));
        gameRepository.save(pandemic);
    }

    public void createPlayerTestData () {
        Player superUser = createPlayer("nagys", "ns-secret","Nagy Sándor",
                "nagy.sandor@gmail.com",Arrays.asList(Role.SUPERUSER),Arrays.asList());
        playerRepository.save(superUser);

        Game game = gameRepository.findById(1L).get();
        Player groupAdmin = createPlayer("kaluluh", "asd-123","Szucs Klaudia",
                "kaluluh@gmail.com",Arrays.asList(Role.GROUP_ADMIN,Role.PLAYER),Arrays.asList(game));
        playerRepository.save(groupAdmin);

        game = gameRepository.findById(2L).get();
        Player player = createPlayer("macko", "asd-123","Nagy Norbert",
                "macko@gmail.com",Arrays.asList(Role.PLAYER),Arrays.asList(game));
        playerRepository.save(player);

    }

    public void createGroupTestData() {
        List<Long> playersId = Arrays.asList(5L,3L);
        Player admin = playerRepository.findById(4L).orElse(null);
        Iterable<Player> players = playerRepository.findAllById(playersId);
        Group group = createGroup("Óbudai Informatika Játék Csoport", Arrays.asList(), (List<Player>) players,admin);
        groupRepository.save(group);
    }

    public void createJoinRequest() {
        JoinRequestId joinRequestId = new JoinRequestId(5L, 6L);
        JoinRequest joinRequest = createJoinRequest(joinRequestId,JoinRequestState.REQUESTED);
        joinRequestRepository.save(joinRequest);
    }

    private Game createGame(String gameName,
                            String description,
                            Integer minimumAge,
                            List<Category> categories,
                            Limits playTime,
                            Limits numOfPlayers
    ) {
        Game game = new Game();
        game.setName(gameName);
        game.setDescription(description);
        game.setMinimumAge(minimumAge);
        game.setCategories(categories);
        game.setPlayTime(playTime);
        game.setNumberOfPlayers(numOfPlayers);
        return game;
    }

    private Player createPlayer(String loginName,
                                String password,
                                String name,
                                String email,
                                List<Role> roles,
                                List<Game> games
    ) {
        Player player = new Player();
        player.setLoginName(loginName);
        player.setPassword(password);
        player.setName(name);
        player.setEmail(email);
        player.setRoles(roles);
        player.setGames(games);
        return player;
    }

    private Group createGroup(String name,
                              List<Event> events,
                              List<Player> players,
                              Player admin) {
        Group group = new Group();
        group.setName(name);
        group.setEvents(events);
        group.setMembers(players);
        group.setAdmin(admin);
        return group;
    }
    private JoinRequest createJoinRequest(JoinRequestId joinRequestId,
                                          JoinRequestState state) {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setJoinRequestId(joinRequestId);
        joinRequest.setJoinRequestState(state);
        return joinRequest;
    }
}
