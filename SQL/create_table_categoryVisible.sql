CREATE TABLE categoryVisible(
    sid INT,
    cname VARCHAR(50),
    gname VARCHAR(50),
    PRIMARY KEY (sid, cname, gname),
    FOREIGN KEY (sid, cname) REFERENCES categories(sid, `name`),
    FOREIGN KEY (sid, gname) REFERENCES `groups`(sid, `name`)
);