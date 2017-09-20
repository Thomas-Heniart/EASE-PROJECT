import React from "react";
import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

class CGUStep extends React.Component{
  constructor(props){
    super(props);
  }
  render() {
    return (
        <div class="contents">
          <Segment>
            <Header as="h1">
              Review the General Terms
            </Header>
            <Divider hidden clearing/>
            <Container style={{maxHeight: '300px', overflow:'hidden', marginBottom: '1rem', paddingLeft: '0'}}>
              Before continuing your registration, please read our General Terms and Privacy Policy.
            </Container>
            <p>
              By clicking « I Agree », you understand and agree to our <a href="/resources/CGU_Ease.pdf" target="_blank">General Terms</a> and <a href="/resources/Privacy_Policy.pdf" target="_blank">Privacy Policy</a>.
            </p>
            <Button positive fluid loading={this.props.loading} onClick={this.props.onStepValidated}>I Agree</Button>
          </Segment>
        </div>
    )
  }
}

module.exports = CGUStep;