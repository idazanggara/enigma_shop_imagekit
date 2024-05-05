package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // query method
    // find by name -< tidak boleh sembarangan buat nama
    // Optional<T> findBy... -> result List/Satuan
    // Stream<T>/List<T> findAllBy.. ->  List
    // contoh lain
    // List<Product> findAllByNameAndPrice(String name, Long price)
    // karena name spesifik, jadi kita pakai query like
    List<Product> findAllByNameLike(String name);
}
