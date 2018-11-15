package com.fundottz.reactive.service;

import com.fundottz.reactive.model.domain.Portfolio;
import com.fundottz.reactive.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolios;

    public Optional<Portfolio> getPortfolio(String account) {
        return portfolios.findByAccount(account);
    }

    public Portfolio save(Portfolio portfolio) {
        return portfolios.save(portfolio);
    }
}
