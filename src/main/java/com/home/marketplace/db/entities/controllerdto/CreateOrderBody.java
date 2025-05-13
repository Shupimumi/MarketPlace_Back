package com.home.marketplace.db.entities.controllerdto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateOrderBody {
    private List<Long> goodsId = new ArrayList<>();
    private String description;
}
