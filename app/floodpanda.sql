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

INSERT INTO diet_preference (diet_preference_id,diet_name, description)
VALUES (1, 'Standard Diet', 'Default diet preference for new users')
ON DUPLICATE KEY UPDATE diet_name = diet_name;

INSERT INTO DIET_PREFERENCE (diet_name, description) VALUES
('Vegan','No animal products'),
('Vegetarian','No meat'),
('Keto','Low carb, high fat'),
('Paleo','Whole foods, no processed'),
('Mediterranean','Balanced diet with olive oil'),
('Gluten-Free','No gluten products'),
('Low Sodium','Reduced salt intake'),
('High Protein','Protein-focused diet'),
('Diabetic-Friendly','Low sugar'),
('Low Fat','Reduced fat intake');

INSERT INTO meal_plan (plan_id, plan_name, description, total_price)
VALUES (1, 'Default Plan', 'Used for registration', 0.00)
ON DUPLICATE KEY UPDATE plan_name = plan_name;
INSERT INTO MEAL_PLAN (plan_name, description, total_price) VALUES
('Vegan Starter','A starter vegan meal plan',500),
('High Protein Plan','Protein-packed meals',1200),
('Low Carb Plan','Low carbohydrate meals',1000),
('Family Plan','Meals for 4 people',2000),
('Gluten-Free Plan','Gluten-free meals',1100),
('Keto Delight','Keto-friendly meals',1300),
('Mediterranean Mix','Balanced Mediterranean diet',1400),
('Vegetarian Combo','Vegetarian meals',900),
('Diabetic-Friendly','Low sugar meals',950),
('Quick Meals Plan','Fast-prep meals',800);


SELECT * FROM diet_preference;
SELECT * FROM client;
INSERT INTO CLIENT (name, contact_no, password, date_created, location_id, plan_id, diet_preference_id, unit_details) VALUES
('Alice Santos','09171230001','passA!28','2025-01-05',5,8,3,'Unit 7B'),
('Bob Reyes','09171230002','Bx7d!plQ','2025-02-10',2,2,7,'House #220'),
('Cathy Lim','09171230003','Cathy@442','2025-03-15',9,5,2,'Blk 5 Lot 30'),
('Daniel Cruz','09171230004','DanCz#91','2025-04-20',1,6,8,'Unit 12A'),
('Ella Ramos','09171230005','Ella$77q','2025-05-25',7,3,6,'House #14'),
('Frank Villanueva','09171230006','FrV_221!','2025-06-30',3,1,1,'Blk 3 Lot 18'),
('Grace Tan','09171230007','Gr8cePw!','2025-07-05',10,4,5,'Unit 9C'),
('Henry Lopez','09171230008','Hnry_33#','2025-08-10',4,9,9,'House #310'),
('Ivy Mendoza','09171230009','IvyM!204','2025-09-15',6,7,4,'Blk 7 Lot 25'),
('Jake Dela Cruz','09171230010','Jake_DC9@','2025-10-20',8,10,10,'Unit 4F'),
('Laura Garcia','09171230011','Laur@556','2025-11-01',2,1,2,'House #18B'),
('Mark Santos','09171230012','MrkSnt#88','2025-11-02',5,6,7,'Blk 2 Lot 12');


INSERT INTO LOCATION (street_address, city, zip_code) VALUES
('8751 Paseo de Roxas St., San Miguel Village', 'Makati City', '1226'),
('7114 Kundiman St., Sampaloc', 'Manila City', '1008'),
('123 Panay Avenue, Talayan', 'Quezon City', '1103'),
('456 F.B. Harrison Ave., Barangay 73', 'Pasay City', '1300'),
('789 Marcos Highway, Barangay San Roque', 'Marikina City', '1800'),
('321 P. Domingo St., Barangay Carmona', 'Makati City', '1207'),
('654 Gen. Malvar St., Barangay Y', 'Parañaque City', '1700'),
('987 Muntinlupa‑Cavite Road, Barangay Z', 'Muntinlupa City', '1770'),
('159 A. Bonifacio Avenue, Barangay A', 'Caloocan City', '1422'),
('753 Lawton Avenue, Barangay B', 'Mandaluyong City', '1550');

ALTER TABLE client
ADD COLUMN unit_details VARCHAR(50) NULL;

SELECT * FROM CLIENT;

INSERT INTO MEAL_PLAN (plan_name, description, total_price) VALUES
('Vegan Starter','A starter vegan meal plan',500),
('High Protein Plan','Protein-packed meals',1200),
('Low Carb Plan','Low carbohydrate meals',1000),
('Family Plan','Meals for 4 people',2000),
('Gluten-Free Plan','Gluten-free meals',1100),
('Keto Delight','Keto-friendly meals',1300),
('Mediterranean Mix','Balanced Mediterranean diet',1400),
('Vegetarian Combo','Vegetarian meals',900),
('Diabetic-Friendly','Low sugar meals',950),
('Quick Meals Plan','Fast-prep meals',800);

SELECT * FROM MEAL_PLAN;

INSERT INTO MEAL (meal_name, price, cost, nutrients, calories, preparation_time, diet_preference_id) VALUES
('Vegan Salad',150,50,'Vitamin A, C',200,10,1),
('Grilled Chicken',250,120,'Protein',450,20,8),
('Salmon Bowl',300,150,'Omega 3, Protein',500,25,3),
('Beef Stir Fry',280,130,'Protein, Iron',550,30,8),
('Quinoa Veggie',180,70,'Protein, Fiber',300,15,1),
('Keto Omelette',200,90,'Protein, Fat',350,12,3),
('Gluten-Free Pasta',220,100,'Carbs, Fiber',400,20,6),
('Mediterranean Bowl',250,120,'Protein, Fiber',420,18,5),
('Vegetarian Pizza',230,110,'Carbs, Fat',450,25,2),
('Diabetic Salad',160,60,'Fiber, Low Sugar',220,10,9),
('Protein Shake',180,80,'Protein',250,5,8);

SELECT * FROM MEAL;

INSERT INTO INGREDIENT (batch_no, ingredient_name, category, storage_type, measurement_unit, stock_quantity, expiry_date, restock_status, supplier_id) VALUES
(101,'Chicken Breast','Protein','Refrigerated','grams',5000,'2025-12-31','Available',3),
(102,'Salmon Fillet','Protein','Frozen','grams',2000,'2025-11-30','Low Stock',3),
(103,'Broccoli','Produce','Refrigerated','grams',3000,'2025-11-20','Available',2),
(104,'Spinach','Produce','Refrigerated','grams',1500,'2025-11-18','Low Stock',6),
(105,'Olive Oil','Fat','Dry','litres',100,'2026-01-31','Available',5),
(106,'Cheddar Cheese','Dairy','Refrigerated','grams',800,'2025-12-15','Available',4),
(107,'Brown Rice','Grains','Dry','grams',10000,'2026-03-31','Available',5),
(108,'Almonds','Protein','Dry','grams',2000,'2026-02-28','Available',9),
(109,'Tomatoes','Produce','Refrigerated','grams',2500,'2025-11-25','Available',2),
(110,'Eggs','Protein','Refrigerated','grams',1000,'2025-12-31','Available',4),
(111, 'Beef Sirloin', 'Protein', 'Refrigerated', 'grams', 4000, '2025-12-15', 'Available', 3);

INSERT INTO DIET_PREFERENCE (diet_name, description) VALUES
('Vegan','No animal products'),
('Vegetarian','No meat'),
('Keto','Low carb, high fat'),
('Paleo','Whole foods, no processed'),
('Mediterranean','Balanced diet with olive oil'),
('Gluten-Free','No gluten products'),
('Low Sodium','Reduced salt intake'),
('High Protein','Protein-focused diet'),
('Diabetic-Friendly','Low sugar'),
('Low Fat','Reduced fat intake');

INSERT INTO SUPPLIER (supplier_name, contact_no, alt_contact_no, location_id) VALUES
('Fresh Farm','09171234567','09181234567',5),
('Green Grocers','09172223344','09173334455',2),
('Protein Plus','09174445566',NULL,3),
('Dairy Delight','09175556677','09176667788',4),
('Grain Masters','09177778899',NULL,6),
('Veggie World','09178889900','09179990011',7),
('Fruitopia','09170001122',NULL,8),
('Healthy Harvest','09171112233','09172223344',9),
('NutriSource','09173334455',NULL,1),
('Organic Supplies','09174445566','09175556677',10);

INSERT INTO MEAL_INGREDIENT (meal_id, ingredient_id, quantity) VALUES
-- 1. Vegan Salad
(1, 3, 150),   -- Broccoli
(1, 4, 50),    -- Spinach
(1, 9, 50),    -- Tomatoes
(1, 8, 30),    -- Almonds
-- 2. Grilled Chicken
(2, 1, 200),   -- Chicken Breast
(2, 5, 10),    -- Olive Oil
(2, 9, 50),    -- Tomatoes
-- 3. Salmon Bowl
(3, 2, 200),   -- Salmon Fillet
(3, 5, 10),    -- Olive Oil
(3, 3, 50),    -- Broccoli
-- 4. Beef Stir Fry
(4, 11, 200),  -- Beef Sirloin 
(4, 3, 50),    -- Broccoli
(4, 5, 10),    -- Olive Oil
-- 5. Quinoa Veggie
(5, 7, 100),   -- Brown Rice
(5, 3, 50),    -- Broccoli
(5, 8, 30),    -- Almonds
-- 6. Keto Omelette
(6, 10, 100),  -- Eggs
(6, 1, 50),    -- Chicken Breast
(6, 6, 20),    -- Cheddar Cheese
-- 7. Gluten-Free Pasta
(7, 7, 150),   -- Brown Rice (used as gluten-free base)
(7, 6, 50),    -- Cheddar Cheese
(7, 5, 10),    -- Olive Oil
-- 8. Mediterranean Bowl
(8, 2, 150),   -- Salmon Fillet
(8, 3, 50),    -- Broccoli
(8, 5, 10),    -- Olive Oil
-- 9. Vegetarian Pizza
(9, 7, 100),   -- Brown Rice crust substitute
(9, 6, 50),    -- Cheddar Cheese
(9, 8, 20),    -- Almonds
-- 10. Diabetic Salad
(10, 3, 100),  -- Broccoli
(10, 9, 50),   -- Tomatoes
(10, 8, 20),   -- Almonds
-- 11. Protein Shake
(11, 8, 50),   -- Almonds
(11, 10, 30);  -- Eggs


SELECT * FROM SUPPLIER;
SELECT * FROM INGREDIENT;
SELECT * FROM MEAL_PLAN;
SELECT * FROM MEAL;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE meal_ingredient;
TRUNCATE TABLE ingredient;
ALTER TABLE ingredient AUTO_INCREMENT = 1;

TRUNCATE TABLE supplier;
ALTER TABLE supplier AUTO_INCREMENT = 1;

-- Truncate other dependent tables if needed
TRUNCATE TABLE meal;
ALTER TABLE meal AUTO_INCREMENT = 1;

TRUNCATE TABLE delivery;
ALTER TABLE delivery AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

SELECT * FROM MEAL_INGREDIENT;

-- -------------------
-- DELIVERY
-- -------------------
INSERT INTO delivery (
    order_date, time_ordered, time_delivered, 
    payment_mode, payment_status, delivery_method, delivery_status, 
    client_id, meal_id, rider_id
) VALUES
('2025-11-01', '08:15:00', '08:50:00', 'Cash on Delivery', 'Paid', 'Motorcycle', 'On-Time', 1, 5, 3),
('2025-11-01', '09:30:00', '10:05:00', 'GCash', 'Paid', 'Truck', 'Delayed', 3, 2, 6),
('2025-11-02', '10:05:00', '10:40:00', 'Bank', 'Paid', 'Drone', 'On-Time', 2, 7, 2),
('2025-11-02', '11:20:00', '12:00:00', 'Cash on Delivery', 'Pending', 'Motorcycle', 'Delayed', 1, 1, 5),
('2025-11-03', '12:00:00', '12:35:00', 'GCash', 'Paid', 'Boat', 'On-Time', 5, 3, 1),
('2025-11-03', '13:10:00', '13:45:00', 'Bank', 'Failed', 'Truck', 'Cancelled', 4, 9, 8),
('2025-11-04', '14:15:00', '14:55:00', 'Cash on Delivery', 'Paid', 'Drone', 'On-Time', 6, 4, 4),
('2025-11-04', '15:00:00', '15:35:00', 'GCash', 'Paid', 'Motorcycle', 'On-Time', 7, 8, 10),
('2025-11-05', '16:20:00', '16:55:00', 'Bank', 'Paid', 'Truck', 'Delayed', 8, 6, 7),
('2025-11-05', '17:10:00', '17:50:00', 'Cash on Delivery', 'Pending', 'Tikling Tricycle', 'On-Time', 3, 11, 9),
('2025-11-06', '08:05:00', '08:40:00', 'GCash', 'Paid', 'Motorcycle', 'On-Time', 2, 2, 2),
('2025-11-06', '09:50:00', '10:25:00', 'Bank', 'Paid', 'Boat', 'Delayed', 5, 5, 3),
('2025-11-07', '10:30:00', '11:00:00', 'Cash on Delivery', 'Paid', 'Drone', 'On-Time', 6, 7, 6),
('2025-11-07', '11:45:00', '12:25:00', 'GCash', 'Pending', 'Truck', 'Delayed', 7, 1, 1),
('2025-11-08', '12:15:00', '12:50:00', 'Bank', 'Paid', 'Motorcycle', 'On-Time', 8, 9, 5),
('2025-11-08', '13:40:00', '14:15:00', 'Cash on Delivery', 'Paid', 'Boat', 'On-Time', 9, 4, 4),
('2025-11-09', '14:20:00', '14:55:00', 'GCash', 'Paid', 'Drone', 'Delayed', 10, 8, 6),
('2025-11-09', '15:35:00', '16:10:00', 'Bank', 'Paid', 'Truck', 'On-Time', 11, 6, 2),
('2025-11-10', '16:05:00', '16:45:00', 'Cash on Delivery', 'Pending', 'Motorcycle', 'Delayed', 12, 3, 9),
('2025-11-10', '17:15:00', '17:50:00', 'GCash', 'Paid', 'Boat', 'On-Time', 1, 10, 7);

SELECT * FROM delivery;

-- -------------------
-- RIDER
-- -------------------
INSERT INTO RIDER (rider_name, hire_date, contact_no) VALUES
('Rider A','2024-01-01','09170000001'),
('Rider B','2024-02-01','09170000002'),
('Rider C','2024-03-01','09170000003'),
('Rider D','2024-04-01','09170000004'),
('Rider E','2024-05-01','09170000005'),
('Rider F','2024-06-01','09170000006'),
('Rider G','2024-07-01','09170000007'),
('Rider H','2024-08-01','09170000008'),
('Rider I','2024-09-01','09170000009'),
('Rider J','2024-10-01','09170000010');

INSERT INTO flood_data (flood_factor, avg_water_level, affected_households, road_condition, special_packaging, alt_delivery_method, location_id) VALUES
('LOW', 0.45, 12, 'Accessible', FALSE, 'Motorcycle', 3),
('MODERATE', 1.20, 60, 'Partially Flooded', TRUE, 'Drone', 1),
('HIGH', 2.50, 120, 'Not Accessible', TRUE, 'Boat', 7),
('SEVERE', 3.10, 250, 'Not Accessible', TRUE, 'Truck', 4),
('LOW', 0.55, 15, 'Accessible', FALSE, 'Tikling Tricycle', 2),
('MODERATE', 1.10, 50, 'Partially Flooded', TRUE, 'Drone', 6),
('HIGH', 2.70, 140, 'Not Accessible', TRUE, 'Boat', 3),
('SEVERE', 3.00, 180, 'Not Accessible', TRUE, 'Truck', 8),
('LOW', 0.35, 10, 'Accessible', FALSE, 'Motorcycle', 5),
('MODERATE', 1.50, 70, 'Partially Flooded', TRUE, 'Drone', 3),
('HIGH', 2.20, 110, 'Not Accessible', TRUE, 'Boat', 9),
('MODERATE', 1.35, 55, 'Partially Flooded', FALSE, 'Tikling Tricycle', 2),
('LOW', 0.30, 8, 'Accessible', FALSE, 'Motorcycle', 7),
('HIGH', 2.90, 150, 'Not Accessible', TRUE, 'Boat', 6),
('SEVERE', 3.50, 220, 'Not Accessible', TRUE, 'Truck', 10),
('MODERATE', 1.40, 65, 'Partially Flooded', TRUE, 'Drone', 1),
('LOW', 0.50, 20, 'Accessible', FALSE, 'Motorcycle', 5),
('HIGH', 2.60, 130, 'Not Accessible', TRUE, 'Boat', 4),
('SEVERE', 3.20, 200, 'Not Accessible', TRUE, 'Truck', 2),
('MODERATE', 1.25, 45, 'Partially Flooded', TRUE, 'Tikling Tricycle', 9);

SELECT * FROM flood_data;

