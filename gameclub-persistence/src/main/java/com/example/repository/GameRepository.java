package com.example.repository;

import com.example.entity.Game;
import com.example.entity.Group;
import com.example.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {
    Game findTopByOrderByIdDesc();
}