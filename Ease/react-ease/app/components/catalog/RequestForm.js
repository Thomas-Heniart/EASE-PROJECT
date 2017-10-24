import React, {Component} from 'react';
import { Input, Button, Message, Header, Icon, Segment, Checkbox, Form, Grid } from 'semantic-ui-react';
import {handleSemanticInput} from "../../utils/utils";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

class SimpleRequestBox extends Component {
  constructor(props){
    super(props);
    this.state = {
      url: '',
      loading: false,
      errorMessage: '',
      buttonText: 'Send'
    }
  }
  handleInput = handleSemanticInput.bind(this);
  reset = () => {
    this.setState({url: '', loading: false, errorMessage: ''});
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.requestWebsite({
      url: this.state.url
    }).then(response => {
      this.reset();
      this.setState({buttonText: 'Suggestion successfully sent!'});
      setTimeout(() => {this.setState({buttonText: 'Send'})}, 3000);
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    return (
        <Segment clearing className="requestAnApp">
          <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Header as="h5" textAlign={'center'}>
              👋 Send suggestion
            </Header>
            <span>Paste website URL</span>
            <Input
                required
                value={this.state.url}
                onChange={this.handleInput}
                type='url'
                name="url"
                fluid
                placeholder='https://'
                size="mini" />
            <Message error size="mini" content={this.state.errorMessage}/>
            <Form.Field>
              <Button positive
                      size="mini"
                      floated="right"
                      loading={this.state.loading}
                      icon="send"
                      content={this.state.buttonText}
                      disabled={this.state.loading}/>
            </Form.Field>
          </Form>
        </Segment>
    )
  }
}

class ExtendedRequestBox extends Component {
  constructor(props){
    super(props);
    this.state = {
      url: '',
      login: '',
      password: '',
      loading:false,
      confirm: false,
      errorMessage: '',
      buttonText: 'Send'
    }
  }
  handleInput = handleSemanticInput.bind(this);
  reset = () => {
    this.setState({
      url: '',
      loading: false,
      confirm: false,
      login: '',
      password: '',
      errorMessage: ''});
  };
  confirm = (e) => {
    e.preventDefault();
    const account_information = {
      login: this.state.login,
      password: this.state.password
    };
    this.setState({loading: true, errorMessage: ''});
    this.props.requestWebsite({
      url: this.state.url,
      account_information: account_information
    }).then(response => {
      this.reset();
      this.setState({buttonText: 'Suggestion successfully sent!'});
      setTimeout(() => {this.setState({buttonText: 'Send'})}, 3000);
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    return (
        <Segment clearing className="requestAnApp">
          <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Header as="h5" textAlign={'center'}>
              🙏 Request an App
              <Header.Subheader>
                Integration realized within 72 hours
              </Header.Subheader>
            </Header>
            <span>Paste website URL</span>
            <Input
                required
                type='url'
                name="url"
                value={this.state.url}
                onChange={this.handleInput}
                fluid
                placeholder='https://'
                size="mini" />
            <span>Login</span>
            <Input
                required
                fluid
                type="text"
                value={this.state.login}
                name="login"
                onChange={this.handleInput}
                placeholder='Login'
                size="mini" />
            <span>Password</span>
            <Input
                required
                fluid
                name="password"
                value={this.state.password}
                onChange={this.handleInput}
                placeholder='Password'
                type='password'
                size="mini" />
            <span className="labelCheck">
                    <Form.Checkbox
                        required
                        name="confirm"
                        checked={this.state.confirm}
                        onClick={this.handleInput}/>
                    In order to add this website to my apps. I authorize Ease.space to use my credentials for a temporarily period of time of 72 hours maximum. More info
                </span>
            <Form.Field>
              <Message error size="mini" content={this.state.errorMessage}/>
            </Form.Field>
            <Button positive
                    size="mini"
                    floated="right"
                    loading={this.state.loading}
                    icon='send'
                    content={this.state.buttonText}
                    disabled={this.state.loading || !this.state.confirm}/>
          </Form>
        </Segment>
    )
  }
}

@connect(store => ({

}), reduxActionBinder)
class RequestForm extends Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
        <Grid columns={2}>
          <Grid.Column>
            <SimpleRequestBox requestWebsite={this.props.catalogRequestWebsite}/>
          </Grid.Column>
          <Grid.Column>
            <ExtendedRequestBox requestWebsite={this.props.catalogRequestWebsite}/>
          </Grid.Column>
        </Grid>
    )
  }
}

export default RequestForm;