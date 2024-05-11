package com.enigma.enigma_shop.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder // nah ini lebih mudah dari pada kalian instance lagi
public class SearchCustomerRequest {
	private String name;
	private String mobilePhoneNo;
	private String birthDate;
	private Boolean status;

	// kalau cutomer mau di tambah pagination
	private Integer page;
	private Integer size;
}
