package com.example.entity;

import com.example.domain.Game;
import com.example.dto.TransformableToDomainObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Embeddable
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GenericGenerator(name = "OverridableIdentity", strategy = "com.example.OverridableIdentityGenerator")
    @GeneratedValue(generator = "OverridableIdentity")
    @Column(unique = true, nullable = false)
    private Long id;
    private LocalDateTime date;
    private String place;
    private String description;
    @ManyToMany
    private List<Player> participants;

    public boolean participatePlayer(Long playerId) {
        Player player = participants.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElse(null);
        return player != null;
    }


}
