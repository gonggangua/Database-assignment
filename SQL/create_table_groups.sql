CREATE TABLE `groups`(
    sid INT,
    `name` VARCHAR(50) NOT NULL,
    canCreate BOOL,
    canBan BOOL,
    canManage BOOL,
    canStats BOOL,
    PRIMARY KEY (sid, `name`),
    FOREIGN KEY (sid) REFERENCES servers(id)
);