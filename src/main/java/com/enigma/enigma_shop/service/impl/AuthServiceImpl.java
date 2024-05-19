package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.UserRole;
import com.enigma.enigma_shop.dto.request.AuthRequest;
import com.enigma.enigma_shop.dto.response.LoginResponse;
import com.enigma.enigma_shop.dto.response.RegisterResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.entity.Role;
import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.repository.UserAccountRepository;
import com.enigma.enigma_shop.service.AuthService;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.service.JwtService;
import com.enigma.enigma_shop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserAccountRepository userAccountRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	// tambah customer ketika register
	private final CustomerService customerService;
	// untuk ambil token
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Override
	public RegisterResponse register(AuthRequest request) throws DataIntegrityViolationException {
//		userAccountRepository.saveAndFlush(new UserAccount());

		// nah register ini, untuk Regis Cutomer kan?
		Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
		String hashPassword = passwordEncoder.encode(request.getPassword());
		UserAccount account = UserAccount.builder()
						.username(request.getUsername())
						.password(hashPassword)
						.role(List.of(role))
						.isEnable(true) // kita kasih true aja, tapi kalian mau improve tambahin OTP silahkan
						.build();
		userAccountRepository.saveAndFlush(account);

		Customer customer = Customer.builder()
						.status(true)
						.userAccount(account)
						.build();
		customerService.create(customer);


		List<String> roles = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

		return RegisterResponse.builder()
						.username(account.getUsername())
//						bisa enggak kita masukkin account.getRole? enggak ya, dia malah dapet getAuthorities(), jadi autorities dia dapetnya, bukan list of role lagi
//						gimana caranya? sedangkan yg di minta RegisterResponse adalah string?
//						kalau kita lihat ke UserAccount Entity si rolenya kita retunt getAuthorities bukan Role lagi
//						si getAuthorities ini kita map, kita ubah dari grantedAuthority ke bentuk string di jadikan toList()

//						.roles(account.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()). toList())
//						.roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
						.roles(roles)
						.build();
	}

	@Override
	public RegisterResponse registerAdmin(AuthRequest request) {
		return null;
	}

	@Override
	public LoginResponse login(AuthRequest request) {
		// untuk pengetest'an apakah bisa buat token atau enggak?
//		String token = jwtService.generateToken("nanti User Account taroh disini");
//		return LoginResponse.builder()
//						.token(token)
//						.build();

		// disini bisa kita panggil UserAccount? dan UserAccount bisa taroh jadi argument di bawah enggak?

		// panggil ini dulu
//		authenticationManager.authenticate(nah  disini butuh Authentication authentication)

		// nah yg ini belum terauthentikasi
		Authentication authentication = new UsernamePasswordAuthenticationToken(
						// nah disini ada dua param yg harus kita isi
						// si principal adalah username dan credentials adalah password
						request.getUsername(),
						request.getPassword()
		);

		// ini adalah Authentication baru, yg sudah terauthentikasi
		Authentication authenticate = authenticationManager.authenticate(authentication);

		// nah si authenticate ini, kalau kita ambil getPriciplenya
		UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
		String token = jwtService.generateToken(userAccount);
		return LoginResponse.builder()
						.token(token)
						.username(userAccount.getUsername())
						.roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
						.build();
	}
}
