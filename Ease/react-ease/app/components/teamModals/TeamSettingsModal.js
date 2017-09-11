import React from 'react';
import classnames from 'classnames';
import {showTeamSettingsModal} from '../../actions/teamModalActions';
import {editTeamName} from '../../actions/teamActions';
import {connect} from "react-redux";

function SingleInputForm(props) {
  return(
      <div class="display-flex flex_direction_column">
        <input type="text"/>
        <div class="display-flex">
          <button class="button-unstyle neutral_background action_text_button">
            Cancel
          </button>
          <button class="button-unstyle positive_background action_text_button">
            Validate
          </button>
        </div>
      </div>
  )
}

@connect((store)=>{
  return {
    team : store.team
  };
})
class TeamSettingsModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      teamName: '',
      teamNameModifying: false
    };
    this.setTeamNameModifying = this.setTeamNameModifying.bind(this);
    this.modifyTeamName = this.modifyTeamName.bind(this);
    this.handleInput = this.handleInput.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  setTeamNameModifying(state){
    if (state){
      this.setState({teamName: this.props.team.name, teamNameModifying: state});
    }else {
      this.setState({teamNameModifying: state});
    }
  }
  modifyTeamName(){
    this.props.dispatch(editTeamName(this.state.teamName)).then(() => {
      this.setTeamNameModifying(false);
    });
  }
  render() {
    return (
        <div className="ease_modal" id="team_settings_modal">
          <div className="modal-background"/>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {
            this.props.dispatch(showTeamSettingsModal(false))
          }}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row justify_content_center">
                <h1>Team settings</h1>
              </div>
              <div class="content_row flex_direction_column">
                <h3>Team name</h3>
                <div class="display-flex sub">
                  <span class="min_flex_shrink">Team name displayed for your team:&nbsp;</span>
                  {!this.state.teamNameModifying ?
                      <div class="display-flex full_flex">
                        <strong class="full_flex">{this.props.team.name}</strong>
                        <button class="button-unstyle underlineOnHover edit-btn" onClick={this.setTeamNameModifying.bind(null, true)}>
                          <strong>Edit</strong>
                        </button>
                      </div>
                      :
                      <div class="display-flex flex_direction_column full_flex">
                        <input type="text"
                               id="teamName"
                               class="input_unstyle modal_input"
                               name="teamName"
                               value={this.state.teamName}
                               placeholder="Team name..."
                               onChange={this.handleInput}/>
                        <div class="display-flex button-set">
                          <button class="button-unstyle neutral_background action_text_button"
                                  onClick={this.setTeamNameModifying.bind(null, false)}>
                            Cancel
                          </button>
                          <button class="button-unstyle positive_background action_text_button"
                                  onClick={this.modifyTeamName}>
                            Validate
                          </button>
                        </div>
                      </div>
                  }
                </div>
                <div class="content_row flex_direction_column">
                  <h3>Team plan</h3>
                  <div class="display-flex sub">
                    <span>You currently use the&nbsp;</span>
                    <strong class="full_flex">Free version</strong>
                    <button class="button-unstyle underlineOnHover"  style={{color:"#45C997"}}>
                      <strong>Upgrade!</strong>
                    </button>
                  </div>
                </div>
                <div class="content_row flex_direction_column">
                  <h3>Billing address</h3>
                </div>
                <div class="content_row flex_direction_column">
                  <h3>Credit card information</h3>
                </div>
                <div class="content_row flex_direction_column">
                  <h3>Manage subscription</h3>
                  <div class="sub">
                    <button class="button-unstyle underlineOnHover edit-btn" style={{color: "#E84855"}}>
                      <strong>Unsubscribe</strong>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamSettingsModal;