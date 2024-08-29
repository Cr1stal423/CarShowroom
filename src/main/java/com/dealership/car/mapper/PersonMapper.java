package com.dealership.car.mapper;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.model.Person;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public Person toPerson(PersonDto personDto){
        Person person = new Person();
        person.setUsername(personDto.getUsername());
        person.setPassword(personDto.getPassword());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setPassportSeries(personDto.getPassportSeries());
        person.setPassportNumber(personDto.getPassportNumber());
        person.setAddress(personDto.getAddress());
        person.setMobileNumber(personDto.getMobileNumber());

        return person;
    }
}
