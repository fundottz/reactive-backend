package com.fundottz.reactive.model.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@Document
public class Quote {
    @Id
    private String security;
    private BigDecimal last;

    @Override
    public String toString() {
        return "Quote{" +
                "security='" + security + '\'' +
                ", last=" + last.setScale(5, RoundingMode.HALF_UP) +
                '}';
    }
}
