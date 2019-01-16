CREATE TABLE users (
  userId int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  password varchar(50) NOT NULL,
  number varchar(11) NOT NULL,
  address varchar(200) NOT NULL,
  url varchar(500) NOT NULL,
  isActivated bit NOT NULL,
  PRIMARY KEY (userId),
  UNIQUE (email)
);
CREATE TABLE admin (
  adminId int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  password varchar(50) NOT NULL,
  PRIMARY KEY (adminId),
  UNIQUE (email)
);
CREATE TABLE deals (
  dealId int(11) NOT NULL AUTO_INCREMENT,
  buyerId int(11) NOT NULL,
  productId int(11) NOT NULL,
  time datetime NOT NULL,
  PRIMARY KEY (dealId)
);
CREATE TABLE messages (
  messageId int(11) NOT NULL AUTO_INCREMENT,
  dealId int(11) NOT NULL,
  userId int(11) NOT NULL,
  content text NOT NULL,
  time datetime NOT NULL,
  PRIMARY KEY (messageId)
);
CREATE TABLE products (
  productId int(11) NOT NULL AUTO_INCREMENT,
  sellerId int(11) NOT NULL,
  productName varchar(100) NOT NULL,
  description varchar(300) NOT NULL,
  quantity int(11) NOT NULL,
  price real NOT NULL,
  location varchar(200) NOT NULL,
  lat varchar(50) NOT NULL,
  lng varchar(50) NOT NULL,
  status varchar(50) NOT NULL,
  productUrl varchar(500) NOT NULL,
  PRIMARY KEY (productId)
);
INSERT INTO admin (name, email, password) VALUES ('admin', 'admin', 'admin')



