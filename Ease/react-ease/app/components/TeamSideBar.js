import React, {Component, Fragment} from "react";
import classnames from "classnames";
import {connect} from "react-redux";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {showTeamMenu} from "../actions/teamActions";
import * as channelActions from "../actions/channelActions";
import * as userActions from "../actions/userActions";
import * as teamModalsActions from "../actions/teamModalActions";
import {isAdmin} from "../utils/helperFunctions";
import { NavLink,withRouter} from 'react-router-dom';
import {findDOMNode} from 'react-dom';
import CircularProgressbar from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import {Popup, Icon} from 'semantic-ui-react';


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
        <Popup size="mini"
               position="right center"
               inverted
               trigger={
                 <button className="heading-button button-unstyle"
                         id="new_channel_button"
                         onClick={e => {props.dispatch(teamModalsActions.showAddTeamChannelModal({active: true, team_id: team.id}))}}>
                   <i className="fa fa-plus-circle"/>
                 </button>
               }
               content={'Create Rooms'}/>
        }
        <NavLink to={`/teams/${props.match.params.teamId}/${props.match.params.itemId}/rooms`} className="section-header">
          <Popup size="mini"
                 position="right center"
                 inverted
                 trigger={
                   <span class="display-inline-block">
                      <span class="inline-tooltipped">
                        Rooms&nbsp;
                      </span>
                      <span className="inline-tooltipped header-count">({Object.keys(rooms).length})</span>
                    </span>
                 }
                 content={'Browse all Rooms'}/>
        </NavLink>
        <div className="section-list">
          {
            myRooms.map(room => {
              return (
                  <NavLink to={`/teams/${team.id}/${room.id}`} class={classnames('section-list-item channel', room.default ? 'default' : null)} key={room.id}>
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

function UserList({team, me, dispatch, match}){
  const user_list = Object.keys(team.team_users).map(user_id => {
    return team.team_users[user_id];
  }).sort((a, b) => {
    if (a.state !== b.state)
      return b.state - a.state;
    if (a.invitation_sent !== b.invitation_sent)
      return b.invitation_sent - a.invitation_sent;
    return a.username.localeCompare(b.username);
  });
  const maxSeats = 10 + team.extra_members;
  const invitedUsers = Object.keys(team.team_users).reduce((stack, team_user_id) => {
    if (team.team_users[team_user_id].invitation_sent)
      return ++stack;
    return stack;
  }, 0);
  const meAdmin = isAdmin(me.role);
  const freeTeam = team.plan_id === 0;
  const userLimitReached = maxSeats <= invitedUsers;

  return (
      <div className="section-holder display-flex flex_direction_column" id="team_users">
        {meAdmin &&
        <Popup size="mini"
               position="right center"
               inverted
               trigger={
                 <button className="heading-button button-unstyle"
                         id="new_member_button"
                         onClick={e => {dispatch(teamModalsActions.showAddTeamUserModal({active: true, team_id: team.id}))}}>
                   <i className="ease-icon fa fa-plus-circle"/>
                 </button>
               }
               content={'Invite new user'}/>
        }
        <NavLink to={`/teams/${match.params.teamId}/${match.params.itemId}/members`} className="section-header">
          <Popup size="mini"
                 position="right center"
                 inverted
                 trigger={
                   <span class="display-inline-block">
                      <span class="inline-tooltipped">
                        Users
                    </span>
                    <span className="header-count"> ({user_list.length})</span>
                    </span>
                 }
                 content={'Open a Desk'}/>
        </NavLink>
        <div className="section-list">
          {
            user_list.map(function(user, idx){
              if (!meAdmin){
                if (user.state >= 1)
                  return (
                      <NavLink to={`/teams/${team.id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                        <div className="primary_action channel_name">
                          <i className="fa fa-user prefix"/>
                          <span className="overflow-ellipsis">{user.username}</span>
                        </div>
                      </NavLink>
                  );
                else
                  return (
                      <NavLink to={`/teams/${team.id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                        <div className="primary_action channel_name userNotAccepted">
                          <i className="fa fa-user-o prefix"/>
                          <span className="overflow-ellipsis">{user.username}</span>
                        </div>
                      </NavLink>
                  );
              } else {
                if (user.state >= 1)
                  return (
                      <NavLink to={`/teams/${team.id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                        <div className="primary_action channel_name">
                          <i className="fa fa-user prefix"/>
                          <span className="overflow-ellipsis">{user.username}</span>
                        </div>
                      </NavLink>
                  );
                else if (user.invitation_sent || !!user.arrival_date)
                  return (
                      <NavLink to={`/teams/${team.id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                        <div className="primary_action channel_name userNotAccepted">
                          <i className="fa fa-clock-o prefix"/>
                          <span className="overflow-ellipsis">{user.username}</span>
                        </div>
                      </NavLink>
                  );
                else
                  return (
                      <NavLink to={`/teams/${team.id}/@${user.id}`} className="section-list-item channel" key={user.id}>
                        <div
                            style={{color: (freeTeam && userLimitReached) ? '#eb555c' : null}}
                            className="primary_action channel_name userNotAccepted">
                          <i className="fa fa-paper-plane prefix"/>
                          <span className="overflow-ellipsis">{user.username}</span>
                        </div>
                      </NavLink>
                  )
              }
            })}
        </div>
      </div>
  )
}

class TeamPasswordsStrengthProgress extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {totalPasswords, strongPasswords} = this.props;

    return (
        <Popup
            size="mini"
            inverted
            position='right center'
            trigger={
              <div class="circular-progress">
                <CircularProgressbar
                    textForPercentage={totalPasswords < 10 ? null : (pct) => `${pct}%`}
                    initialAnimation={true}
                    percentage={100 / totalPasswords * strongPasswords} />
                {totalPasswords < 10 &&
                <Icon name="lock"/>}
              </div>
            }
            content={
              <span>
                this is text of the popup
              </span>
            }
        />
    )
  }
}

@connect()
class TeamSideBar extends React.Component{
  constructor(props){
    super(props);
  }
  render() {
    const team = this.props.team;
    const me = team.team_users[team.my_team_user_id];

    return (
        <div class="client_channels_container">
          <div id="team_menu" onClick={this.props.openMenu}>
            <div class="full_flex" style={{overflow: 'hidden'}}>
              <div className="team_name_container overflow-ellipsis">
                {team.name}
              </div>
              <div className="team_client_user overflow-ellipsis">
                <i className="fa fa-square icon_left"/>
                {me.username}
              </div>
            </div>
            <TeamPasswordsStrengthProgress
                totalPasswords={100}
                strongPasswords={25}/>
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