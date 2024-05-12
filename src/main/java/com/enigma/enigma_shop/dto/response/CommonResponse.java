package com.enigma.enigma_shop.dto.response;

import com.enigma.enigma_shop.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse<T> {
	// atau sebutannya lain web response
	private Integer statusCode;
	private String message;
	private T data;
	private PagingResponse paging;
}

//class Test {
//	public static void main(String[] args) {
//		CommonResponse<Product> response = new CommonResponse<>();
//		response.setData(new Product());
//	}
//}

// sekarang common response di panggil dimana? di semua controller, bahkan controller error sekalipun
