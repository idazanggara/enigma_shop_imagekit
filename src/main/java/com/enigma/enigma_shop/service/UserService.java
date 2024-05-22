package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	// bedanya ini dengan AuthService apa?
	// UserService ini gunanya untuk mengambil informasi data UserAccount aja
	// kalau AuthService, itu gunanya untuk Authentication aja, Untuk Login dan Register
  // kita kosongin dulu

	UserAccount getByUserId(String id);


	UserAccount getByContext();
}
