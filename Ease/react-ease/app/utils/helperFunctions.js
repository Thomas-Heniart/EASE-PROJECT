export function getInfoValueByName(infoList, infoName){
  for (var i = 0; i < infoList.length; i++){
    if (infoList[i].info_name === infoName)
      return infoList[i].info_value;
  }
  return "********";
}

export function selectItemFromListById(list, id){
  for (let i = 0; i < list.length; i++){
    if (list[i].id === id)
      return list[i];
  }
  return null;
}

export function selectUserFromListById(user_list, user_id){
  for (var i = 0; i < user_list.length; i++){
    if (user_list[i].id === user_id)
      return user_list[i];
  }
  return null;
}

export function selectAppFromListById(app_list, app_id){
  for (var i = 0; i < app_list.length; i++){
    if (app_list[i].id === app_id)
      return app_list[i];
  }
  return null;
}

export function selectChannelFromListById(channels, channelId){
  for (var i = 0; i < channels.length; i++){
    if (channels[i].id === channelId)
      return channels[i];
  }
  return null;
}

export function getChannelUsers(channels, channelId, users){
  var channel = selectChannelFromListById(channels, channelId);
  var ret = [];

  channel.userIds.map(function(item){
    ret.push(selectUserFromListById(users, item));
  });
  return ret;
}

export function isUserInChannel(channel, user){
  for (var i = 0; i < channel.userIds.length; i++){
    if (channel.userIds[i] === user.id)
      return true;
  }
  return false;
}

export function findMeInReceivers(receivers, myId){
  for (var i = 0; i < receivers.length; i++){
    if (receivers[i].team_user_id === myId)
      return receivers[i];
  }
  return null;
}

export function getReceiverInList(receivers, id){
  for (var i = 0; i < receivers.length; i++){
    if (receivers[i].team_user_id === id)
      return receivers[i];
  }
  return null;
}

export function isUserInList(users, id){
  for (var i = 0; i < users.length; i++){
    if (users[i].id === id)
      return true;
  }
  return false;
}

export function checkForNewNotifications(n){
  for (let i = 0; i < n.length; i++){
    if (n[i].is_new)
      return true;
  }
  return false;
}

export function isAdminOrMe(user, me){
  const admin = isAdmin(me.role);
  return admin || !admin && user.id === me.id;
}

export function isSuperior(user, me){
  return me.role > user.role;
}
export function isSuperiorOrMe(user, me){
  return isSuperior(user, me) || user.id === me.id;
}

export function isAdmin(userRole){
  return userRole > 1;
}

export function isOwner(userRole){
  return userRole > 2;
}

export const passwordChangeValues = {
  "0": 'never',
  "1": "1 months",
  "3": "3 months",
  "6": "6 months",
  "12": "12 months"
};