CREATE TABLE users(
    `id` INT AUTO_INCREMENT,
    `name` VARCHAR(20) NOT NULL UNIQUE,
    `mail` VARCHAR(50),
    `level` INT,
    `status` VARCHAR(20),
    `registry` DATETIME,
    `money` INT,`assignment`
    PRIMARY KEY(`id`)
);