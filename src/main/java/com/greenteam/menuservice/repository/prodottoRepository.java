package com.greenteam.menuservice.repository;

import com.greenteam.menuservice.entity.Prodotto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface prodottoRepository extends MongoRepository<Prodotto, String> {
}
