var React = require('react');
var classnames = require('classnames');
var ReactRouter = require('react-router-dom');
var TeamSideBar = require('./TeamSideBar');
var TeamHeader = require('./TeamHeader');
var FlexPanels = require('./TeamFlexPanels');
var TeamAppAdderButtons = require('./TeamAppAdderButtons');
var TeamAppAddingUi = require('./TeamAppAddingUi');
var TeamAddUserModal = require('./TeamAddUserModal');
var TeamAddChannelModal = require('./TeamAddChannelModal');
var TeamChannelAddUserModal = require('./TeamChannelAddUserModal');
var TeamDeleteUserModal = require('./TeamDeleteUserModal');

import * as teamActions from "../actions/teamActions"
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"

var api = require('../utils/api');

import {connect} from "react-redux"

@connect((store)=>{
  return {
    team_name: store.team.name,
    users: store.users.users,
    me: store.users.me,
    channels: store.channels.channels,
    selectedItem: store.selection,
    addUserModalActive: store.teamModals.addUserModalActive,
    addChannelModalActive: store.teamModals.addChannelModalActive,
    teamChannelAddUserModalActive: store.teamModals.teamChannelAddUserModalActive,
    teamDeleteUserModalActive: store.teamModals.teamDeleteUserModalActive
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
      this.props.dispatch(userActions.selectTeamUser(this.props.users[0].id));
    });
  }
  render(){
    return (
        <div className="team_view" id="team_view">
          {!this.state.loadingInfo && <TeamSideBar/>}
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
                <div className="apps_container">
                  <div className="apps_scroller_div" id="team_apps_container">

                  </div>
                </div>
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
          {this.props.teamChannelAddUserModalActive &&
              <TeamChannelAddUserModal/>}
          {this.props.teamDeleteUserModalActive &&
          <TeamDeleteUserModal/>}
        </div>
    )
  }
}

module.exports = TeamView;