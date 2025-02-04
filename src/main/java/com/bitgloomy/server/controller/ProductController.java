package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Product;
import com.bitgloomy.server.dto.RequestUploadProductDTO;
import com.bitgloomy.server.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/product")
    public ResponseEntity<?> uploadProduct(@RequestBody RequestUploadProductDTO requestUploadProductDTO){
        // 추후 권한 확인하는 코드 추가 예정
        // 이미지 파일 받아서 서버에 저장후 DB에 경로 저장 부분 추가 할 것
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
    @GetMapping("/products")
    public ResponseEntity<?> findAllProducts(){
        // DB에 저장된 이미지 경로 클라에 응답 추가 할 것
        // url 경로를 서버가 따로 서빙 해주고 json으로 url만 전달하게 변경
        // 이미지 등록시 url 서빙(절대경로에서 외부 접속가능하게) 해서 저장하도록
        try {
            ArrayList<Product> productsList = productService.findAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(productsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

}
