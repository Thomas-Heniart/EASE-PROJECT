CREATE TABLE USER_PERSONAL_INFORMATION (
  id         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255)     NOT NULL DEFAULT '',
  last_name  VARCHAR(255)     NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

INSERT INTO USER_PERSONAL_INFORMATION SELECT
                                        users.id,
                                        teamUsers.firstName,
                                        teamUsers.lastName
                                      FROM users
                                        LEFT JOIN teamUsers ON users.id = teamUsers.user_id;

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE users
  ADD COLUMN personal_information_id INT(10) UNSIGNED NOT NULL;
UPDATE users
SET personal_information_id = id;
ALTER TABLE users
  ADD FOREIGN KEY (personal_information_id) REFERENCES USER_PERSONAL_INFORMATION (id);
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE teamUsers
  DROP COLUMN firstName;
ALTER TABLE teamUsers
  DROP COLUMN lastName;
ALTER TABLE teamUsers
  DROP COLUMN jobTitle;