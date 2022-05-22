package com.example.repository;

import com.example.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PlayerRepository extends CrudRepository<Player,Long> {

    Player findByLoginNameAndPassword(String loginName, String password);

    @Query("SELECT p FROM Player p WHERE p.loginName = :username and p.password = :password")
    Player findPlayerByUserNameAndPasswordNamedParams(
            @Param("username") String username,
            @Param("password") String password);

    @Query("SELECT p FROM Player p WHERE p.loginName = :username")
    Player findByUserName(
            @Param("username") String username);
}
