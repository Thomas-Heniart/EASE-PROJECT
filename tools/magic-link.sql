/* ALTER TABLE teamSingleCards DROP COLUMN magicLink;
ALTER TABLE teamSingleCards DROP COLUMN magicLinkExpirationDate;
ALTER TABLE teamSingleSoftwareCards DROP COLUMN magicLink;
ALTER TABLE teamSingleSoftwareCards DROP COLUMN magicLinkExpirationDate; */

ALTER TABLE teamSingleCards ADD COLUMN magicLink TEXT;
ALTER TABLE teamSingleCards ADD COLUMN magicLinkExpirationDate DATETIME;
ALTER TABLE teamSingleSoftwareCards ADD COLUMN magicLink TEXT;
ALTER TABLE teamSingleSoftwareCards ADD COLUMN magicLinkExpirationDate DATETIME;

ALTER TABLE teamCards ADD COLUMN team_user_sender_id INT(10) UNSIGNED;
ALTER TABLE teamCards ADD CONSTRAINT teamcards_ibfk_4 FOREIGN KEY (team_user_sender_id) REFERENCES teamUsers(id);