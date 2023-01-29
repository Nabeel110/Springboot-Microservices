package com.codewithnabeel.productservice.controller;

import com.codewithnabeel.productservice.dto.ProductRequest;
import com.codewithnabeel.productservice.dto.ProductResponse;
import com.codewithnabeel.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping
  public ResponseEntity<Void> addProduct(@RequestBody ProductRequest productRequest) {
    productService.createProduct(productRequest);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getProducts() {
    List<ProductResponse> productResponse = productService.getAllProducts();

    return new ResponseEntity<>(productResponse, HttpStatus.OK);
  }
}
