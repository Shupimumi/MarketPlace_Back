package com.home.marketplace.db.repositories;

import com.home.marketplace.db.entities.Good;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodRepository extends JpaRepository<Good, Long> {

}
