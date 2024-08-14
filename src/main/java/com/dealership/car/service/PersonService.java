package com.dealership.car.service;

import com.dealership.car.constants.Constants;
import com.dealership.car.model.Keys;
import com.dealership.car.model.Person;
import com.dealership.car.model.Roles;
import com.dealership.car.repository.KeysRepository;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private KeysRepository keysRepository;

    public boolean createNewUser(Person person) {
        Keys password = createPassword(person.getPassword());
        password.setCreatedAt(LocalDateTime.now());
        password.setCreatedBy(Constants.USER_ROLE);

        keysRepository.save(password);

        person.setKeys(password);
        person.setRoles(findDefaultUserRole());
        person.setCreatedAt(LocalDateTime.now());
        person.setCreatedBy(Constants.USER_ROLE);

        Person savedPerson = personRepository.save(person);

        return savedPerson != null && savedPerson.getPersonId() > 0;
    }

    private Keys createPassword(String password) {
        Keys keys = new Keys();
        keys.setPassword(password);
        return keys;
    }

    private Roles findDefaultUserRole() {
        return rolesRepository.findByRoleName(Constants.USER_ROLE);
    }

    public String forgotPassword(String username){
        String response = null;
        Person person = personRepository.findByUsername(username);
        if (person != null && person.getPersonId() > 0) {
            response = person.getKeys().getPassword();
        }
        return response;
    }
    //TODO need to fix(can not commit jpa transaction)
    @Transactional
    public boolean changeUserRole(int id, String newRoles) {
        boolean isUpdated = false;
        try {
            Optional<Person> optionalPerson = personRepository.findById(id);
            Roles role = rolesRepository.findByRoleName(newRoles);
            if (optionalPerson.isPresent() && role != null) {
                Person person = optionalPerson.get();
                person.setRoles(role);
                personRepository.save(person);
                System.out.println("Updated role for person with ID " + id + " to " + newRoles);
                isUpdated = true;
            } else {
                System.out.println("Person not found or Role not available");
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
//            e.printStackTrace();

        }
        return isUpdated;
    }

}
