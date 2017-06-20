var React = require('react');
var classnames = require('classnames');
import {showTeamDeleteChannelModal} from "../actions/teamModalActions"
import {selectChannelFromListById} from "../utils/helperFunctions"
import {deleteTeamChannel} from "../actions/channelActions"
import {connect} from "react-redux"

@connect((store)=>{
  return {
    modal: store.teamModals.teamDeleteChannelModal,
    channels: store.channels.channels
  };
})
class TeamDeleteChannelModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      channel: selectChannelFromListById(this.props.channels, this.props.modal.channel_id),
      checkboxConfirmed: false
    };
    this.handleCheckboxConfirm = this.handleCheckboxConfirm.bind(this);
    this.validateModal = this.validateModal.bind(this);
  }
  handleCheckboxConfirm(e){
    this.setState({checkboxConfirmed: !this.state.checkboxConfirmed});
  }
  validateModal(){
    this.props.dispatch(deleteTeamChannel(this.state.channel.id)).then(() => {
      this.props.dispatch(showTeamDeleteChannelModal(false));
    });
  }
  render(){
    const channel_name = this.state.channel.name;

    return (
        <div className="ease_modal" id="team_delete_channel_modal">
          <div className="modal-background"></div>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamDeleteChannelModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row">
                <h1>You are about to delete #{channel_name}</h1>
              </div>
              <div class="content_row">
                <span>
                  Deleting a Group is usefull to clean things up when you do not anticipate using this group anymore. If you delete this group:
                </span>
              </div>
              <div class="content_row flex_direction_column">
                <span style={{fontSize: '.9rem'}}>-No one will be able to send apps in it anymore</span>
                <span style={{fontSize: '.9rem'}}>-It will be closed for anyone who has it open and all members will be removed</span>
                <span style={{fontSize: '.9rem'}}>-All apps included in it will be deleted from the dashboard of the users concerned</span>
                <span style={{fontSize: '.9rem'}}>-All its content will be removed from Ease.space, and you cannot restore the content once deleted</span>
              </div>
              <div class="content_row">
                <span>Are you sure you want to delete <strong>#{channel_name}</strong> ?</span>
              </div>
              <div class="content_row align_items_center">
                <input type="checkbox" id="confirmation_checkbox"
                       class="checkbox_unstyle"
                        checked={this.state.checkboxConfirmed}
                        onChange={this.handleCheckboxConfirm}/>
                <label class="mrgnLeft5" style={{marginBottom: 0}}  htmlFor="confirmation_checkbox">Yes, I am absolutely sure</label>
              </div>
              <div className="content_row buttons_row">
                <div className="buttons_wrapper">
                  <button className="button-unstyle neutral_background action_text_button mrgnRight5"
                          onClick={e => {this.props.dispatch(showTeamDeleteChannelModal(false))}}>Cancel</button>
                  <button className="button-unstyle alert_background action_text_button"
                          onClick={this.validateModal}
                          disabled={!this.state.checkboxConfirmed}>Delete it</button>
                </div>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamDeleteChannelModal;