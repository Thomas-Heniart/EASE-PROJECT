var React = require('react');
var classnames = require('classnames');
var api = require('../utils/api');
var SimpleAppSharingPreview = require('./teamAppsPreview/SimpleAppSharingPreview');
var LinkAppSharingPreview = require('./teamAppsPreview/LinkAppSharingPreview');
var MultiAppCheckablePreview = require('./teamAppsPreview/MultiAppCheckablePreview');
import {showTeamDeleteUserFromChannelModal} from "../actions/teamModalActions"
import * as userActions from "../actions/userActions"
import {removeTeamUserFromChannel} from "../actions/channelActions"
import {teamAppTransferOwnership} from "../actions/appsActions"
import {
    selectChannelFromListById,
    selectUserFromListById
} from "../utils/helperFunctions"

import {connect} from "react-redux"

function TeamUserShareableAppsList(props){
  const apps = props.apps;
  const users = props.users;
  const printedApps = apps.map(function (item) {
    if (item.type === 'simple')
      return (
          <SimpleAppSharingPreview
              key={item.id}
              app={item}
              users={users}
              checkAppFunc={props.checkApp.bind(null, item.id)}
          />
      );
    else if (item.type === 'link')
      return (
          <LinkAppSharingPreview
              key={item.id}
              app={item}
              users={users}
              checkAppFunc={props.checkApp.bind(null, item.id)}
          />
      );
    else if (item.type === 'multi')
      return (
          <MultiAppCheckablePreview
              key={item.id}
              app={item}
              users={users}
              checkAppFunc={props.checkApp.bind(null, item.id)}
          />
      );
  });
  return (
      <div>
        {printedApps}
      </div>
  )
}

@connect((store)=>{
  return {
    users: store.users.users,
    channels: store.channels.channels,
    modal: store.teamModals.teamDeleteUserFromChannelModal,
    team_id: store.team.id
  };
})
class TeamDeleteUserFromChannelModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      channel: null,
      user: null,
      apps: [],
      loadingApps: true
    };
    this.state.channel = selectChannelFromListById(this.props.channels, this.props.modal.channel_id);
    this.state.user = selectUserFromListById(this.props.users, this.props.modal.team_user_id);
    this.loadApps = this.loadApps.bind(this);
    this.checkApp = this.checkApp.bind(this);
    this.confirmModal = this.confirmModal.bind(this);
  }
  loadApps(){
    api.fetchTeamUserShareableAppsInChannel(this.props.team_id, this.state.channel.id, this.state.user.id).then(response => {
      var apps = response.map(function (item) {
        item.selected = false;
        return item;
      });
      this.setState({apps: apps, loadingApps: false});
    });
  }
  checkApp(app_id){
    const apps = this.state.apps.map(function (item) {
      if (item.id === app_id)
        item.selected = !item.selected;
      return item;
    });
    this.setState({apps: apps});
  }
  confirmModal(){
    const user = this.state.user;
    const channel = this.state.channel;
    const apps = this.state.apps.filter(function (item) {
      return item.selected;
    });
    const trasferApps = apps.map(function (item) {
      return this.props.dispatch(teamAppTransferOwnership(item.id, user.id));
    }, this);
    Promise.all(trasferApps).then(() => {
      this.props.dispatch(removeTeamUserFromChannel(channel.id, user.id)).then(() => {
        this.props.dispatch(showTeamDeleteUserFromChannelModal(false));
      });
    });
  }
  componentWillMount(){
    this.loadApps();
  }
  render(){
    const username = this.state.user.username;
    const channel = this.state.channel;
    const appsNumber = this.state.apps.length;

    return (
        <div className="ease_modal" id="team_delete_user_from_channel_modal">
          <div className="modal-background"></div>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row">
                <h1>You are about to <strong>remove {username} from {channel.name}</strong></h1>
              </div>
              <div class="content_row">
                <span class="first_word_capitalize">{username} will lose access to <strong>#{channel.name}</strong>, and to all Apps included in <strong>{channel.name}</strong>. Also, all Apps from #{channel.name} pinned to {username}'dashboard will disapear.</span>
              </div>
              {!this.state.loadingApps &&
              <div>
                {appsNumber > 0 ?
                    <div>
                      <div class="content_row">
                        <span class="first_word_capitalize">{username} shared <strong>{appsNumber} accounts</strong>, in #{channel.name}. Please select (by ticking) the ones you want to keep. You will become admin for these Apps, instead of {username}. The other ones will be deleted.</span>
                      </div>
                      <div class="content_row">
                        <span>By deleting an app, every person related to it, will lose the access. This action is irreversible.</span>
                      </div>
                      <div class="content_row flex_direction_column">
                        <TeamUserShareableAppsList
                            apps={this.state.apps}
                            users={this.props.users}
                            checkApp={this.checkApp}/>
                      </div>
                    </div>
                    :
                    <div>
                      <div class="content_row">
                        <span class="first_word_capitalize">{username} doesn't have shared Apps in this group.</span>
                      </div>
                    </div>
                }
                <div class="content_row buttons_row">
                  <div class="buttons_wrapper">
                    <button class="button-unstyle neutral_background action_text_button mrgnRight5"
                            onClick={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal(false))}}>
                      Cancel
                    </button>
                    <button class="button-unstyle positive_background action_text_button next_button"
                            onClick={this.confirmModal}>
                      Remove from this group
                    </button>
                  </div>
                </div>
              </div>
              }
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamDeleteUserFromChannelModal;