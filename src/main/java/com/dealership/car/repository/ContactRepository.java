package com.dealership.car.repository;

import com.dealership.car.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {

    public List<Contact> findByStatusEqualsOrStatus(String status1, String status2);
}
