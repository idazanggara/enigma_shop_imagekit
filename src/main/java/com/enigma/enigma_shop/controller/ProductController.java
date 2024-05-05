package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    // nah inika dia butuh contructor seperti ini, dari pada panjang
//    @Autowired
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

//    @PostMapping(path = "/api/products")
//    @GetMapping(path = "/api/products/{id}")
    @PostMapping
    public Product createNewProduct(@RequestBody Product product){
        return productService.create(product);
    }
    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public Product getById(@PathVariable String id) {
        return  productService.getById(id);
    }
    @GetMapping
    public List<Product> getAllProduct(){
        return productService.getAll();
    }
    @PutMapping
    public Product updateProduct(@RequestBody Product product){
        return productService.update(product);
    }
    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public String deleteById(@PathVariable String id) {
        productService.deleteById(id);
        return "Ok Succes Delete";
    }
}
