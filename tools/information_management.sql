ALTER TABLE websitesInformations
ADD priority TINYINT NOT NULL;


ALTER TABLE websitesInformations
ADD placeholder VARCHAR(25) NOT NULL;

ALTER TABLE websitesInformations
ADD placeholder_icon VARCHAR(25) NOT NULL;

UPDATE websitesInformations SET placeholder = 'Login', placeholder_icon = 'fa-user-o' WHERE information_name = 'login';
UPDATE websitesInformations SET placeholder = 'Password', placeholder_icon = 'fa-lock' WHERE information_name = 'password';
UPDATE websitesInformations SET placeholder = 'Team', placeholder_icon = 'fa-slack' WHERE information_name = 'team';

INSERT INTO websitesInformations
SELECT null, id, 'password', 'password', 2 FROM websites WHERE noLogin = 0;

INSERT INTO accountsInformations
SELECT null, id, 'password', password FROM accounts;

ALTER TABLE accounts
DROP COLUMN password;

UPDATE websitesInformations SET priority = 1 WHERE information_name = 'login';
UPDATE websitesInformations SET priority = 2 WHERE information_name = 'password';
UPDATE websitesInformations SET priority = 0 WHERE information_name = 'team';