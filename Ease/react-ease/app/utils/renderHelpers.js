export const renderUserLabel = (label, index, props) => ({
  color: 'blue',
  content: label.username,
  icon: 'user'
});

export const renderRoomLabel = (label, index, props) => ({
  color: 'blue',
  content: label.name,
  icon: 'users'
});