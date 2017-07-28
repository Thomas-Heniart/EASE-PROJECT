var React = require('react');
var classnames = require('classnames');
var TeamAddAppsButton = require('./TeamAddAppsButton');
var EaseMainNavbar = require('./common/EaseMainNavbar');
function TeamHeader(props){
  return (
      <header id="client_header">
        <div className="channel_header">
          <div className="tab_header">
            <TeamAddAppsButton/>
            <div className="channel_title">
              <div id="channel_name_container" className="channel_name_container">
                            <span id="channel_name" className="channel_name">
                              <i className={classnames("fa fa-user icon_wrapper", props.icons[props.selectedItem.type])}/>
                              {props.selectedItem.item.name ? props.selectedItem.item.name : props.selectedItem.item.username}
                            </span>
                <button className={classnames("button-unstyle mrgnLeft5", props.flexActive ? 'active' : '')}
                        data-tip="Room Settings"
                        id="open_card_button"
                        onClick={props.toggleFlexFunc}>
                  <i className="fa fa-cog"/>
                </button>
              </div>
              <div className="channel_header_info">
                {props.selectedItem.item.apps &&
                <div className="channel_header_info_item" id="apps_number">
                  <i className="icon_left fa fa-share-alt-square"/>
                  <span className="value">{props.selectedItem.item.apps.length}</span>
                </div>}
                {props.selectedItem.item.userIds &&
                <div className="channel_header_info_item" id="users_number">
                  <i className="icon_left fa fa-user-o"/>
                  <span className="value">{props.selectedItem.item.userIds.length}</span>
                </div>}
                {props.selectedItem.type === 'channel' && props.selectedItem.item.purpose.length > 0 &&
                <div className="channel_header_info_item" id="channel_purpose">
                  <span className="value">{props.selectedItem.item.purpose}</span>
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