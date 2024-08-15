package com.dealership.car.service;

import com.dealership.car.constants.Constants;
import com.dealership.car.model.Contact;
import com.dealership.car.model.Person;
import com.dealership.car.repository.ContactRepository;
import com.dealership.car.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {

    private ContactRepository contactRepository;

    private PersonRepository personRepository;


    public boolean saveMsg(Contact contact) {
        boolean isSaved = false;
        try {
            contact.setCreatedAt(LocalDateTime.now());
            contact.setCreatedBy("User");
            contactRepository.save(contact);
            isSaved = true;
        } catch (Exception e){
            System.out.println("error with saving message");
            e.printStackTrace();
        }
        return isSaved;
    }
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
}
