CREATE TABLE calls(
    sid INT,
    chname VARCHAR(50),
    `time`  TIME,
    PRIMARY KEY (sid, chname),
    FOREIGN KEY (sid, chname) REFERENCES channels(sid, `name`)
);