package com.mcsv.order.aggregates;

import com.core.apis.commands.CreateOrderCommand;
import com.core.apis.commands.UpdateOrderStatusCommand;
import com.core.apis.events.OrderCreatedEvent;
import com.core.apis.events.UpdateOrderEvent;
import com.mcsv.order.enums.ItemType;
import com.mcsv.order.enums.OrderStatus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private ItemType itemType;
    private BigDecimal price;
    private String currency;
    private OrderStatus orderStatus;


    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand){
        AggregateLifecycle.apply(
                new OrderCreatedEvent(
                        createOrderCommand.getOrderStatus(),
                        createOrderCommand.getItemType(),
                        createOrderCommand.getOrderId(),
                        createOrderCommand.getCurrency(),
                        createOrderCommand.getPrice()
                )
        );
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent){
        this.orderId= orderCreatedEvent.getOrderId();
        this.orderStatus=OrderStatus.valueOf(orderCreatedEvent.getOrderStatus());
        this.itemType=ItemType.valueOf(orderCreatedEvent.getItemType());
        this.currency=orderCreatedEvent.getCurrency();
        this.price=orderCreatedEvent.getPrice();
    }

    @CommandHandler
    public void on (UpdateOrderStatusCommand updateOrderStatusCommand){
        AggregateLifecycle.apply( new UpdateOrderEvent(
                        updateOrderStatusCommand.getOrderId(),
                        updateOrderStatusCommand.getOrderStatus()
                )
        );
    }

    @EventSourcingHandler
    public void on(UpdateOrderEvent updateOrderEvent){
        this.orderId= updateOrderEvent.getOrderId();
        this.orderStatus=OrderStatus.valueOf(updateOrderEvent.getOrderStatus());
    }
}
