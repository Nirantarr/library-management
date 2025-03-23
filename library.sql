CREATE DATABASE library;
USE library;

CREATE TABLE student
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE books
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    author         VARCHAR(255) NOT NULL,
    genre          VARCHAR(100),
    stock          INT          NOT NULL DEFAULT 0
);

CREATE TABLE admin
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)         NOT NULL,
    email    VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL
);
CREATE TABLE book_requests
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    status  VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (book_id) REFERENCES books (id)
);
