var React = require('react');
var classnames = require('classnames');
var SimpleAppSharingPreview = require('./teamAppsPreview/SimpleAppSharingPreview');
var LinkAppSharingPreview = require('./teamAppsPreview/LinkAppSharingPreview');
var MultiAppCheckablePreview = require('./teamAppsPreview/MultiAppCheckablePreview');
import {showTeamDeleteUserModal} from "../actions/teamModalActions"
import * as userActions from "../actions/userActions"
import {teamAppTransferOwnership} from "../actions/appsActions"

import {
    selectChannelFromListById,
    selectUserFromListById
} from "../utils/helperFunctions"

import {connect} from "react-redux"

class TeamUserShareableAppsList extends React.Component {
  constructor(props){
    super(props);
  }
  render() {
    const channelApps = this.props.apps['channel'];
    const userApps = this.props.apps['user'];
    const users = this.props.users;
    const channels = this.props.channels;

    return (
        <div>
          {
            Object.keys(channelApps).map(function (id) {
              const channel = selectChannelFromListById(channels, Number(id));
              return (
                  <div key={id}>
                    <div class="channel_title">
                      #{channel.name}
                    </div>
                    {
                      channelApps[id].map(function (item) {
                        if (item.type === 'simple')
                          return (
                              <SimpleAppSharingPreview
                                  key={item.id}
                                  app={item}
                                  users={users}
                                  checkAppFunc={this.props.checkApp.bind(null, 'channel', id)}
                              />
                          );
                        else if (item.type === 'link')
                          return (
                              <LinkAppSharingPreview
                                  key={item.id}
                                  app={item}
                                  users={users}
                                  checkAppFunc={this.props.checkApp.bind(null, 'channel', id)}
                              />
                          );
                        else if (item.type === 'multi')
                          return (
                              <MultiAppCheckablePreview
                                  key={item.id}
                                  app={item}
                                  users={users}
                                  checkAppFunc={this.props.checkApp.bind(null, 'channel', id)}
                              />
                          );
                      }, this)
                    }
                  </div>
              )
            }, this)
          }
        </div>
    )
  }
}

@connect((store)=>{
  return {
    users: store.users.users,
    channels: store.channels.channels,
    team_name: store.team.name,
    modal: store.teamModals.teamDeleteUserModal,
    me: store.users.me
  };
})
class TeamDeleteUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      apps: {
        channel: {},
        user: {}
      },
      appsNumber: 0,
      team_user: selectUserFromListById(this.props.users, this.props.modal.team_user_id),
      loadingApps: true
    };
    this.loadApps = this.loadApps.bind(this);
    this.checkApp = this.checkApp.bind(this);
    this.confirmModal = this.confirmModal.bind(this);
  }
  checkApp(type, id, app_id){
    var apps = {
      ...this.state.apps
    };
    apps[type][id] = apps[type][id].map(function (item) {
      if (item.id === app_id)
        item.selected = !item.selected;
      return item;
    });
    this.setState({apps: apps});
  }
  confirmModal(){
    var appsToDelete = [];
    const apps = this.state.apps;
    const channelApps = apps['channel'];
    const userApps = apps['user'];

    Object.keys(channelApps).map(function(item){
      for (var i = 0; i < channelApps[item].length; i++){
        const app = channelApps[item][i];
        if (app.selected){
          appsToDelete.push(this.props.dispatch(teamAppTransferOwnership(app.id, this.props.me.id)));
        }
      }
    }, this);
    Object.keys(userApps).map(function(item){
      for (var i = 0; i < userApps[item].length; i++){
        const app = userApps[item][i];
        if (app.selected){
          appsToDelete.push(this.props.dispatch(teamAppTransferOwnership(app.id, this.props.me.id)));
        }
      }
    }, this);
    Promise.all(appsToDelete).then(() => {
      this.props.dispatch(userActions.deleteTeamUser(this.props.modal.team_user_id)).then(() => {
        this.props.dispatch(showTeamDeleteUserModal(false));
      });
    });
  }
  loadApps(){
    this.props.dispatch(userActions.getTeamUserShareableApps(this.state.team_user.id)).then(response => {
      var apps = {
        channel:{},
        user:{}
      };
      response.map(function(item){
        item.selected = false;
        if (apps[item.origin.type][item.origin.id] === undefined)
          apps[item.origin.type][item.origin.id] = [];
        apps[item.origin.type][item.origin.id].push(item);
      });
      this.setState({apps: apps, loadingApps: false, appsNumber: response.length});
    });
  }
  componentWillMount(){
    this.loadApps();
  }
  render(){
    const teamName = this.props.team_name;
    const username = this.state.team_user.username;
    const appsNumber = this.state.appsNumber;

    return (
        <div className="ease_modal" id="team_delete_user_modal">
          <div className="modal-background"></div>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamDeleteUserModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row">
                <h1>You are about to delete {username}'s membership</h1>
              </div>
              <div class="content_row">
                <span class="first_word_capitalize">{username} will lose access to all accounts. But be carefull : we don't delete accounts on the websites themselves... Yet ;) Don't forget to go delete them by yourself.</span>
              </div>
              {!this.state.loadingApps &&
              <div>
                {this.state.appsNumber > 0 ?
                    <div>
                      <div class="content_row">
                        <span>While working here, {username} shared {appsNumber} accounts to {teamName}. Please select(tic) the ones you want to keep. You will become admin for these Apps, instead of {username}. The other ones will be deleted.</span>
                      </div>
                      <div class="content_row">
                        <span>By deleting an app, every person related to it, will lose the access. This action is irreversible.</span>
                      </div>
                      <div class="content_row flex_direction_column">
                        <TeamUserShareableAppsList
                            apps={this.state.apps}
                            users={this.props.users}
                            channels={this.props.channels}
                            checkApp={this.checkApp}/>
                      </div>
                    </div>
                    :
                    <div>
                      <div class="content_row">
                        <span class="first_word_capitalize">{username} doesn't have shared Apps in this team.</span>
                      </div>
                    </div>
                }
                <div class="content_row buttons_row">
                  <div class="buttons_wrapper">
                    <button class="button-unstyle neutral_background action_text_button mrgnRight5"
                            onClick={e => {this.props.dispatch(showTeamDeleteUserModal(false))}}>
                      Cancel
                    </button>
                    <button class="button-unstyle positive_background action_text_button next_button"
                            onClick={this.confirmModal}>
                      Yes, Remove this member
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

module.exports = TeamDeleteUserModal;