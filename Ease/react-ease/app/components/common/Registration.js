import React from "react";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import {passwordRegexp, emailRegexp, jobRoles, checkTeamUsernameErrors} from "../../utils/utils";
import queryString from "query-string";
import {processLogout, setLoginRedirectUrl} from "../../actions/commonActions";
import {connect} from "react-redux";
import SingleEaseLogo from "../common/SingleEaseLogo";
import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
var api = require('../../utils/api');
var post_api = require('../../utils/post_api');

class Step1 extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false
    }
  }
  onSubmit = (e) => {
    e.preventDefault();
  };
  render(){
    return (
        <div class="contents">
          <Segment>
            <Header as="h1">
              What's your name
              <Header.Subheader>
                Your name will be displayed for your team members in Ease.space
              </Header.Subheader>
            </Header>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Input
                  label="Username"
                  required
                  type="text"
                  name="username"
                  value={this.props.username}
                  onChange={this.props.handleInput}/>
              <Form.Field required>
                <label>Email</label>
              <Input
                  required
                  type="email"
                  name="email"
                  value={this.props.email}
                  onChange={this.props.handleInput}/>
                <Label pointing>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. Maximum 22 characters.</Label>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Form.Field>
                <Button positive fluid loading={this.state.loading} type="submit">Next</Button>
              </Form.Field>
            </Form>
          </Segment>
        </div>
    )
  }
}

class Registration extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      username: '',
      email: '',
      digits: '',
      password: '',
      step: 0,
      currentStep: 0
    }
  }
  handleInput = (e, {name, value}) => {
    this.setState({[name]: value});
  };
  incrementStep = () => {
    this.setState({step: this.state.step + 1});
  };
  finalizeRegistration = () => {
    //do stuff
  };
  render(){
    var steps = [];
    steps.push(<Step1
        incrementStep={this.incrementStep}
        handleInput={this.handleInput}
        username={this.state.username}
        email={this.state.email}
    />);
    return (
        <div id="team_join_view" class="full_screen_centered_view">
          <SingleEaseLogo/>
          <ReactCSSTransitionGroup
              component="div"
              className="carousel"
              transitionName="slideLeft"
              transitionAppear={true}
              transitionAppearTimeout={200}
              transitionEnterTimeout={200}
              transitionLeaveTimeout={200}>
            {
              steps.map(function (item,idx) {
                if (idx === this.state.currentStep)
                  return(item);
                return null;
              }, this)
            }
          </ReactCSSTransitionGroup>
        </div>
    )
  }
}