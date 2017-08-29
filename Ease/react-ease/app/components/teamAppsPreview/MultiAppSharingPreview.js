var React = require('react');
var classnames = require('classnames');
import {
    selectUserFromListById,
    passwordChangeValues
} from "../../utils/helperFunctions"

class MultiAppSharingPreview extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const app = this.props.app;
    const senderUser = selectUserFromListById(this.props.users, app.sender_id);
    const webInfo = app.website.information;
    const credentials = app.credentials;
    const username = this.props.username;

    return(
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
            <span>&nbsp;shared on&nbsp;{app.shared_date}
            </span>
          </div>
          <div class="team_app multiple_accounts_app">
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
                    <div class="credentials_line">
                      <div class="credentials_type_icon">
                        <i class='fa fa-user'/>
                      </div>
                      <div class="credentials_value_holder">
                        <span class="credentials_value">
                           Please fill user information
                        </span>
                      </div>
                    </div>
                  </div>
                  <div class="password_change_remind">
                    <div class="password_change_icon"><i class="fa fa-clock-o"/></div>
                        <div class="password_change_info">
                          {passwordChangeValues[app.password_change_interval]}
                        </div>

                  </div>
                </div>
              </div>
              <div class="sharing_info display_flex full_flex flex_direction_column">
                <div class="receivers_wrapper full_flex">
                  <div class="receiver_wrapper">
                    <div class="receiver">
                      <span>
                        {username}
                      </span>
                    </div>
                    <div class="credentials">
                      {
                        Object.keys(credentials).reverse().map(function(item){
                          return (
                              <div class="credential_container" key={item}>
                                <i class={classnames("fa", "mrgnRight5", webInfo[item].placeholderIcon)}/>
                                <input class="value_input input_unstyle"
                                       placeholder={webInfo[item].placeholder}
                                       type={webInfo[item].type}
                                       name={item}
                                       value={credentials[item]}
                                        onChange={e => {this.props.handleCredentialsInput(app.id, e.target.name, e.target.value)}}/>
                              </div>
                          )
                        }, this)
                      }
                    </div>
                  </div>
                  {
                      app.receivers.map(function(item){
                        const user = selectUserFromListById(this.props.users, item.team_user_id);
                        return (
                            <div class="receiver_wrapper" key={item.team_user_id}>
                              <div class="receiver">
                              <span class="receiver_name">
                              {user.username}
                              </span>
                                <i class="fa fa-unlock-alt mrgnLeft5"/>
                              </div>
                              <div class="credentials">
                                {
                                  Object.keys(item.account_information).map(function(info){
                                    return (
                                        <div class="credential_container" key={info}>
                                          <i class={classnames('fa', 'mrgnRight5', webInfo[info].placeholderIcon)}/>
                                          <span class="value">
                                            {item.account_information[info]}
                                          </span>
                                        </div>
                                    )
                                  })
                                }
                              </div>
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

module.exports = MultiAppSharingPreview;