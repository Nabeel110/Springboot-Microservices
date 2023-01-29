package com.codewithnabeel.productservice.service;

import com.codewithnabeel.productservice.dto.ProductRequest;
import com.codewithnabeel.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
  void createProduct(ProductRequest productRequest);

  List<ProductResponse> getAllProducts();
}
