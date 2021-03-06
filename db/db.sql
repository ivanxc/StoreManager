CREATE TABLE IF NOT EXISTS product (
   id serial primary key,
   title varchar(255),
   price double precision,
   barcode integer,
   amount float,
   is_sold boolean default false
);


CREATE TABLE IF NOT EXISTS admission (
   id serial primary key,
   title varchar(255),
   admission_date timestamp default current_timestamp
);


CREATE TABLE IF NOT EXISTS admission_product (
   id serial primary key,
   product_id integer references product (id) ON DELETE CASCADE,
   admission_id integer references admission (id) ON DELETE CASCADE,
   amount float,
   price double precision
);


CREATE TABLE IF NOT EXISTS cashier (
   id serial primary key,
   name varchar(255)
);


CREATE TABLE IF NOT EXISTS checks (
   id serial primary key,
   cashier_id integer references cashier (id) ON DELETE CASCADE,
   price double precision,
   check_date timestamp default current_timestamp
);


CREATE TABLE IF NOT EXISTS selling (
   id serial primary key,
   product_id integer references product (id) ON DELETE CASCADE,
   check_id integer references checks (id) ON DELETE CASCADE,
   price double precision,
   amount float
);


CREATE INDEX product_title ON product(title);
CREATE INDEX product_barcode ON product(barcode);
