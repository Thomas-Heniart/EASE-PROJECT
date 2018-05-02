import React, {Fragment, Component} from "react";
import {Message, Form, Button} from 'semantic-ui-react';
import {connect} from "react-redux";
import {showAppPasswordChangeAskModal} from "../../actions/modalActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";

@connect(store => ({
  app: store.modals.appPasswordChangeAsk.app,
  reason: store.modals.appPasswordChangeAsk.reason,
  team_apps: store.team_apps,
  teams: store.teams
}))
class AppPasswordChangeAskModal extends Component {
  constructor(props){
    super(props);
  }
  close = () => {
    this.props.dispatch(showAppPasswordChangeAskModal({
      active: false
    }));
  };
  getContentText = () => {
    const {app, team_apps, reason, teams} = this.props;
    const teamCard = team_apps[app.team_card_id];
    const team = teams[teamCard.team_id];
    const sender = team.team_users[teamCard.team_user_sender_id];
    const passwordChangeInterval = teamCard.password_reminder_interval;

    if (reason === 'passwordChangeInterval'){
      return `As requested by ${!!sender ? sender.username : 'your administrator'}, the password of ${app.name} needs to be updated each ${passwordChangeInterval} months. This time to go update your password!`;
    } else if (reason === 'weakPassword'){
      return `The password of ${app.name} needs to be changed because it is not strong enough or have been found in a data breach. Modify it asap please.`;
    }
    return null;
  };
  render(){
    const contentText = this.getContentText();

    return(
        <SimpleModalTemplate
            headerContent={'Your password needs to change!'}
            onClose={this.close}>
          <Form class="container">
            <Form.Field>
              {contentText}
            </Form.Field>
            <Form.Field>
              <strong>
                Once it is changed, make sure to modify the app in Ease.space to save the new password.
              </strong>
            </Form.Field>
            <Button
                type="button"
                positive
                className="modal-button"
                onClick={this.close}
                content="OK, I'M GOING TO CHANGE IT"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export  default AppPasswordChangeAskModal;