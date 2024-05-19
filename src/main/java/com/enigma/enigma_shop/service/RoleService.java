package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.constant.UserRole;
import com.enigma.enigma_shop.entity.Role;

public interface RoleService {
	Role getOrSave(UserRole role);
}

// maksud dari method getOrSave adalah ketika Rolenya ada di database gw mau GET, kalau enggak ada gw mau SAVE
// tapi yg mau gw savem gw mau ambil datanya dari enumnya,  jadi kita kasih UserRole di paramternya
// jadi pada saat RoleService di panggil, gw tinggal milih enumnya aja