INSERT INTO ease.sso
SELECT * FROM test.sso;


INSERT INTO ease.websiteAttributes
SELECT null, locked, lockedExpiration, 1, hidden FROM test.websites;

SET @var := 0;

INSERT INTO ease.websites
SELECT website_id, website_url, website_name, folder, sso, noLogin, website_homepage, ratio, position, insertDate, (@var := @var + 1) FROM test.websites ;

INSERT INTO ease.websitesInformations
SELECT * FROM test.websitesInformations;

INSERT INTO ease.loginWithWebsites
SELECT null, website_id FROM test.websites WHERE haveLoginButton = 1;

INSERT INTO ease.websitesLogWithMap
SELECT null, website_id, 1 FROM test.websites WHERE FIND_IN_SET('7', haveLoginWith) > 0;

INSERT INTO ease.websitesLogWithMap
SELECT null, website_id, 2 FROM test.websites WHERE FIND_IN_SET('28', haveLoginWith) > 0;
