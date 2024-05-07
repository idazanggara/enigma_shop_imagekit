package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository // ini optional, tapi kita kasih aja biar konsistent
@Transactional // dari spring framework // biar enggak set autoCommit true, makannya kita pakai transactional
// jadi ketika pakai native Query harus ada transactional
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer>  {
	// bisa multiple extentend ya, jadi kita bisa extend ke JpaSpecificationExecutor

	@Modifying // harus kasih anotasi ini untuk merubah data
	// query manual
	// nah kita buat querynya dimana? kita pakai anotasi query
	@Query(value = "UPDATE m_customer SET status = :status WHERE id = :id", nativeQuery = true)// nah ini yg kita kirimkan nativeQuery, kalau enggak kita state, nanti yg dibaca adalah HQL.
	void updateStatus(@Param("id") String id, @Param("status") Boolean status); // cara pakainya gimana?
	// tinggal panggil di service
}
