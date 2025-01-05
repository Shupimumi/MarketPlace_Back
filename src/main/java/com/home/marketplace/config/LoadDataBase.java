package com.home.marketplace.config;

import com.home.marketplace.db.entities.OrderEntity;
import com.home.marketplace.db.repositories.GoodsRepository;
import com.home.marketplace.db.entities.GoodEntity;
import com.home.marketplace.db.repositories.OrderRepository;
import com.home.marketplace.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Slf4j
@Configuration
public class LoadDataBase {

    @Bean
    CommandLineRunner initDatabase(GoodsRepository goodsRepository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading " + goodsRepository.save(new GoodEntity("BMX", "Bicycle", BigDecimal.valueOf(23.33))));
            log.info("Preloading " + goodsRepository.save(new GoodEntity("Asus", "MotherBoard", BigDecimal.valueOf(33.33))));

            goodsRepository.findAll().forEach(good -> log.info("Preload" + good));

            orderRepository.save(new OrderEntity("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new OrderEntity("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}
