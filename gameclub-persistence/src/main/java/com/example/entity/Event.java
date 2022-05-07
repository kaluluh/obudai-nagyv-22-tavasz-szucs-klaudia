package com.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Embeddable
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    private Long eventId;
    private LocalDateTime date;
    private String place;
    @OneToMany
    @ElementCollection
    private List<Player> participants;
}
