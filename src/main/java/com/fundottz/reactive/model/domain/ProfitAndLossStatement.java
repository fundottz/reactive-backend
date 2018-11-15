package com.fundottz.reactive.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProfitAndLossStatement {

    public ProfitAndLossStatement() {
        this.averagePrice = ZERO;
        this.unrealizedPnL = ZERO;
    }

    private BigDecimal averagePrice;

    private BigDecimal unrealizedPnL;
}