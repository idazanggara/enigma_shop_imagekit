package com.enigma.enigma_shop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
	private String username;
	private List<String> roles;
}

// CUman tipe data Rolenya kita enggak ambil dari Entity, karena gw enggak butuh ID nya
// tapi gw butuh stringnya, karena rolenya bisa banyak kan, lebih dari satu

