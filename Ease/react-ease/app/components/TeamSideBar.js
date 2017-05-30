var React = require('react');

function TeamSideBar(props){
  return (
      <div className="client_channels_container">
        <div id="team_menu">
          <div className="team_name_container">
            {props.team_name}
          </div>
          <div className="team_client_user">
            <i className="fa fa-square icon_left"/>
            {props.me.username}
          </div>
        </div>
        {props.children}
      </div>
  )
}

function ChannelList(props){
  return (
      <div className="section-holder" id="team_channels">
        <button className="heading-button button-unstyle" id="new_channel_button" onClick={props.toggleAddChannelModal}>
          <i className="fa fa-plus-square-o"/>
        </button>
        <div className="section-header">
                                    <span>
                                        Teams
                                    </span>
          <span className="header-count"> ({props.items.length})</span>
        </div>
        <div className="section-list">
          {
            props.items.map(function(channel){
              return (
                  <li onClick={props.selectFunc.bind(null, {type: 'channel', id : channel.id, item: channel})} className={props.selectedItem.type === 'channel' && props.selectedItem.id === channel.id ? "section-list-item channel active" : "section-list-item channel"} key={channel.id}>
                    <div className="primary_action channel_name">
                      <i className="fa fa-users prefix"/>
                      <span className="overflow-ellipsis">{channel.name}</span>
                    </div>
                  </li>
              )
            }, this)
          }
        </div>
      </div>
  )
}

function UserList(props){
  return (
      <div className="section-holder" id="team_channels">
        <button className="heading-button button-unstyle" id="new_member_button" onClick={props.toggleAddUserModal}>
          <i className="ease-icon fa fa-plus-square-o"/>
        </button>
        <div className="section-header">
                                    <span>
                                        Members
                                    </span>
          <span className="header-count"> ({props.items.length})</span>
        </div>
        <div className="section-list">
          {
            props.items.map(function(user){
              return (
                  <li onClick={props.selectFunc.bind(null, {type: 'user', id : user.id, item: user})} className={props.selectedItem.type === 'user' && props.selectedItem.id === user.id ? "section-list-item channel active" : "section-list-item channel"} key={user.id}>
                    <div className="primary_action channel_name">
                      <i className="fa fa-user prefix"/>
                      <span className="overflow-ellipsis">{user.username}</span>
                    </div>
                  </li>
              )
            }, this)
          }
        </div>
      </div>
  )
}

module.exports = {
  TeamSideBar : TeamSideBar,
  UserList : UserList,
  ChannelList : ChannelList
};