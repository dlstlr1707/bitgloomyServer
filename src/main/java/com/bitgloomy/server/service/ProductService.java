package com.bitgloomy.server.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bitgloomy.server.domain.Product;
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
}
