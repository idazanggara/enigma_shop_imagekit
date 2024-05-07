package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping
    public Customer createNewCustomer(@RequestBody Customer product) {
        return customerService.create(product);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    @GetMapping
    public List<Customer> getAllCustomer(
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
        return customerService.getAll(searchCustomerRequest);
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer product) {
        return customerService.update(product);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public String deleteById(@PathVariable String id) {
        customerService.deleteById(id);
        return "OK Succes Delete Customer";
    }
}
