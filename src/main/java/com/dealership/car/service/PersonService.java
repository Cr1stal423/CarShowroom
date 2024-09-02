package com.dealership.car.service;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Keys;
import com.dealership.car.model.Person;
import com.dealership.car.model.Roles;
import com.dealership.car.repository.KeysRepository;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.RolesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private KeysRepository keysRepository;
    @Autowired
    private DynamicFieldValueService dynamicFieldValueService;

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
                if (!optionalPerson.isPresent()) {
                    System.out.println("Person with ID " + id + " not found");
                }
                if (role == null) {
                    System.out.println("Role with name " + newRoles + " not available");
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Entity not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return isUpdated;
    }

    public boolean updateUser(String username, String firstName, String lastName, String address, String mobileNumber) {
        boolean isSaved = false;
        Person optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson != null){
            isSaved = true;
            Person person = optionalPerson;
//            String password = person.getPassword();
//            person.setPassword(password);
            person.setUsername(username);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setAddress(address);
            person.setMobileNumber(mobileNumber);
            personRepository.save(person);
        } else {
            System.out.println(String.format("Can't find person ") );
        }
        return isSaved;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Boolean deleteUserById(Integer id){
        Boolean isDeleted = false;
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()){
            personRepository.delete(optionalPerson.get());
            isDeleted = true;
        }
        return isDeleted;
    }
    public Map<Person,List<DynamicFieldValue>> getDynamicFieldsForAllPerson(List<Person> personList){
        Map<Person, List<DynamicFieldValue>> personDynamicFieldMap = new HashMap<>();
        for (Person person : personList){
            List<DynamicFieldValue> dynamicFieldValueList = dynamicFieldValueService.getAllDynamicValueForEntity(person.getPersonId(),"Person");
            if (!dynamicFieldValueList.isEmpty()){
                personDynamicFieldMap.put(person,dynamicFieldValueList);
            } else {
                personDynamicFieldMap.put(person,new ArrayList<>());
            }
        }
        return personDynamicFieldMap;
    }
}
