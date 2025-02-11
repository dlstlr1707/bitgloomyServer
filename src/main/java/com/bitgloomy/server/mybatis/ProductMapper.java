package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.Cart;
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
    void addCart(Cart cart);
    ArrayList<Cart> findAllCarts(int uid);
    void modifyCart(Cart cart);
    void deleteCart(int uid);
    void deleteAllCart(int userUid);
}
