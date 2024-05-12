package com.enigma.enigma_shop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCustomerRequest {
	private String name;

	@Pattern(regexp = "^(?:\\+62|62|0)[2-9]\\d{7,11}$", message = "mobile phone is invalid")
	private String mobilePhoneNo;

	private String address;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private String birthDate;
}
