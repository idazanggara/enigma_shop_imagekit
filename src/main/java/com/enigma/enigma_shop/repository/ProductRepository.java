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
    // karena name spesifik, jadi kita pakai query like
    List<Product> findAllByNameLike(String name);

    // simulasi filter berdasarkan name, minPrice, maxPrice, dan stock menggunakan operasi or
    // jika ga ketemua maka tampilkan semua product



    // kalau kalian mau query native
//    @Query(nativeQuery = true) // tapi hati-hati, query kalian harus sesuai dengan entity
}
