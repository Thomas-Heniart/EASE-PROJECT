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
var TeamAddUserModal = require('./teamModals/TeamAddUserModal');
var TeamAddChannelModal = require('./teamModals/TeamAddChannelModal');
var TeamDeleteUserModal = require('./teamModals/TeamDeleteUserModal');
var TeamDeleteRoomModal = require('./teamModals/TeamDeleteRoomModal');
var TeamDeleteUserFromRoomModal = require('./teamModals/TeamDeleteUserFromRoomModal');
var PinTeamAppToDashboardModal = require('./teamModals/PinTeamAppToDashboardModal');
var TeamAppsContainer = require('./TeamAppsContainer');
var TeamDeleteAppModal = require('./teamModals/TeamDeleteAppModal');
var TeamLeaveAppModal = require('./teamModals/TeamLeaveAppModal');
var TeamManageAppRequestModal = require('./teamModals/TeamManageAppRequestModal');
var TeamAcceptMultiAppModal = require('./teamModals/TeamAcceptMultiAppModal');
var TeamJoinMultiAppModal = require('./teamModals/TeamJoinMultiAppModal');
var TeamSettingsModal = require('./teamModals/TeamSettingsModal');
var TeamSettings = require('./teamModals/TeamSettings');
var TeamTransferOwnershipModal = require('./teamModals/TeamTransferOwnershipModal');
var TeamPhoneNumberModal = require('./teamModals/TeamPhoneNumberModal');
var RequestWebsiteModal = require('./teamModals/RequestWebsiteModal');
import queryString from "query-string";
import VerifyTeamUserModal from './teamModals/VerifyTeamUserModal';
import ReactivateTeamUserModal from './teamModals/ReactivateTeamUserModal';
import TeamAddMultipleUsersModal from './teamModals/TeamAddMultipleUsersModal';
import TeamBrowsePeopleModal from "./teamModals/TeamBrowsePeopleModal";
import TeamBrowseRoomsModal from "./teamModals/TeamBrowseRoomsModal";
var EaseHeader = require('./common/EaseHeader');
import * as teamActions from "../actions/teamActions"
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"
import * as modalActions from "../actions/teamModalActions"
import  {LeftRightTransition, OpacityTransition} from "../utils/transitions";
import {selectUserFromListById, selectChannelFromListById} from "../utils/helperFunctions";
import {withRouter, Switch, Route} from "react-router-dom";
import TeamsTutorial from "./teams/TeamsTutorial";
import {findDOMNode} from 'react-dom';
var api = require('../utils/api');

import {connect} from "react-redux"

@connect((store)=>{
  return {
    team: store.team,
    common: store.common,
    users: store.users.users,
    channels: store.channels.channels,
    selectedItem: store.selection,
    addUserModalActive: store.teamModals.addUserModalActive,
    addChannelModalActive: store.teamModals.addChannelModalActive,
    teamDeleteUserModal: store.teamModals.teamDeleteUserModal,
    teamDeleteChannelModal: store.teamModals.teamDeleteChannelModal,
    teamDeleteUserFromChannelModal: store.teamModals.teamDeleteUserFromChannelModal,
    teamDeleteAppModal: store.teamModals.teamDeleteAppModal,
    pinTeamAppToDashboardModal: store.teamModals.pinTeamAppToDashboardModal,
    teamLeaveAppModal: store.teamModals.teamLeaveAppModal,
    teamManageAppRequestModal: store.teamModals.teamManageAppRequestModal,
    teamAcceptMultiAppModal: store.teamModals.teamAcceptMultiAppModal,
    teamJoinMultiAppModal: store.teamModals.teamJoinMultiAppModal,
    teamSettingsModalActive: store.teamModals.teamSettingsModalActive,
    verifyTeamUserModal: store.teamModals.verifyTeamUserModal,
    teamAddMultipleUsersModal: store.teamModals.teamAddMultipleUsersModal,
    reactivateTeamUserModal: store.teamModals.reactivateTeamUserModal,
    teamTransferOwnershipModal: store.teamModals.teamTransferOwnershipModal,
    teamPhoneNumberModal: store.teamModals.teamPhoneNumberModal,
    requestWebsiteModal: store.teamModals.requestWebsiteModal
  };
})
class TeamView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loadingInfo : true
    };
    this.getSelectedItem = this.getSelectedItem.bind(this);
    this.autoSelectItem = this.autoSelectItem.bind(this);
    this.isValidTeamItemId = this.isValidTeamItemId.bind(this);
  }
  componentWillReceiveProps(nextProps){
    const itemId = nextProps.match.params.itemId;
    if (this.props !== nextProps) {
      if (this.props.match.params.teamId !== nextProps.match.params.teamId) {
        this.setState({loadingInfo: true});
        this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(nextProps.match.params.teamId)).then(() => {
          this.setState({loadingInfo: false});
          if (this.isValidTeamItemId(itemId)) {
            this.props.dispatch(teamActions.fetchTeamItemApps(itemId));
          } else
            this.autoSelectItem();
        }).catch(err => {
          window.location.href = '/';
        });
      }
      else if (this.props.match.params.itemId !== itemId) {
        if (this.isValidTeamItemId(itemId)) {
          this.props.dispatch(teamActions.fetchTeamItemApps(itemId));
        } else
          this.autoSelectItem();
      }
    }
  }
  componentDidUpdate(){
    if (!this.state.loadingInfo && !this.isValidTeamItemId(this.props.match.params.itemId)) {
      this.autoSelectItem();
    }
  }
  componentDidMount(){
    const itemId = this.props.match.params.itemId;
    if (!this.props.common.authenticated){
      this.props.history.push('/login');
      return;
    }
    this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(this.props.match.params.teamId)).then(()=>{
      this.setState({loadingInfo: false});
      const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
      if (me.phone_number === null && me.role === 3){
        this.props.dispatch(modalActions.showTeamPhoneNumberModal(true));
      }
      if (this.isValidTeamItemId(itemId)) {
        this.props.dispatch(teamActions.fetchTeamItemApps(itemId));
      }else
        this.autoSelectItem();
    }).catch(err => {
      console.log('error !');
      console.log(err);
      window.location.href = '/';
    });
  };
  isValidTeamItemId(itemId){
    if (itemId === undefined)
      return false;
    const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
    if (itemId[0] !== '@' && me.channel_ids.indexOf(Number(itemId)) !== -1)
      return true;
    if (selectUserFromListById(this.props.users, Number(itemId.slice(1, itemId.length))) !== null)
      return true;
    return false;
  }
  autoSelectItem(){
    const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
    const teamId = this.props.match.params.teamId;

    if (me.channel_ids.length > 0)
      this.props.history.push(`/teams/${teamId}/${me.channel_ids[0]}`);
    else
      this.props.history.push(`/teams/${teamId}/@${me.id}`);
  }
  getSelectedItem(){
    const itemId = this.props.match.params.itemId;
    if (itemId === undefined)
      return null;
    let item = (itemId[0] === '@') ?
        selectUserFromListById(this.props.users, Number(itemId.slice(1, itemId.length)))
        :
        selectChannelFromListById(this.props.channels, Number(itemId));
    return item;
  }
  render(){
    const selectedItem = this.getSelectedItem();
    const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);

    return (
        <div id="teamsHandler">
          <div className="team_view" id="team_view">
            {this.state.loadingInfo && <LoadingScreen/>}
            {!this.state.loadingInfo && <TeamSideBar me={me}/>}
            {this.props.team.teamMenuActive &&
            <TeamMenu
                me={me}
                team={this.props.team}
                dispatch={this.props.dispatch}/>}
            {!this.state.loadingInfo && selectedItem !== null &&
            <div className="client_main_container">
              <TeamHeader
                  item={selectedItem}
                  match={this.props.match}
                  appsLength={this.props.selectedItem.apps.length}/>
              <div className="team_client_body bordered_scrollbar">
                <OpacityTransition appear={true}>
                  {this.props.common.user !== null && !this.props.common.user.status.team_tuto_done &&
                  <TeamsTutorial/>}
                </OpacityTransition>
                <div id="col_main">
                  <TeamAppAddingUi
                      selection={selectedItem}/>
                  <TeamAppsContainer />
                </div>
                <div id="col_flex">
                  <Route path={`${this.props.match.url}/flexPanel`} render={(props) => <FlexPanels item={selectedItem}
                                                                                                   me={me}
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
            </Switch>}
            {this.props.addUserModalActive &&
            <TeamAddUserModal key="1"/>}
            {this.props.addChannelModalActive &&
            <TeamAddChannelModal/>}
            {this.props.teamDeleteUserModal.active &&
            <TeamDeleteUserModal/>}
            {this.props.teamDeleteChannelModal.active &&
            <TeamDeleteRoomModal/>}
            {this.props.teamDeleteUserFromChannelModal.active &&
            <TeamDeleteUserFromRoomModal/>}
            {this.props.teamDeleteAppModal.active &&
            <TeamDeleteAppModal/>}
            {this.props.pinTeamAppToDashboardModal.active &&
            <PinTeamAppToDashboardModal/>}
            {this.props.teamLeaveAppModal.active &&
            <TeamLeaveAppModal/>}
            {this.props.teamManageAppRequestModal.active &&
            <TeamManageAppRequestModal/>}
            {this.props.teamAcceptMultiAppModal.active &&
            <TeamAcceptMultiAppModal/>}
            {this.props.teamJoinMultiAppModal.active &&
            <TeamJoinMultiAppModal/>}
            {this.props.teamSettingsModalActive &&
            <TeamSettingsModal/>}
            {this.props.verifyTeamUserModal.active &&
            <VerifyTeamUserModal/>}
            {this.props.reactivateTeamUserModal.active &&
            <ReactivateTeamUserModal/>}
            {this.props.teamTransferOwnershipModal.active &&
            <TeamTransferOwnershipModal/>}
            {this.props.teamPhoneNumberModal.active &&
            <TeamPhoneNumberModal/>}
            {this.props.teamAddMultipleUsersModal.active &&
            <TeamAddMultipleUsersModal/>}
            {this.props.requestWebsiteModal.active &&
            <RequestWebsiteModal/>}
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamView);