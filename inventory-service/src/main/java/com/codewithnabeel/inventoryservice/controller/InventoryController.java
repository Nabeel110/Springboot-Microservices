package com.codewithnabeel.inventoryservice.controller;

import com.codewithnabeel.inventoryservice.service.InventoryService;
import com.codewithnabeel.inventoryservice.dto.InventoryResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {

  @Autowired
  private InventoryService inventoryService;

  @GetMapping
  @SneakyThrows
  public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> skuCode) {
    return new ResponseEntity<>(inventoryService.isInStock(skuCode), HttpStatus.OK);
  }
}
