var React = require('react');
var classnames = require('classnames');
import {showTeamMenu} from "../actions/teamActions";
import {setTeamsTutorial} from "../actions/commonActions";
import {showAddTeamUserModal, showTeamSettingsModal} from "../actions/teamModalActions";
import {isAdmin, isOwner} from "../utils/helperFunctions";

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
        <div id="team_menu_options" class="bordered_scrollbar">
          <div class="popover_mask" onClick={this.hideIt}></div>
          <div class="floating_dropdown">
            <div class="dropdown_content">
              <div class="dropdown_row flex_direction_column">
                <strong class="name">{me.first_name} {me.last_name}</strong>
                <span class="username">@{me.username}</span>
              </div>
              <div class="border"></div>
              <div class="dropdown_row flex_direction_column section user">
                <span class="selectable">Profile information</span>
                <span class="selectable" onClick={e => {this.props.dispatch(setTeamsTutorial(false)); this.hideIt();}}>
                  Re-start tutorial
                </span>
              </div>
              {isAdmin(me.role) &&
              <div class="dropdown_row flex_direction_column section team">
                <strong class="teamName">{team.name}</strong>
                <span class="selectable" onClick={e => {this.props.dispatch(showAddTeamUserModal(true)); this.hideIt();}}>
                  Invite new members
                </span>
                {isOwner(me.role) &&
                  <span class="selectable" onClick={e => {this.props.dispatch(showTeamSettingsModal(true)); this.hideIt();}}>
                  Team settings
                </span>}
              </div>}
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamMenu;