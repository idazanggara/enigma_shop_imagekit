package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ConstantTable.CUSTOMER)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile_phone_no")
    private String mobilePhoneNo;
    @Column(name = "address")
    private String address;
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE) // karena kita menggunakan DATE tipe datanya
    // dan temporal ini kita batasi hanya tanggal aja yg masuk datanya, karena date itu bisa dapet hari tanggal dan jam
    @JsonFormat(pattern = "yyyy-MM-dd")
    // yyyy-mm-dd : format date json
    // date dari java util
    private Date birthDate;
    @Column(name = "status")
    private Boolean status;


    // setelah kita buat update status, bisa jelasin, kalau nanti status itu jangan di update di sini, nanti kita bisa pakai DTO


}
