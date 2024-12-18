
CREATE TABLE IF NOT EXISTS `keys` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `password` varchar(50) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `roles` (
    `role_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_name` varchar(20) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL
);

START TRANSACTION;

CREATE TABLE IF NOT EXISTS `product` (
                                         `product_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         `origin_country` varchar(30) NOT NULL,
                                         `brand` varchar(20) NOT NULL,
                                         `model` varchar(40) NOT NULL,
                                         `color` varchar(30) NOT NULL,
                                         `availability_status` varchar(10) NOT NULL,
                                         `price` int NOT NULL,
                                         `technical_id` int NOT NULL,
                                         `created_at` TIMESTAMP NOT NULL,
                                         `created_by` varchar(50) NOT NULL,
                                         `updated_at` TIMESTAMP DEFAULT NULL,
                                         `updated_by` varchar(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `technical_data` (
                                                `technical_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                                `product_id` int NOT NULL,
                                                `seats` int NOT NULL,
                                                `body_type` varchar(10) NOT NULL,
                                                `doors` int NOT NULL,
                                                `engine_type` varchar(10) NOT NULL,
                                                `engine_placement` varchar(10) NOT NULL,
                                                `engine_capacity` int NOT NULL,
                                                `created_at` TIMESTAMP NOT NULL,
                                                `created_by` varchar(50) NOT NULL,
                                                `updated_at` TIMESTAMP DEFAULT NULL,
                                                `updated_by` varchar(50) DEFAULT NULL
);

ALTER TABLE `technical_data`
    ADD FOREIGN KEY (`product_id`) REFERENCES product(`product_id`);

ALTER TABLE `product`
    ADD FOREIGN KEY (`technical_id`) REFERENCES technical_data(`technical_id`);

COMMIT;



CREATE TABLE IF NOT EXISTS `technical_data` (
    `technical_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` int NOT NULL,
    `seats` int NOT NULL,
    `body_type` varchar(10) NOT NULL,
    `doors` int NOT NULL,
    `engine_type` varchar(10) NOT NULL,
    `engine_placement` varchar(10) NOT NULL,
    `engine_capacity` int NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    FOREIGN KEY (`product_id`) references product(`product_id`)

);



CREATE TABLE IF NOT EXISTS `product` (
    `product_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `origin_country` varchar(30) NOT NULL,
    `brand` varchar(20) NOT NULL,
    `model` varchar(40) NOT NULL,
    `color` varchar(30) NOT NULL,
    `availability_status` varchar(10) NOT NULL,
    `price` int NOT NULL,
    `technical_id` int NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    FOREIGN KEY (`technical_id`) REFERENCES technical_data(`technical_id`)
);

CREATE TABLE IF NOT EXISTS `person` (
    `person_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `orders_id` int DEFAULT NULL,
    `keys_id` int NOT NULL,
    `username` varchar(50) NOT NULL,
    `first_name` varchar(30) NOT NULL,
    `last_name` varchar(30) NOT NULL,
    `passport_series` varchar(30) NOT NULL,
    `passport_number` varchar(30) NOT NULL,
    `address` varchar(100) NOT NULL,
    `mobile_number` varchar(12) NOT NULL,
    `role_id` int NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    FOREIGN KEY (`role_id`) REFERENCES roles(`role_id`),
    FOREIGN KEY (`keys_id`) REFERENCES `keys`(`id`)
);
-- auto-generated definition
create table person
(
    person_id       int auto_increment
        primary key,
    created_at      datetime(6)  null,
    created_by      varchar(255) null,
    updated_at      datetime(6)  null,
    updated_by      varchar(255) null,
    address         varchar(255) null,
    first_name      varchar(255) null,
    last_name       varchar(255) null,
    mobile_number   varchar(255) null,
    passport_number varchar(255) null,
    passport_series varchar(255) null,
    username        varchar(255) null,
    keys_id         int          null,
    role_id         int          not null,
    constraint UKblkytayhlgyjt5xbhj0do1i4k
        unique (role_id),
    constraint UKe3vinlriuexogl3rne2cn0df7
        unique (keys_id),
    constraint FKepb1qy0539gyvgvnt0t1q3wty
        foreign key (keys_id) references `keys` (id)
);

CREATE TABLE IF NOT EXISTS `order_entity` (
    `order_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` int NOT NULL,
    `person_id` int NOT NULL,
    `order_time` TIMESTAMP NOT NULL,
    `delivery` TINYINT(1) NOT NULL,
    `payment_type` varchar(10) NOT NULL,
    `payment_method` varchar(5) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    FOREIGN KEY (`product_id`) REFERENCES product(`product_id`),
    FOREIGN KEY (`person_id`) REFERENCES person(`person_id`)
);

CREATE TABLE IF NOT EXISTS `contact` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `first_name` varchar(50) NOT NULL,
    `last_name` varchar(50) NOT NULL,
    `mobile_number` varchar(12) NOT NULL,
    `message` varchar(250) NOT NULL,
    `status` varchar(20) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL
);
