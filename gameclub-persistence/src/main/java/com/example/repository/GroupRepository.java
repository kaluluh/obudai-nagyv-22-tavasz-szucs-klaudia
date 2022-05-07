package com.example.repository;

import com.example.entity.Group;
import com.example.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends CrudRepository<Group,Long> {

    @Query(value = "SELECT g FROM Group g WHERE :player IN elements(g.members)")
    Group findByPlayer(@Param("player") Player player);

    @Query(value = "SELECT g FROM Group g WHERE g.admin = :player")
    Group findByAdmin(@Param("player") Player player);
}
