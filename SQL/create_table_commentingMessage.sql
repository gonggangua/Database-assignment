CREATE TABLE commentingMessage(
    uid INT,
    `mid` INT,
    `type` VARCHAR(10),
    PRIMARY KEY (uid, `mid`),
    FOREIGN KEY (uid) REFERENCES users(id),
    FOREIGN KEY (`mid`) REFERENCES messages(id),
    CHECK (`type` = 'like' OR `type` = 'dislike')
);