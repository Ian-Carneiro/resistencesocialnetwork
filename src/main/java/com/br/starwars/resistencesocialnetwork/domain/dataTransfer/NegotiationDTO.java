package com.br.starwars.resistencesocialnetwork.domain.dataTransfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(value = "NegotiationDTO")
public class NegotiationDTO {
    @ApiModelProperty(value = "Id do rebelde que negociará seus itens.")
    @NonNull
    private String negotiatorId;
    @ApiModelProperty(value = "Itens que serão negociados.")
    @NonNull
    private List<ItemIdDTO> negotiatorItems;
    @ApiModelProperty(value = "Id do envolvido na negociação.")
    @NonNull
    private String involvedId;
    @ApiModelProperty(value = "Itens requeridos na negociação.")
    @NonNull
    private List<ItemIdDTO> involvedItemsTraded;
}