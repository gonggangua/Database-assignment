/*
SQLyog Community v13.1.9 (64 bit)
MySQL - 8.0.31 : Database - assignment
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`assignment` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `assignment`;

/*Table structure for table `accessingchannel` */

DROP TABLE IF EXISTS `accessingchannel`;

CREATE TABLE `accessingchannel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `sid` int NOT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `accessTime` datetime DEFAULT NULL,
  `leaveTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `sid` (`sid`,`chname`),
  CONSTRAINT `accessingchannel_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `accessingchannel_ibfk_2` FOREIGN KEY (`sid`, `chname`) REFERENCES `channels` (`sid`, `name`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `calls` */

DROP TABLE IF EXISTS `calls`;

CREATE TABLE `calls` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sid` int NOT NULL,
  `chname` varchar(50) NOT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `calls_ibfk_1` (`sid`,`chname`),
  CONSTRAINT `calls_ibfk_1` FOREIGN KEY (`sid`, `chname`) REFERENCES `channels` (`sid`, `name`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `categories` */

DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
  `sid` int NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`sid`,`name`),
  CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `servers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `categoryvisible` */

DROP TABLE IF EXISTS `categoryvisible`;

CREATE TABLE `categoryvisible` (
  `sid` int NOT NULL,
  `cname` varchar(50) NOT NULL,
  `gname` varchar(50) NOT NULL,
  PRIMARY KEY (`sid`,`cname`,`gname`),
  KEY `categoryvisible_ibfk_2` (`sid`,`gname`),
  CONSTRAINT `categoryvisible_ibfk_1` FOREIGN KEY (`sid`, `cname`) REFERENCES `categories` (`sid`, `name`) ON DELETE CASCADE,
  CONSTRAINT `categoryvisible_ibfk_2` FOREIGN KEY (`sid`, `gname`) REFERENCES `groups` (`sid`, `name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `channels` */

DROP TABLE IF EXISTS `channels`;

CREATE TABLE `channels` (
  `sid` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `cname` varchar(50) NOT NULL,
  `type` tinyint(1) NOT NULL,
  PRIMARY KEY (`sid`,`name`),
  KEY `channels_ibfk_1` (`sid`,`cname`),
  CONSTRAINT `channels_ibfk_1` FOREIGN KEY (`sid`, `cname`) REFERENCES `categories` (`sid`, `name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `commentingmessage` */

DROP TABLE IF EXISTS `commentingmessage`;

CREATE TABLE `commentingmessage` (
  `uid` int NOT NULL,
  `mid` int NOT NULL,
  `type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`uid`,`mid`),
  KEY `commentingmessage_ibfk_2` (`mid`),
  CONSTRAINT `commentingmessage_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `commentingmessage_ibfk_2` FOREIGN KEY (`mid`) REFERENCES `messages` (`id`) ON DELETE CASCADE,
  CONSTRAINT `commentingmessage_chk_1` CHECK (((`type` = _utf8mb4'like') or (`type` = _utf8mb4'dislike')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `friends` */

DROP TABLE IF EXISTS `friends`;

CREATE TABLE `friends` (
  `uid` int NOT NULL,
  `target` int NOT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uid`,`target`),
  KEY `friends_ibfk_2` (`target`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`target`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `groups` */

DROP TABLE IF EXISTS `groups`;

CREATE TABLE `groups` (
  `sid` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `canCreate` tinyint(1) DEFAULT NULL,
  `canBan` tinyint(1) DEFAULT NULL,
  `canManage` tinyint(1) DEFAULT NULL,
  `canStats` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`sid`,`name`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `servers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `joiningserver` */

DROP TABLE IF EXISTS `joiningserver`;

CREATE TABLE `joiningserver` (
  `uid` int NOT NULL,
  `sid` int NOT NULL,
  `gname` varchar(50) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`,`sid`),
  KEY `joiningserver_ibfk_2` (`sid`,`gname`),
  CONSTRAINT `joiningserver_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `joiningserver_ibfk_2` FOREIGN KEY (`sid`, `gname`) REFERENCES `groups` (`sid`, `name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `messages` */

DROP TABLE IF EXISTS `messages`;

CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sid` int DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `sender` int DEFAULT NULL,
  `sendTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `messages_ibfk_1` (`sid`,`chname`),
  KEY `messages_ibfk_2` (`sender`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sid`, `chname`) REFERENCES `channels` (`sid`, `name`) ON DELETE CASCADE,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`sender`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `privatemessages` */

DROP TABLE IF EXISTS `privatemessages`;

CREATE TABLE `privatemessages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender` int DEFAULT NULL,
  `receiver` int DEFAULT NULL,
  `sendTime` datetime DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `privatemessages_ibfk_1` (`sender`),
  KEY `privatemessages_ibfk_2` (`receiver`),
  CONSTRAINT `privatemessages_ibfk_1` FOREIGN KEY (`sender`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `privatemessages_ibfk_2` FOREIGN KEY (`receiver`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `serverblacklists` */

DROP TABLE IF EXISTS `serverblacklists`;

CREATE TABLE `serverblacklists` (
  `sid` int NOT NULL,
  `uid` int NOT NULL,
  PRIMARY KEY (`sid`,`uid`),
  KEY `serverblacklists_ibfk_2` (`uid`),
  CONSTRAINT `serverblacklists_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `servers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `serverblacklists_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `servers` */

DROP TABLE IF EXISTS `servers`;

CREATE TABLE `servers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `creator` int NOT NULL,
  `isPrivate` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nameUnique` (`name`),
  KEY `servers_ibfk_1` (`creator`),
  CONSTRAINT `servers_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `userblacklists` */

DROP TABLE IF EXISTS `userblacklists`;

CREATE TABLE `userblacklists` (
  `uid` int NOT NULL,
  `blockid` int NOT NULL,
  PRIMARY KEY (`uid`,`blockid`),
  KEY `userblacklists_ibfk_2` (`blockid`),
  CONSTRAINT `userblacklists_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `userblacklists_ibfk_2` FOREIGN KEY (`blockid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `mail` varchar(50) DEFAULT NULL,
  `level` int DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `registry` datetime DEFAULT NULL,
  `money` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  CONSTRAINT `users_chk_1` CHECK (((`status` = _utf8mb4'offline') or (`status` = _utf8mb4'online') or (`status` = _utf8mb4'busy')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* Trigger structure for table `accessingchannel` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `raiseCall` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `raiseCall` BEFORE INSERT ON `accessingchannel` FOR EACH ROW begin
    if getChannelType(new.sid, new.chname) = true then
        if not exists
            (select * from accessingChannel
            where sid = new.sid and chname = new.chname and leaveTime is null) then
            insert into calls(sid, chname, startTime)
            values (new.sid, new.chname, now());
        end if;
    end if;
end */$$


DELIMITER ;

/* Trigger structure for table `accessingchannel` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `endCall` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `endCall` AFTER UPDATE ON `accessingchannel` FOR EACH ROW begin
    if getChannelType(new.sid, new.chname) = true then
	    if not exists (select * from accessingChannel
		where sid = new.sid and chname = new.chname and leaveTime is null) then
		UPDATE calls
		SET endTime = new.leaveTime
		WHERE sid = new.sid AND chname = new.chname and endTime is null;
        end if;
    end if;
end */$$


DELIMITER ;

/* Trigger structure for table `friends` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `checkFriendBlocked` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `checkFriendBlocked` BEFORE INSERT ON `friends` FOR EACH ROW BEGIN
    IF exists (select * from userBlacklists
        where new.uid = userBlacklists.blockid and 
        new.target = userBlacklists.uid) then
        SIGNAL SQLSTATE '12451';
    else if exists(SELECT * FROM userBlacklists
        WHERE new.uid = userBlacklists.uid AND 
        new.target = userBlacklists.blockid) then
        delete from userBlacklists
        where new.uid = userBlacklists.uid AND 
        new.target = userBlacklists.blockid;
        end if;
    END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `joiningserver` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `checkServerBlocked` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `checkServerBlocked` BEFORE INSERT ON `joiningserver` FOR EACH ROW BEGIN
    IF EXISTS (SELECT * FROM serverBlacklists
    WHERE new.uid = serverBlacklists.uid AND 
    new.sid = serverBlacklists.sid) THEN
    SIGNAL SQLSTATE '12452';
    END IF;
end */$$


DELIMITER ;

/* Trigger structure for table `privatemessages` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `checkMessageBlocked` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `checkMessageBlocked` BEFORE INSERT ON `privatemessages` FOR EACH ROW BEGIN
    IF exists (select * from userBlacklists
        where new.sender = userBlacklists.blockid and 
        new.receiver = userBlacklists.uid) then
        SIGNAL SQLSTATE '12451';
    END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `serverblacklists` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `autoDeleteJoin` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `autoDeleteJoin` AFTER INSERT ON `serverblacklists` FOR EACH ROW BEGIN
    delete from joiningServer
    where joiningServer.uid = new.uid and
    joiningServer.sid = new.sid; 
END */$$


DELIMITER ;

/* Trigger structure for table `servers` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `checkLevel` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `checkLevel` BEFORE INSERT ON `servers` FOR EACH ROW BEGIN
    if (select count(*) 
        from servers 
        where creator = new.creator) >= 
        (select `level` 
        from users
        where id = new.creator) then
        signal sqlstate '12450';
    end if;
END */$$


DELIMITER ;

/* Trigger structure for table `userblacklists` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `autoDeleteFriends` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `autoDeleteFriends` AFTER INSERT ON `userblacklists` FOR EACH ROW BEGIN
    delete from `friends`
    where (friends.uid = new.uid and friends.target = new.blockid) or
    (friends.uid = new.blockid AND friends.target = new.uid);
END */$$


DELIMITER ;

/* Function  structure for function  `checkCreate` */

/*!50003 DROP FUNCTION IF EXISTS `checkCreate` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `checkCreate`(uid INT, sid INT) RETURNS tinyint(1)
    READS SQL DATA
BEGIN
    return (SELECT `groups`.canCreate FROM joiningServer, `groups`
    WHERE `joiningServer`.uid = uid AND 
    `joiningServer`.sid = sid AND
    `joiningServer`.sid = `groups`.sid and
    `joiningServer`.gname = `groups`.name);
END */$$
DELIMITER ;

/* Function  structure for function  `getChannelType` */

/*!50003 DROP FUNCTION IF EXISTS `getChannelType` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `getChannelType`(sid int, `name` varchar(50)) RETURNS tinyint(1)
    READS SQL DATA
BEGIN
    return 
        (select `type` from channels
        where channels.sid = sid and channels.name = `name`);
END */$$
DELIMITER ;

/* Function  structure for function  `getLastTime` */

/*!50003 DROP FUNCTION IF EXISTS `getLastTime` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `getLastTime`(sid INT, `name` VARCHAR(50), uid int) RETURNS datetime
    READS SQL DATA
BEGIN
    return (
        select max(leaveTime) from accessingChannel
        where accessingChannel.sid = sid and
        accessingChannel.chname = `name` and
        accessingChannel.uid = uid);
END */$$
DELIMITER ;

/* Procedure structure for procedure `getCanBan` */

/*!50003 DROP PROCEDURE IF EXISTS  `getCanBan` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getCanBan`(uid INT, sid INT)
BEGIN
    SELECT `groups`.canBan FROM joiningServer, `groups`
    WHERE `joiningServer`.uid = uid AND 
    `joiningServer`.sid = sid AND
    `joiningServer`.sid = `groups`.sid AND
    `joiningServer`.gname = `groups`.name;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getCanCreate` */

/*!50003 DROP PROCEDURE IF EXISTS  `getCanCreate` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getCanCreate`(uid INT, sid INT)
BEGIN
    SELECT `groups`.canCreate FROM joiningServer, `groups`
    WHERE `joiningServer`.uid = uid AND 
    `joiningServer`.sid = sid AND
    `joiningServer`.sid = `groups`.sid and
    `joiningServer`.gname = `groups`.name;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getCanManage` */

/*!50003 DROP PROCEDURE IF EXISTS  `getCanManage` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getCanManage`(uid INT, sid INT)
BEGIN
    SELECT `groups`.canManage FROM joiningServer, `groups`
    WHERE `joiningServer`.uid = uid AND 
    `joiningServer`.sid = sid AND
    `joiningServer`.sid = `groups`.sid AND
    `joiningServer`.gname = `groups`.name;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getCanStats` */

/*!50003 DROP PROCEDURE IF EXISTS  `getCanStats` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getCanStats`(uid INT, sid INT)
BEGIN
    SELECT `groups`.canStats FROM joiningServer, `groups`
    WHERE `joiningServer`.uid = uid AND 
    `joiningServer`.sid = sid AND
    `joiningServer`.sid = `groups`.sid AND
    `joiningServer`.gname = `groups`.name;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getComments` */

/*!50003 DROP PROCEDURE IF EXISTS  `getComments` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getComments`(id int)
begin
    select count(if(`type` = 'like', 1, null)) as likes,
        COUNT(IF(`type` = 'dislike', 1, NULL)) AS dislikes
    from commentingMessage
    where `mid` = id;
end */$$
DELIMITER ;

/* Procedure structure for procedure `getGroup` */

/*!50003 DROP PROCEDURE IF EXISTS  `getGroup` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getGroup`(uid int, sid int)
begin
    select `groups`.* from joiningServer, `groups`
    where `joiningServer`.uid = uid and 
    `joiningServer`.sid = sid and
    `joiningServer`.sid = `groups`.sid AND
    `joiningServer`.gname = `groups`.name;
end */$$
DELIMITER ;

/* Procedure structure for procedure `getNewMessages` */

/*!50003 DROP PROCEDURE IF EXISTS  `getNewMessages` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getNewMessages`(sid INT, `name` VARCHAR(50), uid int)
BEGIN
    if getChannelType(sid, `name`) = true then
        signal sqlstate '12453';
    else
        select * from messages
        where messages.sendTime >= getLastTime(sid, `name`, uid) and 
            messages.sid = sid and messages.chname = `name`;
    end if;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getServerStats` */

/*!50003 DROP PROCEDURE IF EXISTS  `getServerStats` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getServerStats`(id INT)
BEGIN
    select messageCnt, totalLength, callCnt, totalTime from
    (select COUNT(messages.id) AS messageCnt,
    SUM(LENGTH(messages.content)) AS totalLength
    from messages
    where messages.sid = id) as messageStats,
    (select COUNT(calls.id) AS callCnt,
    SEC_TO_TIME(SUM(UNIX_TIMESTAMP(calls.endTime) - UNIX_TIMESTAMP(calls.startTime))) AS totalTime
    FROM calls
    WHERE calls.sid = id) as callStats;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getVisibleCategories` */

/*!50003 DROP PROCEDURE IF EXISTS  `getVisibleCategories` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getVisibleCategories`(uid int, sid int)
begin
    select categories.* from joiningServer, categoryvisible, categories
    where joiningServer.uid = uid and joiningServer.sid = sid and
    categoryvisible.sid = sid and categoryvisible.gname = joiningServer.gname and
    categories.name = cname;
end */$$
DELIMITER ;

/* Procedure structure for procedure `insertComment` */

/*!50003 DROP PROCEDURE IF EXISTS  `insertComment` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `insertComment`(uid INT, `mid` int, `type` varchar(10))
BEGIN
    IF exists (
        select * from commentingMessage
        where commentingMessage.uid = uid and
        commentingMessage.mid = `mid`) THEN
        DELETE FROM commentingMessage
        WHERE  commentingMessage.uid = uid AND
        commentingMessage.mid = `mid`;
        if (select commentingMessage.`type` from commentingMessage
            where commentingMessage.uid = uid AND
            commentingMessage.mid = `mid`) != `type` then
            insert into commentingMessage
            values (uid, `mid`, `type`);
        end if;
    else 
        INSERT INTO commentingMessage
        VALUES (uid, `mid`, `type`);
    end if;
END */$$
DELIMITER ;

/* Procedure structure for procedure `upgrade` */

/*!50003 DROP PROCEDURE IF EXISTS  `upgrade` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `upgrade`(id int)
begin
    if (select money from users 
        where users.id = id) > 100 then
    update users
    set `level` = `level` + 1, money = money - 100
    where users.id = id;
    else 
        signal sqlstate '12455';
    end if;
end */$$
DELIMITER ;

/*Table structure for table `publicserver` */

DROP TABLE IF EXISTS `publicserver`;

/*!50001 DROP VIEW IF EXISTS `publicserver` */;
/*!50001 DROP TABLE IF EXISTS `publicserver` */;

/*!50001 CREATE TABLE  `publicserver`(
 `id` int ,
 `name` varchar(50) ,
 `creator` int 
)*/;

/*Table structure for table `userbasicinfo` */

DROP TABLE IF EXISTS `userbasicinfo`;

/*!50001 DROP VIEW IF EXISTS `userbasicinfo` */;
/*!50001 DROP TABLE IF EXISTS `userbasicinfo` */;

/*!50001 CREATE TABLE  `userbasicinfo`(
 `id` int ,
 `name` varchar(20) 
)*/;

/*View structure for view publicserver */

/*!50001 DROP TABLE IF EXISTS `publicserver` */;
/*!50001 DROP VIEW IF EXISTS `publicserver` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `publicserver` AS select `servers`.`id` AS `id`,`servers`.`name` AS `name`,`servers`.`creator` AS `creator` from `servers` where (`servers`.`isPrivate` = false) */;

/*View structure for view userbasicinfo */

/*!50001 DROP TABLE IF EXISTS `userbasicinfo` */;
/*!50001 DROP VIEW IF EXISTS `userbasicinfo` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `userbasicinfo` AS select `users`.`id` AS `id`,`users`.`name` AS `name` from `users` */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
