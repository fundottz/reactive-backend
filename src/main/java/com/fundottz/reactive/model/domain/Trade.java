package com.fundottz.reactive.model.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Trade {

    private String accountId;

    @NotNull
    private String security;

    @NotNull
    private BigDecimal price;

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal volume;
}
