CREATE TABLE serverBlacklists(
    sid INT,
    uid INT,
    PRIMARY KEY (sid, uid),
    FOREIGN KEY (sid) REFERENCES servers(id),
    FOREIGN KEY (uid) REFERENCES users(id)
);