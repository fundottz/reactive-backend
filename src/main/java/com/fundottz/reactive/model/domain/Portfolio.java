package com.fundottz.reactive.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
public class Portfolio {

    @Id
    private String account;
    private BigDecimal netAssetValue;
    private List<Position> positions;

    public Portfolio(String account) {
        this.account = account;
        this.positions = new ArrayList<>();
    }

    public void update(Position position) {
        List<String> ids = positions.stream()
                .map(Position::getId)
                .collect(toList());

        boolean existed = ids.contains(position.getId());
        if (existed) {
            int index = ids.indexOf(position.getId());
            positions.set(index, position);
        } else {
            positions.add(position);
        }

        this.netAssetValue = calcNetAssetValue();
    }

    private BigDecimal calcNetAssetValue() {
        return positions.stream()
                .map(Position::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
