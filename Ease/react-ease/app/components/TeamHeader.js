var React = require('react');
var classnames = require('classnames');
var EaseMainNavbar = require('./common/EaseMainNavbar');
import {NavLink, withRouter} from "react-router-dom";
import Joyride from "react-joyride";
import {setTipSeen} from "../actions/commonActions";

const FlexPanelButton = ({location, match}) => {
    const isActive = location.pathname === `${match.url}/flexPanel`;
    const path = isActive ? match.url : `${match.url}/flexPanel`;
    return (
        <NavLink class="mrgnLeft5" to={path} id="open_card_button">
            <i className="fa fa-cog"/>
        </NavLink>
    )
};

const FlexPanelButtonWithRouter = withRouter(FlexPanelButton);

function TeamHeader(props){
  const {item, user, dispatch} = props;
  return (
      <header id="client_header">
        <div className="channel_header">
          <div className="tab_header">
            <div className="channel_title">
              <div id="channel_name_container" className="channel_name_container">
                            <span id="channel_name" className="channel_name">
                              <i className={classnames("fa icon_wrapper", props.item.username !== undefined ? 'fa-user' : 'fa-hashtag')}/>
                              {props.item.name ? props.item.name : props.item.username}
                            </span>
                  <FlexPanelButtonWithRouter/>
              </div>
              <div className="channel_header_info">
                <div className="channel_header_info_item" id="apps_number">
                  <i className="icon_left fa fa-square"/>
                  <span className="value">{props.item.team_card_ids.length}</span>
                </div>
                {props.item.team_user_ids &&
                <div className="channel_header_info_item" id="users_number">
                  <i className="icon_left fa fa-user-o"/>
                  <span className="value">{props.item.team_user_ids.length}</span>
                </div>}
                {!!props.item.purpose && !!props.item.purpose.length &&
                <div className="channel_header_info_item" id="channel_purpose">
                  <span className="value">{props.item.purpose}</span>
                </div>}
              </div>
            </div>
          </div>
          {!!item.team_user_ids && !item.default && !user.status.tip_team_channel_settings_seen &&
          <Joyride
              steps={[{
                title: 'Edit rooms here to add or remove people.',
                isFixed: true,
                selector:"#open_card_button",
                position: 'bottom'
              }]}
              locale={{ back: 'Back', close: 'Got it!', last: 'Got it!', next: 'Next', skip: 'Skip the tips' }}
              disableOverlay={true}
              run={true}
              callback={(action) => {
                if (action.type === 'finished')
                  dispatch(setTipSeen({
                    name: 'tip_team_channel_settings_seen'
                  }));
              }}
          />}
          {!!item.room_ids && !user.status.tip_team_user_settings_seen &&
          <Joyride
              steps={[{
                title: <span>Setup personal settings of a user here <span class="fw-normal">(ex: departure date).</span></span>,
                isFixed: true,
                selector:"#open_card_button",
                position: 'bottom'
              }]}
              locale={{ back: 'Back', close: 'Got it!', last: 'Got it!', next: 'Next', skip: 'Skip the tips' }}
              disableOverlay={true}
              run={true}
              callback={(action) => {
                if (action.type === 'finished')
                  dispatch(setTipSeen({
                    name: 'tip_team_user_settings_seen'
                  }));
              }}
          />}
          <EaseMainNavbar/>
        </div>
      </header>
  )
}

module.exports = TeamHeader;