package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// karena kita pakai lombok, kita udah bisa langsung tambahkan anotasi aja, itu sama seperti generate manual, jadi tidak panjang lagi
@Getter
@Setter
// nah disini kita juga bisa tambahkan contructornya, kalau kalian buat contructor manual, nanti error karena tambrakkan
@AllArgsConstructor
@NoArgsConstructor // untuk contructor kosong
// configurasi sama kayak jpa kemaren
@Entity
@Table(name = ConstantTable.PRODUCT)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, columnDefinition = "BIGINT CHECK (price >= 0)")
    private Long price;

    @Column(name = "stock", nullable = false, columnDefinition = "INT CHECK (stock >= 0)")
    private Integer stock;
}
