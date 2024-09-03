package com.dealership.car;

import com.dealership.car.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
@EnableJpaAuditing(auditorAwareRef = "")
public class CarApplication  {
	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(CarApplication.class, args);

//		Product product = new Product();
//		TechnicalData technicalData = new TechnicalData();
//		technicalData.setSeats(4);
//		technicalData.setDoors(4);
//		technicalData.setEngineCapacity(4);
//		technicalData.setCreatedBy("User");
//		technicalData.setCreatedAt(LocalDateTime.now());
//		technicalData.setEnginePlacement(TechnicalData.EnginePlacement.FRONT);
//		technicalData.setEngineType(TechnicalData.EngineType.GASOLINE);
//		technicalData.setBodyType(TechnicalData.BodyType.SEDAN);
//
//		product.setPrice(100L);
//		product.setColor("white");
//		product.setModel("m4");
//		product.setBrand("BMW");
//		product.setAvailabilityStatus(Product.AvailabilityStatus.AVAILABLE);
//		product.setOriginCountry("Germany");
//		product.setTechnicalData(technicalData);


	}
//	public void save(TechnicalData technicalData, Product product){
//		product.setTechnicalData(technicalData);
//		technicalData.setProduct(product);
//		productRepository.save(product);
//		System.out.println("DONE");
//	}
//
//
//	@Override
//	public void run(String... args) throws Exception {
//		Product product = new Product();
//		TechnicalData technicalData = new TechnicalData();
//		technicalData.setSeats(4);
//		technicalData.setDoors(4);
//		technicalData.setEngineCapacity(4);
//		technicalData.setCreatedBy("User");
//		technicalData.setCreatedAt(LocalDateTime.now());
//		technicalData.setEnginePlacement(TechnicalData.EnginePlacement.FRONT);
//		technicalData.setEngineType(TechnicalData.EngineType.GASOLINE);
//		technicalData.setBodyType(TechnicalData.BodyType.SEDAN);
//
//		product.setPrice(100L);
//		product.setColor("white");
//		product.setModel("m4");
//		product.setBrand("BMW");
//		product.setAvailabilityStatus(Product.AvailabilityStatus.AVAILABLE);
//		product.setOriginCountry("Germany");
//		product.setTechnicalData(technicalData);
//
//		save(technicalData,product);
//
//
//	}
}
