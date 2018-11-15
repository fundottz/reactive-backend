package com.fundottz.reactive.repository;

import com.fundottz.reactive.model.domain.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends MongoRepository<Position, String> {

    List<Position> findAllBySecurity(String security);

    Optional<Position> findBySecurityAndAccount(String security, String account);
}
