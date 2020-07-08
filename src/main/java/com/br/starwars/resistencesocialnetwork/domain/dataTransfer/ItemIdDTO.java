package com.br.starwars.resistencesocialnetwork.domain.dataTransfer;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemIdDTO {
    @NonNull

    private String id;
    @NonNull
    private Integer numItems;
}
