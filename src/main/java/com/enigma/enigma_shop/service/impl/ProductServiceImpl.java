package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.repository.ProductRepository;
import com.enigma.enigma_shop.service.ProductService;
import com.enigma.enigma_shop.specification.ProductSpecification;
import com.enigma.enigma_shop.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

// lombok RequiredArgumentConstructor -> constructor injection, jadi enggak perlu pakai @Autowired

@RequiredArgsConstructor // jadi enggak error lagi
@Service // tambahkan component ya di service dan repository
public class ProductServiceImpl implements ProductService {
    // nah cara kalian mengambil repository
    // kaliam paggil productrepository
    private final ProductRepository productRepository;
    // nah disini seharusnya buat contructor untuk ngasih valuenya ke productRepository.
    // kita bisa gunakan lombok

    // jadi sama seperti ini ya untuk @RequiredArgsConstructor
//    @Autowired
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

     private final ValidationUtil validationUtil;

    @Override
    public Product create(NewProductRequest productRequest) {
        // productRepository.
        // kenapa ada method yg cukup banyak? ada yg bisa jelasin?
        // soalnya interface repository kita extend, dan ketika save udah otomatis menerima entity yg kita set di repository, coba kita ganti object
        // saveAndFlush, jadi ketika product gw save, dia akan otomatis mengembalikan id yg berhasil kedave
//        Product newProduct = productRepository.saveAndFlush(product);

        validationUtil.validate(productRequest); // ini selalu di panggil paling atas
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();
        return productRepository.saveAndFlush(product);
    }

    @Override
    public Product getById(String id) {
        // nah kalau kita .var, pengembaliannya jadi Optional. nah optional ini cukup powerfull
        Optional<Product> optionalProduct = productRepository.findById(id);
        // kita bisa validasi kalau misalnya datanya enggak ada
        if (optionalProduct.isEmpty()){ // bisa throw
            // nah catchnya kapan? itu dah di handle sama spring bootnya, jadi sebenernya kita jarang try catch, jadi hanya case tertentu aja nanti kita pakai try-cacth
//            throw new RuntimeException("product not found");
            // Saat sudah menggunakan ErrorController, thrownya kita uba
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
            // nah nanti pada saat di throw dia akan mengarah ke method yg kita buat di errorController
        }
        // nah dengan menggunakan method get ini, maksudnya Optionalnya di buat dan mengembalikan Productnya aja
        // nah tapi method getnya udah ngetrhoe sebenernya, jadi kalau valuenya kosong dia akan ngethrow
        return optionalProduct.get();
    }

    @Override
    public List<Product> getAllQueryMethod(String name) {
//        productRepository.findByName() tapi enggak ada nih find by name
        // harus apa nih gw, bikin query method di repository
        if(name != null ) {
            return productRepository.findAllByNameLike("%" + name + "%");
        }
        return productRepository.findAll();
    }

    @Override
    public Product update(Product product) {
        // pertama bisa di cari dulu idnya
//        productRepository.findById(product.getId());// tapi ini kan berulang, kita udah ada getbyid diatas
        getById(product.getId()); // dah begini aja, jangan di return apapun, kalau nanti idnya enggak ada, dia akan throw diatas
        return productRepository.saveAndFlush(product);
        // nah update sama nih, tinggal save aja, tapi kalian bisa validasi diatas
    }

    @Override
    public void deleteById(String id) {
    // delete juga sama
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }

    @Override
    public Page<Product> getAll(SearchProductRequest request) {
        // kalau mau di kasih validasi
        if(request.getPage() <= 0) {
            request.setPage(1);
        }
        String validSortBy ;
        if ("name".equalsIgnoreCase(request.getSortBy()) || "price".equalsIgnoreCase(request.getSortBy()) || "stock".equalsIgnoreCase(request.getSortBy())) {
            validSortBy = request.getSortBy();
        } else {
            validSortBy = "name";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()) /*mengambil arah sortingnya*/,validSortBy /*request.getSortBy() /*berdasarkan apa nih sortingnya*/);
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Specification<Product> specification = ProductSpecification.getSpecification(request);
        return productRepository.findAll(specification ,pageable);
    }
}
