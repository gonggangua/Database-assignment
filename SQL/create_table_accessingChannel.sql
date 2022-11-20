CREATE TABLE accessingChannel(
    uid INT,
    sid INT,
    chname VARCHAR(50),
    accessTime TIME,
    lastTime TIME,
    PRIMARY KEY (uid, sid, chname),
    FOREIGN KEY (sid, chname) REFERENCES channels(sid, `name`)
);