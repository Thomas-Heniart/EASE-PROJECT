import * as channelActions from "../../actions/channelActions";
import {connect} from "react-redux";
import React, {Component} from "react";
import {isAdmin} from "../../utils/helperFunctions";
import {List, Icon, Label, Segment} from 'semantic-ui-react';

function ChannelJoinRequestList(props){
  const {room, team} = props;
  const requests = room.join_requests;

  return (
    <List>
      {requests.map(item => {
        const user = team.team_users[item];
        return (
          <div className="user_invitation_indicator" style={{color: "white"}}>
            <Segment style={{backgroundColor: "#ff9a00"}}>
              <List.Item key={item}>
                  <Icon name="user"/>
                  {user.username} would like to access this group.&nbsp;
                  <a style={{fontWeight: "bold"}} onClick={e => {props.dispatch(channelActions.addTeamUserToChannel({
                    team_id: room.team_id,
                    channel_id: room.id,
                    team_user_id: item
                  }))}}>
                    Accept</a>
                  &nbsp;or&nbsp;
                  <a style={{fontWeight: "bold"}} onClick={e => {props.dispatch(channelActions.deleteJoinChannelRequest({
                    team_id: team.id,
                    room_id: room.id,
                    team_user_id: item
                  }))}}>
                    refuse</a> ?
              </List.Item>
            </Segment>
          </div>
        )
      })}
    </List>
  )
}


@connect(store => ({
  teams: store.teams
}))
class BannerTeams extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {team, me, room} = this.props;
    if (isAdmin(me.role))
      return (
        <ChannelJoinRequestList room={room} team={team} dispatch={this.props.dispatch}/>
      );
    return null;
  }
}

export default BannerTeams;
