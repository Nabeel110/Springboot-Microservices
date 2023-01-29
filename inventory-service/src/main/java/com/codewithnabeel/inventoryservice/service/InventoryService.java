package com.codewithnabeel.inventoryservice.service;

import com.codewithnabeel.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

  List<InventoryResponse> isInStock(List<String> skuCode) throws InterruptedException;
}
