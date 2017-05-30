var React = require('react');
var classnames = require('classnames');

function TeamHeader(props){
  return (
      <header id="client_header">
        <div className="channel_header">
          <div className="tab_header">
            <div className="channel_title">
              <div id="channel_name_container" className="channel_name_container">
                            <span id="channel_name" className="channel_name">
                              <i className={classnames("fa fa-user icon_wrapper", props.icons[props.selectedItem.type])}/>
                              {props.selectedItem.item.name ? props.selectedItem.item.name : props.selectedItem.item.username}
                            </span>
              </div>
              <div className="channel_header_info">
                {props.selectedItem.item.apps && <div className="channel_header_info_item" id="apps_number">
                  <i className="icon_left fa fa-share-alt-square"/>
                  <span className="value">{props.selectedItem.item.apps.length}</span>
                </div>}
                {props.selectedItem.item.userIds && <div className="channel_header_info_item" id="users_number">
                  <i className="icon_left fa fa-user-o"/>
                  <span className="value">{props.selectedItem.item.userIds.length}</span>
                </div>}
              </div>
            </div>
            <div className="channel_title_info">
              <button className={classnames("button-unstyle ease_tipped", props.flexActive ? 'active' : '')} id="open_card_button" onClick={props.toggleFlexFunc}>
                <span className="ease_tip">Card</span>
                <i className="fa fa-id-card-o"/>
              </button>
            </div>
          </div>
        </div>
      </header>
  )
}

module.exports = TeamHeader;