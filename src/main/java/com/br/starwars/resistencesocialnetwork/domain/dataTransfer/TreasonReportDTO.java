package com.br.starwars.resistencesocialnetwork.domain.dataTransfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ApiModel(value = "NegotiationDTO")
public class TreasonReportDTO {
    @ApiModelProperty(value = "Id do acusador.")
    @NonNull
    String idAccuser;
    @ApiModelProperty(value = "Id do rebelde acusado.")
    @NonNull
    String idAccused;
}
