export function getInfoValueByName(infoList, infoName){
  for (var i = 0; i < infoList.length; i++){
    if (infoList[i].info_name === infoName)
      return infoList[i].info_value;
  }
  return "********";
}

export function selectUserFromListById(user_list, user_id){
  for (var i = 0; i < user_list.length; i++){
    if (user_list[i].id === user_id)
      return user_list[i];
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

export const teamUserRoles = {
  '1': 'Member',
  '2': 'Admin',
  '3': 'Owner'
};

export const passwordChangeValues = {
  "0": 'never',
  "1": "1 months",
  "3": "3 months",
  "6": "6 months",
  "12": "12 months"
};