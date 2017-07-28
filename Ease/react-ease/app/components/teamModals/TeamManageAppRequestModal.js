var React = require('react');
var classnames = require('classnames');
import {showTeamManageAppRequestModal} from "../../actions/teamModalActions"
import {
    selectChannelFromListById,
    selectUserFromListById,
    findMeInReceivers
} from "../../utils/helperFunctions"

import {connect} from "react-redux"

@connect((store)=>{
  return {
    modal: store.teamModals.teamManageAppRequestModal,
    channels: store.channels.channels,
    users: store.users.users
  };
})
class TeamManageAppRequestModal extends React.Component {
  constructor(props){
    super(props);
    this.confirmModal = this.confirmModal.bind(this);
  }
  confirmModal(){
      this.props.dispatch(showTeamManageAppRequestModal(false));
  }
  render(){
    const app = this.props.modal.app;
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamManageAppRequestModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_team_leave_app">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamManageAppRequestModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Who do you allow to join ?
            </div>
            <div class="row display-flex flex_direction_column" style={{padding: '20px',fontSize:".9rem"}}>
              <span style={{marginBottom: "20px"}}>People would like to access <strong>{app.name}</strong> ?</span>
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    onClick={this.confirmModal}>
              DONE
            </button>
          </div>
        </div>
    )
  }
}

module.exports = TeamManageAppRequestModal;