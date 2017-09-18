var React = require('react');
import {connect} from "react-redux";
import {showTeamMenu} from "../actions/teamActions";
import * as channelActions from "../actions/channelActions";
import * as userActions from "../actions/userActions";
import * as teamModalsActions from "../actions/teamModalActions";
import {isAdmin} from "../utils/helperFunctions";
import { NavLink,withRouter} from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import {findDOMNode} from 'react-dom';

function ChannelList(props){
  return (
      <div className="section-holder display-flex flex_direction_column" id="team_channels">
        {isAdmin(props.me.role) &&
        <button className="heading-button button-unstyle"
                ref={(ref) => {window.refs.roomAdd = ref}}
                data-tip="Create Rooms"
                data-place="top"
                id="new_channel_button"
                onClick={e => {props.dispatch(teamModalsActions.showAddTeamChannelModal(true))}}>
          <i className="fa fa-plus-square-o"/>
        </button>}
        <NavLink to={`/teams/${props.match.params.teamId}/${props.match.params.itemId}/rooms`} className="section-header">
          <span class="inline-tooltipped" data-tip="Browse all Rooms" data-place="right">
            Rooms&nbsp;
          </span>
          <span className="inline-tooltipped header-count" data-tip="Browse all Rooms"  data-place="right" ref={(ref) => {window.refs.rooms = ref}}> ({props.items.length})</span>
        </NavLink>
        <div className="section-list">
          {
            props.items.map(function(channel){
              if (channel.userIds.indexOf(props.me.id) !== -1)
                return (
                  <NavLink to={`/teams/${props.team_id}/${channel.id}`} className="section-list-item channel" key={channel.id}>
                    <div className="primary_action channel_name">
                      <i className="fa fa-hashtag prefix"/>
                      <span className="overflow-ellipsis full_flex">{channel.name}</span>
                      <span class="inline-notification" style={{display:'none'}}>3</span>
                    </div>
                  </NavLink>);
              else
                return null
            }, this)
          }
        </div>
      </div>
  )
}

function UserList(props){
  return (
      <div className="section-holder display-flex flex_direction_column" id="team_channels">
        {isAdmin(props.me.role) &&
        <button className="heading-button button-unstyle"
                data-tip="Invite new user"
                data-place="top"
                id="new_member_button"
                onClick={e => {props.dispatch(teamModalsActions.showAddTeamUserModal(true))}}>
          <i className="ease-icon fa fa-plus-square-o"/>
        </button>}
        <NavLink to={`/teams/${props.match.params.teamId}/${props.match.params.itemId}/members`} className="section-header">
          <span class="inline-tooltipped" data-tip="Open a Desk" data-place="right">
            Desks
          </span>
          <span className="header-count"> ({props.items.length})</span>
        </NavLink>
        <div className="section-list">
          {
            props.items.map(function(user){
              return (
                  <NavLink to={`/teams/${props.team_id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                    <div className="primary_action channel_name">
                      <i className="fa fa-user prefix"/>
                      <span className="overflow-ellipsis">{user.username}</span>
                    </div>
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
    const me = this.props.me;
    return (
        <div className="client_channels_container">
          <div id="team_menu" onClick={e => {this.props.dispatch(showTeamMenu(true))}}>
            <div className="team_name_container">
              {this.props.team_name}
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
                  selectedItem={this.props.selectedItem}
                  match={this.props.match}
                  team_id={this.props.team_id}
                  items={this.props.channels}
                  dispatch={this.props.dispatch}/>
              <UserList
                  me={me}
                  team_id={this.props.team_id}
                  match={this.props.match}
                  selectedItem={this.props.selectedItem}
                  items={this.props.users}
                  dispatch={this.props.dispatch}/>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamSideBar);