package com.enigma.enigma_shop.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
	private String customerId;
	private List<TransactionDetailRequest> transactionDetails; // janga namain transactionDetailRequest, nanti key dari jsonnya harus sama dengan transaction detail request
}
