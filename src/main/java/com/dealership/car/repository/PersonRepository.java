package com.dealership.car.repository;

import com.dealership.car.constants.Constants;
import com.dealership.car.model.Person;
import com.dealership.car.model.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person,Integer> {
    @Transactional
    public Person findByUsername(String username);
    @Transactional
    @Query("SELECT p FROM Person p JOIN p.roles r WHERE r.roleName = :roleName")
    public List<Person> findByRoles(String roleName);

    public Person findByMobileNumber(String number);

    @Transactional
    @Query("SELECT DISTINCT p FROM Person p JOIN p.roles r WHERE r.roleName IN :roleNames")
    public List<Person> findByRoles(@Param("roleNames") List<String> roleNames);
}
