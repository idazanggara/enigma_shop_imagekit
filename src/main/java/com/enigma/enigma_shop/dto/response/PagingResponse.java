package com.enigma.enigma_shop.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingResponse {
	private Integer totalPages;
	private Long totalElement;
	private Integer page;
	private Integer size;
	private Boolean hasNext;
	private Boolean hasPrevious;
}
