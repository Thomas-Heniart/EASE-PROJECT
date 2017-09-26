import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import classnames from "classnames";
import api from "../../utils/api";
import {passwordChangeOptions, passwordChangeValues, copyTextToClipboard} from "../../utils/utils";
import React, {Component} from "react";

export const setUserDropdownText = (user) => {
  return (user.username + (user.first_name.length > 0 || user.last_name > 0 ? ` - ${user.first_name} ${user.last_name}` : ''));
};

export const renderSimpleAppUserLabel = (label, index, props) => {
  return (
      <Label color="blue" class="user-label">
        {label.username}
        <Icon link name={label.can_see_information ? 'unhide' : 'hide'} onClick={label.toggleCanSeeInformation}/>
        <Icon name="delete" onClick={e => {props.onRemove(e, label)}}/>
      </Label>
  )
};

export const PasswordChangeDropdown = ({value, onChange, disabled}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <Dropdown class="mini icon"
                         disabled={disabled}
                         value={value}
                         onChange={onChange}
                         button
                         name="password_change_interval"
                         icon="refresh"
                         labeled
                         options={passwordChangeOptions}/>
             }
             content='Password update reminder'/>
  )
};

export const ExtendFillSwitch = ({value, onClick}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <div class="enterprise-extend-switch">
                 I fill user’s credentials myslef
                 <Checkbox toggle class="enterprise-app-switch" name="fill_in_switch" checked={value} onClick={onClick}/>
               </div>
             }
             content='You can fill logins and passwords for each of your users (enabled only on Pro plan), or let them do it.'/>
  )
};

export const SharingRequestButton = ({onClick}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             flowing
             trigger={
               <Icon name="user add" class="team_app_indicator" style={{color: '#e84855'}} link onClick={onClick}/>
             }
             content='User(s) would like to acces this App'/>
  )
};

export const PinAppButton = ({is_pinned, onClick}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <Icon name="pin" class={classnames('pin_button team_app_indicator', is_pinned ? 'active' : null)} onClick={onClick}/>
             }
             content='Pin to your Personnal Space'/>
  )
};

export const TeamAppActionButton = ({onClick, icon, text, disabled}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <button class="button-unstyle"
                       class={classnames('button-unstyle', disabled ? 'disabled': null)}
                       type="button"
                       onClick={onClick}>
                 <Icon name={icon}/>
               </button>
             }
             content={text}/>
  )
};

export const PasswordChangeHolder = ({value}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <Button as='div' icon="refresh" size="mini" labelPosition='left' content={passwordChangeValues[value]}/>
             }
             content='Password update reminder'/>
  )
};

export const PasswordChangeManagerLabel = ({username})=> {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <Label class="pwd-manager">{username}&nbsp;&nbsp;<Icon name="eye"/></Label>
             }
             content={`${username} can see the password and will update it`}/>
  )
};

export class CopyPasswordButton extends Component {
  constructor(props){
    super(props);
    this.state = {
      state: 0,
      open: false,
      pwd: ''
    }
  }
  copyPassword = () => {
    copyTextToClipboard(this.state.pwd);
    this.setState({state: 3, open: true});
    setTimeout(() => {
      this.setState({state: 0, open: false});
    }, 2000);
  };
  fetchPassword = () => {
    this.setState({state: 1, open: true});
    api.teamApps.getSharedAppPassword({team_id: this.props.team_id, shared_app_id: this.props.shared_app_id}).then(pwd => {
      this.setState({pwd: pwd, state: 2, open: true});
    }).catch(err => {
      this.setState({state: 4, open: true});
      setTimeout(() => {
        this.setState({state: 0, open: false});
      }, 2000);
    });
  };
  render(){
    const content = <div>
      {this.state.state === 0 &&
      'Copy password'}
      {this.state.state === 1 &&
      <Icon name="asterisk" loading/>}
      {this.state.state === 2 &&
      <Button size="mini" positive onClick={this.copyPassword} content={'Click to copy'}/>}
      {this.state.state === 3 &&
      'Copied!'}
      {this.state.state === 4 &&
      'Error'}
    </div>;
    return (
        <Popup size="mini"
               position="top center"
               open={this.state.state > 0 ? true : undefined}
               inverted
               hoverable
               trigger={
                 <Icon name="copy" class="copy_pwd_button" link onClick={this.fetchPassword}/>
               }
               content={content}/>
    )
  }
};
