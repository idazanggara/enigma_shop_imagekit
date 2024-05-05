package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.entity.Product;

import java.util.List;

public interface ProductService {
    // save biasanya void kan
    // saat save, kita mengembalikan product baru yg sudah ada Idnya
    Product create(Product product);
    Product getById(String id);
    List<Product> getAll(String name);
    // ini nanti mengebalik product yg sudah terupdate
    Product update(Product product);
    void deleteById(String id);
}
