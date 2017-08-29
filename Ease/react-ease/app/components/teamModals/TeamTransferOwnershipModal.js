var React = require('react');
var classnames = require('classnames');
import {showTeamTransferOwnershipModal} from "../../actions/teamModalActions";
import {transferTeamOwnership} from "../../actions/userActions";
import {connect} from "react-redux"

@connect((store)=>{
  return {
    team: store.team,
    modal: store.teamModals.teamTransferOwnershipModal
  };
})
class TeamTransferOwnershipModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      password : ""
    };
    this.confirmModal = this.confirmModal.bind(this);
    this.handleInput = this.handleInput.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  confirmModal(){
    this.props.dispatch(transferTeamOwnership(this.state.password, this.props.modal.user.id)).then(r => {
      this.props.dispatch(showTeamTransferOwnershipModal(false));
    });
  }
  render(){
    const user = this.props.modal.user;
    const myId = this.props.team.myTeamUserId;

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamTransferOwnershipModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_verify_team_user">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamTransferOwnershipModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Transfer Team ownership
            </div>
            <div class="row display-flex flex_direction_column single-row">
              <span>For safety reasons, we need know the phone number of the team owner.</span>
              <span>Before transfering the ownership to {user.username}, we need his/her phone number.</span>
              <span>Don't worry, you will receive a notification once ownership transfer is completed.</span>
              <span><strong>Transfering ownership is one-way street. You won't be able to undo this action.</strong></span>
              <span>Enter your Ease.space password to confirm the trasfer.</span>
              <input type="password"
                     name="password"
                     placeholder="Password"
                     onChange={this.handleInput}
                     value={this.state.password}
                     id="password"
                     class="modal_input input_unstyle"/>
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    disabled={this.state.password.length === 0}
                    onClick={this.confirmModal}>
              CONFIRM ACCESS
            </button>
          </div>
        </div>
    )
  }
}

module.exports = TeamTransferOwnershipModal;