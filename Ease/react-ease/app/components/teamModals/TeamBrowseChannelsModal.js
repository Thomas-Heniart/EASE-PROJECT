import React from 'react';
import classnames from 'classnames';
import {showTeamBrowseChannelsModal} from '../../actions/teamModalActions';
import {isUserInChannel} from "../../utils/helperFunctions";
import {connect} from "react-redux";

@connect((store)=>{
  return {
    channels: store.channels.channels,
    me: store.users.me
  };
})
class TeamBrowseChannelsModal extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div className="ease_modal" id="team_browse_channels_modal">
          <div className="modal-background"></div>
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
                  {this.props.channels.map(function (item) {
                    const inChannel = isUserInChannel(item, this.props.me);
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
                              {!inChannel &&
                                <button class="button-unstyle big-button action">
                                  Ask to join
                                </button>}
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