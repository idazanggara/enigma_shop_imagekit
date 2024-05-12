package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer product) {
        Customer customer = customerService.create(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customer);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Customer customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "mobilePhoneNo", required = false) String phoneNumber,
            @RequestParam(name = "birthDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String birthDate, // pakai json format untuk format datenya
            @RequestParam(name = "status", required = false) Boolean status
    ) {
//        return customerService.getAll(name, phoneNumber, birthDate, status); kalau kayak gini ada ketambahan data filtering lagi? bakan diubah enggak contraknya? interfacenya bakal diubah kan?
        // nah kita bisa enggak harus ubah interfacenya, biar lebih flexible kalian tau enggak gunain apa?
        // kita buat package dto
        // dengan kalian menambahkan DTO SearchCustomeRequest disini, kalian tidak perlu mengubah kontraknya kalau nanti ada ketambahan filtering data

        SearchCustomerRequest searchCustomerRequest = SearchCustomerRequest.builder()
                .name(name)
                .mobilePhoneNo(phoneNumber)
                .birthDate(birthDate)
                .status(status)
                .build(); // ini seperti keyword new
        List<Customer> customers = customerService.getAll(searchCustomerRequest);
        return ResponseEntity.ok(customers);
    }

    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer payload) {
        Customer customer = customerService.update(payload);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("OK Succes Delete Customer");
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> UpdateStatus(
            @PathVariable String id,
            @RequestParam(name = "status") Boolean status){
        customerService.updateStatusById(id, status);
        return ResponseEntity.ok("Oke success update status");
    }

}
