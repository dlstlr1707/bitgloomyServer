package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Cart;
import com.bitgloomy.server.domain.Product;
import com.bitgloomy.server.domain.ProductImg;
import com.bitgloomy.server.dto.RequestAddCartDTO;
import com.bitgloomy.server.dto.RequestModifyCartDTO;
import com.bitgloomy.server.dto.RequestUploadProductDTO;
import com.bitgloomy.server.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Transactional
    @PostMapping("/product")
    public ResponseEntity<?> uploadProduct(@RequestPart(value = "mainImg") MultipartFile file, @RequestPart(value = "subImg") List<MultipartFile> files, @RequestPart(value = "productInfo") RequestUploadProductDTO requestUploadProductDTO, HttpServletRequest request){
        // 추후 권한 확인하는 코드 추가 예정
        HttpSession session = request.getSession(false);
        if(session==null || session.getAttribute("auth")!="role_admin"){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String mainImgURL;
        List<String> subImgURL = new ArrayList<>();
        try {
            mainImgURL = productService.save(file);
            for (int i=0;i<files.size();i++) {
                String tempURL = productService.save(files.get(i));
                subImgURL.add(i,tempURL);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Product product = new Product();
        ProductImg productImg = new ProductImg();
        product.setPname(requestUploadProductDTO.getPname());
        product.setContents(requestUploadProductDTO.getContents());
        product.setProductMaterial(requestUploadProductDTO.getProductMaterial());
        product.setFabric(requestUploadProductDTO.getFabric());
        product.setSize(requestUploadProductDTO.getSize());
        product.setPrice(Integer.parseInt(requestUploadProductDTO.getPrice()));
        product.setQuantity(Integer.parseInt(requestUploadProductDTO.getQuantity()));
        product.setCategory(requestUploadProductDTO.getCategory());
        product.setProductImg(productImg);
        try {
            productService.saveProduct(product);
            product.getProductImg().setSimilarProductName(requestUploadProductDTO.getSimilarProductName());
            product.getProductImg().setImgURL(mainImgURL);
            product.getProductImg().setSubImgUrl1(subImgURL.get(0));
            product.getProductImg().setSubImgUrl2(subImgURL.get(1));
            product.getProductImg().setSubImgUrl3(subImgURL.get(2));
            product.getProductImg().setSubImgUrl4(subImgURL.get(3));
            product.getProductImg().setSubImgUrl5(subImgURL.get(4));
            product.getProductImg().setSubImgUrl6(subImgURL.get(5));
            product.getProductImg().setSubImgUrl7(subImgURL.get(6));
            product.getProductImg().setSubImgUrl8(subImgURL.get(7));
            product.getProductImg().setSubImgUrl9(subImgURL.get(8));
            product.getProductImg().setSubImgUrl10(subImgURL.get(9));
            productService.saveProductImg(product);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/products")
    public ResponseEntity<?> findAllProducts(){
        try {
            ArrayList<Product> productsList = productService.findAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(productsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @GetMapping("/detail/{pname}")
    public ResponseEntity<?> findProductByPname(@PathVariable(value = "pname")String pname){
        try {
            Product foundProduct = productService.findProductByPname(pname);
            return ResponseEntity.status(HttpStatus.OK).body(foundProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/cart")
    public ResponseEntity<?> addCart(@RequestBody RequestAddCartDTO requestAddCartDTO,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            productService.addCart(requestAddCartDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
    @GetMapping("/carts/{uid}")
    public ResponseEntity<?> findAllCarts(@PathVariable(value = "uid")int uid,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            ArrayList<Cart> cartsList = productService.findAllCarts(uid);
            return ResponseEntity.status(HttpStatus.OK).body(cartsList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PatchMapping("/cart")
    public ResponseEntity<?> modifyCartInfo(@RequestBody RequestModifyCartDTO requestModifyCartDTO,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            productService.modifyCart(requestModifyCartDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/cart/{uid}")
    public ResponseEntity<?> deleteCart(@PathVariable(value = "uid")int uid,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            productService.deleteCart(uid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/carts/{userUid}")
    public ResponseEntity<?> deleteAllCart(@PathVariable(value = "userUid")String userUid,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null || session.getAttribute("userUid").equals(userUid)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            productService.deleteAllCart(Integer.parseInt(userUid));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
