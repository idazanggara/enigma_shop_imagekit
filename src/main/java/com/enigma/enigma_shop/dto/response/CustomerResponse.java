package com.enigma.enigma_shop.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
	private String id;
	private String name;
	private String mobilePhoneNo;
	private String address;
	private Boolean status;
	private String userAccountId; // jadi disini yg kita berikan Idnya aja ke client, enggak usah detailnya
}
