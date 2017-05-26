var React = require('react');
var classnames = require('classnames');
var ReactRouter = require('react-router-dom');
var TeamSideBar = require('./TeamSideBar').TeamSideBar;
var UserList = require('./TeamSideBar').UserList;
var ChannelList = require('./TeamSideBar').ChannelList;
var TeamHeader = require('./TeamHeader');
var FlexPanels = require('./TeamFlexPanels');
var TeamAppAdderButtons = require('./TeamAppAdderButtons');
var TeamAppAddingUi = require('./TeamAppAddingUi');
var TeamAddUserModal = require('./TeamAddUserModal');
var TeamAddChannelModal = require('./TeamAddChannelModal');
var api = require('../utils/api');

class TeamView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      team_id: this.props.team_id,
      team_name: null,
      selectedItem : {
        type: null,
        id: -1,
        item: null
      },
      channels : null,
      users : null,
      me: null,
      icons : {
        user: 'fa-user',
        channel: 'fa-users'
      },
      flexActive : false,
      addUserModalActive: false,
      addChannelModalActive: false,
      loadingInfo : true
    };
    this.toggleAddUserModal = this.toggleAddUserModal.bind(this);
    this.toggleAddChannelModal = this.toggleAddChannelModal.bind(this);
    this.toggleFlexPanel = this.toggleFlexPanel.bind(this);
    this.selectItem = this.selectItem.bind(this);
    this.loadTeam = this.loadTeam.bind(this);
    this.getChannelById = this.getChannelById.bind(this);
    this.getUserById = this.getUserById.bind(this);
  }
  loadTeam(team_id){
    this.setState(function () {
      return {
        loadingInfo: true
      }
    });
    api.fetchTeam(team_id).then(function(data){
      var ret = {};
      ret.team_id = data.id;
      ret.team_name = data.name;
      ret.users = data.teamUsers;
      ret.channels = data.channels;
      for (var i = 0; i < data.teamUsers.length; i++){
        if (data.teamUsers[i].id === data.myTeamUserId){
          ret.me = data.teamUsers[i];
          break;
        }
      }
      ret.loadingInfo = false;
      this.setState(ret);
      this.selectItem({type: 'channel', id: ret.channels[0].id, item: null});
    }.bind(this))
  }

  selectItem(toSelect) {
    if (toSelect.type === 'channel'){
      api.fetchTeamChannel(this.state.team_id, toSelect.id).then(function(data){
        toSelect.item = data;
        this.setState({selectedItem: toSelect});
      }.bind(this));
    } else {
      api.fetchTeamUser(this.state.team_id, toSelect.id).then(function (data) {
        toSelect.item = data;
        this.setState({selectedItem: toSelect});
      }.bind(this));
    }
  }

  getUserById(id){
    for (var i = 0; i < this.state.users.length; i++){
      if (this.state.users[i].id === id)
        return this.state.users[i];
    }
  }
  getChannelById(id){
    for (var i = 0; i < this.state.channels.length; i++){
      if (this.state.channels[i].id === id)
        return this.state.channels[i];
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
  componentDidMount(){
    this.loadTeam(this.state.team_id);
  }
  componentWillMount(){
    /*    this.setState({
     selectedItem : {
     type : 'channel',
     id : this.state.channels[0].id,
     item: this.state.channels[0]
     }
     });*/
  }
  render(){
    return (
        <div className="team_view" id="team_view">
          {!this.state.loadingInfo && <TeamSideBar
              team_name={this.state.team_name}
              me={this.state.me}>
            <div id="col_channels">
              <div id="col_channels_scroller">
                <ChannelList
                    selectedItem={this.state.selectedItem}
                    items={this.state.channels}
                    selectFunc={this.selectItem}
                    toggleAddChannelModal={this.toggleAddChannelModal}/>
                <UserList
                    selectedItem={this.state.selectedItem}
                    items={this.state.users}
                    selectFunc={this.selectItem}
                    toggleAddUserModal={this.toggleAddUserModal}/>
              </div>
            </div>
          </TeamSideBar>}
          {this.state.selectedItem.item &&
          <div className="client_main_container">
            <TeamHeader
                selectedItem={this.state.selectedItem}
                icons={this.state.icons}
                flexActive={this.state.flexActive}
                toggleFlexFunc={this.toggleFlexPanel}/>
            <div className="team_client_body">
              <div id="col_main">
                <TeamAppAddingUi
                    team_id={this.state.team_id}
                    selectedItem={this.state.selectedItem}
                    userSelectFunc={this.getUserById}/>
                <div className="apps_container">
                  <div className="apps_scroller_div" id="team_apps_container">

                  </div>
                </div>
              </div>
              <div id="col_flex">
                <FlexPanels selectedItem={this.state.selectedItem}
                            flexActive={this.state.flexActive}
                            toggleFlexFunc={this.toggleFlexPanel}
                            userGetter={this.getUserById}/>
              </div>
            </div>
          </div>}
          {this.state.addUserModalActive &&
          <TeamAddUserModal
            toggleModalFunc={this.toggleAddUserModal}
            channels={this.state.channels}/>}
          {this.state.addChannelModalActive &&
              <TeamAddChannelModal
              toggleModalFunc={this.toggleAddChannelModal}
              users={this.state.users}/>
          }
        </div>
    )
  }
}

module.exports = TeamView;