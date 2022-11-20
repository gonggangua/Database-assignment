CREATE TABLE channels(
    sid INT,
    `name` VARCHAR(50) NOT NULL,
    cname VARCHAR(50),
    `type` VARCHAR(10),
    PRIMARY KEY (sid, `name`),
    FOREIGN KEY (sid, cname) REFERENCES categories(sid, `name`),
    CHECK (`type` = 'voice' OR `type` = 'message')
);