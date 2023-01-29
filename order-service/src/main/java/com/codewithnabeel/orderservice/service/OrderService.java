package com.codewithnabeel.orderservice.service;

import com.codewithnabeel.orderservice.dto.OrderRequest;

import java.util.concurrent.CompletableFuture;

public interface OrderService {

  String placeOrder(OrderRequest orderRequest);
}
