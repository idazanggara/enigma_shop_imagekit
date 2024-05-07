package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository // ini optional, tapi kita kasih aja biar konsistent
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer>  {
	// bisa multiple extentend ya, jadi kita bisa extend ke JpaSpecificationExecutor
}
