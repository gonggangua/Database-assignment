CREATE TABLE userBlacklists(
    uid INT,
    blockid INT,
    PRIMARY KEY (uid, blockid),
    FOREIGN KEY (uid) REFERENCES users(id),
    FOREIGN KEY (blockid) REFERENCES users(id)
);