package com.enigma.enigma_shop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerRequest {
	private String id;

	private String name;

	@Pattern(regexp = "^(?:\\+62|62|0)[2-9]\\d{7,11}$", message = "mobile phone is invalid")
	private String mobilePhoneNo;

	private String address;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private String birthDate;
}
