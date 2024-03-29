import React from 'react';
import classnames from 'classnames';
import {showTeamBrowseChannelsModal} from '../../actions/teamModalActions';
import {isUserInChannel} from "../../utils/helperFunctions";
import {askJoinChannel, addTeamUserToChannel} from "../../actions/channelActions";
import {connect} from "react-redux";

@connect((store)=> ({
  teams: store.teams,
  channels: store.channels.channels,
  me: store.users.me
}))
class TeamBrowseChannelsModal extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const team = this.props.team[this.props.match.params.teamId];
    const me = team.team_users[team.my_team_user_id];

    return (
        <div className="ease_modal" id="team_browse_channels_modal">
          <div className="modal-background"/>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamBrowseChannelsModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row">
                <h1>Browse all rooms</h1>
              </div>
              <div class="content_row flex_direction_column">
                <div class="modal_input_wrapper">
                  <i class="fa fa-search input_icon"/>
                  <input type="text" value="" class="input_unstyle" id="name_input"
                         placeholder="Search room" name="name"/>
                </div>
                <div class="mrgnTop5">
                  {team.rooms.map(function (id) {
                    const item = team.rooms[id];
                    const inChannel = item.team_user_ids.indexOf(team.my_team_user_id);
                    const isAsked = item.join_requests.indexOf(team.my_team_user_id);
                    return (
                        <div class="channel_card display-flex" key={item.id}>
                          <div class="display-flex flex_direction_column full_flex">
                            <span class="mrgnBottom5"># <strong class="name">{item.name}</strong></span>
                            <span class="purpose">{item.purpose}</span>
                          </div>
                          <div class="display-flex flex_direction_column">
                            <div class="text-right mrgnBottom5 full_flex">
                              <i class="fa fa-user-o"/>&nbsp;{item.userIds.length}
                            </div>
                            <div>
                              {!inChannel && me.role > 1 &&
                              <button class="button-unstyle big-button action"
                                      onClick={e => {this.props.dispatch(addTeamUserToChannel({
                                        team_id: team.id,
                                        channel_id: item.id,
                                        team_user_id: team.my_team_user_id
                                      }))}}>
                                Join this channel
                              </button>}
                              {!inChannel && !isAsked && me.role === 1 &&
                              <button class="button-unstyle big-button action"
                                      onClick={e => {this.props.dispatch(askJoinChannel(item.id))}}>
                                Ask to join
                              </button>}
                              {!inChannel && isAsked && me.role === 1 &&
                              <span>Join request sent</span>}
                            </div>
                          </div>
                        </div>
                    )
                  }, this)}
                </div>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamBrowseChannelsModal;