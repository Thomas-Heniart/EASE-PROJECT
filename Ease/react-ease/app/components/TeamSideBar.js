var React = require('react');
import {connect} from "react-redux";
import {showTeamMenu} from "../actions/teamActions";
import * as channelActions from "../actions/channelActions";
import * as userActions from "../actions/userActions";
import * as teamModalsActions from "../actions/teamModalActions";
import ReactTooltip from 'react-tooltip';
import {findDOMNode} from 'react-dom';

function ChannelList(props){
  return (
      <div className="section-holder" id="team_channels">
        <button className="heading-button button-unstyle"
                ref={(ref) => {window.refs.roomAdd = ref}}
                data-tip="Create Room"
                data-place="top"
                id="new_channel_button"
                onClick={e => {props.dispatch(teamModalsActions.showAddTeamChannelModal(true))}}>
          <i className="fa fa-plus-square-o"/>
        </button>
        <div className="section-header" onClick={e => {props.dispatch(teamModalsActions.showTeamBrowseChannelsModal(true))}}>
          <span class="inline-tooltipped" data-tip="Browse all Rooms" data-place="right">
            Rooms&nbsp;
          </span>
          <span className="inline-tooltipped header-count" data-tip="Browse all Rooms"  data-place="right" ref={(ref) => {window.refs.rooms = ref}}> ({props.items.length})</span>
        </div>
        <div className="section-list">
          {
            props.items.map(function(channel){
              return (
                  <li onClick={(e) => {props.dispatch(channelActions.selectTeamChannel(channel.id))}} className={props.selectedItem.type === 'channel' && props.selectedItem.item.id === channel.id ? "section-list-item channel active" : "section-list-item channel"} key={channel.id}>
                    <div className="primary_action channel_name">
                      <i className="fa fa-users prefix"/>
                      <span className="overflow-ellipsis full_flex">{channel.name}</span>
                      <span class="inline-notification">3</span>
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
        <button className="heading-button button-unstyle"
                data-tip="Invite new user"
                data-place="top"
                id="new_member_button"
                onClick={e => {props.dispatch(teamModalsActions.showAddTeamUserModal(true))}}>
          <i className="ease-icon fa fa-plus-square-o"/>
        </button>
        <div className="section-header">
          <span class="inline-tooltipped" data-tip="Open a Personal Space" data-place="right">
            People
          </span>
          <span className="header-count"> ({props.items.length})</span>
        </div>
        <div className="section-list">
          {
            props.items.map(function(user){
              return (
                  <li onClick={(e) => {props.dispatch(userActions.selectTeamUser(user.id))}} className={props.selectedItem.type === 'user' && props.selectedItem.item.id === user.id ? "section-list-item channel active" : "section-list-item channel"} key={user.id}>
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

@connect((store)=>{
  return {
    team_name: store.team.name,
    users: store.users.users,
    me: store.users.me,
    channels: store.channels.channels,
    selectedItem: store.selection
  };
})
class TeamSideBar extends React.Component{
  constructor(props){
    super(props);
  }
  componentDidMount(){
    ReactTooltip.rebuild();
  }
  render() {
    return (
        <div className="client_channels_container">
          <div id="team_menu" onClick={e => {this.props.dispatch(showTeamMenu(true))}}>
            <div className="team_name_container">
              {this.props.team_name}
            </div>
            <div className="team_client_user">
              <i className="fa fa-square icon_left"/>
              {this.props.me.username}
            </div>
          </div>
          <div id="col_channels">
            <div id="col_channels_scroller">
              <ChannelList
                  selectedItem={this.props.selectedItem}
                  items={this.props.channels}
                  dispatch={this.props.dispatch}/>
              <UserList
                  selectedItem={this.props.selectedItem}
                  items={this.props.users}
                  dispatch={this.props.dispatch}/>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamSideBar;