package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor // ini sudah termasuk autowired, jadi sama seperti constructor injection
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
    public ResponseEntity<Product>  createNewProduct(@RequestBody Product product){
        // return productService.create(product);
        Product newProduct = productService.create(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newProduct);
    }
    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<Product> getById(@PathVariable String id) {
        // return  productService.getById(id);
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProduct(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            // kan nanti bisa banyak yg kita kirim ya
            // nanti ada filter data macem-macem disini
            // berarti kita pakai apa? requestDTO

            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,

            // sekarang kita coba tambahkan spesification
            @RequestParam(name = "name" ,required = false) String name
    ){
        SearchProductRequest request = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();
//        return productService.getAll(request);
        Page<Product> products = productService.getAll(request);
        return ResponseEntity.ok(products);
    }
    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){
//        return productService.update(product);
        Product update = productService.update(product);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        productService.deleteById(id);
//        return "Ok Succes Delete Product";
        return ResponseEntity.ok("Ok Succes Delete Product");
    }
}
