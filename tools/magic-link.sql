ALTER TABLE teamSingleCards DROP COLUMN magicLink;
ALTER TABLE teamSingleCards DROP COLUMN magicLinkExpirationDate;
ALTER TABLE teamSingleSoftwareCards DROP COLUMN magicLink;
ALTER TABLE teamSingleSoftwareCards DROP COLUMN magicLinkExpirationDate;

ALTER TABLE teamSingleCards ADD COLUMN magicLink TEXT;
ALTER TABLE teamSingleCards ADD COLUMN magicLinkExpirationDate DATETIME;
ALTER TABLE teamSingleSoftwareCards ADD COLUMN magicLink TEXT;
ALTER TABLE teamSingleSoftwareCards ADD COLUMN magicLinkExpirationDate DATETIME;