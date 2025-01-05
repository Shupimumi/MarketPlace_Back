package com.home.marketplace.db.repositories;

import com.home.marketplace.db.entities.GoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<GoodEntity, Long> {

}
