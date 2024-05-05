package com.enigma.enigma_shop;

import com.enigma.enigma_shop.entity.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController // untuk membuat sebuah API
public class EnigmaShopApplication1File {
    public static void main(String[] args) {
        SpringApplication.run(EnigmaShopApplication.class, args);
    }
    // sekarang kita buat endpind

    // ini cara terbaru
    // ini 2 sama aja, path atau value. tapi secara default yg keisi value
    // cosume = itu dari client ke server, maksudnya server kalian mengkonsumsi data apa?
    // produces = ini adalah dari server kita, kita mau mengembalikan data apa ke clinet, kalau disni data json ata  kembalian dari method kita, atau return
    // MediaType dari org.spring
    @GetMapping(path = "/hello-world", produces = MediaType.APPLICATION_JSON_VALUE)
//    @GetMapping(value = "/hello-world")
//    @GetMapping("/hello-world") // ini bisa dan yg keisi value, tapi ini kalau satu aja
    // cara lamanya pakai apa?
//    @RequestMapping(method = RequestMethod.GET, path = "/hello-world")
    public  String helloWorld() {
//        return "Hello World";
        return "<h1>Hello World</h1>"; // nah ini kan returnya string
        // tadi kan @restcontroller membantu mengubak json jadi object
    }

    @GetMapping(path = "/products", produces = "application/json")
    public Map<String, Object> getProduct() {
        return Map.of(
                "id", "1",
                "name", "Apel",
                "price", 3000
        );
    }

    // request/query param
    // domain.com/page?key1=value1&key2=value2
    // seperti ini : localhost:8080/menus?name=....&maxPrice=....
    // http://localhost:8080/menus?name=pentol&maxPrice=10000
    @GetMapping(path ="/menus")
    public List<Map<String, Object>> getMenusFilter(
            @RequestParam(name = "name", required = false) String nama,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice){
        Map<String, Object> menu = Map.of(
                "id" , 1,
                "name" , ""+nama,
                "price", ""+maxPrice
        );
        List<Map<String,Object>> menus = new ArrayList<>();
        menus.add(menu);
        return menus;
    }

    // path variable
    // gunanya untuk apa? mencari data satuan, membuat data detail
    // boleh id / menuId, tapi jangan pakai :
    @GetMapping(path = "/menus/{id}")
    // id dengan parameter id harus sama
    // kalau mau beda kita perlu tambahkan property nama seperti ini @PathVariable(name = "id")
    public String getMenuById(@PathVariable(name = "id") String menuId) {
//        return  "Product " + id;
        return "Product " + menuId;
    }

    // untuk post kan kita butuh object untuk dikirim, jadi buat enitity deh
    @PostMapping(
            path = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Product createNewProduct(@RequestBody Product product){
        return product;
    }
}
