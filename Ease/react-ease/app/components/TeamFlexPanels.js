var React = require('react');
var classnames = require('classnames');

function TeamChannelFlexTab(props){
  return (
      <div className="flex_contents_panel active" id="team_tab">
        <div className="tab_heading">
          <div className="heading_row">
            <span className="heading_text">
              Team's information
            </span>
            <button className="button-unstyle button_close_flexpanel" onClick={props.toggleFlexFunc}>
              <i className="fa fa-times"/>
            </button>
          </div>
        </div>
        <div className="tab_content_body">
          <div className="tab_content_row">
            <h2 className="name_holder">
              <strong className="name">{props.item.name}</strong>
            </h2>
          </div>
          <div className="tab_content_row">
            <span className="purpose_holder">
              <strong>Purpose: </strong>
              <span className="purpose">{props.item.desc}</span>
            </span>
          </div>
          <div className="tab_content_row">
            <span className="number_apps_holder">
              <strong>Shared apps: </strong>
              <span className="number_apps">{props.item.apps.length}</span>
            </span>
          </div>
          <div className="tab_content_row">
            <div className="members_holder">
              <strong className="heading">Members:</strong>
              <div className="members_list">
                {props.item.userIds.map(function(item){
                  return (
                      <span className="member_name_holder" key={item}>
                        <span className="icon_wrapper">
                          <i className="fa fa-user"/>
                        </span>
                       <span className="member_name" >{props.userGetter(item).username}</span>
                      </span>
                  )
                }, this)}
              </div>
            </div>
          </div>
          <div className="tab_content_row">
            <button className="button-unstyle team_delete_button">
              <u>Delete team</u>
            </button>
          </div>
        </div>
      </div>
  )
}

function TeamUserFlexTab(props) {
  return (
      <div className="flex_contents_panel active" id="team_user_tab">
        <div className="tab_heading">
          <div className="heading_row">
            <span className="heading_text">
              User's information
            </span>
            <button className="button-unstyle button_close_flexpanel" onClick={props.toggleFlexFunc}>
              <i className="fa fa-times"/>
            </button>
          </div>
        </div>
        <div className="tab_content_body">
          <div className="tab_content_row">
            <h2 className="name_holder">
              <strong className="firstname">{props.item.firstName}</strong>
              <strong className="lastname">{props.item.lastName}</strong>
            </h2>
          </div>
          <div className="tab_content_row">
            <span className="username_holder">
              @<span className="username">{props.item.username}</span>
            </span>
          </div>
          <div className="tab_content_row">
            <span className="email_holder">
              <strong>Email: </strong>
              <span className="email">
                {props.item.email}
              </span>
            </span>
          </div>
          <div className="tab_content_row">
            <span className="role_holder">
              <strong>Role: </strong>
              <span className="role">{props.item.role}</span>
            </span>
          </div>
          <div className="tab_content_row">
            <span className="join_date_holder">
              <strong>First connection:</strong>
              <span className="join_date">
                {props.item.arrivalDate}
              </span>
            </span>
          </div>
          <div className="tab_content_row">
            <span className="leave_date_holder">
              <strong>Departure planned:</strong>
              <span className="leave_date">
                {props.item.departureDate}
              </span>
            </span>
          </div>
          <div className="tab_content_row">
            <div className="teams_holder">
              <strong className="heading">Teams:</strong>
              <div className="teams_list">
              {
                  props.item.teams && props.item.teams.map(function(item){
                    return (
                          <span className="team_name_holder" key={item.id}>
                          #
                            <span className="team_name">
                              {item.name}
                            </span>
                        </span>
                    )
                  }, this)
              }
              </div>
            </div>
          </div>
          <div className="tab_content_row">
            <span className="using_apps_holder">
              <strong>Apps used:</strong>
              <span className="using_apps">
                {props.item.apps && props.item.apps.length}
              </span>
            </span>
          </div>
          <div className="tab_content_row">
            <button className="button-unstyle team_user_delete_button">
              <u>Desactivate account</u>
            </button>
          </div>
        </div>
      </div>
  )
}
class FlexPanels extends React.Component {
  render(){
    return (
        <div id="flex_contents">
          {this.props.selectedItem.type === 'channel' && this.props.flexActive &&
          <TeamChannelFlexTab
              item={this.props.selectedItem.item}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.props.toggleFlexFunc}
              userGetter={this.props.userGetter}/>}

          {this.props.selectedItem.type === 'user' && this.props.flexActive &&
          <TeamUserFlexTab
              item={this.props.selectedItem.item}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.props.toggleFlexFunc}/>}
        </div>
    )
  }
}

module.exports = FlexPanels;