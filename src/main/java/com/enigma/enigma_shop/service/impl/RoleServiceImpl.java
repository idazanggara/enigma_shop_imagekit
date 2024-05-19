package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.UserRole;
import com.enigma.enigma_shop.entity.Role;
import com.enigma.enigma_shop.repository.RoleRepository;
import com.enigma.enigma_shop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;

	@Override
	public Role getOrSave(UserRole role) {
		// gw pengen kalian buat logic disini
		// "jika ada di database, maka return/ambil, jika tidak aja maka save ke database
		// kalau bisa tambahkan method baru di repo"
		// silahkan kalian buat gw kasih waktu 20 menit, enggak di test/jalanin, ngoding aja



		return roleRepository.findByRole(role)
						.orElseGet(() -> roleRepository.saveAndFlush(Role.builder().role(role).build()));
		// nah disitu kita cari rolenya dengan findByRole, trus kita gunakan orElseGet, kan kembaliannya Optional kalau Optional kan kita ambil datanya menggunakan .get() kan. dengan orElseGet ini, dah otomatis guys
		// jadi logicnya kalau enggak ada atau or berarti kita buat Role baru dengan -> roleRepository.saveAndFlush
		//
	}
}

