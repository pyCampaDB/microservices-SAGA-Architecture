package com.mcsv.order.services.commands;

import com.core.apis.commands.CreateOrderCommand;
import com.mcsv.order.enums.ItemType;
import com.mcsv.order.enums.OrderStatus;
import com.mcsv.order.dtos.commands.OrderCreatedDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@Service
public class OrderCommandServiceImpl implements OrderCommandService{
    @Autowired
    private CommandGateway commandGateway;
    @Override
    public CompletableFuture<String> createOrder(OrderCreatedDTO orderCreatedDTO) {
        if (orderCreatedDTO.getItemType() == null || orderCreatedDTO.getCurrency() == null || orderCreatedDTO.getPrice() == null) {
            throw new IllegalArgumentException("ItemType, Currency, and Price cannot be null");
        }
        // Opcional: Validación de que el itemType es un valor válido
        try {
            ItemType.valueOf(orderCreatedDTO.getItemType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ItemType: " + orderCreatedDTO.getItemType());
        }
        return commandGateway.send(
                new CreateOrderCommand(
                        UUID.randomUUID().toString(),
                        String.valueOf(OrderStatus.CREATED),
                        orderCreatedDTO.getItemType(),
                        orderCreatedDTO.getCurrency(),
                        orderCreatedDTO.getPrice()
                )
        );
    }
}
