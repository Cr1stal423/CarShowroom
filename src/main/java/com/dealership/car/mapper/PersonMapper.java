package com.dealership.car.mapper;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.model.Person;
import org.springframework.stereotype.Component;

/**
 * PersonMapper is a component responsible for mapping between PersonDto and Person entities.
 *
 * Methods:
 * - toPerson(PersonDto personDto): Converts a PersonDto to a Person entity.
 * - updatePersonFromDto(PersonDto personDto, Person person): Updates an existing Person entity
 *   with the details from a PersonDto.
 */
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
    public void updatePersonFromDto(PersonDto personDto, Person person) {
        person.setPassword(personDto.getPassword());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setPassportSeries(personDto.getPassportSeries());
        person.setPassportNumber(personDto.getPassportNumber());
        person.setAddress(personDto.getAddress());
        person.setMobileNumber(personDto.getMobileNumber());
        person.setPassportSeries(personDto.getPassportSeries());
        person.setPassportNumber(personDto.getPassportNumber());
    }
}
