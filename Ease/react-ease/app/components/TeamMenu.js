var React = require('react');
var classnames = require('classnames');
import {showTeamMenu} from "../actions/teamActions";
import {setTeamsTutorial} from "../actions/commonActions";
import {showAddTeamUserModal} from "../actions/teamModalActions";

class TeamMenu extends React.Component {
  constructor(props){
    super(props);
    this.hideIt = this.hideIt.bind(this);
  }
  hideIt(){
    this.props.dispatch(showTeamMenu(false));
  }
  render(){
    const me = this.props.me;
    const team = this.props.team;

    return (
        <div id="team_menu_options">
          <div class="popover_mask" onClick={this.hideIt}></div>
          <div class="floating_dropdown">
            <div class="dropdown_content">
              <div class="dropdown_row">
                <strong>{me.first_name} {me.last_name}</strong>
              </div>
              <div class="dropdown_row">
                <strong>{me.username}</strong>
              </div>
              <div class="dropdown_row selectable">
                Profile information
              </div>
              <div class="dropdown_row selectable" onClick={e => {this.props.dispatch(setTeamsTutorial(true)); this.hideIt();}}>
                Re-start tutorial
              </div>
              <div class="dropdown_row">
                <strong>{team.name}</strong>
              </div>
              <div class="dropdown_row selectable" onClick={e => {this.props.dispatch(showAddTeamUserModal(true)); this.hideIt();}}>
                Invite people
              </div>
              <div class="dropdown_row selectable">
                Team settings
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamMenu;