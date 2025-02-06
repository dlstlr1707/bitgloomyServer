package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ProductMapper {
    void saveProduct(Product product);
    void saveProductImg(Product product);
    Product findProductByPname(String pname);
    Product findProductUidByPname(String pname);
    ArrayList<Product> findAllProducts();
}
