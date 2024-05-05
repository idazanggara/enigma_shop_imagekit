package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.repository.ProductRepository;
import com.enigma.enigma_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Product create(Product product) {
        // productRepository.
        // kenapa ada method yg cukup banyak? ada yg bisa jelasin?
        // soalnya interface repository kita extend, dan ketika save udah otomatis menerima entity yg kita set di repository, coba kita ganti object
        Product newProduct = productRepository.saveAndFlush(product);
        // saveAndFlush, jadi ketika product gw save, dia akan otomatis mengembalikan id yg berhasil kedave
        return newProduct;
    }

    @Override
    public Product getById(String id) {
        // nah kalau kita .var, pengembaliannya jadi Optional. nah optional ini cukup powerfull
        Optional<Product> optionalProduct = productRepository.findById(id);
        // kita bisa validasi kalau misalnya datanya enggak ada
        if (optionalProduct.isEmpty()){ // bisa throw
            // nah catchnya kapan? itu dah di handle sama spring bootnya, jadi sebenernya kita jarang try catch, jadi hanya case tertentu aja nanti kita pakai try-cacth
            throw new RuntimeException("product not found");
        }
        // nah dengan menggunakan method get ini, maksudnya Optionalnya di buat dan mengembalikan Productnya aja
        // nah tapi method getnya udah ngetrhoe sebenernya, jadi kalau valuenya kosong dia akan ngethrow
        return optionalProduct.get();
    }

    @Override
    public List<Product> getAll(String name) {
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
}
