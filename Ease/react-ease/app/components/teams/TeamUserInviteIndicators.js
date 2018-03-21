import React, {Component, Fragment} from "react";
import {withRouter, NavLink} from "react-router-dom";
import {showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {sendInvitationToTeamUserList, reInviteAllInvitedTeamUsers, sendTeamUserInvitation, inviteAllUninvitedTeamUsers} from "../../actions/userActions";
import {connect} from "react-redux";
import { Icon, Segment} from 'semantic-ui-react';
import {basicDateFormat, reflect} from "../../utils/utils";


class TeamUserInviteLimitReachedSegment extends Component {
  constructor(props){
    super(props);
  }
  upgradeToPro = () => {
    this.props.dispatch(showUpgradeTeamPlanModal({
      active: true,
      team_id: this.props.team_user.team_id
    }));
  };
  render(){
    const {team_user} = this.props;
    return (
        <div class="user_invitation_indicator">
          <Segment inverted style={{backgroundColor: "#eb555c"}}>
            {team_user.username} can't be invited, your team is full. To get more seats,
            &nbsp;
            <a onClick={this.upgradeToPro}>check Pro</a>
            &nbsp;or&nbsp;
            <NavLink to={`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}/settings/referral`}>referre friends</NavLink>.
          </Segment>
        </div>
    )
  }
}

const TeamUserInviteLimitReachedSegmentWithRouter = withRouter(TeamUserInviteLimitReachedSegment);

class TeamUserInviteSegment extends Component {
  constructor(props){
    super(props);
    this.state = {
      sent: false
    }
  }
  inviteUser = () => {
    const {team_user} = this.props;
    this.setState({sent: true});
    reflect(this.props.dispatch(sendTeamUserInvitation({
      team_id: team_user.team_id,
      team_user_id: team_user.id
    }))).then(response => {
      setTimeout(() => {
        this.setState({sent: false})
      }, 2000);
    });
  };
  inviteAllUsers = () => {
    const {teams, team_user} = this.props;
    const team = teams[team_user.team_id];

    this.setState({sent: true});
    let team_user_id_list = [];
    team_user_id_list.push(team_user.id);

    Object.keys(team.team_users).forEach(team_user_id => {
      const user = team.team_users[team_user_id];
      if (user.id !== team_user.id && !user.invitation_sent)
        team_user_id_list.push(user.id);
    });

    this.props.dispatch(sendInvitationToTeamUserList({
      team_id: team.id,
      team_user_id_list: team_user_id_list
    })).then(response => {
      setTimeout(() => {
        this.setState({sent: false})
      }, 2000);
    }).catch(err => {
      setTimeout(() => {
        this.setState({sent: false})
      }, 2000);
    });
  };
  render(){
    const {team_user} = this.props;

    return (
        <div class="user_invitation_indicator">
          <Segment
              disabled={this.state.sent}
              inverted
              style={{backgroundColor: "#ff9a00"}}>
            {this.state.sent ?
                <div style={{textAlign: 'center'}}>
                  <span style={{textDecoration: 'none'}}>Invitations sent ! <Icon name='rocket'/></span>
                </div> :
                <React.Fragment>
                  <a onClick={this.inviteUser}>Invite {team_user.username}</a> to join your team now.<Icon name="send"/>
                  <a style={{float:'right'}} onClick={this.inviteAllUsers}>Send to all uninvited people<Icon style={{textDecoration: 'none'}}name="rocket"/></a>
                </React.Fragment>}
          </Segment>
        </div>
    )
  }
}

class TeamUserReInviteSegment extends Component {
  constructor(props){
    super(props);
    this.state = {
      sent: false
    }
  }
  inviteUser = () => {
    const {team_user} = this.props;
    this.setState({sent: true});
    reflect(this.props.dispatch(sendTeamUserInvitation({
      team_id: team_user.team_id,
      team_user_id: team_user.id
    }))).then(response => {
      setTimeout(() => {
        this.setState({sent: false})
      }, 2000);
    });
  };
  reInviteAllUsers = () => {
    const {teams, team_user} = this.props;

    this.setState({sent: true});

    this.props.dispatch(reInviteAllInvitedTeamUsers({
      team_id: team_user.team_id
    })).then(response => {
      setTimeout(() => {
        this.setState({sent: false})
      }, 2000);
    }).catch(err => {
      setTimeout(() => {
        this.setState({sent: false})
      }, 2000);
    });
  };
  render(){
    const {team_user} = this.props;

    return (
        <div class="user_invitation_indicator">
          <Segment
              disabled={this.state.sent}
              inverted
              style={{backgroundColor: "#ffd933"}}>
            {this.state.sent ?
                <div style={{textAlign: 'center'}}>
                  <span style={{textDecoration: 'none'}}>Invitations sent ! <Icon name='rocket'/></span>
                </div> :
                <React.Fragment>
                  <a onClick={this.inviteUser}>Remind {team_user.username}</a> to join your team.<Icon name="send"/>
                  <a style={{float:'right'}} onClick={this.reInviteAllUsers}>Resend all pending invitations<Icon style={{textDecoration: 'none'}} name="rocket"/></a>
                </React.Fragment>}
          </Segment>
        </div>
    )
  }
}

class TeamUserArrivalDateIndicator extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {team_user} = this.props;

    return (
        <div class="user_invitation_indicator">
          <Segment
              inverted
              style={{backgroundColor: "#4a90e2"}}>
            {team_user.username} will be invited to join your team on {moment(team_user.arrival_date).format('DD/MM/YYYY')}. <NavLink to={`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}/flexPanel`}>Manage</NavLink>
          </Segment>
        </div>
    )
  }
}

const TeamUserArrivalDateIndicatorWithRouter = withRouter(TeamUserArrivalDateIndicator);

@connect(store => ({
  teams: store.teams
}))
class TeamUserInviteIndicators extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {team_user} = this.props;
    const team = this.props.teams[team_user.team_id];
    const maxSeats = 10 + team.extra_members;
    const invitedUsers = Object.keys(team.team_users).reduce((stack, team_user_id) => {
      if (team.team_users[team_user_id].invitation_sent)
        return ++stack;
      return stack;
    }, 0);
    if (team_user.state === 0 && !!team_user.arrival_date)
      return (
          <TeamUserArrivalDateIndicatorWithRouter {...this.props}/>
      );
    if (team_user.state === 0 && team_user.invitation_sent)
      return (
          <TeamUserReInviteSegment {...this.props}/>
      );
    if (team.plan_id === 0 && maxSeats === invitedUsers)
      return (
          <TeamUserInviteLimitReachedSegmentWithRouter {...this.props}/>
      );
    if (team_user.state === 0 && !team_user.invitation_sent)
      return (
          <TeamUserInviteSegment {...this.props}/>
      );
    return null;
  }
}

export default TeamUserInviteIndicators;