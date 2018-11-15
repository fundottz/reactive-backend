package com.fundottz.reactive.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SimulationSettingDto {
    @JsonProperty
    private Boolean quotesEnabled;

    @JsonProperty
    private Boolean tradesEnabled;
}

