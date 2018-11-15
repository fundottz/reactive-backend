package com.fundottz.reactive.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathService {

    private static final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_UP);

    public static BigDecimal averagePrice(BigDecimal beforeAmount, BigDecimal beforePrice,
                                          BigDecimal actualAmount, BigDecimal actualPrice) {

        return (beforeAmount.signum() == actualAmount.signum() || beforePrice.signum() == 0) ?
                beforeAmount.multiply(beforePrice)
                        .add(actualAmount.multiply(actualPrice))
                        .divide(beforeAmount.add(actualAmount), MATH_CONTEXT)
                : beforePrice;
    }

    private MathService() {
        throw new IllegalStateException("Utility class");
    }
}
