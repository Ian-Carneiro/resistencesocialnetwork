package com.br.starwars.resistencesocialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class Rebel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String name;
    private Integer age;
    private String genre;
    private Integer treasonReportNum;
    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "locationName"))
    @AttributeOverride(name = "latitude", column = @Column(name = "locationLatitude"))
    @AttributeOverride(name = "longitude", column = @Column(name = "locationLongitude"))
    private Location location;
    @OneToMany(mappedBy = "rebel", cascade = CascadeType.ALL)
    private List<RebelItem> rebelItemList;

    @OneToMany()
    private List<Rebel> accusers;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private Rebel accused;


    public Rebel(String name, Integer age, String genre, Location location, List<RebelItem> rebelItemList) {
        this.name = name;
        this.age = age;
        this.genre = genre;
        this.treasonReportNum = 0;
        this.accusers = new ArrayList<>();
        this.accused = null;
        this.location = location;
        for (RebelItem rebelItem : rebelItemList) {
            rebelItem.setRebel(this);
        }
        this.rebelItemList = rebelItemList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addRebelItem(RebelItem rebelItem) {
        rebelItem.setRebel(this);
        this.rebelItemList.add(rebelItem);
    }

    public void setAccuser(Rebel accusers) {
        this.accusers.add(accusers);
    }

    public void setAccused() {
        this.accused = this;
    }

    public Integer treasonReport() {
        if (treasonReportNum == null) this.treasonReportNum = 1;
        return ++treasonReportNum;
    }

    public boolean isTreason() {
        if (treasonReportNum == null) return false;
        return this.treasonReportNum > 2;
    }
}
