package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANSACTION_DETAIL)
public class TransactionDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	// ManyToOne = TransactionDetail dengan Transaction
	@ManyToOne
	@JoinColumn(name = "trx_id", nullable = false) // ini untuk FK nya
	@JsonBackReference // ini yg handle stackoverflow, ini di taroh di Mto1
	private Transaction transaction;
	// ini masalah stackoverflow

	// ManyToOne = TransactionDetail dengan Product
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "product_price", updatable = false, nullable = false)
	private Long productPrice;

	@Column(name = "qty", nullable = false)
	private Integer qty;
}
