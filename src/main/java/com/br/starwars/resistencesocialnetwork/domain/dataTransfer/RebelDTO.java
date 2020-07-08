package com.br.starwars.resistencesocialnetwork.domain.dataTransfer;

import com.br.starwars.resistencesocialnetwork.domain.Location;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RebelDTO {
    private String id;
    @NonNull
    private String name;
    @NonNull
    private Integer age;
    @NonNull
    private String genre;
    @NonNull
    private LocationDTO location;
    @NonNull
    private List<ItemIdDTO> items;
}
