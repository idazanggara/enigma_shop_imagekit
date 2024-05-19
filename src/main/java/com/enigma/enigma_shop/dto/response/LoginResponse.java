package com.enigma.enigma_shop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
	private String username;
	private String token;
	private List<String> roles;
}

// ketika login kasih response berarti kan berhasil, nah disini dia ngembaliin token