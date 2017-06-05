var React = require('react');
var classnames = require('classnames');
import {showTeamChannelAddUserModal} from "../actions/teamModalActions"

import {connect} from "react-redux"

@connect((store)=>{
  return {
    selectedChannel: store.selection.item,
    users: store.users.users
  };
})
class TeamChannelAddUserModal extends React.Component{
  constructor(props){
    super(props);
    this.state = {
    };

    this.isUserInList = this.isUserInList.bind(this);
    this.state.users = this.props.users.filter(function(item){
      return this.isUserInList(item.id);
    }.bind(this));
  }
  isUserInList(id){
    if (this.props.selectedChannel.userIds.indexOf(id) >= 0)
      return true;
    return false;
  }
  render(){
    return (
        <div className="ease_modal" id="team_channel_add_user_modal">
          <div className="modal-background"></div>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamChannelAddUserModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row">
                <h1>Browse people</h1>
                </div>
              <div class="content_row flex_direction_column">
                <div class="modal_input_wrapper">
                  <i class="fa fa-search ease_icon"/>
                    <input type="text" value="" class="input_unstyle" id="name_input" placeholder="Search user name or email" name="name"/>
                  </div>
              </div>
              <div class="content_row">
                <div class="user_selecting_list width100">
                    {
                        this.state.users.map(function(item){
                          return (
                              <div class="user display-flex flex_direction_column" key={item.id}>
                                {
                                    item.first_name != null ?
                                        <strong class="name">{item.first_name} {item.last_name}</strong>
                                        :
                                        <strong class="name"><em>User name not setup yet</em></strong>
                                }
                                <span>@{item.username} - {item.email}</span>
                              </div>
                          )
                        }, this)
                    }
                  </div>
                </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamChannelAddUserModal;