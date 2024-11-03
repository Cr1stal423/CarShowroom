-- Вставка ролей
INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('USER', CURDATE(), 'OWNER');

INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('OWNER', CURDATE(), 'OWNER');

INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('ADMIN', CURDATE(), 'OWNER');

INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('OPERATOR', CURDATE(), 'OWNER');

INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('USER', CURDATE(), 'OWNER');

INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('USER', CURDATE(), 'OWNER');

INSERT INTO `roles` (`role_name`, `created_at`, `created_by`)
VALUES ('USER', CURDATE(), 'OWNER');

-- Вставка ключів
INSERT INTO `keys` (password, created_at, created_by)
VALUES ('owner', CURDATE(), 'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('admin', CURDATE(), 'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('password1', CURDATE(), 'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('password2', CURDATE(), 'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('password3', CURDATE(), 'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('password4', CURDATE(), 'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('password5', CURDATE(), 'OWNER');

-- Вставка персон
INSERT INTO `person`
(`keys_id`, `username`, `first_name`, last_name, `passport_series`, `passport_number`, `address`, `mobile_number`, `role_id`, `created_at`, `created_by`)
VALUES
    (1, 'owner', 'owner', 'owner', '-', '-', '-', '-', 2, CURDATE(), 'OWNER'),
    (2, 'admin', 'admin', 'admin', '-', '-', '-', '-', 3, CURDATE(), 'OWNER'),
    (3, 'user1', 'test1', 'user1', '1234567890', '2341756089', 'userAddress1', '0509138089', 1, CURDATE(), 'OWNER'),
    (4, 'user2', 'test2', 'user2', '5467321890', '6343756089', 'userAddress2', '0662982747', 4, CURDATE(), 'OWNER'),
    (5, 'user3', 'test3', 'user3', '9876543210', '1234567890', 'userAddress3', '0501234567', 5, CURDATE(), 'OWNER'),
    (6, 'user4', 'test4', 'user4', '4567891230', '9876543210', 'userAddress4', '0661234567', 6, CURDATE(), 'OWNER'),
    (7, 'user4', 'test4', 'user4', '4567891230', '9876543210', 'userAddress4', '0661234567', 7, CURDATE(), 'OWNER');

INSERT INTO `database`.`supplier`(`created_at`,`created_by`,`is_delayed`,`name`)
VALUES (CURDATE(),'OWNER',1,'Supplier1');

INSERT INTO `database`.`supplier`(`created_at`,`created_by`,`is_delayed`,`name`)
VALUES (CURDATE(),'OWNER',1,'Supplier2');

INSERT INTO `database`.`supplier`(`created_at`,`created_by`,`is_delayed`,`name`)
VALUES (CURDATE(),'OWNER',0,'Supplier3');



INSERT INTO `database`.technical_data(`created_at`,`created_by`,`body_type`,`doors`,`engine_capacity`,`engine_placement`,`engine_type`,`seats`)
VALUES (CURDATE(),'OWNER','SEDAN',4,4.4,'FRONT','GASOLINE',4);

INSERT INTO `database`.technical_data(`created_at`,`created_by`,`body_type`,`doors`,`engine_capacity`,`engine_placement`,`engine_type`,`seats`)
VALUES (CURDATE(),'OWNER','COUPE',2,3.0,'FRONT','GASOLINE',4);

INSERT INTO `database`.technical_data(`created_at`,`created_by`,`body_type`,`doors`,`engine_capacity`,`engine_placement`,`engine_type`,`seats`)
VALUES (CURDATE(),'OWNER','SUV',4,4.0,'FRONT','GASOLINE',4);

INSERT INTO `database`.technical_data(`created_at`,`created_by`,`body_type`,`doors`,`engine_capacity`,`engine_placement`,`engine_type`,`seats`)
VALUES (CURDATE(),'OWNER','SEDAN',4,3.6,'FRONT','GASOLINE',4);

INSERT INTO `database`.technical_data(`created_at`,`created_by`,`body_type`,`doors`,`engine_capacity`,`engine_placement`,`engine_type`,`seats`)
VALUES
    (CURDATE(),'OWNER','HATCHBACK',4,1.5,'FRONT','DIESEL',5),
    (CURDATE(),'OWNER','SEDAN',4,2.0,'FRONT','GASOLINE',5),
    (CURDATE(),'OWNER','HATCHBACK',4,2.5,'FRONT','GASOLINE',5),
    (CURDATE(),'OWNER','VAN',5,3.5,'FRONT','DIESEL',7),
    (CURDATE(),'OWNER','SEDAN',2,3.0,'FRONT','GASOLINE',2),
    (CURDATE(),'OWNER','CONVERTIBLE',2,2.2,'FRONT','GASOLINE',4),
    (CURDATE(),'OWNER','SUV',5,2.4,'FRONT','DIESEL',7),
    (CURDATE(),'OWNER','SUV',4,2.8,'FRONT','GASOLINE',5),
    (CURDATE(),'OWNER','WAGON',5,2.0,'FRONT','GASOLINE',5),
    (CURDATE(),'OWNER','COUPE',2,3.5,'FRONT','GASOLINE',4);





INSERT INTO `database`.`product` (`created_at`, `created_by`, `availability_status`, `brand`, `color`, `model`, `origin_country`, `price`, `technical_id`)
VALUES (CURDATE(), 'owner', 'AVAILABLE', 'BMW', 'Blue', 'M5', 'Germany', 90000, 1);

INSERT INTO `database`.`product` (`created_at`, `created_by`, `availability_status`, `brand`, `color`, `model`, `origin_country`, `price`, `technical_id`)
VALUES (CURDATE(), 'owner', 'AVAILABLE', 'BMW', 'Black', 'M2', 'Germany', 50000, 2);

INSERT INTO `database`.`product` (`created_at`, `created_by`, `availability_status`, `brand`, `color`, `model`, `origin_country`, `price`, `technical_id`)
VALUES (CURDATE(), 'owner', 'AVAILABLE', 'BMW', 'White', 'X5M', 'Germany', 80000, 3);

INSERT INTO `database`.`product` (`created_at`, `created_by`, `availability_status`, `brand`, `color`, `model`, `origin_country`, `price`, `technical_id`)
VALUES (CURDATE(), 'owner', 'AVAILABLE', 'BMW', 'White', 'M4', 'Germany', 75000, 4);

INSERT INTO `database`.`product` (`created_at`, `created_by`, `availability_status`, `brand`, `color`, `model`, `origin_country`, `price`, `technical_id`)
VALUES
    (CURDATE(), 'owner', 'AVAILABLE', 'Toyota', 'Red', 'Camry', 'Japan', 30000, 5),
    (CURDATE(), 'owner', 'AVAILABLE', 'Honda', 'Silver', 'Civic', 'Japan', 25000, 6),
    (CURDATE(), 'owner', 'AVAILABLE', 'Ford', 'Blue', 'Mustang', 'USA', 55000, 7),
    (CURDATE(), 'owner', 'AVAILABLE', 'Chevrolet', 'Black', 'Silverado', 'USA', 60000, 8),
    (CURDATE(), 'owner', 'AVAILABLE', 'Nissan', 'White', 'Altima', 'Japan', 27000, 9),
    (CURDATE(), 'owner', 'AVAILABLE', 'Subaru', 'Green', 'Outback', 'Japan', 32000, 10),
    (CURDATE(), 'owner', 'AVAILABLE', 'Volkswagen', 'Yellow', 'Golf', 'Germany', 22000, 11),
    (CURDATE(), 'owner', 'AVAILABLE', 'Mazda', 'Purple', 'CX-5', 'Japan', 35000, 12),
    (CURDATE(), 'owner', 'AVAILABLE', 'Hyundai', 'Orange', 'Elantra', 'South Korea', 21000, 13),
    (CURDATE(), 'owner', 'AVAILABLE', 'Kia', 'Brown', 'Sportage', 'South Korea', 23000, 14);





INSERT INTO `database`.`order_entity` (created_at, created_by, delivery, order_time, payment_method, payment_type, person_id, product_id, supplier_id)
VALUES (curdate(), 'OWNER', 1, curdate(), 'CASH', 'BUY', 3, 1, 1);





delete from person where person_id = 2;

START TRANSACTION;

INSERT INTO `technical_data`
(seats, body_type, doors, engine_type, engine_placement, engine_capacity, created_at, created_by, updated_at, updated_by)
VALUES
    (2, 'SEDAN', 4, 'HYBRID', 'FRONT', 12, CURDATE(), 'OWNER', NULL, NULL);

INSERT INTO `product`
(origin_country, brand, model, color, availability_status, price, technical_id, created_at, created_by, updated_at, updated_by)
VALUES
    ('Germany', 'BMW', 'M4', 'blue', 'AVAILABLE', 80000, LAST_INSERT_ID(), CURDATE(), 'OWNER', NULL, NULL);

COMMIT;

delete from roles where role_id = 5;

delete from person where person_id = 1;

delete from technical_data where technical_id =1;

select * from person where person_id = 1;