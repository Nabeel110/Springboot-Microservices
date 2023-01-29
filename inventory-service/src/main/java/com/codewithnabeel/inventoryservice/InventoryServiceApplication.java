package com.codewithnabeel.inventoryservice;

import com.codewithnabeel.inventoryservice.model.Inventory;
import com.codewithnabeel.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InventoryServiceApplication.class, args);
  }

  @Bean
  public CommandLineRunner loadInventory(InventoryRepository inventoryRepository) {
    return args -> {
      Inventory inventory1 = Inventory.builder().skuCode("iphone_13").quantity(100).build();

      Inventory inventory2 = Inventory.builder().skuCode("iphone_14_pro").quantity(0).build();

      inventoryRepository.save(inventory1);
      inventoryRepository.save(inventory2);
    };
  }

}
