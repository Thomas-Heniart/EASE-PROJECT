import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
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

export const PasswordChangeHolder = ({value}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <Button icon="refresh" size="mini" labelPosition='left' content={passwordChangeValues[value]}/>
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
