USE ease;
DELETE FROM logWithApps
WHERE logWithWebsite_id IN (SELECT app_id
                            FROM teamCardReceivers);
DELETE FROM classicApps
WHERE id IN (SELECT app_id
             FROM teamCardReceivers);
DELETE FROM linkApps
WHERE id IN (SELECT app_id
             FROM teamCardReceivers);
DELETE FROM teamSingleCardReceivers;
DELETE FROM teamEnterpriseCardReceivers;
DELETE FROM teamLinkCardReceivers;
DELETE FROM teamCardReceivers;
DELETE FROM websiteApps
WHERE id NOT IN (SELECT id
                 FROM classicApps) AND id NOT IN (SELECT id
                                                  FROM logWithApps) AND id NOT IN (SELECT logWith_website_app_id
                                                                                   FROM logWithApps);
DELETE FROM apps
WHERE id NOT IN (SELECT id
                 FROM websiteApps) AND id NOT IN (SELECT id
                                                  FROM linkApps);
DELETE FROM accountsInformations
WHERE account_id NOT IN (SELECT id
                         FROM accounts
                         WHERE id NOT IN (SELECT account_id
                                          FROM classicApps) AND id NOT IN (SELECT account_id
                                                                           FROM teamSingleCards) AND
                               id NOT IN (SELECT account_id
                                          FROM joinTeamEnterpriseCardRequests));
DELETE FROM accounts
WHERE id NOT IN (SELECT account_id
                 FROM classicApps) AND id NOT IN (SELECT account_id
                                                  FROM teamSingleCards) AND id NOT IN (SELECT account_id
                                                                                       FROM
                                                                                         joinTeamEnterpriseCardRequests);
DELETE FROM appsInformations
WHERE id NOT IN (SELECT app_info_id
                 FROM apps);
DELETE FROM linkAppInformations
WHERE id NOT IN (SELECT link_app_info_id
                 FROM linkApps);
DELETE FROM pendingJoinChannelRequests;
DELETE FROM joinTeamEnterpriseCardRequests;
DELETE FROM joinTeamEnterpriseCardRequests;
DELETE FROM joinTeamCardRequests;
DELETE FROM teamSingleCards;
DELETE FROM teamEnterpriseCards;
DELETE FROM teamLinkCards;
DELETE FROM teamWebsiteCards;
DELETE FROM teamCards;
DELETE FROM pendingTeamCreations;
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