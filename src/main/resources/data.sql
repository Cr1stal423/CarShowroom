INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('USER',CURDATE(),'OWNER');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('OWNER',CURDATE(),'OWNER');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('ADMIN',CURDATE(),'OWNER');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('OPERATOR',CURDATE(),'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('owner',CURDATE(),'OWNER');

INSERT INTO `keys` (password, created_at, created_by)
VALUES ('admin',CURDATE(),'OWNER');

INSERT INTO `person`
    (`keys_id`,`username`,`first_name`,last_name,`passport_series`,`passport_number`,`address`,`mobile_number`,`role_id`,`created_at`,`created_by`)
    VALUES (1,'owner','owner','owner','-','-','-','-',2,CURDATE(),'OWNER');

INSERT INTO `person`
(`keys_id`,`username`,`first_name`,last_name,`passport_series`,`passport_number`,`address`,`mobile_number`,`role_id`,`created_at`,`created_by`)
VALUES (2,'admin','admin','admin','-','-','-','-',3,CURDATE(),'OWNER');


delete from person where person_id = 2;

START TRANSACTION;

INSERT INTO `technical_data`
(product_id,seats, body_type, doors, engine_type, engine_placement, engine_capacity, created_at, created_by, updated_at, updated_by)
VALUES
    (1,2, 'SEDAN', 4, 'HYBRID', 'FRONT', 12, CURDATE(), 'OWNER', NULL, NULL);

INSERT INTO `product`
(origin_country, brand, model, color, availability_status, price, technical_id, created_at, created_by, updated_at, updated_by)
VALUES
    ('Germany', 'BMW', 'M4', 'blue', 'AVAILABLE', 80000, LAST_INSERT_ID(), CURDATE(), 'OWNER', NULL, NULL);

COMMIT;

delete from roles where role_id = 5

delete from person where person_id = 1;

delete from technical_data where technical_id =1;

select * from person where person_id = 1;