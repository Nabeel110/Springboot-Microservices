package com.codewithnabeel.productservice.service;

import com.codewithnabeel.productservice.dto.ProductRequest;
import com.codewithnabeel.productservice.dto.ProductResponse;
import com.codewithnabeel.productservice.model.Product;
import com.codewithnabeel.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;


  @Override
  public void createProduct(ProductRequest productRequest) {

    Product newProduct = Product.builder().id(UUID.randomUUID().toString()).name(productRequest.getProductName())
        .description(productRequest.getProductDescription()).price(productRequest.getPrice()).build();
    productRepository.save(newProduct);
    log.info("Product {} is saved", newProduct.getId());
  }

  @Override
  public List<ProductResponse> getAllProducts() {
    List<Product> productList = productRepository.findAll();

    return productList.stream().map(this::mapToProductResponse).toList();

  }

  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder().id(product.getId()).productName(product.getName()).productDescription(product.getDescription())
        .price(product.getPrice()).build();
  }
}
