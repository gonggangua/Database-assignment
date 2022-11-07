CREATE TABLE servers(
    `id` INT AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `creator` INT NOT NULL,
    `isPrivate` BOOL NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(creator) REFERENCES users(id)
);