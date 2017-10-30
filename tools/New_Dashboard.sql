DROP TABLE pendingTeamUserVerifications;
ALTER TABLE teamUsers ADD COLUMN invitation_code VARCHAR(255);
UPDATE teamUsers t LEFT JOIN pendingTeamInvitations t1 ON t.id = t1.teamUser_id SET t.invitation_code = t1.code;
DROP TABLE pendingTeamInvitations;

DELETE t1
FROM classicApps AS t1, classicApps AS t2
WHERE t1.website_app_id = t2.website_app_id AND t1.id <> t2.id;

DELETE FROM websiteApps
WHERE type LIKE 'logWithApp' AND id IN (SELECT website_app_id
                                        FROM classicApps);
DELETE FROM logWithApps
WHERE logWith_website_app_id IN (SELECT id
                                 FROM websiteApps
                                 WHERE type LIKE 'classicApp' AND id NOT IN (SELECT website_app_id
                                                                             FROM classicApps));
DELETE FROM websiteApps
WHERE type LIKE 'classicApp' AND id NOT IN (SELECT website_app_id
                                            FROM classicApps);
DELETE FROM classicApps
WHERE website_app_id IN (SELECT id
                         FROM websiteApps
                         WHERE type LIKE 'websiteApp');

DELETE FROM websiteApps
WHERE type LIKE 'websiteApp' AND id IN (SELECT website_app_id
                                        FROM classicApps);
DELETE FROM websiteApps
WHERE type LIKE 'logWithApp' AND id NOT IN (SELECT website_app_id
                                            FROM logWithApps);
DELETE FROM profileAndAppMap
WHERE app_id IN (SELECT id
                 FROM apps
                 WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                             FROM websiteApps));
DELETE FROM sharedApps
WHERE shareable_app_id IN (SELECT id
                           FROM shareableApps
                           WHERE id IN (SELECT id
                                        FROM apps
                                        WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                                                    FROM websiteApps)));
DELETE FROM sharedApps
WHERE id IN (SELECT id
             FROM apps
             WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                         FROM websiteApps));
DELETE FROM shareableApps
WHERE id IN (SELECT id
             FROM apps
             WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                         FROM websiteApps));
DELETE FROM apps
WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                            FROM websiteApps);
DELETE FROM apps
WHERE type LIKE 'linkApp' AND id NOT IN (SELECT app_id
                                         FROM linkApps);
DELETE FROM appsInformations
WHERE id NOT IN (SELECT app_info_id
                 FROM apps);
DELETE FROM accountsInformations
WHERE account_id IN (SELECT id
                     FROM accounts
                     WHERE id NOT IN (SELECT account_id
                                      FROM classicApps));
DROP TABLE sharedKeys, tmpSharedApps;
DELETE FROM accounts
WHERE id NOT IN (SELECT account_id
                 FROM classicApps);
