DELETE FROM enterpriseAppAttributes;
DELETE FROM pendingJoinAppRequests;
DELETE classicApps FROM classicApps
  JOIN websiteApps ON classicApps.website_app_id = websiteApps.id
  JOIN apps ON websiteApps.app_id = apps.id
  JOIN sharedApps ON apps.id = sharedApps.id;
DELETE websiteApps FROM websiteApps
  JOIN apps ON websiteApps.app_id = apps.id
  JOIN sharedApps ON apps.id = sharedApps.id;
DELETE linkApps FROM linkApps
  JOIN apps ON linkApps.app_id = apps.id
  JOIN sharedApps ON apps.id = sharedApps.id;
DELETE classicApps FROM classicApps
  JOIN websiteApps ON classicApps.website_app_id = websiteApps.id
  JOIN apps ON websiteApps.app_id = apps.id
  JOIN shareableApps ON apps.id = shareableApps.id;
DELETE websiteApps FROM websiteApps
  JOIN apps ON websiteApps.app_id = apps.id
  JOIN shareableApps ON apps.id = shareableApps.id;
DELETE linkApps FROM linkApps
  JOIN apps ON linkApps.app_id = apps.id
  JOIN shareableApps ON apps.id = shareableApps.id;
DELETE FROM sharedApps;
DELETE FROM shareableApps;
DELETE FROM profileAndAppMap
WHERE app_id IN (SELECT id
                 FROM apps
                 WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                             FROM websiteApps));
DELETE FROM profileAndAppMap
WHERE app_id IN (SELECT id
                 FROM apps
                 WHERE type LIKE 'linkApp' AND id NOT IN (SELECT app_id
                                                          FROM websiteApps));
DELETE FROM apps
WHERE type LIKE 'linkApp' AND id NOT IN (SELECT app_id
                                         FROM linkApps);
DELETE FROM apps
WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                            FROM ease.websiteApps);
DELETE FROM pendingJoinChannelRequests;
DELETE FROM pendingJoinTeamRequests;
DELETE FROM pendingTeamCreations;
DELETE FROM pendingteamuserverifications;
DELETE FROM pendingTeamInvitations;
DELETE FROM userAndEmailInvitationsMap;
DELETE FROM notifications;
DELETE FROM channelAndTeamUserMap;
DELETE FROM teamAndWebsiteMap;
DELETE FROM channels;
UPDATE teamUsers
SET admin_id = NULL;
DELETE FROM teamUsers;
DELETE FROM teamUserStatus;
DELETE FROM teamUserRoles;
DELETE FROM teamCredit;
DELETE FROM teamAndEmailInvitationsMap;
DELETE FROM teams;