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

CREATE TABLE IF NOT EXISTS CLIENT_AUTH (
    auth_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255),
    FOREIGN KEY (client_id) REFERENCES client(client_id)
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
