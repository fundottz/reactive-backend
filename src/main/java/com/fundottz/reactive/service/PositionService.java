package com.fundottz.reactive.service;

import com.fundottz.reactive.model.domain.Position;
import com.fundottz.reactive.model.domain.ProfitAndLossStatement;
import com.fundottz.reactive.model.domain.Quote;
import com.fundottz.reactive.model.domain.Trade;
import com.fundottz.reactive.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.fundottz.reactive.service.MathService.averagePrice;

@Component
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positions;

    public BigDecimal calculateVolume(Position position, Quote quote) {
        return volume(position.getAmount(), quote.getLast());
    }

    private BigDecimal volume(BigDecimal amount, BigDecimal price) {
        return amount.multiply(price);
    }

    public ProfitAndLossStatement calculateStatement(Position position, Quote quote) {
        ProfitAndLossStatement statement = Optional.ofNullable(position.getStatement())
                .map(s -> s.toBuilder().build())
                .orElse(new ProfitAndLossStatement());

        BigDecimal price = statement.getAveragePrice();
        BigDecimal amount = position.getAmount();
        BigDecimal pnl = price.multiply(amount)
                .subtract(price.multiply(quote.getLast()));

        BigDecimal averagePrice = averagePrice(position.getAmount(), price, position.getAmount(), quote.getLast());

        statement.setAveragePrice(averagePrice);
        statement.setUnrealizedPnL(pnl);
        return statement;
    }

    public ProfitAndLossStatement calculateStatement(Position position, Trade trade) {
        ProfitAndLossStatement statement = Optional.ofNullable(position.getStatement())
                .map(s -> s.toBuilder().build())
                .orElse(new ProfitAndLossStatement());

        BigDecimal price = statement.getAveragePrice();
        BigDecimal amount = position.getAmount();
        BigDecimal pnl = price.multiply(amount)
                .subtract(price.multiply(trade.getPrice()));

        BigDecimal newAveragePrice = averagePrice(position.getAmount(), price, position.getAmount(), price);

        statement.setUnrealizedPnL(pnl);
        statement.setAveragePrice(newAveragePrice);
        return statement;
    }

    public Position save(Position position) {
        return positions.save(position);
    }

    public List<Position> getPositionsForUpdate(Quote quote) {
        return positions.findAllBySecurity(quote.getSecurity());
    }

    public Optional<Position> getPositionForUpdate(Trade trade) {
        return positions.findBySecurityAndAccount(trade.getSecurity(), trade.getAccountId());
    }

    public Position incrementPosition(Trade trade, Position position) {
        BigDecimal newAmount = position.getAmount().add(trade.getAmount());
        position.setAmount(newAmount);
        ProfitAndLossStatement statement = calculateStatement(position, trade);
        BigDecimal newVolume = volume(newAmount, trade.getPrice());

        return position.toBuilder()
                .amount(newAmount)
                .statement(statement)
                .volume(newVolume)
                .build();
    }
}
