package com.example.entity;

import com.example.domain.Category;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(name = "OverridableIdentity", strategy = "com.example.OverridableIdentityGenerator")
    @GeneratedValue(generator = "OverridableIdentity")
    @Column(unique = true, nullable = false)
    private Long id;
    private String name;
    @Lob
    private String description;
    private Integer minimumAge;
    @ElementCollection
    private List<Category> categories;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "minimum", column = @Column(name = "playtime_min")),
            @AttributeOverride( name = "maximum", column = @Column(name = "playtime_max"))
    })
    private Limits playTime;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "minimum", column = @Column(name = "playernum_min")),
            @AttributeOverride( name = "maximum", column = @Column(name = "playernum_max"))
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
