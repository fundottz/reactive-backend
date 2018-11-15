package com.fundottz.reactive.service.simulation;

import com.fundottz.reactive.model.domain.Position;
import com.fundottz.reactive.model.domain.Quote;
import com.fundottz.reactive.model.domain.Trade;
import com.fundottz.reactive.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final ApplicationEventPublisher eventPublisher;
    private final PositionRepository positions;
    private final SimulationSetting simulation;

    private static final String ACCOUNT_ID = "50027";
    private final List<String> tickers = Arrays.asList("GAZP", "SBER", "LKOH", "AFLT");

    @Scheduled(fixedRate = 5000)
    public void produceQuote() {
        if (simulation.isQuotesEnabled()) {

            Quote quote = new Quote();
            quote.setSecurity(randomTicker());
            quote.setLast(randomValue(50f, 200f));

            log.info("new {}", quote);
            this.eventPublisher.publishEvent(quote);
        }
    }

    @Scheduled(fixedRate = 4000)
    public void produceTrade() {
        if (simulation.isTradesEnabled()) {

            Trade trade = new Trade();
            trade.setSecurity(randomTicker());
            trade.setPrice(randomValue(100f, 300f));
            trade.setAccountId(ACCOUNT_ID);
            trade.setDate(LocalDate.now());
            trade.setAmount(randomValue(-10, 10));

            log.info("new {}", trade);
            this.eventPublisher.publishEvent(trade);
        }
    }

    @PostConstruct
    public void setupPositions() {
        Position position = Position.builder()
                .account(ACCOUNT_ID)
                .security("GAZP")
                .amount(BigDecimal.valueOf(100))
                .build();

        Position position2 = Position.builder()
                .account(ACCOUNT_ID)
                .security("AFLT")
                .amount(BigDecimal.valueOf(50))
                .build();

        List<Position> positions = this.positions.saveAll(asList(position, position2));
        log.info("Saved {}", positions);
    }

    private BigDecimal randomValue(float leftLimit, float rightLimit) {
        float generatedFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
        return BigDecimal.valueOf(generatedFloat);
    }

    private BigDecimal randomValue(int leftLimit, int rightLimit) {
        float generatedFloat = leftLimit + new Random().nextInt() * (rightLimit - leftLimit);
        return BigDecimal.valueOf(generatedFloat);
    }

    private String randomTicker() {
        Random random = new Random();
        return tickers.get(random.nextInt(tickers.size()));
    }
}
