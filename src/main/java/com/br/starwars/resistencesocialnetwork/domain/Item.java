package com.br.starwars.resistencesocialnetwork.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Item {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String name;
    private Integer points;
    @OneToMany(mappedBy = "item")
    private List<RebelItem> rebelItemList;

    public Item(String name, Integer points) {
        this.name = name;
        this.points = points;
    }
}
