package com.fundottz.reactive.service.simulation;

import com.fundottz.reactive.model.domain.Position;
import com.fundottz.reactive.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimulationInitService {

    private final PositionRepository positions;

    @PostConstruct
    public void setupPositions() {
        Position position = Position.builder()
                .account("50027")
                .security("GAZP")
                .amount(BigDecimal.valueOf(100000))
                .build();

        Position position2 = Position.builder()
                .account("50027")
                .security("AFLT")
                .amount(BigDecimal.valueOf(10000))
                .build();

        List<Position> positions = this.positions.saveAll(asList(position, position2));//fbbc5d50-3cd2-37e2-9319-dc19c7740c6f
        log.info("Saved positions {}", positions);
    }
}
