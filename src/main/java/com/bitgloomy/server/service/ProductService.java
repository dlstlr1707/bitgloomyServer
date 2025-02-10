package com.bitgloomy.server.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bitgloomy.server.domain.Cart;
import com.bitgloomy.server.domain.Product;
import com.bitgloomy.server.dto.RequestAddCartDTO;
import com.bitgloomy.server.dto.RequestModifyCartDTO;
import com.bitgloomy.server.mybatis.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class ProductService {
    ProductMapper productMapper;
    private final AmazonS3Client amazonS3Client;

    @Autowired
    public ProductService(ProductMapper productMapper, AmazonS3Client amazonS3Client) {
        this.productMapper = productMapper;
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private void validateExist(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception();
        }
    }
    public String save(MultipartFile file) throws IOException {
        try {
            validateExist(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    public void saveProduct(Product product) throws Exception {
        Product foundProduct = productMapper.findProductByPname(product.getPname());
        if(foundProduct != null){
            throw new Exception();
        }
        productMapper.saveProduct(product);
    }
    public void saveProductImg(Product product) throws Exception {
        Product foundProduct = productMapper.findProductUidByPname(product.getPname());
        if(foundProduct == null){
            throw new Exception();
        }
        product.getProductImg().setUid(foundProduct.getUid());
        // 관련상품의 이미지 가져와서 product에 셋팅하는 코드 있어야함
        product.getProductImg().setSimilarImgUrl(findSimilarProductImgURL(product.getProductImg().getSimilarProductName()));
        productMapper.saveProductImg(product);
    }
    public String findSimilarProductImgURL(String similarProductName)throws Exception{
        // 구분자(,)로 구성된 String 분할해서 String으로 url가져와서 구분자(,)로 다시 리팩후 응답
        if(similarProductName == null){
            throw new Exception();
        }
        String[] splitStringArr = similarProductName.split(",");
        String silimarProductImgURL="";
        for(int i=0; i<splitStringArr.length;i++){
            Product foundSimilarProduct = productMapper.findProductByPname(splitStringArr[i]);
            silimarProductImgURL = silimarProductImgURL+","+foundSimilarProduct.getProductImg().getImgURL();
        }
        return silimarProductImgURL;
    }
    public Product findProductByPname(String pname) throws Exception {
        Product foundProduct = productMapper.findProductByPname(pname);
        if(foundProduct != null){
            return foundProduct;
        }else{
            throw new Exception();
        }
    }
    public ArrayList<Product> findAllProducts() throws Exception {
        ArrayList<Product> resultArr = productMapper.findAllProducts();
        if(resultArr.isEmpty()){
            throw new Exception();
        }
        return resultArr;
    }
    public void addCart(RequestAddCartDTO requestAddCartDTO)throws Exception{
        // 이미 카트에 해당상품 있으면 에러
        Cart cart = new Cart();
        cart.setUserUid(Integer.parseInt(requestAddCartDTO.getUserUid()));
        cart.setProductUid(requestAddCartDTO.getProductUid());
        cart.setProductName(requestAddCartDTO.getProductName());
        String[] tempSize = requestAddCartDTO.getSize().split(",");
        String[] tempAmount = requestAddCartDTO.getAmount().split(",");
        String[] tempPrice = requestAddCartDTO.getPrice().split(",");
        for(int i=0;i<tempSize.length;i++){
            System.out.println(tempSize[i]);
            cart.setAmount(Integer.parseInt(tempAmount[i]));
            cart.setPrice(Integer.parseInt(tempPrice[i]));
            cart.setSize(tempSize[i]);
            productMapper.addCart(cart);
        }
    }
    public ArrayList<Cart> findAllCarts(int uid) throws Exception {
        ArrayList<Cart> resultArr = productMapper.findAllCarts(uid);
        if(resultArr.isEmpty()){
            throw new Exception();
        }
        return resultArr;
    }
    public void modifyCart(RequestModifyCartDTO requestModifyCartDTO){
        Cart cart = new Cart();
        cart.setUid(requestModifyCartDTO.getUid());
        cart.setAmount(Integer.parseInt(requestModifyCartDTO.getAmount()));
        cart.setPrice(Integer.parseInt(requestModifyCartDTO.getPrice()));
        productMapper.modifyCart(cart);
    }
    public void deleteCart(int uid){
        productMapper.deleteCart(uid);
    }
}
