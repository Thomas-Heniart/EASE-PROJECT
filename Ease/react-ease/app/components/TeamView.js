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
    const teamId = this.props.match.params.teamId;
    const team = this.props.teams[teamId];

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
    const team = this.props.teams[teamId];

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

    const itemId = this.props.match.params.itemId;
    if (!itemId)
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
    if (!team)
      return null;
    const itemId = this.props.match.params.itemId;
    if (!itemId){
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
    if (teamUser.disabled && isAdmin(me.role) && teamUser.id !== me.id)
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
  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    const selectedItem = this.getSelectedItem();
    const me = !!team ? team.team_users[team.my_team_user_id] : null;
    return (
        <div id="teamsHandler">
          {!!team &&
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
              <div className="team_client_body bordered_scrollbar">
                <OpacityTransition appear={true}>
                  {!!this.props.common.user && !this.props.common.user.status.team_tuto_done &&
                  <TeamsTutorial team_id={team.id}/>}
                </OpacityTransition>
                <div id="col_main">
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
          </div>}
        </div>
    )
  }
}

module.exports = TeamView;