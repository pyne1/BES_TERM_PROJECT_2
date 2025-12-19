DROP DATABASE IF EXISTS thriftstore_products;
CREATE DATABASE thriftstore_products;
USE thriftstore_products;

CREATE TABLE Customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    creditCardNumber VARCHAR(50),
    creditCardExpiry VARCHAR(10),
    creditCardCVV VARCHAR(10),
    billingAddress VARCHAR(255),
    billingCity VARCHAR(100),
    billingPostal VARCHAR(20),
    shippingAddress VARCHAR(255),
    shippingCity VARCHAR(100),
    shippingPostal VARCHAR(20)
);

CREATE TABLE brands (
    brand_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    brand_id INT,
    category_id INT,
    image_url VARCHAR(255),
    stock INT NOT NULL DEFAULT 0,
    FOREIGN KEY (brand_id) REFERENCES brands(brand_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customerEmail VARCHAR(255) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO Customer (
    firstName, lastName, email, passwordHash,
    billingAddress, billingCity, billingPostal,
    shippingAddress, shippingCity, shippingPostal
) VALUES (
    'Admin', 'User', 'admin@everything.yorku.ca', 'admin123',
    '4700 Keele St', 'Toronto', 'M3J1P3',
    '4700 Keele St', 'Toronto', 'M3J1P3'
);

INSERT INTO brands (name) VALUES
('Ralph Lauren'),
('Calvin Klein'),
('Gap'),
('Nike'),
('Adidas'),
('Levi''s'),
('H&M'),
('Lululemon'),
('Guess'),
('Old Navy'),
('Banana Republic');

INSERT INTO categories (name) VALUES
('Clothing'),
('Outerwear'),
('Accessories'),
('Bags'),
('Activewear');

INSERT INTO products (name, description, price, brand_id, category_id, image_url, stock) VALUES
('Ralph Lauren Men''s Oxford Shirt', 'Classic long-sleeve oxford button-down shirt.', 29.99,
 (SELECT brand_id FROM brands WHERE name='Ralph Lauren'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/rl_oxford.jpg', 5),

('Ralph Lauren Quarter-Zip Sweater', 'Cotton-blend quarter-zip sweater, great for fall weather.', 34.99,
 (SELECT brand_id FROM brands WHERE name='Ralph Lauren'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/rl_qzip.jpg', 4),

('Ralph Lauren Classic Polo Tee', 'Iconic polo shirt with embroidered logo.', 24.99,
 (SELECT brand_id FROM brands WHERE name='Ralph Lauren'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/rl_polo.jpg', 6),

('Calvin Klein Slim-Fit Jeans', 'Modern slim-fit denim jeans.', 25.99,
 (SELECT brand_id FROM brands WHERE name='Calvin Klein'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/ck_jeans.jpg', 8),

('Calvin Klein Basic Crewneck Tee', 'Soft cotton crewneck tee with minimalist design.', 14.99,
 (SELECT brand_id FROM brands WHERE name='Calvin Klein'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/ck_tee.jpg', 10),

('Calvin Klein Lightweight Hoodie', 'Lightweight zip-up hoodie ideal for daily wear.', 22.99,
 (SELECT brand_id FROM brands WHERE name='Calvin Klein'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/ck_hoodie.jpg', 7),

('Gap Logo Hoodie', 'Fleece hoodie with classic Gap logo.', 19.99,
 (SELECT brand_id FROM brands WHERE name='Gap'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/gap_hoodie.jpg', 12),

('Gap Stretch Chinos', 'Comfortable stretch chinos great for casual and semi-formal wear.', 21.99,
 (SELECT brand_id FROM brands WHERE name='Gap'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/gap_chinos.jpg', 9),

('Gap Denim Jacket', 'Timeless blue denim jacket.', 27.99,
 (SELECT brand_id FROM brands WHERE name='Gap'),
 (SELECT category_id FROM categories WHERE name='Outerwear'),
 'images/gap_jacket.jpg', 6),

('Nike Dri-FIT Training Shirt', 'Moisture-wicking athletic training tee.', 17.99,
 (SELECT brand_id FROM brands WHERE name='Nike'),
 (SELECT category_id FROM categories WHERE name='Activewear'),
 'images/nike_drifit.jpg', 15),

('Nike Heritage Backpack', 'Lightweight durable backpack.', 24.99,
 (SELECT brand_id FROM brands WHERE name='Nike'),
 (SELECT category_id FROM categories WHERE name='Bags'),
 'images/nike_backpack.jpg', 8),

('Nike Club Fleece Sweatpants', 'Soft fleece sweatpants for training or lounging.', 22.99,
 (SELECT brand_id FROM brands WHERE name='Nike'),
 (SELECT category_id FROM categories WHERE name='Activewear'),
 'images/nike_sweats.jpg', 11),

('Lululemon Metal Vent Tech Shirt', 'Breathable anti-stink training shirt.', 29.99,
 (SELECT brand_id FROM brands WHERE name='Lululemon'),
 (SELECT category_id FROM categories WHERE name='Activewear'),
 'images/lulu_metalvent.jpg', 4),

('Lululemon ABC Joggers', 'Lightweight stretchy joggers designed for movement.', 49.99,
 (SELECT brand_id FROM brands WHERE name='Lululemon'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/lulu_joggers.jpg', 3),

('Lululemon Define Jacket', 'Fitted athletic jacket with sleek design.', 39.99,
 (SELECT brand_id FROM brands WHERE name='Lululemon'),
 (SELECT category_id FROM categories WHERE name='Outerwear'),
 'images/lulu_define.jpg', 2),

('Guess Faux-Leather Jacket', 'Stylish faux-leather moto jacket.', 32.99,
 (SELECT brand_id FROM brands WHERE name='Guess'),
 (SELECT category_id FROM categories WHERE name='Outerwear'),
 'images/guess_jacket.jpg', 5),

('Guess Graphic Tee', 'Cotton tee with Guess graphic print.', 14.99,
 (SELECT brand_id FROM brands WHERE name='Guess'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/guess_tee.jpg', 10),

('Guess Slim-Fit Jeans', 'Slim-fit dark wash denim.', 23.99,
 (SELECT brand_id FROM brands WHERE name='Guess'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/guess_jeans.jpg', 7),

('Levi''s 511 Slim Jeans', 'Signature 511 slim-fit denim.', 26.99,
 (SELECT brand_id FROM brands WHERE name='Levi''s'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/levis_511.jpg', 9),

('Levi''s Sherpa Trucker Jacket', 'Iconic trucker jacket with sherpa lining.', 34.99,
 (SELECT brand_id FROM brands WHERE name='Levi''s'),
 (SELECT category_id FROM categories WHERE name='Outerwear'),
 'images/levis_sherpa.jpg', 4),

('Levi''s Classic Denim Shirt', 'Button-up denim shirt.', 21.99,
 (SELECT brand_id FROM brands WHERE name='Levi''s'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/levis_shirt.jpg', 6),

('H&M Puffer Vest', 'Lightweight padded puffer vest.', 19.99,
 (SELECT brand_id FROM brands WHERE name='H&M'),
 (SELECT category_id FROM categories WHERE name='Outerwear'),
 'images/hm_vest.jpg', 7),

('H&M Slim-Fit Chinos', 'Comfortable slim-fit chinos for daily wear.', 17.99,
 (SELECT brand_id FROM brands WHERE name='H&M'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/hm_chinos.jpg', 10),

('H&M Knit Sweater', 'Cozy knit pullover sweater.', 18.99,
 (SELECT brand_id FROM brands WHERE name='H&M'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/hm_sweater.jpg', 8),

('Old Navy Pullover Hoodie', 'Soft cotton-blend pullover hoodie.', 16.99,
 (SELECT brand_id FROM brands WHERE name='Old Navy'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/on_hoodie.jpg', 11),

('Old Navy Linen-Blend Shirt', 'Breathable linen-blend button-up.', 14.99,
 (SELECT brand_id FROM brands WHERE name='Old Navy'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/on_shirt.jpg', 9),

('Old Navy Everyday Shorts', 'Casual shorts perfect for summer.', 13.99,
 (SELECT brand_id FROM brands WHERE name='Old Navy'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/on_shorts.jpg', 12),

('Banana Republic Merino Wool Sweater', 'Premium merino wool sweater.', 27.99,
 (SELECT brand_id FROM brands WHERE name='Banana Republic'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/br_sweater.jpg', 5),

('Banana Republic Button-Down Oxford', 'Tailored cotton oxford shirt.', 25.99,
 (SELECT brand_id FROM brands WHERE name='Banana Republic'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/br_oxford.jpg', 6),

('Banana Republic Traveler Pants', 'Stretchy wrinkle-resistant pants.', 29.99,
 (SELECT brand_id FROM brands WHERE name='Banana Republic'),
 (SELECT category_id FROM categories WHERE name='Clothing'),
 'images/br_traveler.jpg', 4);
 
 SHOW TABLES; 
 SELECT * FROM customer; 
