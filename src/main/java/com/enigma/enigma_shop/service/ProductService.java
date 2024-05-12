package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    // save biasanya void kan
    // saat save, kita mengembalikan product baru yg sudah ada Idnya
//    Product create(Product product);
    Product create(NewProductRequest productRequest);
    Product getById(String id);

    List<Product> getAllQueryMethod(String name);
    // ini nanti mengebalik product yg sudah terupdate
    Product update(Product product);
    void deleteById(String id);
    Page<Product> getAll(SearchProductRequest request);

}
