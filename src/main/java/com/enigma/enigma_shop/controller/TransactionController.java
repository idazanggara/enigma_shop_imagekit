package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionController {
	// service, ini kosong dulu

	// setelah buat DTO requestnya baru panggil service dan buat service
	private final TransactionService transactionService;
	@PostMapping
	public Transaction createNewTransaction(@RequestBody TransactionRequest request) {
		// logic untuk memanggil service
		return transactionService.create(request);
	}

}
