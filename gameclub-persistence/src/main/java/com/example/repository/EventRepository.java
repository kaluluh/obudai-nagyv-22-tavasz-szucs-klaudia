package com.example.repository;

import com.example.entity.Event;
import com.example.entity.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event,Long> {
    Event findTopByOrderByIdDesc();
}
