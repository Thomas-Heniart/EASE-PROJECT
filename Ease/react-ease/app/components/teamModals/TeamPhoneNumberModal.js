var React = require('react');
var classnames = require('classnames');
import {showTeamPhoneNumberModal} from "../../actions/teamModalActions";
import {selectUserFromListById} from "../../utils/helperFunctions";
import {editTeamUserPhone} from "../../actions/userActions";
import rrui from 'react-phone-number-input/rrui.css'
import rpni from 'react-phone-number-input/style.css'
import Phone from 'react-phone-number-input';
import {connect} from "react-redux";

@connect((store)=>{
  return {
    team: store.team,
    users: store.users.users
  };
})
class TeamPhoneNumberModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      phone: ''
    };
    this.confirmModal = this.confirmModal.bind(this);
  }
  confirmModal(){
    const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);

    this.props.dispatch(editTeamUserPhone(me.id, this.state.phone)).then(r => {
      this.props.dispatch(showTeamPhoneNumberModal(false));
    });
  }
  render(){
    const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamPhoneNumberModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_verify_team_user">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamPhoneNumberModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              You are Team Owner!
            </div>
            <div class="row display-flex flex_direction_column single-row">
              <span>As you are the one creating team, you are the team owner. It is an important role that you will be able to give to another team member later.</span>
              <span>For safety reasons, we need your phone number in case you lose you Ease.space password.</span>
              <Phone
                  placeholder="Enter phone number"
                  value={ this.state.phone }
                  onChange={ phone => {this.setState({ phone })} } />
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    disabled={this.state.phone.length === 0}
                    onClick={this.confirmModal}>
              SAVE
            </button>
          </div>
        </div>
    )
  }
}

module.exports = TeamPhoneNumberModal;