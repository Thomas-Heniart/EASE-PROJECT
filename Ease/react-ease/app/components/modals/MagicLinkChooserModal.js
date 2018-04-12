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
            headerContent="Important question">
          <Form
              class="container"
              onSubmit={this.process}
              error={!!this.state.errorMessage.length}>
            <Form.Field>
              <strong>Here is the situation</strong>
              <span>
                One main feature on Ease.space is the automatique connection (when click on an app from the dashboard, we log you into the website automatically). The thing is, there are regular bugs, it doesn’t work on all websites, and each time a websites changes, the automatic connection doesn’t work anymore and we have to fix it.
              </span>
            </Form.Field>
            <Form.Field>
              <strong>Will you keep using Ease.space if:</strong>
              <span>
                - we stop connecting you automatically<br/>
                - we keep the dashboard as a homepage that helps you go to your websites<br/>
                - when you are logged out on a website, you can find your account password already filled in.
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
            <Form.Field style={{color: '#949eb7'}}>
              You won’t be able to change your mind.
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