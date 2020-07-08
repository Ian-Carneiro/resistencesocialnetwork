package com.br.starwars.resistencesocialnetwork.domain.dataTransfer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationDTO {
    @NonNull
    private String name;
    @NonNull
    private Float longitude;
    @NonNull
    private Float latitude;
}
