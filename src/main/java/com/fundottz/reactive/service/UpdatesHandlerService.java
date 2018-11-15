package com.fundottz.reactive.service;

import com.fundottz.reactive.model.domain.*;
import com.fundottz.reactive.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatesHandlerService {

    private final PositionService positionService;
    private final QuoteRepository quoteRepository;
    private final PortfolioService portfolioService;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleQuoteUpdate(Quote quote) {
        quoteRepository.save(quote);
        positionService.getPositionsForUpdate(quote)
                .stream()
                .map(position -> {
                    BigDecimal volume = positionService.calculateVolume(position, quote);
                    ProfitAndLossStatement statement = positionService.calculateStatement(position, quote);
                    Position updatedPosition = position.toBuilder()
                            .volume(volume)
                            .statement(statement)
                            .build();
                    return positionService.save(updatedPosition);
                })
                .forEach(this::producePositionUpdatedEvent);
    }

    @EventListener
    public void handleTradeUpdate(Trade trade) {
        Optional<Position> position = positionService.getPositionForUpdate(trade);

        if (position.isPresent()) {
            Position updatedPosition = positionService.incrementPosition(trade, position.get());
            Position savedPosition = positionService.save(updatedPosition);
            producePositionUpdatedEvent(savedPosition);
        } else {
            ProfitAndLossStatement statement = new ProfitAndLossStatement();
            statement.setAveragePrice(trade.getPrice());
            Position newPosition = mapToPosition(trade, statement);
            Position savedPosition = positionService.save(newPosition);
            producePositionUpdatedEvent(savedPosition);
        }
    }

    @EventListener
    public void handlePositionUpdate(Position position) {
        Portfolio portfolio = portfolioService.getPortfolio(position.getAccount())
                .orElseGet(() -> new Portfolio(position.getAccount()));
        portfolio.update(position);

        Portfolio savedPortfolio = portfolioService.save(portfolio);
        producePortfolioUpdatedEvent(savedPortfolio);
    }

    private void producePositionUpdatedEvent(Position position) {
        log.info("published {}", position);
        this.eventPublisher.publishEvent(position);
    }

    private void producePortfolioUpdatedEvent(Portfolio portfolio) {
        log.info("published {}", portfolio);
        this.eventPublisher.publishEvent(portfolio);
    }

    private Position mapToPosition(Trade trade, ProfitAndLossStatement statement) {
        return Position.builder()
                .account(trade.getAccountId())
                .security(trade.getSecurity())
                .amount(trade.getAmount())
                .statement(statement)
                .volume(trade.getVolume())
                .build();
    }
}
