package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.TransactionDetail;
import com.enigma.enigma_shop.repository.TransactionDetailRepository;
import com.enigma.enigma_shop.service.TransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {
	private final TransactionDetailRepository transactionDetailRepository;

	@Override
	public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails) {
		// saveAllAndFlush kan dia minta Iterable, nah list bisa masuk enggak?
		return transactionDetailRepository.saveAllAndFlush(transactionDetails);
	}
}
