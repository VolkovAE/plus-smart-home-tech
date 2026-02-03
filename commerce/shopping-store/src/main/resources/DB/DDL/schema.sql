;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS shopping_store;

CREATE TABLE IF NOT EXISTS shopping_store.products (
    product_Id VARCHAR(36) PRIMARY KEY,
    product_Name  VARCHAR(150) NOT NULL CHECK(TRIM('	 ' FROM description) != ''),
    description VARCHAR NOT NULL CHECK(TRIM('	 ' FROM description) != ''),
    quantity_State VARCHAR(10) NOT NULL CHECK(TRIM('	 ' FROM description) != ''),
    product_State VARCHAR(15) NOT NULL CHECK(TRIM('	 ' FROM description) != ''),
    product_Category VARCHAR(25) NOT NULL CHECK(TRIM('	 ' FROM description) != ''),
    price NUMERIC(15, 2) NOT NULL,
    image_Src VARCHAR
);
