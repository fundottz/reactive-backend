package com.fundottz.reactive.model.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

import static java.lang.String.join;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@Getter
@Setter
@ToString
@Document
@Builder(toBuilder = true)
public class Position {

    @Id
    private final String id;

    private String account;

    private String security;

    private BigDecimal amount;

    private BigDecimal volume;

    private ProfitAndLossStatement statement;

    public Position(String id, String account, String security, BigDecimal amount, BigDecimal volume, ProfitAndLossStatement statement) {
        this.id = firstNonNull(id, hash(join(account, security)));
        this.account = account;
        this.security = security;
        this.amount = amount;
        this.volume = volume;
        this.statement = statement;
    }

    private String hash(String id) {
        return UUID.nameUUIDFromBytes(id.getBytes()).toString();
    }
}
