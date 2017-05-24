var React = require('react');
var classnames = require('classnames');
var api = require('../utils/api');

class TeamAppSearch extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      apps : []
    };
    this.updateList = this.updateList.bind(this);
  }
  updateList(team_id, query){
    api.teamAppSearch(team_id, query).then(function(data){
      this.setState({apps: data});
    }.bind(this));
  }
  componentWillReceiveProps(props){
    props != this.props && this.updateList(props.team_id, props.query);
  }
  componentDidMount(){
    this.updateList(this.props.team_id, this.props.query);
  }
  render() {
    return (
        <div className="floating_dropdown app_selectors show">
          <div className="dropdown_content">
            {
              this.state.apps.length ?
                  this.state.apps.map(function (item) {
                    return (
                        <div className="dropdown_row app_selector"
                             key={item.single_id}
                             onClick={this.props.selectFunc.bind(null, item)}
                        >
                          <img className="logo" src={item.logo}/>
                          <span className="app_name overflow-ellipsis">{item.name}</span>
                        </div>
                    )
                  }, this) :
                  <div>No results</div>
            }
          </div>
        </div>
    )
  }
}

function SimpleUserSelect(props){
  return (
      <div className="user_adding_container">
        <div className="icon_wrapper">
          <i className="fa fa-plus"/>
        </div>
        <div className="add_container full_flex display_flex">
          {
            props.selectedUsers.map(function(item){
              return (
                  <div className="user_token" key={item.id}>
                    <span className="name_hodler overflow-ellipsis">{item.username}</span>
                    <button className="button_delete action_button button-unstyle" onClick={props.deselectFunc.bind(null, item.id)}>
                      <i className="fa fa-times"/>
                    </button>
                  </div>
              )
            }, this)
          }
          <input className="true_input input_unstyle full_flex" type="text"/>
        </div>
        <div className="floating_dropdown user_selectors show">
          {
            props.users.map(function(item){
              return (
                  <div className={classnames('user_selector', item.selected ? 'selected':null)} key={item.id} onClick={props.selectFunc.bind(null, item.id)}>
                                  <span className="username text_strong">
                                    {item.username}
                                  </span>
                    -
                    <span className="fname">{item.firstName}</span>
                    <span className="lname">{item.lastName}</span>
                  </div>
              )
            }, this)
          }
        </div>
      </div>
  )
}
class SimpleTeamAppAdd extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      choosenApp: null,
      appName: '',
      credentials: {},
      passwordRemind : '0',
      comment: '',
      selectedUsers: [],
      users: []
    };
    this.state.users = [];
    if (this.props.selectedItem.type === 'channel'){
      this.props.selectedItem.item.userIds.map(function(item){
        var user = this.props.userSelectFunc(item);
        user.selected = false;
        console.log(user);
        this.state.users.push(user);
      }, this);
    } else {
      var item = this.props.selectedItem.item;
      item.selected = false;
      this.state.users.push(item);
    }

    this.handleAppNameChange = this.handleAppNameChange.bind(this);
    this.chooseApp = this.chooseApp.bind(this);
    this.resetApp = this.resetApp.bind(this);
    this.handleCredentialInput = this.handleCredentialInput.bind(this);
    this.handlePasswordRemind = this.handlePasswordRemind.bind(this);
    this.handleComment = this.handleComment.bind(this);
    this.handleUserSelect = this.handleUserSelect.bind(this);
    this.handleUserDeselect = this.handleUserDeselect.bind(this);
  }
  handleAppNameChange(event){
    this.setState({appName: event.target.value});
  }
  handleUserDeselect(id){
    var users = this.state.users;
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (users[i].selected){
          users[i].selected = false;
          selectedUsers.splice(selectedUsers.indexOf(users[i]), 1);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  handleUserSelect(id){
    var users = this.state.users;
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (!users[i].selected){
          users[i].selected = true;
          selectedUsers.push(users[i]);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  chooseApp(app){
    api.fetchWebsiteInfo(app.single_id).then(function(data){
      var credentials = {};
      data.map(function (item) {
        credentials[item.name] = '';
      });
      this.setState({choosenApp: {info : app, inputs: data}, appName : app.name, credentials: credentials});
    }.bind(this));
  }
  handleComment(event){
    this.setState({comment: event.target.value});
  }
  handlePasswordRemind(event){
    this.setState({passwordRemind: event.target.value});
  }
  handleCredentialInput(event){
    var state = this.state.credentials;
    state[event.target.name] = event.target.value;
    this.setState({credentials: state});
  }
  resetApp(){
    this.setState({choosenApp: null});
  }
  render(){
    return (
        <div className="add_content_container full_flex team_app active" id="simple_app_adder">
          <div className="add_actions_holder">
            <button className="button-unstyle send_button action_text_button positive_background">
              Send
            </button>
            <button className="button-unstyle action_text_button alert_background close_button">
              Cancel
            </button>
          </div>
          <div className="app_name_input_handler display_flex locked">
            <i className="fa fa-search icon_holder"/>
            <div className="app_name_input_wrapper">
              <input placeholder="App name..."
                     name="app_name"
                     className="true_input input_unstyle"
                     value={this.state.appName}
                     onChange={this.handleAppNameChange}
              />
              {!this.state.choosenApp && this.state.appName.length > 0 &&
              <TeamAppSearch
                  team_id={this.props.team_id}
                  query={this.state.appName}
                  selectFunc={this.chooseApp}/>}
            </div>
            {this.state.choosenApp &&
            <button className="button-unstyle action_button modify_app_button" onClick={this.resetApp}>
              <i className="fa fa-times"/>
            </button>
            }
          </div>
          {this.state.choosenApp  &&
          <div>
            <div className="info_holder">
              <div className="info">
                <div className="logo_holder">
                  <img src={this.state.choosenApp.info.logo} alt="website logo"/>
                </div>
                <div className="credentials_holder">
                  <div className="credentials">
                    {
                      this.state.choosenApp.inputs.map(function(item){
                        return (
                            <div className="credentials_line" key={item.name}>
                              <i className={classnames("fa icon_handler credentials_type_icon", item.placeholderIcon)}/>
                              <input placeholder={item.placeholder}
                                     autoComplete="off"
                                     className="credentials_value_input value_input"
                                     type={item.type}
                                     name={item.name}
                                     value={this.state.credentials[item.name]}
                                     onChange={this.handleCredentialInput}/>
                            </div>
                        )
                      }, this)
                    }
                  </div>
                  <div className="password_change_remind">
                    <div className="password_change_icon">
                      <i className="fa fa-refresh"/>
                    </div>
                    <span className="text_helper">Password change reminder:</span>
                    <div className="password_change_info">
                      <select name="password_change_interval"
                              className="password_change_input value_input"
                              value={this.state.passwordRemind}
                              onChange={this.handlePasswordRemind}>
                        <option value="0">never</option>
                        <option value="1">1 months</option>
                        <option value="3">3 months</option>
                        <option value="6">6 months</option>
                        <option value="12">12 months</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="sharing_info full_flex">
                <SimpleUserSelect
                    users={this.state.users}
                    selectedUsers={this.state.selectedUsers}
                    selectFunc={this.handleUserSelect}
                    deselectFunc={this.handleUserDeselect}
                />
              </div>
            </div>
            <div className="comment_holder">
              <div className="comment_icon">
                <i className="fa fa-sticky-note-o"/>
              </div>
              <div className="comment">
                <textarea value={this.state.comment} onChange={this.handleComment} className="comment_input value_input" placeholder="Your comment..."/>
              </div>
            </div>
          </div>}
        </div>
    )
  }
}

class TeamAppAddingUi extends React.Component {
  render(){
    return (
        <div className="add_ui_holder full_flex display_flex">
          <SimpleTeamAppAdd
              team_id={this.props.team_id}
              selectedItem={this.props.selectedItem}
              userSelectFunc={this.props.userSelectFunc}/>
        </div>
    )
  }
}

module.exports = TeamAppAddingUi;