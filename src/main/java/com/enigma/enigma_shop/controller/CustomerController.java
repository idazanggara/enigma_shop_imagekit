package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.request.UpdateCustomerRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;
//    @PostMapping
//    public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer product) {
//        Customer customer = customerService.create(product);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(customer);
//    }
//    udah enggak ada lagi, karena kita create customer saat register

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getOneById(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully fetch data")
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }


    // methodnya
    // hasAnyRole() -> multi role
    // hasRole() -> single role
    // nah di constant nya kan ada ROLE_nya, tapi disini kita enggak perlu tambahkan
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer(
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
         List<CustomerResponse> customers = customerService.getAll(searchCustomerRequest);
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully fetch data")
                .data(customers)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody UpdateCustomerRequest payload) {
        CustomerResponse customer = customerService.update(payload);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully update data")
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<String>> deleteById(@PathVariable String id) {
        customerService.deleteById(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK Succes Delete Customer")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> UpdateStatus(
            @PathVariable String id,
            @RequestParam(name = "status") Boolean status){
        customerService.updateStatusById(id, status);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Oke success update status")
                .build();
        return ResponseEntity.ok(response);
    }

}
