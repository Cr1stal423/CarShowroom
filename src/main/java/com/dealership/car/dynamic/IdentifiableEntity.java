package com.dealership.car.dynamic;

/**
 * Interface to represent an entity with an identifier.
 * Classes implementing this interface should provide an implementation
 * for retrieving the unique identifier of the entity.
 */
public interface IdentifiableEntity {
    Integer getId();
}
