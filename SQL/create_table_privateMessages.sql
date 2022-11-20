CREATE TABLE privateMessages(
    id INT AUTO_INCREMENT,
    sender INT,
    receiver INT,
    sendTime TIME,
    content VARCHAR(500),
    PRIMARY KEY (id),
    FOREIGN KEY (sender) REFERENCES users(id),
    FOREIGN KEY (receiver) REFERENCES users(id)
);