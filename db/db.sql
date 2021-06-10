CREATE DATABASE lab2;


CREATE TABLE product (
   id serial primary key,
   title varchar(255),
   price double precision,
   barcode integer,
   amount integer
);


CREATE TABLE admission (
   id serial primary key,
   title varchar(255),
   admission_date timestamp default current_timestamp
);


CREATE TABLE admission_product (
   id serial primary key,
   product_id integer references product (id),
   admission_id integer references admission (id),
   amount integer,
   price double precision
);


CREATE TABLE cashier (
   id serial primary key,
   name varchar(255)
);


CREATE TABLE checks (
   id serial primary key,
   cashier_id integer references cashier (id),
   price double precision,
   check_date timestamp default current_timestamp
);


CREATE TABLE selling (
   id serial primary key,
   product_id integer references product (id),
   check_id integer references checks (id),
   price double precision,
   amount integer
);
