import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import {Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import {showLockedTeamAppModal} from "../../actions/modalActions";

@connect()
class LockedTeamAppModal extends Component {
  constructor(props){
    super(props);
  }
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
              We need your admin approval first in order for you to use this app again. He or she has already been notified.
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