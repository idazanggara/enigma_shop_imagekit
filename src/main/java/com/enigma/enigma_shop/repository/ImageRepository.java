package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// imagenya jangan salah import ya
public interface ImageRepository extends JpaRepository<Image, String> {
}
