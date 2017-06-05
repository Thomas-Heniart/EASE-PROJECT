var React = require('react');
var classnames = require('classnames');
import {connect} from "react-redux"

import {showAddTeamUserModal} from "../actions/teamModalActions"

class FirstStepAddUser extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      dropdown: false
    }
  }
  render() {
    return (
        <div className="contents" id="first_step">
          <div className="content_row">
            <h1 className="full_width">Invite a team member</h1>
          </div>
          <div className="content_row">
            <div className="signed_input">
              <label htmlFor="email_input">Email address</label>
              <input value={this.props.email}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="email_input" name="email" type="email" placeholder="name@company.com" className="full_width modal_input"/>
            </div>
            <div className="signed_input">
              <label htmlFor="fname_input">First name</label>
              <input value={this.props.fname}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="fname_input" name="fname" type="text" placeholder="Optional" className="full_width modal_input"/>
            </div>
            <div className="signed_input">
              <label htmlFor="lname_input">Last name</label>
              <input value={this.props.lname}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="lname_input" name="lname" type="text" placeholder="Optional" className="full_width modal_input"/>
            </div>
          </div>
          <div className="content_row">
            <div className="signed_input">
              <label htmlFor="user_role_select">User role</label>
              <select value={this.props.role}
                      onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                      id="user_role_select" name='role' className="full_width select_unstyle modal_input">
                <option value="1">Member</option>
                <option value="3">Moderator</option>
                <option value="7">Admin</option>
              </select>
            </div>
            <div className="signed_input">
              <label htmlFor="departure_date_input">Departure date</label>
              <input value={this.props.departure_date}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="departure_date_input" name="departure_date" type="date" className="full_width modal_input" placeholder="Optional"/>
            </div>
          </div>
          <div className="content_row">
            <div className="tagged_input_container full_width">
              <label htmlFor="teams_input">Team(s)</label>
              <div className="modal_input_wrapper full_width item_list">
                {
                    this.props.selectedChannels.map(function(item){
                      return (
                          <div className="input_tag" key={item.id}>
                            <span>{item.name}</span>
                            <button className="button-unstyle" onClick={this.props.deselectChannelFunc.bind(null, item.id)}>
                              <i className="fa fa-times"/>
                            </button>
                          </div>
                      )
                    }, this)
                }
                {this.state.dropdown && <div className="popover_mask" onClick={(e) => {this.setState({dropdown:false})}}></div>}
                <input id="teams_input" className="full_width input_unstyle" name="teams" type="text" placeholder="Search by name"
                        onFocus={(e) => {this.setState({dropdown: true})}}/>
                <div className={classnames("floating_dropdown", this.state.dropdown ? "show" : null)}>
                  <div className="dropdown_content">
                      {
                          this.props.channels.map(function (item) {
                            return (
                                <div onClick={this.props.selectChannelFunc.bind(null, item.id)} className={classnames("dropdown_row selectable", item.selected ? "selected": null)} key={item.id}>
                                  <span className="main_value">{item.name}</span>{item.purpose.length && <span className="text-muted">&nbsp;- {item.purpose}</span>}
                                </div>
                            )
                          }, this)
                      }
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="content_row buttons_row">
            <div className="buttons_wrapper">
              <button className="button-unstyle neutral_background action_text_button cancel_button">
                Cancel
              </button>
              <button className="button-unstyle positive_background action_text_button next_button">
                Next
              </button>
            </div>
          </div>
        </div>
    )
  }
}

@connect((store)=>{
  return {
    channels: store.channels.channels
  };
})
class TeamAddUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      email: '',
      fname: '',
      lname: '',
      role: '1',
      departure_date: '',
      selectedChannels: [],
      channels: []
    };
    this.state.channels = this.props.channels.map(function (item) {
      item.selected = false;
      return item;
    });
    this.handleInputs = this.handleInputs.bind(this);
    this.selectChannel = this.selectChannel.bind(this);
    this.deselectChannel = this.deselectChannel.bind(this);
  }
  handleInputs(name, value){
    this.setState({[name]: value});
  }
  deselectChannel(id){
    var selectedChannels = this.state.selectedChannels;
    var channels = this.state.channels.map(function (item) {
      if (item.id === id){
        selectedChannels.splice(selectedChannels.indexOf(item), 1);
        item.selected = false;
      }
      return item;
    });
    this.setState({channels: channels, selectedChannels: selectedChannels});
  }
  selectChannel(id){
    var selectedChannels = this.state.selectedChannels;
    var channels = this.state.channels.map(function(item){
      if (item.id === id) {
        if (item.selected)
          return item;
        item.selected = true;
        selectedChannels.push(item);
      }
      return item;
    });
    this.setState({channels: channels, selectedChannels: selectedChannels});
  }
  render(){
    return (
        <div className="ease_modal" id="add_user_modal">
          <div className="modal-background"></div>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showAddTeamUserModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <FirstStepAddUser
                email={this.state.email}
                fname={this.state.fname}
                lname={this.state.lname}
                role={this.state.role}
                departure_date={this.state.departure_date}
                handleInputs={this.handleInputs}
                channels={this.state.channels}
                selectedChannels={this.state.selectedChannels}
                selectChannelFunc={this.selectChannel}
                deselectChannelFunc={this.deselectChannel}/>
          </div>
        </div>
    )
  }
}

module.exports = TeamAddUserModal;