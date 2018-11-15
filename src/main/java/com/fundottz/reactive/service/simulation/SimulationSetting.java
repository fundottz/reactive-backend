package com.fundottz.reactive.service.simulation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SimulationSetting {

    @JsonProperty
    private boolean quotesEnabled = true;

    @JsonProperty
    private boolean tradesEnabled;
}
