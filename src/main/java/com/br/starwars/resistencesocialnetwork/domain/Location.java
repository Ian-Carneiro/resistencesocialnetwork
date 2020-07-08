package com.br.starwars.resistencesocialnetwork.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    private String name;
    private Float longitude;
    private Float latitude;
}
