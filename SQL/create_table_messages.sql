CREATE TABLE messages(
    id INT AUTO_INCREMENT,
    sid INT,
    chname VARCHAR(50),
    content VARCHAR(500),
    sender INT,
    sendTime DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (sid, chname) REFERENCES channels(sid, `name`),
    FOREIGN KEY (sender) REFERENCES users(id)
);