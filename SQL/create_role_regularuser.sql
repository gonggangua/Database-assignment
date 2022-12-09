CREATE ROLE regularUser;
GRANT SELECT, SHOW VIEW ON assignment.userbasicinfo TO regularUser;
GRANT SELECT, SHOW VIEW ON assignment.publicServer TO regularUser;
