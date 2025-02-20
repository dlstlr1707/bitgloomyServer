package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Cart;
import com.bitgloomy.server.domain.Product;
import com.bitgloomy.server.domain.ProductImg;
import com.bitgloomy.server.dto.*;
import com.bitgloomy.server.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public ResponseEntity<?> uploadProduct(@RequestPart(value = "mainImg") MultipartFile file, @RequestPart(value = "subImg") List<MultipartFile> files, @RequestPart(value = "productInfo") RequestUploadProductDTO requestUploadProductDTO){
        // 추후 권한 확인하는 코드 추가 예정
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
            if(subImgURL.size()>1 && subImgURL.get(1)!=null){
                product.getProductImg().setSubImgUrl2(subImgURL.get(1));
            }else{
                product.getProductImg().setSubImgUrl2("");
            }
            if(subImgURL.size()>2 && subImgURL.get(2)!=null){
                product.getProductImg().setSubImgUrl3(subImgURL.get(2));
            }else{
                product.getProductImg().setSubImgUrl3("");
            }
            if(subImgURL.size()>3 && subImgURL.get(3)!=null){
                product.getProductImg().setSubImgUrl4(subImgURL.get(3));
            }else{
                product.getProductImg().setSubImgUrl4("");
            }
            if(subImgURL.size()>4 && subImgURL.get(4)!=null){
                product.getProductImg().setSubImgUrl5(subImgURL.get(4));
            }else{
                product.getProductImg().setSubImgUrl5("");
            }
            if(subImgURL.size()>5 && subImgURL.get(5)!=null){
                product.getProductImg().setSubImgUrl6(subImgURL.get(5));
            }else{
                product.getProductImg().setSubImgUrl6("");
            }
            if(subImgURL.size()>6 && subImgURL.get(6)!=null){
                product.getProductImg().setSubImgUrl7(subImgURL.get(6));
            }else{
                product.getProductImg().setSubImgUrl7("");
            }
            if(subImgURL.size()>7 && subImgURL.get(7)!=null){
                product.getProductImg().setSubImgUrl8(subImgURL.get(7));
            }else{
                product.getProductImg().setSubImgUrl8("");
            }
            if(subImgURL.size()>8 && subImgURL.get(8)!=null){
                product.getProductImg().setSubImgUrl9(subImgURL.get(8));
            }else{
                product.getProductImg().setSubImgUrl9("");
            }
            if(subImgURL.size()>9 && subImgURL.get(9)!=null){
                product.getProductImg().setSubImgUrl10(subImgURL.get(9));
            }else{
                product.getProductImg().setSubImgUrl10("");
            }
            productService.saveProductImg(product);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PatchMapping("/product")
    public ResponseEntity<?> modifyProduct(@RequestPart(value = "mainImg") MultipartFile file, @RequestPart(value = "subImg") List<MultipartFile> files, @RequestPart(value = "modifyPInfo")RequestModifyProductDTO requestModifyProductDTO){
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
        product.setPname(requestModifyProductDTO.getPname());
        product.setContents(requestModifyProductDTO.getContents());
        product.setProductMaterial(requestModifyProductDTO.getProductMaterial());
        product.setFabric(requestModifyProductDTO.getFabric());
        product.setSize(requestModifyProductDTO.getSize());
        product.setPrice(Integer.parseInt(requestModifyProductDTO.getPrice()));
        product.setQuantity(Integer.parseInt(requestModifyProductDTO.getQuantity()));
        product.setCategory(requestModifyProductDTO.getCategory());
        product.setProductImg(productImg);
        try {
            productService.modifyProduct(product,requestModifyProductDTO.getModifyPname());
            product.getProductImg().setSimilarProductName(requestModifyProductDTO.getSimilarProductName());
            product.getProductImg().setImgURL(mainImgURL);
            product.getProductImg().setSubImgUrl1(subImgURL.get(0));
            if(subImgURL.size()>1 && subImgURL.get(1)!=null){
                product.getProductImg().setSubImgUrl2(subImgURL.get(1));
            }else{
                product.getProductImg().setSubImgUrl2("");
            }
            if(subImgURL.size()>2 && subImgURL.get(2)!=null){
                product.getProductImg().setSubImgUrl3(subImgURL.get(2));
            }else{
                product.getProductImg().setSubImgUrl3("");
            }
            if(subImgURL.size()>3 && subImgURL.get(3)!=null){
                product.getProductImg().setSubImgUrl4(subImgURL.get(3));
            }else{
                product.getProductImg().setSubImgUrl4("");
            }
            if(subImgURL.size()>4 && subImgURL.get(4)!=null){
                product.getProductImg().setSubImgUrl5(subImgURL.get(4));
            }else{
                product.getProductImg().setSubImgUrl5("");
            }
            if(subImgURL.size()>5 && subImgURL.get(5)!=null){
                product.getProductImg().setSubImgUrl6(subImgURL.get(5));
            }else{
                product.getProductImg().setSubImgUrl6("");
            }
            if(subImgURL.size()>6 && subImgURL.get(6)!=null){
                product.getProductImg().setSubImgUrl7(subImgURL.get(6));
            }else{
                product.getProductImg().setSubImgUrl7("");
            }
            if(subImgURL.size()>7 && subImgURL.get(7)!=null){
                product.getProductImg().setSubImgUrl8(subImgURL.get(7));
            }else{
                product.getProductImg().setSubImgUrl8("");
            }
            if(subImgURL.size()>8 && subImgURL.get(8)!=null){
                product.getProductImg().setSubImgUrl9(subImgURL.get(8));
            }else{
                product.getProductImg().setSubImgUrl9("");
            }
            if(subImgURL.size()>9 && subImgURL.get(9)!=null){
                product.getProductImg().setSubImgUrl10(subImgURL.get(9));
            }else{
                product.getProductImg().setSubImgUrl10("");
            }
            productService.modifyProductImg(product);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @DeleteMapping("/product/{pname}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "pname")String pname){
        try {
            productService.deleteProduct(pname);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    @PostMapping("/delete/carts")
    public ResponseEntity<?> deleteCarts(@RequestBody ArrayList<RequestDeleteSelectedCartDTO> requestDeleteSelectedCartDTO, HttpServletRequest request){
        System.out.println(requestDeleteSelectedCartDTO.get(0).getUid());
        HttpSession session = request.getSession(false);
        if(session==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            productService.deleteCarts(requestDeleteSelectedCartDTO);
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
    @GetMapping("/search/{text}")
    public ResponseEntity<?> searchProducts(@PathVariable(value = "text")String text){
        try{
            ArrayList<Product> productsList = productService.searchProducts(text);
            return ResponseEntity.status(HttpStatus.OK).body(productsList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
