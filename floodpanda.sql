CREATE SCHEMA IF NOT EXISTS floodpanda;
USE floodpanda;

CREATE TABLE IF NOT EXISTS  LOCATION (
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    street_address VARCHAR(100) NOT NULL,
    city VARCHAR(50),
    zip_code VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS  DIET_PREFERENCE (
    diet_preference_id INT PRIMARY KEY AUTO_INCREMENT,
    diet_name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS  SUPPLIER (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_no VARCHAR(15) NOT NULL,
    alt_contact_no VARCHAR(15),
    location_id INT,
    FOREIGN KEY (location_id) REFERENCES LOCATION(location_id)
);

CREATE TABLE IF NOT EXISTS  INGREDIENT (
    ingredient_id INT PRIMARY KEY AUTO_INCREMENT,
    batch_no INT,
    ingredient_name VARCHAR(100),
    category ENUM('Protein','Produce','Dairy','Grains','Fat','Condiments'),
    storage_type ENUM('Dry','Refrigerated','Frozen'),
    measurement_unit ENUM('grams','litres'),
    stock_quantity DECIMAL(10,4),
    expiry_date DATE,
    restock_status ENUM('Available','Low Stock','Out of Stock'),
    supplier_id INT,
    FOREIGN KEY (supplier_id) REFERENCES SUPPLIER(supplier_id)
);

CREATE TABLE IF NOT EXISTS  MEAL_PLAN (
    plan_id INT PRIMARY KEY AUTO_INCREMENT,
    plan_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    total_price DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS  CLIENT (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_no VARCHAR(15),
    date_created DATE,
    location_id INT,
    plan_id INT,
    diet_preference_id INT,
    FOREIGN KEY (location_id) REFERENCES LOCATION(location_id),
    FOREIGN KEY (plan_id) REFERENCES MEAL_PLAN(plan_id),
    FOREIGN KEY (diet_preference_id) REFERENCES DIET_PREFERENCE(diet_preference_id)
);

CREATE TABLE IF NOT EXISTS  RIDER (
    rider_id INT PRIMARY KEY AUTO_INCREMENT,
    rider_name VARCHAR(100) NOT NULL,
    hire_date DATE,
    contact_no VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS  MEAL (
    meal_id INT PRIMARY KEY AUTO_INCREMENT,
    meal_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    cost DECIMAL(10,2),
    nutrients VARCHAR(200),
    calories INT,
    preparation_time INT,
    date_added DATE DEFAULT (CURRENT_DATE),
    diet_preference_id INT,
    FOREIGN KEY (diet_preference_id) REFERENCES DIET_PREFERENCE(diet_preference_id)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS  MEAL_INGREDIENT (
    meal_id INT,
    ingredient_id INT,
    quantity DECIMAL(10,2),
    PRIMARY KEY (meal_id, ingredient_id),
    FOREIGN KEY (meal_id) REFERENCES MEAL(meal_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES INGREDIENT(ingredient_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS  DELIVERY (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    order_date DATE,
    time_ordered TIME,
    time_delivered TIME,
    payment_mode ENUM('Bank','GCash','Cash on Delivery'),
    payment_status ENUM('Paid','Pending','Failed'),
    delivery_method ENUM('Motorcycle','Tikling Tricycle','Drone','Boat','Truck'),
    delivery_status ENUM('On-Time','Delayed','Cancelled'),
    client_id INT,
    meal_id INT,
    rider_id INT,
    FOREIGN KEY (client_id) REFERENCES CLIENT(client_id),
    FOREIGN KEY (meal_id) REFERENCES MEAL(meal_id),
    FOREIGN KEY (rider_id) REFERENCES RIDER(rider_id)
);

CREATE TABLE IF NOT EXISTS FLOOD_DATA (
    flood_id INT PRIMARY KEY AUTO_INCREMENT,
    flood_factor ENUM('LOW','MODERATE','HIGH','SEVERE'),
    avg_water_level DECIMAL(5,2),
    affected_households INT,
    road_condition ENUM('Accessible','Partially Flooded','Not Accessible'),
    special_packaging BOOLEAN,
    alt_delivery_method ENUM('Motorcycle','Tikling Tricycle','Drone','Boat','Truck'),
    location_id INT,
    FOREIGN KEY (location_id) REFERENCES LOCATION(location_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS MEAL_DELIVERY (
    meal_id INT,
    transaction_id INT,
    remarks VARCHAR(100),
    PRIMARY KEY (meal_id, transaction_id),
    FOREIGN KEY (meal_id) REFERENCES MEAL(meal_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (transaction_id) REFERENCES DELIVERY(transaction_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS MEAL_MEAL_PLAN (
    plan_id INT,
    meal_id INT,
    remarks VARCHAR(100),
    PRIMARY KEY (plan_id, meal_id),
    FOREIGN KEY (plan_id) REFERENCES MEAL_PLAN(plan_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (meal_id) REFERENCES MEAL(meal_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS CLIENT_DIET_PREFERENCE (
    diet_preference_id INT,
    client_id INT,
    PRIMARY KEY (diet_preference_id, client_id),
    FOREIGN KEY (diet_preference_id) REFERENCES DIET_PREFERENCE(diet_preference_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (client_id) REFERENCES CLIENT(client_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

