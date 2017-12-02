var React = require('react');
import {connect} from "react-redux";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {showTeamMenu} from "../actions/teamActions";
import * as channelActions from "../actions/channelActions";
import * as userActions from "../actions/userActions";
import * as teamModalsActions from "../actions/teamModalActions";
import {isAdmin} from "../utils/helperFunctions";
import { NavLink,withRouter} from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import {findDOMNode} from 'react-dom';

function ChannelList(props){
  const {team, me} = props;
  const rooms = team.rooms;
  const myRooms = me.room_ids.map(id => {
    return rooms[id];
  }).sort((a, b) => {
    if (a.default)
      return -1000;
    else if (b.default)
      return 1000;
    return a.name.localeCompare(b.name);
  });
  return (
      <div className="section-holder display-flex flex_direction_column" id="team_channels">
        {isAdmin(props.me.role) &&
        <button className="heading-button button-unstyle"
                ref={(ref) => {window.refs.roomAdd = ref}}
                data-tip="Create Rooms"
                data-place="top"
                id="new_channel_button"
                onClick={e => {props.dispatch(teamModalsActions.showAddTeamChannelModal({active: true, team_id: team.id}))}}>
          <i className="fa fa-plus-circle"/>
        </button>}
        <NavLink to={`/teams/${props.match.params.teamId}/${props.match.params.itemId}/rooms`} className="section-header">
          <span class="inline-tooltipped" data-tip="Browse all Rooms" data-place="right">
            Rooms&nbsp;
          </span>
          <span className="inline-tooltipped header-count" data-tip="Browse all Rooms"  data-place="right" ref={(ref) => {window.refs.rooms = ref}}> ({Object.keys(rooms).length})</span>
        </NavLink>
        <div className="section-list">
          {
            myRooms.map(room => {
              return (
                  <NavLink to={`/teams/${team.id}/${room.id}`} className="section-list-item channel" key={room.id}>
                    <div className="primary_action channel_name">
                      <i className="fa fa-hashtag prefix"/>
                      <span className="overflow-ellipsis full_flex">{room.name}</span>
                    </div>
                  </NavLink>)
            }, this)
          }
        </div>
      </div>
  )
}

function UserList(props){
  const {team, me} = props;
  const user_list = Object.keys(team.team_users).map(user_id => {
    return team.team_users[user_id];
  });
  return (
      <div className="section-holder display-flex flex_direction_column" id="team_channels">
        {isAdmin(me.role) &&
        <button className="heading-button button-unstyle"
                data-tip="Invite new user"
                data-place="top"
                id="new_member_button"
                onClick={e => {props.dispatch(teamModalsActions.showAddTeamUserModal({active: true, team_id: team.id}))}}>
          <i className="ease-icon fa fa-plus-circle"/>
        </button>}
        <NavLink to={`/teams/${props.match.params.teamId}/${props.match.params.itemId}/members`} className="section-header">
          <span class="inline-tooltipped" data-tip="Open a Desk" data-place="right">
            Users
          </span>
          <span className="header-count"> ({user_list.length})</span>
        </NavLink>
        <div className="section-list">
          {
            user_list.map(function(user){
              return (
                  <NavLink to={`/teams/${team.id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                    {user.state >= 1 ?
                      <div className="primary_action channel_name">
                        <i className="fa fa-user prefix"/>
                        <span className="overflow-ellipsis">{user.username}</span>
                      </div>
                    :
                      <div className="primary_action channel_name">
                        <i className="fa fa-user-o prefix"/>
                        <span className="overflow-ellipsis userNotAccepted">{user.username}</span>
                      </div>}
                  </NavLink>
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
    team_id: store.team.id,
    users: store.users.users,
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
    const team = this.props.team;
    const me = team.team_users[team.my_team_user_id];

    return (
        <div class="client_channels_container">
          <div id="team_menu" onClick={this.props.openMenu}>
            <div className="team_name_container">
              {team.name}
            </div>
            <div className="team_client_user">
              <i className="fa fa-square icon_left"/>
              {me.username}
            </div>
          </div>
          <div id="col_channels">
            <div id="col_channels_scroller">
              <ChannelList
                  me={me}
                  team={team}
                  match={this.props.match}
                  dispatch={this.props.dispatch}/>
              <UserList
                  me={me}
                  team={team}
                  match={this.props.match}
                  dispatch={this.props.dispatch}/>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamSideBar);