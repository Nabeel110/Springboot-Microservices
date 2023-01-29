package com.codewithnabeel.orderservice.controller;

import com.codewithnabeel.orderservice.dto.OrderRequest;
import com.codewithnabeel.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping
  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
  @TimeLimiter(name = "inventory")
  @Retry(name = "inventory")
  public CompletableFuture<ResponseEntity<String>> placeOrder(@RequestBody OrderRequest orderRequest) {
    return CompletableFuture.supplyAsync(() ->
        new ResponseEntity<>(orderService.placeOrder(orderRequest), HttpStatus.CREATED));
  }

  public CompletableFuture<ResponseEntity<String>> fallbackMethod(OrderRequest orderRequest, Throwable ex) {
    return CompletableFuture.supplyAsync(
        () -> new ResponseEntity<>("Oops! Something went wrong, please order after some time", HttpStatus.SERVICE_UNAVAILABLE));
  }
}
