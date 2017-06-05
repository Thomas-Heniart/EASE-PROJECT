var React = require('react');
var classnames = require('classnames');
import {connect} from "react-redux"
import {showAddTeamChannelModal} from "../actions/teamModalActions"

@connect((store)=>{
  return {
    users: store.users.users
  };
})
class TeamAddChannelModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      users: [],
      selectedUsers: [],
      name: '',
      purpose: '',
      dropdown: false
    };
    this.state.users = this.props.users.map(function (item) {
      item.selected = false;
      return (item);
    });
    this.selectUser = this.selectUser.bind(this);
    this.deselectUser = this.deselectUser.bind(this);
    this.handleInput = this.handleInput.bind(this);
    this.hideDropdown = this.hideDropdown.bind(this);
    this.showDropdown = this.showDropdown.bind(this);
  }
  hideDropdown(){
    this.setState({dropdown: false});
  }
  showDropdown(){
    this.setState({dropdown: true});
  }
  handleInput(name, value){
    this.setState(function () {
      return {
        [name]: value
      }
    });
  }
  deselectUser(id){
    var selectedUsers = this.state.selectedUsers;
    var users = this.state.users.map(function (item) {
      if (item.id === id){
        selectedUsers.splice(selectedUsers.indexOf(item), 1);
        item.selected = false;
      }
      return item;
    });
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  selectUser(id){
    var selectedUsers = this.state.selectedUsers;
    var users = this.state.users.map(function(item){
      if (item.id === id) {
        if (item.selected)
          return item;
        item.selected = true;
        selectedUsers.push(item);
      }
      return item;
    });
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  render(){
    return (
        <div className="ease_modal" id="add_channel_modal">
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showAddTeamChannelModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div className="contents">
              <div className="content_row flex_direction_column">
                <h1 className="width100">Create a group</h1>
                <span>Groups are where your team members securely exchange tools and credentials. They can be organized by topic, working group, or specific project.</span>
              </div>
              <div className="content_row flex_direction_column">
                <label htmlFor="name_input">Name</label>
                <div className="modal_input_wrapper">
                  <i className="fa fa-hashtag ease_icon"/>
                  <input onChange={(e)=>{this.handleInput(e.target.name, e.target.value)}}
                         value={this.state.name}
                         className="input_unstyle" id="name_input" type="text" placeholder="Name" name="name"/>
                </div>
              </div>
              <div className="content_row flex_direction_column">
                <label>Purpose</label>
                <input onChange={(e)=>{this.handleInput(e.target.name, e.target.value)}}
                       value={this.state.purpose}
                       className="modal_input" id="purpose_input" type="text" placeholder="Optional" name="purpose"/>
              </div>
              <div className="content_row flex_direction_column">
                <label style={{fontWeight:'normal'}}><strong>Members :</strong> people who will have access to this group</label>
                <div className="modal_input_wrapper item_list">
                  {
                    this.state.selectedUsers.map(function(item){
                      return (
                          <div className="input_tag" key={item.id}>
                            <span>{item.username}</span>
                            <button className="button-unstyle" onClick={this.deselectUser.bind(null,item.id)}>
                              <i className="fa fa-times"/>
                            </button>
                          </div>
                      )
                    }, this)
                  }
                  <input onFocus={this.showDropdown} className="input_unstyle" id="members_input" type="text" placeholder="Search by name" name="members"/>
                  <div className={classnames("floating_dropdown", this.state.dropdown ? "show" : null)}>
                    <div className="dropdown_content">
                      {
                        this.state.users.map(function (item) {
                          return (
                              <div onClick={this.selectUser.bind(null, item.id)}
                                   className={classnames("dropdown_row selectable", item.selected ? "selected": null)}
                                   key={item.id}>
                                <span className="main_value">{item.username}</span>
                                {item.first_name != null && <span className="text-muted">&nbsp;- {item.first_name}&nbsp;{item.last_name}</span>}
                              </div>
                          )
                        }, this)
                        }
                    </div>
                  </div>
                </div>
              </div>
              <div className="content_row buttons_row">
                <div className="buttons_wrapper">
                  <button className="button-unstyle neutral_background action_text_button mrgnRight5"
                          onClick={e => {this.props.dispatch(showAddTeamChannelModal(false))}}>Cancel</button>
                  <button className="button-unstyle positive_background action_text_button">Next</button>
                </div>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamAddChannelModal;