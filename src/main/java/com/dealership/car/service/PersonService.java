package com.dealership.car.service;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.mapper.PersonMapper;
import com.dealership.car.model.Keys;
import com.dealership.car.model.Person;
import com.dealership.car.model.Roles;
import com.dealership.car.repository.KeysRepository;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.RolesRepository;
import com.dealership.car.security.AuthentictionProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.w3c.dom.ls.LSInput;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service class for handling operations related to Person entities.
 */
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
    @Autowired
    private PersonMapper personMapper;

    /**
     * Creates a new user in the system with the given person details.
     *
     * @param personDto the Data Transfer Object containing the person's details
     * @return true if the user was successfully created, false otherwise
     */
    public boolean createNewUser(PersonDto personDto) {
        Person optionalPerson = personRepository.findByMobileNumber(personDto.getMobileNumber());
        if (optionalPerson != null) {
            return false;
        }
        Person person = personMapper.toPerson(personDto);
        Keys password = createPassword(person.getPassword());

        keysRepository.save(password);

        person.setKeys(password);
        person.setRoles(findDefaultUserRole());
        person.setCreatedAt(LocalDateTime.now());
        person.setCreatedBy(Constants.USER_ROLE);

        Person savedPerson = personRepository.save(person);

        return savedPerson != null && savedPerson.getPersonId() > 0;
    }

    /**
     * Creates a Keys object and sets its password.
     *
     * @param password the password to set in the Keys object.
     * @return a Keys object with the specified password set.
     */
    private Keys createPassword(String password) {
        Keys keys = new Keys();
        keys.setPassword(password);
        return keys;
    }

    /**
     * Finds the default user role by querying the roles repository using a predefined constant role name.
     *
     * @return the default user role as a {@link Roles} object
     */
    private Roles findDefaultUserRole() {
        return rolesRepository.findByRoleName(Constants.USER_ROLE);
    }

    /**
     * Method to retrieve the forgotten password for a user based on their username.
     *
     * @param username the username of the user who forgot their password
     * @return the password of the user if found, otherwise null
     */
    public String forgotPassword(String username) {
        String response = null;
        Person person = personRepository.findByUsername(username);
        if (person != null && person.getPersonId() > 0) {
            response = person.getKeys().getPassword();
        }
        return response;
    }

    /**
     * Changes the role of a user specified by their ID to the new role provided.
     * The method ensures a new transaction is initiated for this operation.
     *
     * @param id the ID of the user whose role is to be changed
     * @param newRoles the new role to be assigned to the user
     * @return true if the user's role was successfully changed, false otherwise
     */
    //TODO need to fix(can not commit jpa transaction)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    /**
     * Updates the details of an existing user identified by their username.
     * If the user is found, the user's details are updated with the provided information.
     *
     * @param username the username of the user to be updated
     * @param firstName the updated first name of the user
     * @param lastName the updated last name of the user
     * @param address the updated address of the user
     * @param mobileNumber the updated mobile number of the user
     * @return true if the user was found and updated successfully, false otherwise
     */
    public boolean updateUser(String username, String firstName, String lastName, String address, String mobileNumber) {
        boolean isSaved = false;
        Person optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson != null) {
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
            System.out.println(String.format("Can't find person "));
        }
        return isSaved;
    }

    /**
     * Retrieves a list of all Person entities from the repository.
     *
     * @return a list of all Person entities.
     */
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    /**
     * Deletes a user identified by the given ID from the repository.
     *
     * @param id the ID of the user to be deleted
     * @return true if the user was successfully deleted, false otherwise
     */
    public Boolean deleteUserById(Integer id) {
        Boolean isDeleted = false;
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            personRepository.delete(optionalPerson.get());
            isDeleted = true;
        }
        return isDeleted;
    }

    /**
     * Retrieves dynamic field values for a list of persons.
     *
     * @param personList the list of persons for whom to retrieve dynamic field values
     * @return a map where each key is a Person and the corresponding value is a list of DynamicFieldValue objects associated with that Person
     */
    public Map<Person, List<DynamicFieldValue>> getDynamicFieldsForAllPerson(List<Person> personList) {
        Map<Person, List<DynamicFieldValue>> personDynamicFieldMap = new HashMap<>();
        for (Person person : personList) {
            List<DynamicFieldValue> dynamicFieldValueList = dynamicFieldValueService.getAllDynamicValueForEntity(person.getPersonId(), "Person");
            if (!dynamicFieldValueList.isEmpty()) {
                personDynamicFieldMap.put(person, dynamicFieldValueList);
            } else {
                personDynamicFieldMap.put(person, new ArrayList<>());
            }
        }
        return personDynamicFieldMap;
    }
    /**
     * Retrieves the username of the currently authenticated user from the security context.
     *
     * @return the username of the currently authenticated user, or null if no user is authenticated
     */
    public String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails){
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }
        return null;
    }

    /**
     * Updates the details of a user based on the provided parameters.
     *
     * @param id the unique identifier of the person to update
     * @param username the new username of the person
     * @param firstName the new first name of the person
     * @param lastName the new last name of the person
     * @param address the new address of the person
     * @param mobileNumber the new mobile number of the person
     * @param passportSeries the new passport series of the person
     * @param passportNumber the new passport number of the person
     * @return true if the user's details were successfully updated; false otherwise
     */
    @Transactional
    public boolean updateroleA(Integer id, String username, String firstName, String lastName, String address, String mobileNumber, String passportSeries, String passportNumber) {
        Boolean isUpdated = false;
        Optional<Person> optionalPerson = personRepository.findById(id);
        System.out.println("Before update: " + optionalPerson.get());
        if (optionalPerson.isPresent()) {
            isUpdated = true;
            Person person = optionalPerson.get();
            person.setUsername(username);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setAddress(address);
            person.setMobileNumber(mobileNumber);
            person.setPassportSeries(passportSeries);
            person.setPassportNumber(passportNumber);
            personRepository.save(person);
            System.out.println("After update: " + person);
        } else {
            System.out.printf("Can't find person \n");
        }
        return isUpdated;
    }
}
