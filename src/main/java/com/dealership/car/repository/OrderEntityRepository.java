package com.dealership.car.repository;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer> {

    public List<OrderEntity> findByPerson(Person person);

    public Optional<OrderEntity> findByProduct(Product product);

    public List<OrderEntity> findByPerson_Username(String username);

    public List<OrderEntity> findByPaymentType(OrderEntity.PaymentType paymentType);
    @Query("SELECT DISTINCT o.paymentType FROM OrderEntity o")
    public List<String> findAllUniquePaymentTypes();

    public List<OrderEntity> findByCreatedBy(String username);

    List<OrderEntity> findAllByPersonIn(List<Person> workers);

    @Query(value = """
SELECT 
    t.year,
    t.quarter,
    t.brand,
    t.model,
    t.color,
    SUM(t.sales_count) AS total_sales_count
FROM (
    SELECT 
        EXTRACT(YEAR FROM o.created_at) AS year,
        EXTRACT(QUARTER FROM o.created_at) AS quarter,
        p.brand,
        p.model,
        p.color,
        COUNT(o.order_id) AS sales_count
    FROM 
        order_entity o
    JOIN 
        product p ON o.product_id = p.product_id
    GROUP BY 
        year, quarter, p.brand, p.model, p.color
) t
GROUP BY 
    t.year, t.quarter, t.brand, t.model, t.color
ORDER BY 
    total_sales_count DESC
LIMIT 10;
""", nativeQuery = true)
    List<Object[]> findTopSellingCarsPerQuarter();
}
