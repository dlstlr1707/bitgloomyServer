package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Product;
import com.bitgloomy.server.dto.RequestUploadProductDTO;
import com.bitgloomy.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Controller
public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/product")
    public ResponseEntity<?> uploadProduct(@RequestBody RequestUploadProductDTO requestUploadProductDTO){
        // 추후 권한 확인하는 코드 추가 예정
        Product product = new Product();
        product.setPname(requestUploadProductDTO.getPname());
        product.setContents(requestUploadProductDTO.getContents());
        product.setProductMaterial(requestUploadProductDTO.getProductMaterial());
        product.setFabric(requestUploadProductDTO.getFabric());
        product.setSize(requestUploadProductDTO.getSize());
        product.setPrice(requestUploadProductDTO.getPrice());
        product.setQuantity(requestUploadProductDTO.getQuantity());
        product.setCategory(requestUploadProductDTO.getCategory());
        try {
            productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("products")
    public ResponseEntity<?> findAllProducts(){
        try {
            ArrayList<Product> productsList = productService.findAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(productsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

}
