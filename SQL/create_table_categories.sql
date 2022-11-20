CREATE TABLE categories(
    sid INT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (sid, `name`),
    FOREIGN KEY (sid) REFERENCES servers(id)
);