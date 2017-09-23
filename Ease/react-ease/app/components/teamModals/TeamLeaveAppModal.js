var React = require('react');
var classnames = require('classnames');
import {showTeamLeaveAppModal} from "../../actions/teamModalActions";
import {teamAppDeleteReceiver} from "../../actions/appsActions";
import {
    selectChannelFromListById,
    selectUserFromListById,
    findMeInReceivers
} from "../../utils/helperFunctions";

import {connect} from "react-redux";

@connect((store)=>{
  return {
    modal: store.teamModals.teamLeaveAppModal,
    channels: store.channels.channels,
    users: store.users.users,
    team_id: store.team.id
  };
})
class TeamLeaveAppModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      confirmed: false
    };
    this.confirm = this.confirm.bind(this);
    this.confirmModal = this.confirmModal.bind(this);
  }
  confirmModal(){
    const receiver = findMeInReceivers(this.props.modal.app.receivers, this.props.modal.team_user_id);
    this.props.dispatch(teamAppDeleteReceiver({
      team_id: this.props.team_id,
      app_id: this.props.modal.app.id,
      team_user_id: this.props.modal.team_user_id,
      shared_app_id: receiver.shared_app_id
    })).then(response => {
      this.props.dispatch(showTeamLeaveAppModal(false));
    });
  }
  confirm(){
    this.setState({confirmed: !this.state.confirmed});
  }
  render(){
    const app = this.props.modal.app;
    const channel = selectChannelFromListById(this.props.channels, app.origin.id);

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamLeaveAppModal(false))}}/>
          <div class="ease_popup ease_team_popup" id="modal_team_leave_app">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamLeaveAppModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Leave {app.name}
            </div>
            <div class="row display-flex flex_direction_column" style={{padding: '20px',fontSize:".9rem"}}>
              <span style={{marginBottom: "20px"}}>Are you sure you want to leave <strong>{app.name}</strong>, in #{channel.name} ?</span>
              <span>If yes:</span>
              <ul style={{marginBottom: 0}}>
                <li>You will not be able to access this App anymore.</li>
                <li>This app will be removed from your Dashboard in case it is pinned to it.</li>
                <li>The App Admin will be notified that you do not have access to it anymore.</li>
              </ul>
            </div>
            <div class="row display-flex align_items_center" style={{fontSize: ".9rem", marginBottom:"20px"}}>
              <input style={{margin: "0 15px 0 22px"}} type="checkbox" id="confirm" checked={this.state.confirmed} onClick={this.confirm}/>
              <label htmlFor="confirm" style={{margin: 0, fontWeight: 'normal'}}>
                Yes, I am absolutely sure
              </label>
            </div>
            <button class="row button-unstyle negative_button big_validate_button"
                    disabled={!this.state.confirmed}
                    onClick={this.confirmModal}>
              LEAVE <span class="text-uppercase">{app.name}</span>
            </button>
          </div>
        </div>
    )
  }
}

module.exports = TeamLeaveAppModal;