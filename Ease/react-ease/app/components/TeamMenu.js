var React = require('react');
var classnames = require('classnames');
import {showTeamMenu} from "../actions/teamActions"

class TeamMenu extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const me = this.props.me;
    const team = this.props.team;

    return (
        <div id="team_menu_options">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamMenu(false))}}></div>
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
              <div class="dropdown_row selectable">
                Re-start tutorial
              </div>
              <div class="dropdown_row">
                <strong>{team.name}</strong>
              </div>
              <div class="dropdown_row selectable">
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