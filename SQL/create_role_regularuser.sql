CREATE ROLE regularUser;
GRANT SELECT ON assignment.userbasicinfo TO regularUser;
GRANT SHOW VIEW ON assignment.userbasicinfo TO regularUser;