package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.repository.CustomerRepository;
import com.enigma.enigma_shop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Customer> getAll() {
        // buat filter data berdasarkan
        /**
         * Menggunakan Query Param
         * 1. Name
         * 2. Mobile Phone
         * 3. Birth Date
         * 4. Status
         *
         * untuk operasinya and atau or
         *1.  jika nama yg di cari ada, ya dapet namanya
         *2.  cuman kalau namanya enggak sesuai, tapi mobile phonenya sesuai,
         * yaudah sesuai dengan mobile phone nya
         *
         *
         * Materi Besok
         * 1. Transaksi
         * 2. Specification/Criteria
         * */
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void deleteById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
}
