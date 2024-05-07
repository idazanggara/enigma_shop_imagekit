package com.enigma.enigma_shop.specification;

import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CustomerSpecification {
	// T nya kiita kasih ke entity apa yg kita lakukan select
	public static Specification<Customer> getSpecification(SearchCustomerRequest request) {
		// langsung aja return new Spec nya
		// ini specification kan sebuah interface, bisa enggak kita singkat lagi? bisa ya pakai lamda
		// tapi kalau kita liha, ini udah ada semua, dari root, CriteriaQuery, CriteriaBuilder. jadi enggak perlu kita enggak perlu EnitityManager lagi
//		@Override
//		public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//			return null;
//		}
		return (root, query, criteriaBuilder) -> {
			/**
			 * 3 Object Criteria yg diguankan
			 * 1. Criteria Builder -> untuk membangun query expression (< , > , like, <>, !=) dan membangun (query-select, udpate, delete)
			 * 2. Criteria (Query(select), Update, Delete) (Criteria Query) -> where, orderBy, having, groupBy, distinct, select, from, join, order
			 * 3. Root -> mereprensentasikan dari entity (table)
			 */

//			String name = "siregar";
//			String phone = "0856238423";

			List<Predicate> predicates = new ArrayList<>();
			if (request.getName() != null) {
				Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
				predicates.add(namePredicate);
			}

			if (request.getMobilePhoneNo() != null) {
				Predicate phonePredicate = criteriaBuilder.equal(root.get("mobilePhoneNo"), request.getMobilePhoneNo());
				predicates.add(phonePredicate);
			}

			if (request.getBirthDate() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
				Date tempDate = new Date();
				try {
					tempDate = sdf.parse(request.getBirthDate());
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
				Predicate birthDatePredicate = criteriaBuilder.equal(root.get("birthDate"), tempDate);
				predicates.add(birthDatePredicate);
			}

			if (request.getStatus() != null) {
				Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), request.getStatus());
				predicates.add(statusPredicate);
			}


			//where ini kan masih criteriaQuery, gimana cara ubah jadi Predicate, sedangkan return yg diminta adalah predicate
			if (request.getName() == null && request.getMobilePhoneNo() == null && request.getBirthDate() == null && request.getStatus() == null){
				return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
			}

//			return query.where(predicates.toArray(new Predicate[]{})).getRestriction(); // nah getRestriction ini yg ubah dia menjadi predicate
			return query.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{}))).getRestriction();
		};


	}
}
