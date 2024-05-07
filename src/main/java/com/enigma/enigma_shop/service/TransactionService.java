package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.entity.Transaction;

import java.util.List;

public interface TransactionService {
	Transaction create(TransactionRequest request);

	List<Transaction> getAll();
}
