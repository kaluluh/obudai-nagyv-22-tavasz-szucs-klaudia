package com.example.entity;

import com.example.domain.Category;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "games")
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Lob
    private String description;
    private Integer minimumAge;
    @ElementCollection
    private List<Category> categories;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "minimum", column = @Column(name = "fromPlaytime")),
            @AttributeOverride( name = "maximum", column = @Column(name = "toPlaytime"))
    })
    private Limits playTime;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "minimum", column = @Column(name = "minNumOfPlayers")),
            @AttributeOverride( name = "maximum", column = @Column(name = "maxNumOfPlayers"))
    })
    private Limits numberOfPlayers;

    @Override
    public boolean equals(Object o) {
       return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.id.intValue();
    }
}
