package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.repository.UserAccountRepository;
import com.enigma.enigma_shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserAccountRepository userAccountRepository;



	@Override
	// yang menangani proses login yg dikelola oleh AuthenticationManager
	// jadi kita enggak panggil method loadUserByUsername, secara spesifik guys,
	// tapi kita memangil AuthenticationManager, nah baru si AM ini yg memanggil secara langsung loadUserByUsername
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// logicnya simple, kita check di db aja, denngan mengambil berdasarkan UserName
		// karena enggak ada method mengambil data berdasrakan Username, jadi harus buat dulu

		return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));	}

	@Override
	public UserAccount getByUserId(String id) {
		return userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
	}

	@Override
	public UserAccount getByContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userAccountRepository.findByUsername(authentication.getPrincipal().toString())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
	}
}
