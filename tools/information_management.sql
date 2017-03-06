ALTER TABLE websitesInformations
ADD priority TINYINT NOT NULL;

INSERT INTO websitesInformations
SELECT null, id, 'password', 'password', 2 FROM websites WHERE noLogin = 0;

INSERT INTO accountsInformations
SELECT null, id, 'password', password FROM accounts;

ALTER TABLE accounts
DROP COLUMN password;

UPDATE websitesInformations SET priority = 1 WHERE information_name = 'login';
UPDATE websitesInformations SET priority = 2 WHERE information_name = 'password';
UPDATE websitesInformations SET priority = 0 WHERE information_name = 'team';