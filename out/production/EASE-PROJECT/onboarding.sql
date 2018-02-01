CREATE TABLE USER_PERSONAL_INFORMATION (
  id         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255)     NOT NULL DEFAULT '',
  last_name  VARCHAR(255)     NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

INSERT INTO USER_PERSONAL_INFORMATION SELECT
                                        id,
                                        '',
                                        ''
                                      FROM users;

ALTER TABLE users
  ADD COLUMN personal_information_id INT(10) UNSIGNED NOT NULL;
UPDATE users
SET personal_information_id = id;

UPDATE USER_PERSONAL_INFORMATION upi
  JOIN users u ON u.personal_information_id = upi.id
  JOIN teamUsers tU ON u.id = tU.user_id
SET upi.first_name = tU.firstName, upi.last_name = tU.lastName;

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE users
  ADD FOREIGN KEY (personal_information_id) REFERENCES USER_PERSONAL_INFORMATION (id);
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE teamUsers
  DROP COLUMN firstName;
ALTER TABLE teamUsers
  DROP COLUMN lastName;
ALTER TABLE teamUsers
  DROP COLUMN jobTitle;

ALTER TABLE users CHANGE COLUMN firstName username VARCHAR(255) NOT NULL DEFAULT '';