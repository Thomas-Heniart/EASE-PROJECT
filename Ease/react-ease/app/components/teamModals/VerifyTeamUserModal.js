var React = require('react');
var classnames = require('classnames');
import {showVerifyTeamUserModal} from "../../actions/teamModalActions";
import {verifyTeamUserArrive} from "../../actions/userActions";
import {connect} from "react-redux";

@connect((store)=>{
  return {
    team: store.team,
    modal: store.teamModals.verifyTeamUserModal
  };
})
class VerifyTeamUserModal extends React.Component {
  constructor(props){
    super(props);
    this.confirmModal = this.confirmModal.bind(this);
  }
  confirmModal(){
    this.props.dispatch(verifyTeamUserArrive(this.props.modal.user.id)).then(() => {
      this.props.dispatch(showVerifyTeamUserModal(false));
    });
  }
  render(){
    const user = this.props.modal.user;
    const team = this.props.team;

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showVerifyTeamUserModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_verify_team_user">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showVerifyTeamUserModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Ready to get in the team!
            </div>
            <div class="row display-flex flex_direction_column single-row">
              <span><strong>{user.username}</strong> accepted your invitation to join {team.name}.</span>
              <span><strong>{user.username}</strong>'s user information is:<br/>{user.first_name} {user.last_name} - {user.email}</span>
              <span>Congrats! Together you can now work on the tools of your team without worrying about passwords.</span>
              <span>This is your final confirmation to let {user.username} access the team {team.name}</span>
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    onClick={this.confirmModal}>
              CONFIRM ACCESS
            </button>
          </div>
        </div>
    )
  }
}

module.exports = VerifyTeamUserModal;