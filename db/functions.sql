CREATE OR REPLACE FUNCTION add_products(IN titles varchar(255)[],
					                    IN prices double precision[],
					                    IN barcodes integer[])
RETURNS void AS
$$
BEGIN
   INSERT INTO product (title, price, barcode) SELECT UNNEST(titles), UNNEST(prices), UNNEST (barcodes);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_admission(IN title varchar(255))
RETURNS void AS
$$
BEGIN
   INSERT INTO admission (title) values (title);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_admission_product(IN product_id integer,
						                         IN admission_id integer,
						                         IN price double precision,
						                         IN amount float)
RETURNS void AS
$$
BEGIN
   INSERT INTO admission_product (product_id, admission_id, price, amount)
                                 values (product_id, admission_id, price, amount);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_all_products()
RETURNS TABLE (id integer,
               title varchar(255),
               amount float,
               price double precision,
               barcode integer) AS
$$
BEGIN
   RETURN QUERY(SELECT * FROM product);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_product_price(IN id integer, IN new_price integer)
RETURNS void AS
$$
BEGIN
   UPDATE product SET price = $2 WHERE product.id = $1;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_product_amount(IN id integer, IN additional_amount float)
RETURNS void AS
$$
BEGIN
   UPDATE product SET amount = amount + $2 WHERE product.id = $1;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION change_product_amount(IN id integer, IN new_amount integer)
RETURNS void AS
$$
BEGIN
   UPDATE product SET amount = $2 WHERE product.id = $1;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_cashier(IN name varchar(255))
RETURNS void AS
$$
BEGIN
   INSERT INTO cashier (name) VALUES ($1);
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION add_selling(IN product_id integer,
                                       IN check_id integer,
                                       IN amount float,
                                       IN price double precision)
RETURNS void AS
$$
BEGIN
   INSERT INTO selling (product_id, check_id, amount, price) VALUES ($1, $2, $3, $4);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_check(IN price double precision, IN cashier_id integer)
RETURNS void AS
$$
BEGIN
   INSERT INTO checks (price, cashier_id) VALUES ($1, $2);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_checks_from_period(IN top_period varchar(255), low_period varchar(255) DEFAULT '')
RETURNS TABLE (id integer, cashier_id integer, price double precision, check_date timestamp) AS
$$
BEGIN
   IF $2 = '' THEN
       RETURN QUERY (SELECT * FROM checks WHERE date(checks.check_date) <= $1::date);
   ELSE
       RETURN QUERY (SELECT * FROM checks WHERE date(checks.check_date) <= $1::date AND date(checks.check_date) >= $2::date);
   END IF;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION sell_product_by_id(IN id integer)
RETURNS void AS
$$
BEGIN
     UPDATE product SET is_sold = true WHERE product.id = $1;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION clear_table(IN table_name varchar(255))
RETURNS void AS
$$
DECLARE
   destination varchar;
BEGIN
   SELECT setval(table_name || '_id_' || 'seq', 1, FALSE)::varchar INTO destination;
   EXECUTE 'TRUNCATE ' || table_name || ' CASCADE';
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION clear_all()
RETURNS void AS
$$
DECLARE
   name varchar;
   table_names varchar[] := ARRAY['product', 'admission', 'admission_product', 'cashier', 'checks', 'selling'];
BEGIN
   FOREACH name IN ARRAY table_names
      LOOP
         EXECUTE 'DELETE FROM ' || name || ' CASCADE';
      END LOOP;
END;
$$ LANGUAGE plpgsql;
