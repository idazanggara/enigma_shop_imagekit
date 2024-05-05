package com.enigma.enigma_shop.entity;

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
public class Product {
    private String id;
    private String name;
    private Long price;
}
