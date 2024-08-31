package com.mcsv.order.services.commands;

import com.mcsv.order.dtos.commands.OrderCreatedDTO;

import java.util.concurrent.CompletableFuture;

public interface OrderCommandService {
    public CompletableFuture<String> createOrder(OrderCreatedDTO orderCreatedDTO);
}
