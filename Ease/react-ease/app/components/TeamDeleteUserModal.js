var React = require('react');
var classnames = require('classnames');
import {showTeamDeleteUserModal} from "../actions/teamModalActions"

import {connect} from "react-redux"

@connect((store)=>{
  return {
    selectedUser: store.selection.item,
    team_name: store.team.name
  };
})
class TeamDeleteUserModal extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const teamName = this.props.team_name;
    const username = this.props.selectedUser.username;

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
                <h1>You are about to desactivate {username}'s membership</h1>
                </div>
              <div class="content_row">
               <span class="first_word_capitalize">{username} will lose access to all accounts. But be carefull : we don't delete accounts on the websites themselves... Yet ;) Don't forget to go delete them by yourself.</span>
                </div>
              <div class="content_row">
                <span class="first_word_capitalize">While working here, {username} shared X accounts to {teamName}. Please select(tic) the ones you want to keep. The other ones will be archived.</span>
                </div>
              <div class="content_row">
                <span>By archiving an app, every person related to it, will lose the access.</span>
                </div>
              </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamDeleteUserModal;