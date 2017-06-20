var React = require('react');
var classnames = require('classnames');
import * as teamModalActions from "../actions/teamModalActions"
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"
import {showTeamDeleteUserModal,
    showTeamDeleteChannelModal,
    showTeamDeleteUserFromChannelModal}
    from "../actions/teamModalActions"
import {teamUserRoles} from "../utils/helperFunctions"

import {connect} from "react-redux"

class TeamChannelFlexTab extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      purposeModifying: false,
      modifiedPurpose: null,
      nameModifying: false,
      modifiedName: null
    };
    this.handlePurposeInput = this.handlePurposeInput.bind(this);
    this.setPurposeModifying = this.setPurposeModifying.bind(this);
    this.handleNameInput = this.handleNameInput.bind(this);
    this.setNameModifying = this.setNameModifying.bind(this);
    this.confirmNameChange = this.confirmNameChange.bind(this);
    this.confirmPurposeChange = this.confirmPurposeChange.bind(this);
  }
  confirmNameChange(){
    this.props.dispatch(channelActions.editTeamChannelName(this.props.item.id, this.state.modifiedName)).then(response => {
      this.setState({
        nameModifying: false
      });
    });
  }
  handleNameInput(event){
    this.setState({modifiedName: event.target.value})
  }
  setNameModifying(state){
    if (state){
      this.setState({
        nameModifying: state,
        modifiedName: this.props.item.name
      });
    } else {
      this.setState({
        nameModifying: state
      });
    }
  }
  confirmPurposeChange(){
    this.props.dispatch(channelActions.editTeamChannelPurpose(this.props.item.id, this.state.modifiedPurpose)).then(response => {
      this.setState({
        purposeModifying: false
      });
    });
  }
  handlePurposeInput(event){
    this.setState({modifiedPurpose: event.target.value})
  }
  setPurposeModifying(state){
    if (state){
      this.setState({
        purposeModifying: state,
        modifiedPurpose: this.props.item.purpose
      });
    } else {
      this.setState({
        purposeModifying: state
      });
    }
  }
  render() {
    return (
        <div className="flex_contents_panel active" id="team_tab">
          <div className="tab_heading">
            <div className="heading_row">
            <span className="heading_text">
              Team's information
            </span>
              <button className="button-unstyle button_close_flexpanel" onClick={this.props.toggleFlexFunc}>
                <i className="fa fa-times"/>
              </button>
            </div>
          </div>
          <div className="tab_content_body">
            <div className="tab_content_row">
              <h2 className="name_holder">
                {!this.state.nameModifying ?
                    <span>
                  <strong className="name">{this.props.item.name}</strong>
                  <button class="button-unstyle mrgnLeft5 action_button"
                          onClick={this.setNameModifying.bind(null, true)}>
                    <i class="fa fa-pencil"/>
                  </button>
                </span>
                    :
                    <div class="overflow-hidden">
                      <input class="modal_input width100 mrgnBottom5" type="text" name="name"
                             value={this.state.modifiedName}
                             onChange={this.handleNameInput}/>
                      <button class="button-unstyle action_text_button positive_background floatRight"
                              onClick={this.confirmNameChange}>
                        Done
                      </button>
                      <button class="button-unstyle action_text_button neutral_background mrgnRight5 floatRight"
                              onClick={this.setNameModifying.bind(null, false)}>
                        Cancel
                      </button>
                    </div>
                }
              </h2>
            </div>
            <div className="tab_content_row">
            <span className="purpose_holder">
              <strong>Purpose: </strong>
              {!this.state.purposeModifying ?
                  <span>
                    <span className="purpose">{this.props.item.purpose}</span>
                    <button class="button-unstyle mrgnLeft5 action_button" onClick={this.setPurposeModifying.bind(null,true)}>
                      <i class="fa fa-pencil"/>
                    </button>
                  </span>
                  :
                  <div class="overflow-hidden">
                    <textarea
                        class="width100 modal_input mrgnBottom5"
                        value={this.state.modifiedPurpose}
                        onChange={this.handlePurposeInput}>
                    </textarea>
                    <button class="button-unstyle action_text_button positive_background floatRight"
                            onClick={this.confirmPurposeChange}>
                      Done
                    </button>
                    <button class="button-unstyle action_text_button neutral_background mrgnRight5 floatRight"
                            onClick={this.setPurposeModifying.bind(null, false)}>
                      Cancel
                    </button>
                  </div>
              }
            </span>
            </div>
            <div className="tab_content_row">
            <span className="number_apps_holder">
              <strong>Shared apps: </strong>
              <span className="number_apps">{this.props.item.apps.length}</span>
            </span>
            </div>
            <div className="tab_content_row">
              <div className="members_holder">
                <strong className="heading">Members:</strong>
                <div className="members_list">
                  {this.props.item.userIds.map(function (item) {
                    const user = this.props.userGetter(item);
                    return (
                      <span className="member_name_holder" key={item}>
                        <i className="fa fa-user mrgnRight5"/>
                       <span className="member_name">{user.username}</span>
                        <button class="button-unstyle remove_button mrgnLeft5"
                                onClick={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal(true, this.props.item.id, user.id))}}>
                          <u>remove</u>
                        </button>
                      </span>
                    )
                  }, this)}
                </div>
                <button class="button-unstyle underlineOnHover" onClick={e => {this.props.dispatch(teamModalActions.showTeamChannelAddUserModal(true, this.props.item.id))}}>
                  <strong>+ invite member</strong>
                </button>
              </div>
            </div>
            <div className="tab_content_row">
              <button className="button-unstyle team_delete_button"
                      onClick={e => {this.props.dispatch(showTeamDeleteChannelModal(true, this.props.item.id))}}>
                <u>Delete group</u>
              </button>
            </div>
          </div>
        </div>
    )
  }
}


class TeamUserFlexTab extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      first_name: null,
      last_name: null,
      firstNameLastNameModifying: false,
      username: null,
      usernameModifying: false,
      role: null,
      roleModifying: false,
      departureDate:'',
      departureDateModifying: false
    };
    this.setFirstLastNameModifying = this.setFirstLastNameModifying.bind(this);
    this.setUsernameModifying = this.setUsernameModifying.bind(this);
    this.setRoleModifying = this.setRoleModifying.bind(this);
    this.setDepartureDateModifying = this.setDepartureDateModifying.bind(this);
    this.handleInput = this.handleInput.bind(this);
    this.confirmUsernameChange = this.confirmUsernameChange.bind(this);
    this.confirmUserLastFirstNameChange = this.confirmUserLastFirstNameChange.bind(this);
    this.confirmUserRoleChange = this.confirmUserRoleChange.bind(this);
    this.confirmUserDepartureDateChange = this.confirmUserDepartureDateChange.bind(this);
  }
  handleInput(e){
    this.setState({
      [e.target.name] : e.target.value
    });
  }
  setDepartureDateModifying(state){
    if (state){
      this.setState({
        departureDateModifying: true,
        role: this.props.item.departureDate
      });
    }else {
      this.setState({
        departureDateModifying: false
      });
    }
  }
  setRoleModifying(state){
    if (state){
      this.setState({
        roleModifying: true,
        role: this.props.item.role
      });
    }else {
      this.setState({
        roleModifying: false
      });
    }
  }
  setFirstLastNameModifying(state){
    if (state){
      this.setState({
        firstNameLastNameModifying: true,
        first_name: this.props.item.first_name,
        last_name: this.props.item.last_name
      });
    }else {
      this.setState({
        firstNameLastNameModifying: false
      });
    }
  }
  setUsernameModifying(state){
    if (state){
      this.setState({
        usernameModifying: true,
        username: this.props.item.username,
      });
    }else {
      this.setState({
        usernameModifying: false
      });
    }
  }
  confirmUsernameChange(){
    if (this.state.username !== this.props.item.username){
      this.props.dispatch(userActions.editTeamUserUsername(this.props.item.id, this.state.username)).then(response => {
        this.setState({usernameModifying: false});
      });
    }
  }
  confirmUserLastFirstNameChange(){
    this.props.dispatch(userActions.editTeamUserFirstName(this.props.item.id, this.state.first_name)).then(response => {
      this.setState({firstNameLastNameModifying: false});
    });
    this.props.dispatch(userActions.editTeamUserLastName(this.props.item.id, this.state.last_name)).then(response => {
      this.setState({firstNameLastNameModifying: false});
    });
  }
  confirmUserRoleChange(){
    if (this.state.role !== this.props.item.role){
      this.props.dispatch(userActions.editTeamUserRole(this.props.item.id, this.state.role)).then(response => {
        this.setState({roleModifying: false});
      });
    }
  }
  confirmUserDepartureDateChange(){
    if (this.state.departureDate != this.props.item.departureDate){
      this.props.dispatch(userActions.editTeamUserDepartureDate(this.props.item.id, this.state.departureDate)).then(response => {
        this.setState({departureDateModifying: false});
      });
    }
  }
  render() {
    return (
        <div className="flex_contents_panel active" id="team_user_tab">
          <div className="tab_heading">
            <div className="heading_row">
            <span className="heading_text">
              User's information
            </span>
              <button className="button-unstyle button_close_flexpanel" onClick={this.props.toggleFlexFunc}>
                <i className="fa fa-times"/>
              </button>
            </div>
          </div>
          <div className="tab_content_body">
            <div className="tab_content_row">
              {!this.state.firstNameLastNameModifying ?
                  <h2 className="name_holder">
                    <strong className="firstname mrgnRight5">{this.props.item.first_name}</strong>
                    <strong className="lastname">{this.props.item.last_name}</strong>
                    <button class="button-unstyle mrgnLeft5 action_button"
                            onClick={this.setFirstLastNameModifying.bind(null, true)}>
                      <i class="fa fa-pencil"/>
                    </button>
                  </h2>
                  :
                  <div class="overflow-hidden">
                    <div class="display-flex mrgnBottom5">
                      <input class="modal_input width50 mrgnRight5" type="text" name="first_name"
                             value={this.state.first_name}
                             onChange={this.handleInput}/>
                      <input class="modal_input width50" type="text" name="last_name"
                             value={this.state.last_name}
                             onChange={this.handleInput}/>
                    </div>
                    <button class="button-unstyle action_text_button positive_background floatRight"
                            onClick={this.confirmUserLastFirstNameChange}>
                      Done
                    </button>
                    <button class="button-unstyle action_text_button neutral_background mrgnRight5 floatRight"
                            onClick={this.setFirstLastNameModifying.bind(null, false)}>
                      Cancel
                    </button>
                  </div>
              }
            </div>
            <div className="tab_content_row">
              {!this.state.usernameModifying ?
                  <span className="username_holder">
              @<span className="username">
              {this.props.item.username}
            </span>
             <button class="button-unstyle mrgnLeft5 action_button"
                     onClick={this.setUsernameModifying.bind(null, true)}>
                      <i class="fa fa-pencil"/>
                    </button>
            </span>
                  :
                  <div class="overflow-hidden">
                    <div class="modal_input_wrapper mrgnBottom5">
                      <i class="fa fa-at ease_icon"/>
                      <input class="input_unstyle" type="text" name="username"
                             value={this.state.username}
                             onChange={this.handleInput}/>
                    </div>
                    <button class="button-unstyle action_text_button positive_background floatRight"
                            onClick={this.confirmUsernameChange}>
                      Done
                    </button>
                    <button class="button-unstyle action_text_button neutral_background mrgnRight5 floatRight"
                            onClick={this.setUsernameModifying.bind(null, false)}>
                      Cancel
                    </button>
                  </div>
              }
            </div>
            <div className="tab_content_row">
                  <span className="email_holder">
              <strong>Email: </strong>
              <span className="email">
                {this.props.item.email}
              </span>
            </span>
            </div>
            <div className="tab_content_row">
            <span className="role_holder">
              <strong>Role: </strong>
              {!this.state.roleModifying ?
                  <span className="role">{teamUserRoles[this.props.item.role]}
                    <button class="button-unstyle mrgnLeft5 action_button"
                            onClick={this.setRoleModifying.bind(null, true)}>
                      <i class="fa fa-pencil"/>
                    </button>
              </span>
                  :
                  <div class="display-inline-block">
                    <select class="select_unstyle"
                            value={this.state.role}
                            onChange={this.handleInput}
                            id="user_role_select" name='role'>
                      {
                        Object.keys(teamUserRoles).map(function (item) {
                          return (
                              <option value={item} key={item}>{teamUserRoles[item]}</option>
                          )
                        }, this)
                      }
                    </select>
                    <button class="button-unstyle action_button mrgnRight5 mrgnLeft5"
                            onClick={this.setRoleModifying.bind(null, false)}>
                      <i class="fa fa-times"/>
                    </button>
                    <button class="button-unstyle action_button"
                            onClick={this.confirmUserRoleChange}>
                      <i class="fa fa-check"/>
                    </button>
                  </div>
              }
            </span>
            </div>
            <div className="tab_content_row">
            <span className="join_date_holder">
              <strong>First connection:</strong>
              <span className="join_date">
                {this.props.item.arrival_date}
              </span>
            </span>
            </div>
            <div className="tab_content_row">
            <span className="leave_date_holder">
              <strong>Departure planned:</strong>
              {!this.state.departureDateModifying ?
                  <span className="leave_date">
                {this.props.item.departure_date}
                    <button class="button-unstyle mrgnLeft5 action_button"
                            onClick={this.setDepartureDateModifying.bind(null, true)}>
                      <i class="fa fa-pencil"/>
                    </button>
              </span>
                  :
                  <div>
                    <input type="date" name="departureDate"
                           id="departure_date"
                           class="select_unstyle"
                           value={this.state.departureDate}
                           onChange={this.handleInput}/>
                    <button class="button-unstyle action_button mrgnRight5 mrgnLeft5"
                            onClick={this.setDepartureDateModifying.bind(null, false)}>
                      <i class="fa fa-times"/>
                    </button>
                    <button class="button-unstyle action_button"
                            onClick={this.confirmUserDepartureDateChange}>
                      <i class="fa fa-check"/>
                    </button>
                  </div>
              }
            </span>
            </div>
            <div className="tab_content_row">
              <div className="teams_holder">
                <strong className="heading">Teams:</strong>
                <div className="teams_list">
                  {
                    this.props.item.teams && this.props.item.teams.map(function (item) {
                      return (
                          <span className="team_name_holder" key={item.id}>
                          #
                            <span className="team_name">
                              {item.name}
                            </span>
                        </span>
                      )
                    }, this)
                  }
                </div>
              </div>
            </div>
            <div className="tab_content_row">
            <span className="using_apps_holder">
              <strong>Apps used:</strong>
              <span className="using_apps">
                {this.props.item.apps && this.props.item.apps.length}
              </span>
            </span>
            </div>
            <div className="tab_content_row">
              <button className="button-unstyle team_user_delete_button"
                      onClick={e => {this.props.dispatch(showTeamDeleteUserModal(true, this.props.item.id))}}>
                <u>Delete user</u>
              </button>
            </div>
          </div>
        </div>
    )
  }
}

@connect((store)=>{
  return {
    me: store.users.me,
    selectedItem: store.selection
  };
})
class FlexPanels extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div id="flex_contents">
          {this.props.flexActive && this.props.selectedItem.type === 'channel' &&
          <TeamChannelFlexTab
              item={this.props.selectedItem.item}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.props.toggleFlexFunc}
              userGetter={this.props.userGetter}
              dispatch={this.props.dispatch}/>}
          {this.props.flexActive && this.props.selectedItem.type === 'user' &&
          <TeamUserFlexTab
              item={this.props.selectedItem.item}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.props.toggleFlexFunc}
              dispatch={this.props.dispatch}/>}
        </div>
    )
  }
}

module.exports = FlexPanels;