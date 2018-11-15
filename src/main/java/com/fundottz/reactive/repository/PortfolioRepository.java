package com.fundottz.reactive.repository;


import com.fundottz.reactive.model.domain.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
    Optional<Portfolio> findByAccount(String account);
}

