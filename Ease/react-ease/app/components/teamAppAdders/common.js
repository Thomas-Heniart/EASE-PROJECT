import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import classnames from "classnames";
import {passwordChangeOptions, passwordChangeValues} from "../../utils/utils";
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
             content={`${username} updates the password`}/>
  )
};
