package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import com.enigma.enigma_shop.constant.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ConstantTable.USER_ROOL)
@Builder
// 1 User Banyak Role
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	// ROLE bisa kita buat sebagai enum
	// kita buat enumnya dulu
	@Column(name = "role")
	@Enumerated(EnumType.STRING) // tanpa ini, nanti yg di generate interger, karena yg diambil ordinalnya nanti, yg mulai dari 0
	private UserRole role;
}
