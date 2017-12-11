import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import {Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import {showPasswordLostInformationModal} from "../../actions/modalActions";
import {handleSemanticInput} from "../../utils/utils";

@connect()
class PasswordLostInformationModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      checked: false
    }
  }
  close = () => {
    this.props.dispatch(showPasswordLostInformationModal({active: false}));
  };
  handleInput = handleSemanticInput.bind(this);
  render(){
    return (
        <SimpleModalTemplate
            headerContent={'Welcome back!'}>
          <Form as="div" class="container">
            <Form.Field>
              As your Ease.space password was the only way to unlock your personal passwords, you will have to add them again to your accounts.
            </Form.Field>
            <Form.Field>
              If you are part of a team, we need to double check with your admin to give your accesses again. He or she has been notified automatically already. You can contact him/her directly to speed up the process.
            </Form.Field>
            <Form.Field>
              <Checkbox label="Alright, I understand" name="checked" checked={this.state.checked} onClick={this.handleInput}/>
            </Form.Field>
            <Button
                positive
                disabled={!this.state.checked}
                onClick={this.close}
                className="modal-button"
                content="OK, GOT IT!"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default PasswordLostInformationModal;