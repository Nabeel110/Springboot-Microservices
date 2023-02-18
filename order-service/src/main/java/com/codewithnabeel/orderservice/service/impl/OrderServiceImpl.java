package com.codewithnabeel.orderservice.service.impl;

import com.codewithnabeel.orderservice.dto.InventoryResponse;
import com.codewithnabeel.orderservice.dto.OrderItemsDto;
import com.codewithnabeel.orderservice.dto.OrderRequest;
import com.codewithnabeel.orderservice.event.OrderPlacedEvent;
import com.codewithnabeel.orderservice.model.Order;
import com.codewithnabeel.orderservice.model.OrderItems;
import com.codewithnabeel.orderservice.repository.OrderRepository;
import com.codewithnabeel.orderservice.service.OrderService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private WebClient.Builder webClientBuilder;
  @Autowired
  private Tracer tracer;
  @Autowired
  private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

  @Override
  public String placeOrder(OrderRequest orderRequest) {

    Order newOrder = new Order();
    newOrder.setOrderNumber(UUID.randomUUID().toString());

    List<OrderItems> orderItems = orderRequest.getOrderItems().stream().map(this::mapToDTo).toList();

    newOrder.setOrderItems(orderItems);

    List<String> skuCodes = newOrder.getOrderItems().stream().map(OrderItems::getSkuCode).toList();

    log.info("Calling inventory service");

    Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

    try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {

      inventoryServiceLookup.tag("call", "inventory-service");
      // Call Inventory service and place order if product is in stock
      InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
          .uri("http://INVENTORY-SERVICE/api/v1/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()).retrieve()
          .bodyToMono(InventoryResponse[].class).block();

      boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);

      if (Boolean.TRUE.equals(allProductsInStock)) {
        orderRepository.save(newOrder);
        kafkaTemplate.send("notificationTopic", "Order Placed successfully {}", new OrderPlacedEvent(newOrder.getOrderNumber()));
        return "Order Placed Successfully";
      } else {
        throw new IllegalArgumentException("Product is not in stock, Please try again later!");
      }

    } finally {
      inventoryServiceLookup.end();
    }
  }

  private OrderItems mapToDTo(OrderItemsDto orderItemsDto) {
    OrderItems orderItems = new OrderItems();
    orderItems.setPrice(orderItemsDto.getPrice());
    orderItems.setQuantity(orderItemsDto.getQuantity());
    orderItems.setSkuCode(orderItemsDto.getSkuCode());

    return orderItems;
  }
}
