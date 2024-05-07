package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.TransactionDetailRequest;
import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.entity.TransactionDetail;
import com.enigma.enigma_shop.repository.TransactionDetailRepository;
import com.enigma.enigma_shop.repository.TransactionRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.service.ProductService;
import com.enigma.enigma_shop.service.TransactionDetailService;
import com.enigma.enigma_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	// nah kalau begini enggak di sarankan guys, ini namanya
	// Cross Repository
	// jadi kita hanya boleh memanggil 1 repo,
	private final TransactionRepository transactionRepository;

	// jangan ke TransactionDetailRepository, karena cross, jadi ke service yga bener
//	private final TransactionDetailRepository transactionDetailRepository;

	private final TransactionDetailService transactionDetailService;

	private final CustomerService customerService;
	private final ProductService productService;

	// karena bagian dari transaksi, kita pakai anotasi
	@Transactional(rollbackFor = Exception.class) // ini auto commit, trus rollback kalau ada Exeption
	// jadi enggak ada logika lagi, udah di tanganin sama anotasi
	@Override
	public Transaction create(TransactionRequest request) {
		// kita harus cari/validasi cutomer
		Customer customer = customerService.getById(request.getCustomerId()); // degnan getById, kalian udah validasi juga, jadi kalau customernya enggak ada, udah di throw dan di rollback

		// kita simpan dulu Transaction baru setelah itu simpan TransactionDetail
		// 1 Save Transaction table
		Transaction trx = Transaction.builder()
						.customer(customer) // nah ini kan ke cutomer, boleh enggak panggil repo customer? enggak kan, jadi kita manggil customer service
						.transDate(new Date())
//						.transactionDetails() ini belum ada isinya ya
						.build();
		transactionRepository.saveAndFlush(trx);
//		transactionRepository.saveAndFlush(request);// begini kenapa enggak bisa? karena request bukan entity

		// 2 Save Transaction Detail Table
		// nah bisa bikin list dulu ya guys
		List<TransactionDetail> trxDetail = request.getTransactionDetails().stream()
						.map(detailRequest -> {
											//validasi product
							Product product = productService.getById(detailRequest.getProductId());
							// buat ngurangi stock
							if(product.getStock() - detailRequest.getQty() < 0) throw new RuntimeException("Sold Out");

							product.setStock(product.getStock() - detailRequest.getQty());

							return TransactionDetail.builder()
											.product(product)
											// cuman ada yg kuran nih, si transactionDetail ini butuh transaksi enggak? butuh ya, buat masukkin transaksi_idnya
											.transaction(trx)
											.qty(detailRequest.getQty())
											.productPrice(product.getPrice())
											.build();
						}).toList();
//		transactionDetailService.createBulk(request.getTransactionDetails()); // ini enggak sesuai typedatanya ya, jadi enggak masuk, yg diminta itu entity transactionDetail yg di berikan DTO transactionDetailReq uest

		transactionDetailService.createBulk(trxDetail);
		trx.setTransactionDetails(trxDetail); // ini buat nambahin data trxDetail ke trxnya, biar saat kita return, dia dapet. enggak null

		return trx;
	}
}
