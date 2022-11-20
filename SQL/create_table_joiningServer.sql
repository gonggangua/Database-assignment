CREATE TABLE `joiningServer`(
    `uid` INT,
    `sid` INT,
    `group` VARCHAR(50),
    `time` DATETIME,
    PRIMARY KEY (uid, sid),
    FOREIGN KEY (uid) REFERENCES `users`(id),
    FOREIGN KEY (sid) REFERENCES `servers`(id)
);