CREATE TABLE friends(
    uid INT,
    target INT,
    `status` BOOL, 
    PRIMARY KEY (uid, target),
    FOREIGN KEY (uid) REFERENCES users(id),
    FOREIGN KEY (target) REFERENCES users(id)
);