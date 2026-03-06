create table if not exists products (
    id bigint AUTO_INCREMENT primary key,
    name varchar(255) not null,
    price decimal(10, 2) not null
);