import React, {Fragment, Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Message, Form, Button, Radio} from 'semantic-ui-react';
import {magicLinkResponse} from "../../actions/commonActions";
import {showMagicLinkChooserModal} from "../../actions/modalActions";
import {connect} from "react-redux";

@connect()
class MagicLinkChooserModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      response: -1,
      loading: false,
      errorMessage: ''
    }
  }
  handleInput = (e, {value}) => {
    this.setState({response: value});
  };
  process = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(magicLinkResponse({
      agree: !!this.state.response
    })).then(response => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showMagicLinkChooserModal({
      active: false
    }));
  };
  render(){
    return (
        <SimpleModalTemplate
            id="ask_magic_link_modal"
            headerContent={
              <span>Important question <i class="em-svg em-warning"/><i class="em-svg em-blue_heart"/></span>
            }>
          <Form
              class="container"
              onSubmit={this.process}
              error={!!this.state.errorMessage.length}>
            <Form.Field>
              <strong>Here is the situation</strong>
              <span>
                Our automatic connection (a click on an app from the dashboard logs you automatically on the website) has regular bugs. It doesn’t work on all websites and each time a website changes, it doesn’t work anymore.
              </span>
            </Form.Field>
            <Form.Field>
              <strong>Will you keep using Ease.space if:</strong>
              <span>
                - we stop connecting you automatically<br/>
                - the apps on the dashboard open the websites<br/>
                - if you are logged out of a website, you will find the password already fill in by our extension.
              </span>
            </Form.Field>
            <Form.Field class="radio_chose_field">
              <Radio label="Yes"
                     value={0}
                     onClick={this.handleInput}
                     checked={this.state.response === 0}/>
              <Radio label="Non"
                     value={1}
                     onClick={this.handleInput}
                     checked={this.state.response === 1}/>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                disabled={this.state.response === -1}
                class="modal-button"
                content="SEND"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default MagicLinkChooserModal;