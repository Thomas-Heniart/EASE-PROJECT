import React, {Component} from "react";
import {Icon, Input, Form, Menu, Message, Button, Checkbox} from 'semantic-ui-react';
import {handleSemanticInput} from "../../utils/utils";

export const LabeledInput = ({icon, value, onChange, readOnly, disabled, type, name, placeholder, required}) => {
  return (
      <Input fluid
             className="modalInput team-app-input"
             size='large'
             readOnly={readOnly}
             disabled={disabled}
             type={type ? type : 'text'}
             name={name}
             placeholder={placeholder}
             onChange={onChange}
             required={required}
             label={{ icon: icon }}
             labelPosition='left'
             value={value} />
  )
};

export const AppSettingsMenu = ({view, onChange}) => {
  return (
      <Menu tabular>
        <Menu.Item name="Account" active={view === 'Account'} onClick={onChange.bind(null, null, {name: 'view', value:'Account'})}/>
        <Menu.Item name="Remove" active={view === 'Remove'} onClick={onChange.bind(null, null, {name: 'view', value:'Remove'})}/>
        <Menu.Item name="Share" active={view === 'Share'} onClick={onChange.bind(null, null, {name: 'view', value:'Share'})}/>
      </Menu>
  )
};

export const ShareSection = (props) => {
  return (
      <Form>
        <Form.Field>
          This feature is coming soon!
        </Form.Field>
        <Form.Field>
          However you can already share passwords with your team, thanks to another way. If you don’t have a team yet click on <Icon name="users" fitted/> in the top right bar of your screen ☝.
        </Form.Field>
        <Button
            type="submit"
            disabled
            negative
            className="modal-button"
            content="CONFIRM"/>
      </Form>
  )
};

export class RemoveSection extends Component {
  constructor(props){
    super(props);
    this.state = {
      checked: false,
      errorMessage: '',
      loading: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  submit = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage:''});
    this.props.onRemove().then(response => {
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    return (
        <Form onSubmit={this.submit} error={this.state.errorMessage.length > 0}>
          <Form.Field>
            Are you sure about removing this app from your Personal Space ?
          </Form.Field>
          <Form.Field>
            The corresponding data will be lost permanently.
          </Form.Field>
          <Form.Field>
            <Checkbox label="Yes, I want to delete this app." name="checked" checked={this.state.checked} onClick={this.handleInput}/>
          </Form.Field>
          <Message error content={this.state.errorMessage}/>
          <Button
              type="submit"
              loading={this.state.loading}
              disabled={!this.state.checked || this.state.loading}
              negative
              className="modal-button"
              content="CONFIRM"/>
        </Form>
    )
  }
}
