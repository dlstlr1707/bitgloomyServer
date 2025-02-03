package com.bitgloomy.server.service;

import com.bitgloomy.server.domain.Product;
import com.bitgloomy.server.mybatis.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {
    ProductMapper productMapper;
    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }
    public void saveProduct(Product product) throws Exception {
        Product foundProduct = productMapper.findProductByPname(product.getPname());
        if(foundProduct != null){
            throw new Exception();
        }
        productMapper.saveProduct(product);
    }
    public Product findProductById(String pname) throws Exception {
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
