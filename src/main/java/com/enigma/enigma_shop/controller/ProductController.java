package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.PagingResponse;
import com.enigma.enigma_shop.dto.response.ProductResponse;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor // ini sudah termasuk autowired, jadi sama seperti constructor injection
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    private final ObjectMapper objectMapper;

    // nah inika dia butuh contructor seperti ini, dari pada panjang
//    @Autowired
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

//    @PostMapping(path = "/api/products")
//    @GetMapping(path = "/api/products/{id}")
//    @PostMapping
//    public ResponseEntity<CommonResponse<Product>>  createNewProduct(@RequestBody NewProductRequest productRequest){
//        // return productService.create(product);
//        Product newProduct = productService.create(productRequest);
////        return ResponseEntity
////                .status(HttpStatus.CREATED)
////                .body(newProduct); // cuman returnya jadi error nih guys, harus kita bungkus jadi common response dulu nih\
//        // nah kalau tanda tanya tadi enggak wajib, tapi kalau bukan tanda tanya wajib kita kasih objectnya
//        CommonResponse<Product> response = CommonResponse.<Product>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message("successfully create new customer")
//                .data(newProduct)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//
//    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>>  createNewProductWithImage(
            @RequestPart(name = "product") String jsonProductRequest,
            @RequestPart(name = "image") MultipartFile image // MultipartFile by defaul kalau kita enggak kirim image, datanya sudah ada.
    ){
        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();
        try {
            NewProductRequest productRequest = objectMapper.readValue(jsonProductRequest, new TypeReference<>() {
            });
            productRequest.setImage(image);// jadi nanti begini guys,
            System.out.println("========================== testing"+productRequest);
            ProductResponse newProduct = productService.create(productRequest);

            // nanti setelah selesai
            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message("successfully save data");
            responseBuilder.data(newProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.build());
        }catch (Exception e){
            responseBuilder.message("internal server error");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<Product>> getById(@PathVariable String id) {
        // return  productService.getById(id);
        Product product = productService.getById(id);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully fetch data")
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping
//    public ResponseEntity<Page<Product>> getAllProductOld(
//            @RequestParam(name = "page",defaultValue = "1") Integer page,
//            @RequestParam(name = "size", defaultValue = "10") Integer size,
//            // kan nanti bisa banyak yg kita kirim ya
//            // nanti ada filter data macem-macem disini
//            // berarti kita pakai apa? requestDTO
//
//            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
//            @RequestParam(name = "direction", defaultValue = "asc") String direction,
//
//            // sekarang kita coba tambahkan spesification
//            @RequestParam(name = "name" ,required = false) String name
//    ){
//        SearchProductRequest request = SearchProductRequest.builder()
//                .page(page)
//                .size(size)
//                .sortBy(sortBy)
//                .direction(direction)
//                .name(name)
//                .build();
////        return productService.getAll(request);
//        Page<Product> products = productService.getAll(request);
//        return ResponseEntity.ok(products);
//    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProduct(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name" ,required = false) String name
    ){
        SearchProductRequest request = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();
        Page<ProductResponse> products = productService.getAll(request);
        // kita ubah jadi common response
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(products.getTotalPages())
                .totalElement(products.getTotalElements())
                .page(products.getPageable().getPageNumber() + 1) // kita balikin bentuk awal, karena kalau halaman 1 itu biasanya mengembalikan 0
                .size(products.getPageable().getPageSize())
                .hasNext(products.hasNext())
                .hasPrevious(products.hasPrevious())
                .build();

        // kita buat list dulu, baru seletah itu pagingResponse
        CommonResponse<List<ProductResponse>> response = CommonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("success get all product")
                .data(products.getContent())
                .paging(pagingResponse)// paginhnya mana? kita buat dulu diatas
                .build();

        return ResponseEntity.ok(response);
    }
    @PutMapping
    public ResponseEntity<CommonResponse<Product>> updateProduct(@RequestBody Product product){
//        return productService.update(product);
        Product update = productService.update(product);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully update data")
                .data(update)
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<?>> deleteById(@PathVariable String id) {
        productService.deleteById(id);
//        return "Ok Succes Delete Product";
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Ok Succes Delete Product")
                .build();
        return ResponseEntity.ok(response);
    }
}
