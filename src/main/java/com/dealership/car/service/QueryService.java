package com.dealership.car.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to handle the execution of SQL queries.
 *
 * This service class is responsible for executing both SELECT and non-SELECT SQL queries.
 * It uses an EntityManager to interact with the database and provides transactional support.
 */
@Service
public class QueryService {

    /**
     * EntityManager instance for managing database operations.
     *
     * This EntityManager is injected by the JPA PersistenceContext and is used to create and
     * execute database queries, both SELECT and non-SELECT. It acts as a bridge between the
     * application and the underlying database.
     */
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Executes the provided SQL query and returns the result.
     *
     * This method supports both SELECT queries, returning a list of results,
     * and non-SELECT queries, returning the number of affected rows.
     *
     * @param queryStr the SQL query string to be executed
     * @return a list of results for SELECT queries, or the number of affected rows for non-SELECT queries
     */
    @Transactional
    public Object executeQuery(String queryStr){
        String trimmedQuery = queryStr.trim().toLowerCase();

        List<String> results = new ArrayList<>();

        if (trimmedQuery.startsWith("select")){
            Query query = entityManager.createNativeQuery(queryStr);
            List<Object[]> queryResults = query.getResultList();
            for (Object[] row : queryResults) {
                String result = Arrays.stream(row)
                        .map(obj -> obj != null ? obj.toString() : "null") 
                        .collect(Collectors.joining(", "));
                results.add(result);
            }
            return results;
        } else {
            Query query = entityManager.createNativeQuery(queryStr);
            int result = query.executeUpdate();
            return result;
        }
    }
}
