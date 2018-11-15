package com.fundottz.reactive.controller;

import com.fundottz.reactive.model.dto.SimulationSettingDto;
import com.fundottz.reactive.service.simulation.SimulationSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationSetting simulationSetting;

    @PutMapping
    public void changeSettings(@RequestBody SimulationSettingDto setting) {
        Boolean quotesEnabled = setting.getQuotesEnabled();
        if (quotesEnabled != null) simulationSetting.setQuotesEnabled(quotesEnabled);

        Boolean tradesEnabled = setting.getTradesEnabled();
        if (tradesEnabled != null)
            simulationSetting.setTradesEnabled(tradesEnabled);
    }
}
