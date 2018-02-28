ALTER TABLE status ADD COLUMN registered TINYINT(1) NOT NULL DEFAULT 0;
UPDATE status SET registered = 1;

ALTER TABLE userKeys ADD COLUMN access_code_hash TEXT;
ALTER TABLE userKeys CHANGE COLUMN password password TEXT;