package com.dealership.car.service;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Contact;
import com.dealership.car.model.Person;
import com.dealership.car.repository.ContactRepository;
import com.dealership.car.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ContactService class is responsible for handling operations related to contacts.
 * It provides methods to save, close, and fetch dynamic fields associated with contacts.
 */
@Service
@AllArgsConstructor
public class ContactService {

    private final DynamicFieldValueService dynamicFieldValueService;
    private ContactRepository contactRepository;

    private PersonRepository personRepository;


    /**
     * Saves a contact message with additional details like creation time and creator info.
     * Checks if the user associated with the contact already exists and sets the contact status accordingly.
     *
     * @param contact The contact entity that contains the details of the message to be saved.
     * @return A boolean indicating whether the message was saved successfully.
     */
    public boolean saveMsg(Contact contact) {
        boolean isSaved = false;
        try {
            contact.setCreatedAt(LocalDateTime.now());
            contact.setCreatedBy("User");
            if (isUserAlreadyExist(contact)){
                contact.setStatus(Constants.USER_EXIST);
            }
            contactRepository.save(contact);
            isSaved = true;
        } catch (Exception e){
            System.out.println("error with saving message");
            e.printStackTrace();
        }
        return isSaved;
    }
    /**
     * Checks if a user with the same mobile number as the given contact already exists in the system.
     * Updates the status of the contact to either USER_EXIST or NEW_USER based on the existence.
     *
     * @param contact the contact object containing the mobile number to be checked
     * @return true if a user with the same mobile number exists, false otherwise
     */
    public boolean isUserAlreadyExist(Contact contact){
        boolean isExist = false;
        Person person = personRepository.findByMobileNumber(contact.getMobileNumber());
        if (person != null){
            isExist = true;
            contact.setStatus(Constants.USER_EXIST);
        }
        contact.setStatus(Constants.NEW_USER);
        return isExist;
    }

    /**
     * Closes a contact message by its identifier.
     * If the contact is found, its status is set to "Message closed" and the contact
     * is saved. If the contact is not found, an error message is printed.
     *
     * @param id the identifier of the contact message to be closed
     */
    public void closeMsg(int id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()){
            contact.get().setStatus(Constants.CLOSE_STATUS);
            contactRepository.save(contact.get());
        } else {
            //TODO add exceptionHandler
            System.out.println("Error");
        }
    }
    /**
     * Retrieves all dynamic field values associated with each contact in the provided list.
     *
     * @param contactList A list of Contact objects for which dynamic field values are to be retrieved.
     * @return A map where the key is a Contact object and the value is a list of DynamicFieldValue objects
     * containing the dynamic field values associated with that contact.
     */
    public Map<Contact, List<DynamicFieldValue>> getAllDynamicFieldsForContact(List<Contact> contactList){
        Map<Contact,List<DynamicFieldValue>> contactDynamicFieldMap = new HashMap<>();
        for (Contact contact : contactList){
            List<DynamicFieldValue> dynamicFieldValueList =
                    dynamicFieldValueService.getAllDynamicValueForEntity(contact.getId(),"Contact");
            if (!dynamicFieldValueList.isEmpty()){
                contactDynamicFieldMap.put(contact,dynamicFieldValueList);
            } else {
                contactDynamicFieldMap.put(contact,new ArrayList<>());
            }
        }
        return contactDynamicFieldMap;
    }
}
