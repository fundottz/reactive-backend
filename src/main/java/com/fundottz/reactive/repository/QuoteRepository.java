package com.fundottz.reactive.repository;

import com.fundottz.reactive.model.domain.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteRepository extends MongoRepository<Quote, String> {
}
