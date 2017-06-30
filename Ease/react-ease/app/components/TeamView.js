var React = require('react');
var classnames = require('classnames');
var ReactRouter = require('react-router-dom');
var TeamMenu = require('./TeamMenu');
var TeamSideBar = require('./TeamSideBar');
var TeamHeader = require('./TeamHeader');
var FlexPanels = require('./TeamFlexPanels');
var TeamAppAdderButtons = require('./TeamAppAdderButtons');
var TeamAppAddingUi = require('./TeamAppAddingUi');
var TeamAddUserModal = require('./TeamAddUserModal');
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
import * as teamActions from "../actions/teamActions"
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"

var api = require('../utils/api');

import {connect} from "react-redux"

@connect((store)=>{
  return {
    team: store.team,
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
    teamJoinMultiAppModal: store.teamModals.teamJoinMultiAppModal
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
      loadingInfo : true
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
  componentWillMount(){
    this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(this.props.team_id)).then(()=>{
      this.setState({loadingInfo: false});
      this.props.dispatch(channelActions.selectTeamChannel(this.props.channels[0].id));
    });
  }
  render(){
    return (
        <div className="team_view" id="team_view">
          {!this.state.loadingInfo && <TeamSideBar/>}
          {this.props.team.teamMenuActive &&
              <TeamMenu
                me={this.props.me}
                team={this.props.team}
                dispatch={this.props.dispatch}/>}
          {this.props.selectedItem.item &&
          <div className="client_main_container">
            <TeamHeader
                selectedItem={this.props.selectedItem}
                icons={this.state.icons}
                flexActive={this.state.flexActive}
                toggleFlexFunc={this.toggleFlexPanel}/>
            <div className="team_client_body">
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
          {this.props.addUserModalActive &&
          <TeamAddUserModal/>}
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
        </div>
    )
  }
}

module.exports = TeamView;