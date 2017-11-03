var React = require('react');
var classnames = require('classnames');
var TeamAddAppsButton = require('./TeamAddAppsButton');
var EaseMainNavbar = require('./common/EaseMainNavbar');
import {NavLink, withRouter} from "react-router-dom";

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
  return (
      <header id="client_header">
        <div className="channel_header">
          <div className="tab_header">
            <TeamAddAppsButton target={props.item} setAddAppView={props.setAddAppView}/>
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
                  <span className="value">{props.item.app_ids.length}</span>
                </div>
                {props.item.user_ids &&
                <div className="channel_header_info_item" id="users_number">
                  <i className="icon_left fa fa-user-o"/>
                  <span className="value">{props.item.user_ids.length}</span>
                </div>}
                {props.item.purpose !== undefined && props.item.purpose.length > 0 &&
                <div className="channel_header_info_item" id="channel_purpose">
                  <span className="value">{props.item.purpose}</span>
                </div>}
              </div>
            </div>
          </div>
          <EaseMainNavbar/>
        </div>
      </header>
  )
}

module.exports = TeamHeader;