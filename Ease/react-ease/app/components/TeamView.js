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
var TeamAddUserModal = require('./TeamAddUserModal');
var TeamBrowseChannelsModal = require('./teamModals/TeamBrowseChannelsModal');
var TeamAddChannelModal = require('./TeamAddChannelModal');
var TeamChannelAddUserModal = require('./TeamChannelAddUserModal');
var TeamDeleteUserModal = require('./TeamDeleteUserModal');
var TeamDeleteChannelModal = require('./TeamDeleteChannelModal');
var TeamDeleteUserFromChannelModal = require('./TeamDeleteUserFromChannelModal');
var PinTeamAppToDashboardModal = require('./teamModals/PinTeamAppToDashboardModal');
var TeamAppsContainer = require('./TeamAppsContainer');
var TeamDeleteAppModal = require('./teamModals/TeamDeleteAppModal');
var TeamLeaveAppModal = require('./teamModals/TeamLeaveAppModal');
var TeamManageAppRequestModal = require('./teamModals/TeamManageAppRequestModal');
var TeamAcceptMultiAppModal = require('./teamModals/TeamAcceptMultiAppModal');
var TeamJoinMultiAppModal = require('./teamModals/TeamJoinMultiAppModal');
var TeamSettingsModal = require('./teamModals/TeamSettingsModal');
var EaseHeader = require('./common/EaseHeader');
import * as teamActions from "../actions/teamActions"
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"
import  {LeftRightTransition, OpacityTransition} from "../utils/transitions";
import {withRouter} from "react-router-dom";
import TeamsTutorial from "./teams/TeamsTutorial";
import {findDOMNode} from 'react-dom';
import ReactTooltip from 'react-tooltip';

var api = require('../utils/api');

import {connect} from "react-redux"

@connect((store)=>{
  return {
    team: store.team,
    common: store.common,
    users: store.users.users,
    me: store.users.me,
    channels: store.channels.channels,
    selectedItem: store.selection,
    addUserModalActive: store.teamModals.addUserModalActive,
    addChannelModalActive: store.teamModals.addChannelModalActive,
    teamChannelAddUserModal: store.teamModals.teamChannelAddUserModal,
    teamDeleteUserModal: store.teamModals.teamDeleteUserModal,
    teamDeleteChannelModal: store.teamModals.teamDeleteChannelModal,
    teamDeleteUserFromChannelModal: store.teamModals.teamDeleteUserFromChannelModal,
    teamDeleteAppModal: store.teamModals.teamDeleteAppModal,
    pinTeamAppToDashboardModal: store.teamModals.pinTeamAppToDashboardModal,
    teamLeaveAppModal: store.teamModals.teamLeaveAppModal,
    teamManageAppRequestModal: store.teamModals.teamManageAppRequestModal,
    teamAcceptMultiAppModal: store.teamModals.teamAcceptMultiAppModal,
    teamJoinMultiAppModal: store.teamModals.teamJoinMultiAppModal,
    teamBrowseChannelsModalActive: store.teamModals.teamBrowseChannelsModalActive,
    teamSettingsModalActive: store.teamModals.teamSettingsModalActive
  };
})
class TeamView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      icons : {
        user: 'fa-user',
        channel: 'fa-users'
      },
      flexActive : false,
      loadingInfo : true,
      team_id: 2
    };
    this.toggleAddUserModal = this.toggleAddUserModal.bind(this);
    this.toggleAddChannelModal = this.toggleAddChannelModal.bind(this);
    this.toggleFlexPanel = this.toggleFlexPanel.bind(this);
    this.getChannelById = this.getChannelById.bind(this);
    this.getUserById = this.getUserById.bind(this);
  }

  getUserById(id){
    for (var i = 0; i < this.props.users.length; i++){
      if (this.props.users[i].id === id)
        return this.props.users[i];
    }
  }
  getChannelById(id){
    for (var i = 0; i < this.props.channels.length; i++){
      if (this.props.channels[i].id === id)
        return this.props.channels[i];
    }
  }
  toggleAddChannelModal(){
    this.setState({addChannelModalActive: !this.state.addChannelModalActive});
  }
  toggleAddUserModal(){
    this.setState({addUserModalActive: !this.state.addUserModalActive});
  }
  toggleFlexPanel(){
    this.setState({
      flexActive: !this.state.flexActive
    });
  }
  componentWillReceiveProps(nextProps){
    if (this.props.match.params.teamId !== nextProps.match.params.teamId){
      this.setState({loadingInfo: true});
      this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(nextProps.match.params.teamId)).then(()=>{
        this.setState({loadingInfo: false});
        this.props.dispatch(channelActions.selectTeamChannel(this.props.channels[0].id));
      });
    }
  }
  componentDidMount(){
    if (!this.props.common.authenticated){
      window.location.href = "/login";
//      this.props.history.push('/login');
      return;
    }
    this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(this.props.match.params.teamId)).then(()=>{
      this.setState({loadingInfo: false});
      this.props.dispatch(channelActions.selectTeamChannel(this.props.channels[0].id));
    });
  }
  render(){
    return (
        <div id="teamsHandler">
          <div className="team_view" id="team_view">
            {this.state.loadingInfo && <LoadingScreen/>}
            {!this.state.loadingInfo && <TeamSideBar/>}
            {this.props.team.teamMenuActive &&
            <TeamMenu
                me={this.props.me}
                team={this.props.team}
                dispatch={this.props.dispatch}/>}
            {!this.state.loadingInfo && this.props.selectedItem.item &&
            <div className="client_main_container">
              <TeamHeader
                  selectedItem={this.props.selectedItem}
                  icons={this.state.icons}
                  flexActive={this.state.flexActive}
                  toggleFlexFunc={this.toggleFlexPanel}/>
              <div className="team_client_body">
                <OpacityTransition appear={true}>
                  {this.props.common.teamsTutorial &&
                  <TeamsTutorial/>}
                </OpacityTransition>
                <div id="col_main">
                  <TeamAppAddingUi
                      userSelectFunc={this.getUserById}/>
                  <TeamAppsContainer />
                </div>
                <div id="col_flex">
                  <FlexPanels flexActive={this.state.flexActive}
                              toggleFlexFunc={this.toggleFlexPanel}
                              userGetter={this.getUserById}/>
                </div>
              </div>
            </div>}
            <LeftRightTransition appear={true}>
              {this.props.addUserModalActive &&
              <TeamAddUserModal key="1"/>}
              {this.props.addChannelModalActive &&
              <TeamAddChannelModal/>}
              {this.props.teamChannelAddUserModal.active &&
              <TeamChannelAddUserModal/>}
              {this.props.teamDeleteUserModal.active &&
              <TeamDeleteUserModal/>}
              {this.props.teamDeleteChannelModal.active &&
              <TeamDeleteChannelModal/>}
              {this.props.teamDeleteUserFromChannelModal.active &&
              <TeamDeleteUserFromChannelModal/>}
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
              {this.props.teamBrowseChannelsModalActive &&
              <TeamBrowseChannelsModal/>}
              {this.props.teamSettingsModalActive &&
              <TeamSettingsModal/>}
            </LeftRightTransition>
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamView);