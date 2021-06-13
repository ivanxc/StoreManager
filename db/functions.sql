CREATE OR REPLACE FUNCTION add_products(IN title varchar(255),
					                    IN price double precision,
					                    IN barcode integer,
					                    IN amount float)
RETURNS integer AS
$$
   INSERT INTO product (title, price, barcode, amount) VALUES (title, price, barcode, amount) RETURNING id;
$$ LANGUAGE SQL;


CREATE OR REPLACE FUNCTION add_admission(IN title varchar(255))
    RETURNS integer AS
$$
INSERT INTO admission (title) values (title)
RETURNING admission.id;
$$ LANGUAGE sql;


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
                   price double precision,
                   barcode integer,
                   amount float,
                   is_sold boolean) AS
$$
BEGIN
    RETURN QUERY(SELECT * FROM product);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_product_price(IN id integer, IN new_price double precision)
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
RETURNS integer AS
$$
   INSERT INTO checks (price, cashier_id) VALUES ($1, $2) RETURNING checks.id;
$$ LANGUAGE SQL;


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


CREATE OR REPLACE FUNCTION sell_product_by_id(IN id integer, sell_amount double precision)
    RETURNS void AS
$$
DECLARE
    product_amount double precision;
BEGIN
    UPDATE product SET amount = amount - sell_amount
    WHERE product.id = $1;

    SELECT amount INTO product_amount FROM product WHERE product.id = $1;

    IF product_amount = 0 THEN
        UPDATE product SET is_sold = true WHERE product.id = $1;
    END IF;
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
   destination varchar;
   table_names varchar[] := ARRAY['product', 'admission', 'admission_product', 'cashier', 'checks', 'selling'];
BEGIN
   FOREACH name IN ARRAY table_names
      LOOP
         SELECT setval(name || '_id_' || 'seq', 1, FALSE)::varchar INTO destination;
         EXECUTE 'DELETE FROM ' || name || ' CASCADE';
      END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_cashier_id(cashier_name varchar(256)) RETURNS integer AS
$$
SELECT cashier.id
FROM cashier
WHERE cashier.name = cashier_name;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION delete_cashier(IN name varchar)
RETURNS void AS
$$
BEGIN
   UPDATE checks SET cashier_id = NULL WHERE cashier_id = (SELECT id FROM cashier WHERE cashier.name = $1);
   DELETE FROM cashier WHERE cashier.name = $1;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_product_by_title_or_barcode(IN product_title varchar DEFAULT '',
                                                            IN product_barcode integer DEFAULT 0)
RETURNS TABLE (id integer,
               title varchar(255),
               price double precision,
               barcode integer,
               amount float,
               is_sold boolean) AS
$$
BEGIN
   IF $2 = 0 AND $1 = '' THEN
      RETURN QUERY (SELECT * FROM product);
   ELSIF $2 = 0 THEN
      RETURN QUERY (SELECT * FROM product WHERE product.title LIKE FORMAT('%s%%', $1));
   ELSE
      RETURN QUERY (SELECT * FROM product WHERE product.barcode = $2);
   END IF;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_product_by_id(IN product_id integer)
RETURNS TABLE (id integer,
               title varchar(255),
               price double precision,
               barcode integer,
               amount float,
               is_sold boolean) AS
$$
BEGIN
   RETURN QUERY (SELECT * FROM product WHERE product.id = $1);
END;
$$ LANGUAGE plpgsql;

