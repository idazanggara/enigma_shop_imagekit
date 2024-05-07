package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}


// PR
// kan ada database WMB dan ERD WMB
// menu dan menu_price di lebur jadi satu, jadi relasinya bill_detail dengan menu
// m_discount dan customer_discount di hapus aja
// jadi nanti ada 6 table aja
// m_menu, m_table, t_bill, t_bill_detail, m_trans_type, m_customer
// silahkan buat API nya, mau disamakan seperti enigma_shop enggak papa, logic bebas kalian yg penting bisa melakukan transaksi
// kalau bisa semua pakai DTO aja semua, tapi kalau enggak bisa, minila di bill dan bill_detail untuk DTOnya
// deadlinenya setelah springboot selesai
// commit per fitur ya, biar bisa gw lihat progres kalian