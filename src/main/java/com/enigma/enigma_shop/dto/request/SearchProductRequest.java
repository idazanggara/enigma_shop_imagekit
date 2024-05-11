package com.enigma.enigma_shop.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchProductRequest {
	private Integer page;
	private Integer size;

	private String sortBy;
	private String direction;

	private String name;
}
