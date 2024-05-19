package com.enigma.enigma_shop.security;

import com.enigma.enigma_shop.dto.response.JwtClaims;
import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.service.JwtService;
import com.enigma.enigma_shop.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // ada di kelelola spring
@RequiredArgsConstructor
@Slf4j // kita try catch kalau ada erro kita tampilkan terminal, soalnya nanti banyak pengecheckan jadi kalau error bisa kita lempar keterminal
public class AuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserService userService;
	final String AUTH_HEADER = "Authorization";



	@Override
	protected void doFilterInternal(
					HttpServletRequest request, // ini dari client
					HttpServletResponse response,// ini ketika kita kembalikan response
					FilterChain filterChain) throws ServletException, IOException {
		try {
			// ini untuk ngambil token dari Heaser dan keynya Authorization
//			String bearerToken = request.getHeader("Authorization");
			String bearerToken = request.getHeader(AUTH_HEADER);

			if (bearerToken != null && jwtService.verifyJwtToken(bearerToken)) {
				// kalau misalnya tokenya ada server akan menyimpan informasi user yg terverifikasi ke storage security context selama/sampai permintaan http selesai
				JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken); // di sini kita udah decode si jwt. dan kita dapat useridnya

				// ada enggak sebuah service yg mencari user berdasarkan id?
				UserAccount userAccount = userService.getByUserId(jwtClaims.getUserAccountId());

				// nah userAccount nya nanti kita simpan ke dalam SECURITY CONTExt
				// sama seperti proses login
				// tapi ini lebih ke verifikasi token, sebenernya ini proses Authorization
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userAccount.getUsername(), // pengecheck'an idnya
								null, // karena ini authtozation jadi enggak prlu password
								userAccount.getAuthorities() // untuk pengecheck'an rolernya
				);
				// lalu setelah proses authorization ngapain? kita simpan ke SECURITY CONTExt

				// bisa menyimpan informasi tambahan berupa IP address dll
				authentication.setDetails(new WebAuthenticationDetails(request)); // ini menyimpan detail informasi tambah berupa IP Addres dll, seprti ngehit httpnya menggunakan postman kah, menggunakan browser kah
				// tujuannya buat apa? kita bisa ngetrack siapa yg ngehit http kita


				// atau kalau di bahasa lain kalin bisa gunakan next
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}catch (Exception e) {
			log.error("Cannot set user authentication: {}", e.getMessage());
		}


		// mau error atau enggak, filter ini kita panggil
		// jasi setiap kita memanggil filter, dia akan melanjutkan ke controller
		// tujuannya "meneruskan request ke controller"
		filterChain.doFilter(request, response);
		// kalau filterChain do filter ini enggak kita panggil, dia akan stuck di filterChain aja

	}
}
