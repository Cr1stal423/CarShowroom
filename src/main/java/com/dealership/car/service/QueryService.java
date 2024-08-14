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

@Service
public class QueryService {

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public Object executeQuery(String queryStr){
        String trimmedQuery = queryStr.trim().toLowerCase();

        List<String> results = new ArrayList<>();

        if (trimmedQuery.startsWith("select")){
            Query query = entityManager.createNativeQuery(queryStr);
            List<Object[]> queryResults = query.getResultList();
            for (Object[] row : queryResults) {
                // Перетворюємо масив об'єктів в рядок, з'єднуючи значення через кому або інший роздільник
                String result = Arrays.stream(row)
                        .map(obj -> obj != null ? obj.toString() : "null") // Перевірка на null і заміна на порожній рядок
                        .collect(Collectors.joining(", ")); // З'єднуємо через кому
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
