import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import post_api from "../../utils/post_api";
import {Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import {showLockedTeamAppModal} from "../../actions/modalActions";

@connect(store => ({
  team_user_id: store.modals.lockedTeamApp.team_user_id
}))
class LockedTeamAppModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      notifyButtonText: 'Notify again'
    }
  }
  sendNotification = () => {
    post_api.teamUser.sendActivationReminderToAdmin({
      team_user_id: this.props.team_user_id
    }).then(response => {
      this.setState({notifyButtonText: 'Notification sent!'});
      setTimeout(() => {
        this.setState({notifyButtonText: 'Notify again'});
      }, 2000);
    }).catch(err => {
      this.setState({notifyButtonText: 'Error :('});
      setTimeout(() => {
        this.setState({notifyButtonText: 'Notify again'});
      }, 2000);
    });
  };
  close = () =>  {
    this.props.dispatch(showLockedTeamAppModal({active: false}));
  };
  render(){
    return (
        <SimpleModalTemplate
            headerContent={'Apps from your team'}
            onClose={this.close}>
          <Form as="div" class="container">
            <Form.Field>
              We need your admin approval first in order for you to use this app again. He or she has already been notified.<br/>
              <button onClick={this.sendNotification} class="button-unstyle inline-text-button">
                {this.state.notifyButtonText}
              </button>
            </Form.Field>
            <Button
                positive
                onClick={this.close}
                className="modal-button"
                content="OK, I UNDERSTAND"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default LockedTeamAppModal;