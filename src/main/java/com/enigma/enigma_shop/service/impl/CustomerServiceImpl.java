package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.repository.CustomerRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.specification.CustomerSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final EntityManager entityManager; // dan sudah di instance ya
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    public List<Customer> getAllAwal() {
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

        // query manual : SELECT * FROM m_customer
        // criteria dengan java yg dapat menghasilkan query seperti diatas

        /**
         * 3 Object Criteria yg diguankan
         * 1. Criteria Builder -> untuk membangun query expression (< , > , like, <>, !=) dan membangun (query-select, udpate, delete)
         * 2. Criteria (Query(select), Update, Delete) (Criteria Query) -> where, orderBy, having, groupBy, distinct, select, from, join, order
         * 3. Root -> mereprensentasikan dari entity (table)
         */

        // SELECT * FROM m_customer
        // pertama kita ambil Criteria Builder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // cara buatnya gimana?
        // ada juga criteriaUpdate() dan criteriaDelete()
        // argumernya adalah classnya yg kita buat querynya

        // ini baru query "SELECT" doang
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            // kalau ada yg tanya, kenapa butuh .select di bawah
            // CriteriaQuery<Customer> query = cb.createQuery(Customer.class).select(root); // kalau kayak gini kan rootnya baru di set di bawah

        // sekaranag udah FROM m_customer
        Root<Customer> root = criteriaQuery.from(Customer.class);
        // setelah itu baru kita panggail untuk melengkapi agar bisa di eksekusi

        // SELECT * FROM m_customer
        criteriaQuery.select(root);
        // jadi habis select bisa where dll

        // SELECT * FROM m_customer WHERE name LIKE %budiono%
//        criteriaQuery.where(
//                criteriaBuilder.like(
//                        root.get("name"), // ini tablenya
//                        "%" + "Budiono" + "%") // ini value nya
//        ); // ini belum pakai query param dulu

        // sekarang kita coba lebih dari satu
        // dan ini otomatis operasi and
//        criteriaQuery.where(
//                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + "budiono".toLowerCase() + "%"),
//                criteriaBuilder.equal(root.get("mobilePhoneNo"), "0856238491")
//        );
        // kalau mau or bisa seperti ini
//        criteriaQuery.where(
//                criteriaBuilder.or(
//                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + "siregar".toLowerCase() + "%"),
//                        criteriaBuilder.equal(root.get("mobilePhoneNo"), "0856238423")
//                )
//        );


        // kalau mau pakai query param
        // anggep ini query param

        String name = "siregar";
        String phone = "0856238423";
        // null kalau langsung di toLowerCase kan error

        // jadi kalian bisa melakukan validasi dulu

        // kalain bisa buat pricate
        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            predicates.add(namePredicate);
        }

        if (phone != null) {
            Predicate phonePredicate = criteriaBuilder.equal(root.get("mobilePhoneNo"), phone);
            predicates.add(phonePredicate);
        }

        criteriaQuery.where(
                criteriaBuilder.or(
                        // jadi predicatenya diubah menajdi array dengan toArray yg nanti bisa di baca oleh variable argument  dan kita kasih dia itu bentuknya class new Predicate itu untuk kasih tau tipe data arraynya itu apa.
                        // biasanya kan buat array itu seperti ini
                        // Predicate[] variable={}
                        predicates.toArray(new Predicate[]{})
                )
        );

        // SELECT * FROM m_customer
        //     List<Customer> resultList = entityManager.createQuery("FROM ").getResultList(); // HQL
        return entityManager.createQuery(criteriaQuery).getResultList();
//        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getAll(SearchCustomerRequest request) {
        // kalau kita findAll, ini bisa menerima paramter cuma enggak ada ada nih spesification
        // kita harus tambahkan extend dulu di repositorynya
        // nah kalau udah di extend baru dia ada tuh paramter specification
        // tinggal kita panggil specification dari customerspecification
        Specification<Customer> customerSpecification = CustomerSpecification.getSpecification(request);// karena dia static method jadi bisa langsung
        return customerRepository.findAll(customerSpecification);
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
        // artinya, kita findById, kalau enggak ada dilempar atau di Throw,
        // jadi kan sebenernya di bungkus sama optional dan kita pakai.get maka datanya menjadi Customer kayak kemaren. tapi dengan .orElseThrow kita juga gunakan .get ketika ada datanya dan otomatis di lempat throw ketika data null
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }


    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateStatus(id, status);

    }


}
