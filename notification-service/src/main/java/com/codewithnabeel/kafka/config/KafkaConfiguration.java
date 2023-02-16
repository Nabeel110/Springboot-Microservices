package com.codewithnabeel.kafka.config;

import com.codewithnabeel.kafka.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class KafkaConfiguration {

  @KafkaListener(topics = "notificationTopic")
  public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
    // send out an email notification
    log.info("Received notification for order id {}", orderPlacedEvent.getOrderNumber());
  }
}
