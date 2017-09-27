var React = require('react');
var classnames = require('classnames');
var TeamAddAppsButton = require('./TeamAddAppsButton');
var EaseMainNavbar = require('./common/EaseMainNavbar');
import {showUpgradeTeamPlanModal} from "../actions/teamModalActions";
import { NavLink } from 'react-router-dom';

function TeamHeader(props){
  return (
      <header id="client_header">
        <div className="channel_header">
          <div className="tab_header">
            <button onClick={e => {props.dispatch(showUpgradeTeamPlanModal(true, 'bite'))}}>click me!</button>
            <TeamAddAppsButton target={props.item}/>
            <div className="channel_title">
              <div id="channel_name_container" className="channel_name_container">
                            <span id="channel_name" className="channel_name">
                              <i className={classnames("fa icon_wrapper", props.item.username !== undefined ? 'fa-user' : 'fa-hashtag')}/>
                              {props.item.name ? props.item.name : props.item.username}
                            </span>
                <NavLink class="mrgnLeft5" to={`${props.match.url}/flexPanel`} id="open_card_button">
                  <i className="fa fa-cog"/>
                </NavLink>
              </div>
              <div className="channel_header_info">
                <div className="channel_header_info_item" id="apps_number">
                  <i className="icon_left fa fa-square"/>
                  <span className="value">{props.appsLength}</span>
                </div>
                {props.item.userIds &&
                <div className="channel_header_info_item" id="users_number">
                  <i className="icon_left fa fa-user-o"/>
                  <span className="value">{props.item.userIds.length}</span>
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