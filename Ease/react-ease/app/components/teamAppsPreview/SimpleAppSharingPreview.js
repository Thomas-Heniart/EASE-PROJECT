var React = require('react');
var classnames = require('classnames');
import {
    selectUserFromListById,
    passwordChangeValues
} from "../../utils/helperFunctions"

class SimpleAppSharingPreview extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const app = this.props.app;
    const webInfo = app.website.information;
    const senderUser = selectUserFromListById(this.props.users, app.sender_id);

    return (
        <div class='team_app_holder display-flex align_items_center'>
          <div style={{width:"75px", textAlign: 'center'}}>
            <input type="checkbox"
                    checked={app.selected}
                    onChange={this.props.checkAppFunc.bind(null, app.id)}/>
          </div>
          <div class="full_flex">
            <div class="team_app_sender_info">
            <span class="team_app_sender_name">
              <i class="fa fa-user mrgnRight5"/>
              {senderUser.username}
            </span>
              <span>&nbsp;shared on&nbsp;{app.shared_date}</span>
            </div>
            <div class="team_app">
              <div class="name_holder">
                {app.name}
              </div>
              <div class="info_holder">
                <div class="info">
                  <div class="logo_holder">
                    <img src={app.website.logo} alt="logo"/>
                  </div>
                  <div class="credentials_holder">
                    <div class="credentials">
                      {
                        Object.keys(app.account_information).reverse().map(function(item){
                          return (
                              <div class="credentials_line" key={item}>
                                <div class="credentials_type_icon">
                                  <i class={classnames('fa', webInfo[item].placeholderIcon)}/>
                                </div>
                                <div class="credentials_value_holder">
                                    <span class="credentials_value">
                                      {app.account_information[item]}
                                  </span>
                                </div>
                              </div>
                          )
                        }, this)
                      }
                    </div>
                    <div class="password_change_remind">
                      <div class="password_change_icon"><i class="fa fa-clock-o"/></div>
                      <div class="password_change_info">
                        {passwordChangeValues[app.password_change_interval]}
                      </div>
                    </div>
                  </div>
                </div>
                <div class="sharing_info full_flex">
                  <div class="receivers_wrapper full_flex">
                    {
                      app.receivers.map(function (item) {
                        const user = selectUserFromListById(this.props.users, item.team_user_id);
                        return (
                            <div class={classnames("receiver", item.accepted ? "accepted": null)} key={item.team_user_id}>
                              <span class="receiver_name">
                              {user.username}
                              </span>
                              <i class={classnames("fa", "mrgnLeft5", item.can_see_information ? "fa-eye": "fa-eye-slash")}/>
                            </div>
                        )
                      }, this)
                    }
                  </div>
                </div>
              </div>
              <div class="comment_holder">
                <div class="comment_icon">
                  <i class="fa fa-sticky-note-o"/>
                </div>
                <div class="comment">
                    <span class="comment_value value">
                  {app.description.length > 0 ?
                      app.description :
                      "There is no comment for this app yet..."
                  }
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = SimpleAppSharingPreview;