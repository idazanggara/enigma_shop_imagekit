package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.USER_ACCOUNT)
public class UserAccount implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "is_enable")
	private Boolean isEnable; // ini untuk akun kita aktif atau enggak


	// ini dibuat nanti aja, buat dulu entity Rolernya
	@ManyToMany(fetch = FetchType.EAGER)
	// untuk many to many ada 2 FetchType Eager dan Lazy
	private List<Role> role; // nah rolenya, kita harus buat relasi dan buat table baru ya,
	// relasinya many to many
	// karena many to many di bungkus List
	// kenapa di tempel disini aja, karena kita buat user langsung buat rolenya

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return null;
//	} // biar enggak error kasih anotasi @id untuk idnya
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.stream().map(role -> new SimpleGrantedAuthority(role.getRole().name())).toList();
		// ini role kita nanti di extends ke GrantedAuthority
		// role kita map, lalu kita ubah perRolenya. nah si GrantedAuthority ini interface, nah untuk implemetnya ada SimpleGrantedAuthority
		// jadi per rolenya kita ubah menjadi SimpleGrantedAuthority trus dia minta rolenya tapi cuman string, nah role kita kan masih onject ya, kita getrole, trus ambil stringnya dari .name, jangan lupa kita ubah ke list
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() { // kalau mau pakai email, nanti nama propertynya aja yg beda. kalau methodnya enggak usah, karena sudah disediakan seperti itu
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // ya bener ganti true
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// ini bisa buat otp, jadi pertama buat akun jadi false dulu, kalau sudah selesaikan otp beru jadi true
		return isEnable; // jadi nanti pertama buat akun kita true dulu karena belum menerapkan otp
	}
}
