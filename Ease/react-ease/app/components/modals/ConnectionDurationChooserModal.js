import React, {Component, Fragment} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Message, Form, Button, Radio} from 'semantic-ui-react';
import {setConnectionLifetime, connectionLifetimeModalSeen} from "../../actions/commonActions";
import {showConnectionDurationChooserModal} from "../../actions/modalActions";
import {connect} from "react-redux";

@connect()
class ConnectionDurationChooserModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      connectionDuration: -1
    }
  }
  handleInput = (e, {value}) => {
    this.setState({connectionDuration: value});
  };
  close = () => {
    this.props.dispatch(showConnectionDurationChooserModal({
      active: false
    }));
  };
  process = async (e) => {
    e.preventDefault();
    this.setState({loading: true});
    try {
      await this.props.dispatch(setConnectionLifetime({
        connection_lifetime: this.state.connectionDuration
      }));
      await this.props.dispatch(connectionLifetimeModalSeen());
      this.setState({loading: false});
      this.props.dispatch(showConnectionDurationChooserModal({
        active: false
      }));
    } catch (e) {
      this.setState({loading: false, errorMessage: e});
    }
  };
  render(){
    return (
        <SimpleModalTemplate
            headerContent='How often do you want to login?'>
          <Form class="container"
                onSubmit={this.process}
                error={!!this.state.errorMessage.length}>
            <Form.Field style={{lineHeight: 1.75, fontSize: '16px'}}>
              You can now choose if you want to login every day (better for security) or once a week. Make sure you don’t forget your password ;)
            </Form.Field>
            <Form.Field class="radio_chose_field">
              <Radio label="Login everyday"
                     value={1}
                     onClick={this.handleInput}
                     checked={this.state.connectionDuration === 1}/>
              <Radio label="Login once a week"
                     value={7}
                     onClick={this.handleInput}
                     checked={this.state.connectionDuration === 7}/>
            </Form.Field>
            <Form.Field style={{fontSize: '16px', color: "#949eb7"}}>
              If you change your mind, you’ll be able to change this in Settings.
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                disabled={this.state.connectionDuration === -1}
                class="modal-button"
                content="DONE!"
            />
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default ConnectionDurationChooserModal;
