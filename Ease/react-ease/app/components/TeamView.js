var React = require('react');
var classnames = require('classnames');
var ReactRouter = require('react-router-dom');
var LoadingScreen = require('./common/LoadingScreen');
var TeamMenu = require('./TeamMenu');
var TeamSideBar = require('./TeamSideBar');
var TeamHeader = require('./TeamHeader');
var FlexPanels = require('./TeamFlexPanels');
var TeamAppAdderButtons = require('./TeamAppAdderButtons');
var TeamAppAddingUi = require('./TeamAppAddingUi');
var TeamAppsContainer = require('./TeamAppsContainer');
var TeamSettings = require('./teamModals/TeamSettings');
import { Icon, Segment, Dimmer, Loader } from 'semantic-ui-react';
import queryString from "query-string";
import TeamBrowsePeopleModal from "./teamModals/TeamBrowsePeopleModal";
import TeamBrowseRoomsModal from "./teamModals/TeamBrowseRoomsModal";
import FreeTrialEndModal from "./teamModals/FreeTrialEndModal";
import StaticUpgradeTeamPlanModal from "./teamModals/StaticUpgradeTeamPlanModal";
import * as teamActions from "../actions/teamActions"
import * as modalActions from "../actions/teamModalActions"
import {OpacityTransition} from "../utils/transitions";
import {teamUserState} from "../utils/utils";
import {isAdmin} from "../utils/helperFunctions";
import {showVerifyTeamUserModal, showReactivateTeamUserModal, showDepartureDateEndModal} from "../actions/teamModalActions";
import {Route, Switch, withRouter} from "react-router-dom";
import TeamsTutorial from "./teams/TeamsTutorial";
import {connect} from "react-redux";
import {reflect} from "../utils/utils";
import {sendTeamUserInvitation} from "../actions/userActions";
var EaseHeader = require('./common/EaseHeader');
var api = require('../utils/api');
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

@connect((store)=>({
  teams: store.teams,
  common: store.common,
  card: store.teamCard
}))
class TeamView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loadingInfo : false,
      addAppView: 'none',
      teamMenuActive: false
    };
  }
  setTeamMenu = (state) => {
    this.setState({teamMenuActive: state})
  };
  setAddAppView = (view) => {
    this.setState({addAppView: view});
  };
  componentWillMount() {
    document.title = "Team Space"
  }
  componentDidUpdate(){
    if (!this.state.loadingInfo && !this.isValidTeamItemId(this.props.match.params.itemId)) {
      this.autoSelectItem();
    }
  }
  componentWillReceiveProps(nextProps){
    if (this.props !== nextProps){
      if (this.props.match.params.teamId !== nextProps.match.params.teamId || this.props.match.params.itemId !== nextProps.match.params.itemId)
        this.setAddAppView('none');
    }
  }
  componentDidMount() {
    const teamId = this.props.match.params.teamId;
    const team = this.props.teams[Number(teamId)];

    if (!this.isValidTeamItemId()){
      this.autoSelectItem();
    }
    const me = team.team_users[team.my_team_user_id];
    if (me.phone_number === null && me.role === 3){
      this.props.dispatch(modalActions.showTeamPhoneNumberModal({
        active: true,
        team_id: team.id,
        team_user_id: me.id
      }));
    }
  }
  isValidTeamItemId = () => {
    const teamId = Number(this.props.match.params.teamId);
    const team = this.props.teams[teamId];
    if (team === undefined) {
      window.location.href = '/';
      return;
    }
    const itemId = this.props.match.params.itemId;
    if (itemId === undefined)
      return false;
    const me = team.team_users[team.my_team_user_id];
    if (itemId[0] !== '@' && me.room_ids.indexOf(Number(itemId)) !== -1)
      return true;
    const team_user = team.team_users[Number(itemId.slice(1, itemId.length))];
    if (!!team_user) {
      this.checkUser(team_user);
      return true;
    }
    return false;
  };
  autoSelectItem = () => {
    const teamId = this.props.match.params.teamId;
    const rooms = this.props.teams[teamId].rooms;
    const defaultRoom = Object.keys(rooms).map(id => {
      return rooms[id];
    }).find(item => (item.default));
    this.props.history.replace(`/teams/${teamId}/${defaultRoom.id}`);
  };
  getSelectedItem = () => {
    const team = this.props.teams[this.props.match.params.teamId];
    const itemId = this.props.match.params.itemId;
    if (itemId === undefined){
      return null;
    }
    let item = (itemId[0] === '@') ?
        team.team_users[Number(itemId.slice(1, itemId.length))]
        :
        team.rooms[Number(itemId)];
    return item !== undefined ? item : null;
  };
  checkUser = (teamUser) => {
    const team = this.props.teams[this.props.match.params.teamId];
    const me = team.team_users[team.my_team_user_id];
    const dispatch = this.props.dispatch;
    if (teamUser.state === teamUserState.registered && isAdmin(me.role) && teamUser.id !== me.id)
      dispatch(showVerifyTeamUserModal({
        active: true,
        team_user_id: teamUser.id,
        team_id: team.id
      }));
    else if (teamUser.disabled && isAdmin(me.role) && teamUser.id !== me.id)
      dispatch(showReactivateTeamUserModal({
        active:true,
        team_user_id: teamUser.id,
        team_id: team.id
      }));
    else if (teamUser.departure_date !== null && new Date().getTime() > teamUser.departure_date && isAdmin(me.role) && teamUser.id !== me.id)
      dispatch(showDepartureDateEndModal({
        active: true,
        team_user_id: teamUser.id,
        team_id: team.id
      }));
  };
  sendInvitation = (teamUser) => {
    this.setState({loading: true});
    const team = this.props.teams[this.props.match.params.teamId];
    this.props.dispatch(sendTeamUserInvitation({
      team_id: team.id,
      team_user_id: teamUser.id
    }));
    this.setState({loading: false});
  };
  sendAllInvitations = () => {
    const team = this.props.teams[this.props.match.params.teamId];

    const calls = Object.keys(team.team_users).map(item => {
      if (team.team_users[item].state === 0 && team.team_users[item].invitation_sent === false) {
        this.props.dispatch(sendTeamUserInvitation({
          team_id: team.id,
          team_user_id: Number(item)
        }));
      }
    });
    const response = calls.filter(item => {
      if (item)
        return true
    });
    Promise.all(response.map(reflect)).then(r => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
      this.setState({errorMessage: err});
    });
  };
  reSendAllInvitations = () => {
    const team = this.props.teams[this.props.match.params.teamId];

    const calls = Object.keys(team.team_users).map(item => {
      if (team.team_users[item].state === 0 && team.team_users[item].invitation_sent === true) {
        this.props.dispatch(sendTeamUserInvitation({
          team_id: team.id,
          team_user_id: Number(item)
        }));
      }
    });
    const response = calls.filter(item => {
      if (item)
        return true
    });
    Promise.all(response.map(reflect)).then(r => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
      this.setState({errorMessage: err});
    });
  };

  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    const selectedItem = this.getSelectedItem();
    const me = team.team_users[team.my_team_user_id];
    return (
        <div id="teamsHandler">
          <div className="team_view" id="team_view">
            {!this.state.loadingInfo && team.payment_required &&
            <FreeTrialEndModal team_id={team.id}/>}
            {this.state.loadingInfo && <LoadingScreen/>}
            <TeamSideBar team={team} me={me} openMenu={this.setTeamMenu.bind(null, true)}/>
            {this.state.teamMenuActive &&
            <TeamMenu
                closeMenu={this.setTeamMenu.bind(null, false)}
                me={me}
                team={team}
                dispatch={this.props.dispatch}/>}
            {!this.state.loadingInfo && selectedItem !== null &&
            <div className="client_main_container">
              <TeamHeader
                  item={selectedItem}
                  setAddAppView={this.setAddAppView}
                  match={this.props.match}
                  dispatch={this.props.dispatch}/>
              {selectedItem.state === 0 &&
              <div id='invitation'>
                {selectedItem.invitation_sent ?
                  <Segment className='resend' inverted disabled={this.state.loading}>
                    <p>
                      {selectedItem.username} hasn’t joined your team yet. <span onClick={e => this.sendInvitation(selectedItem)}>Resend invitation<Icon name='send'/></span>
                      <Loader active={this.state.loading} inverted size='tiny'/>
                      <span className='right' onClick={this.reSendAllInvitations}>Resend all pending invitations<Icon name='rocket'/></span>
                    </p>
                  </Segment>
                  :
                  <Segment className='send' inverted disabled={this.state.loading}>
                      <p>
                        {selectedItem.username} hasn’t been invited to join your team yet. <span onClick={e => this.sendInvitation(selectedItem)}>Send invitation<Icon name='send'/></span>
                        <Loader active={this.state.loading} inverted size='tiny'/>
                        <span className='right' onClick={this.sendAllInvitations}>Send to all uninvited people<Icon name='rocket'/></span>
                      </p>
                  </Segment>
                }
              </div>
              }
              <div className="team_client_body bordered_scrollbar">
                <OpacityTransition appear={true}>
                  {!!this.props.common.user && !this.props.common.user.status.team_tuto_done &&
                  <TeamsTutorial/>}
                </OpacityTransition>
                <div id="col_main">
                  {/*<Route path={`${this.props.match.url}/SingleApp`} render={(props) => <TeamAppAddingUi addAppView='Simple' item={selectedItem} website_id={this.chosenApp()} />} />*/}
                  {/*<Route path={`${this.props.match.url}/EnterpriseApp`} render={(props) => <TeamAppAddingUi addAppView='Multi' item={selectedItem} website_id={this.chosenApp()} />} />*/}
                  {/*<Route path={`${this.props.match.url}/LinkApp`} render={(props) => <TeamAppAddingUi addAppView='Link' item={selectedItem} website_id={this.chosenApp()} />} />*/}
                    {(this.props.card.type && this.props.card.channel_id === selectedItem.id) &&
                    <TeamAppAddingUi
                      addAppView={this.props.card.type}
                      item={selectedItem}
                      website={this.props.card.app} />}
                  <TeamAppsContainer team={team} item={selectedItem}/>
                </div>
                <div id="col_flex">
                  <Route path={`${this.props.match.url}/flexPanel`} render={(props) => <FlexPanels item={selectedItem}
                                                                                                   me={me}
                                                                                                   team={team}
                                                                                                   backLink={this.props.match.url}
                                                                                                   {...props}/>} />
                </div>
              </div>
            </div>}
            {!this.state.loadingInfo &&
            <Switch>
              <Route path={`${this.props.match.path}/settings`}
                     component={TeamSettings}/>
              <Route path={`${this.props.match.path}/members`}
                     component={TeamBrowsePeopleModal}/>
              <Route path={`${this.props.match.path}/rooms`}
                     component={TeamBrowseRoomsModal}/>
              <Route path={`${this.props.match.path}/upgrade`}
                     component={StaticUpgradeTeamPlanModal}/>
            </Switch>}
          </div>
        </div>
    )
  }
}

module.exports = TeamView;