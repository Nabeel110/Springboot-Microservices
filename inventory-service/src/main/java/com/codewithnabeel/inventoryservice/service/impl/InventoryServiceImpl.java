package com.codewithnabeel.inventoryservice.service.impl;

import com.codewithnabeel.inventoryservice.service.InventoryService;
import com.codewithnabeel.inventoryservice.dto.InventoryResponse;
import com.codewithnabeel.inventoryservice.repository.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

  @Autowired
  private InventoryRepository inventoryRepository;

  @Override
  @Transactional(readOnly = true)
  @SneakyThrows
  public List<InventoryResponse> isInStock(List<String> skuCodes) {
//    log.info("Wait Started");
//    Thread.sleep(10000);
//    log.info("Wait Ended");
    return inventoryRepository.findBySkuCodeIn(skuCodes)
        .stream()
        .map(inventory ->
            InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build()
        ).toList();
  }
}
