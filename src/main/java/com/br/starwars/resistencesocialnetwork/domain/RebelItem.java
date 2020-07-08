package com.br.starwars.resistencesocialnetwork.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
public class RebelItem implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne
    @JoinColumn
    private Rebel rebel;

    @ManyToOne
    @JoinColumn
    private Item item;
    private Integer numItems;

    public RebelItem(Item item, Integer numItems) {
        this.item = item;
        this.numItems = numItems;
    }

    public void addNumItems(int num) {
        this.numItems += num;
    }

    public void removeNumItems(int num) {
        this.numItems -= num;
    }
}
