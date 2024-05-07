package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
	// ini kita bisa membuat lebih dari satu datanya,
	// jadi yg kita simpen bukan satuaan
	List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails);
	// nah disini kita enggak papa pakai entitynya aja.
}
