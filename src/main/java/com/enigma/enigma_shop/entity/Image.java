package com.enigma.enigma_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_image")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "size", nullable = false)
	private Long size;

	@Column(name = "content_type", nullable = false)
	private String contentType;
	// "image/png", "image/jpeg"
	// ini extension nya, nah di contentType ini kalian nanti bisa kasih kondisi, yg boleh masuk hanya content
}
