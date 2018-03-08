import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import classnames from "classnames";
import api from "../../utils/api";
import {showTeamEditEnterpriseAppModal, showFillSimpleCardCredentialsModal, showUpgradeTeamPlanModal, showSimpleAppFillerChooserModal} from "../../actions/teamModalActions";
import {passwordChangeOptions, passwordChangeValues, copyTextToClipboard} from "../../utils/utils";
import React, {Component} from "react";
import post_api from "../../utils/post_api";
import {isAdmin} from "../../utils/helperFunctions";
import {connect} from "react-redux";
import {renewMagicLink} from "../../actions/magicLinkActions";

export class EmptyCredentialsSimpleAppIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      reminderSent: false
    }
  }
  sendReminder = () => {
    const {team_card} = this.props;
    if (this.state.reminderSent)
      return;
    this.setState({reminderSent: true});
    post_api.teamApps.sendSingleCardFillerReminder({
      team_card_id: team_card.id
    }).then(response => {
      setTimeout(() => {
        this.setState({reminderSent: false});
      }, 2000);
    }).catch(err => {
      setTimeout(() => {
        this.setState({reminderSent: false});
      }, 2000);
    });
  };
  renewLink = () => {
    this.props.dispatch(renewMagicLink({
      team_id: this.props.team_card.team_id,
      team_card_id: this.props.team_card.id
    })).then(response => {
      // ouvrir popup
    })
  };
  chooseMember = () => {
    this.props.dispatch(showSimpleAppFillerChooserModal({
      active: true,
      team_card: this.props.team_card
    }));
  };
  fillCredentials = () => {
    this.props.dispatch(showFillSimpleCardCredentialsModal({
      active: true,
      team_card: this.props.team_card
    }));
  };
  render(){
    const {team_card, team_users, meReceiver, me} = this.props;

    return (
        <Button
            as='div'
            icon
            className={team_card.magic_link !== '' && team_card.magic_link_expiration_date < new Date().getTime() ? "empty_app_indicator link_expired" : "empty_app_indicator"}
            size="mini"
            labelPosition='left'>
          <Icon name="user"/>
          {(team_card.team_user_filler_id === -1 && team_card.magic_link === '') &&
          <u onClick={this.chooseMember}>
            Choose a user to fill connection info.
          </u>}
          {(team_card.team_user_filler_id === -1 && team_card.magic_link_expiration_date > new Date().getTime()) &&
          <span>Waiting for login and password. <u onClick={this.fillCredentials}>Manage request link</u></span>}
          {(team_card.team_user_filler_id === -1 && team_card.magic_link_expiration_date < new Date().getTime()) &&
          <span>Link has expired. <u onClick={this.renewLink}>Get a new link</u></span>}
          {(team_card.team_user_filler_id !== -1 && team_card.team_user_filler_id === me.id) &&
              <span>Waiting for <strong>{me.username}</strong> to<u onClick={this.fillCredentials}>fill info</u></span>}
          {(team_card.team_user_filler_id !== -1 && team_card.team_user_filler_id !== me.id) &&
              <span>
                  Waiting for {team_users[team_card.team_user_filler_id].username} to fill info.
                {this.props.actions_enabled &&
                <React.Fragment>
                  {(!!meReceiver || isAdmin(me.role)) &&
                  <u onClick={this.sendReminder}>
                    {this.state.reminderSent ?
                        'Reminder sent!' :
                        'Send reminder'}
                  </u>}
                  {isAdmin(me.role) &&
                  <React.Fragment>
                    &nbsp;or
                    <u onClick={this.chooseMember}>
                      choose another person
                    </u>
                  </React.Fragment>}
                </React.Fragment>
                }
              </span>
          }
        </Button>
    )
  }
}

export class EmptyCredentialsEnterpriseAppIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      sent: false
    }
  }
  sendReminder = () => {
    const {receiver} = this.props;
    if (this.state.sent)
      return;
    this.setState({sent: true});
    post_api.teamApps.sendFillerEnterpriseCardReminder({
      team_id: receiver.team_id,
      team_card_id: receiver.team_card_id,
      team_card_receiver_id: receiver.id
    }).then(response => {
      setTimeout(() => {
        this.setState({sent: false});
      }, 2000);
    }).catch(err => {
      setTimeout(() => {
        this.setState({sent: false});
      }, 2000);
    });
  };
  fillCredentials = () => {
    this.props.dispatch(showTeamEditEnterpriseAppModal({
      active: true,
      team_card_id: this.props.receiver.team_card_id
    }));
  };
  render(){
    const {team_user, meAdmin, me, receiver} = this.props;
    return (
        <Button
            as='div'
            icon
            class="empty_app_indicator"
            size="mini"
            labelPosition='left'>
          <Icon name="user"/>
          {receiver.team_user_id === me.id ?
              <span>Waiting for <strong>{me.username}</strong> to<u onClick={this.fillCredentials}>fill info</u></span> :
              <span>
              Waiting for {team_user.username} to fill info.
                {meAdmin &&
                <u onClick={this.sendReminder}>
                  {this.state.sent ?
                      'Reminder sent!' :
                      'Send reminder'}
                </u>}
              </span>}
        </Button>
    )
  }
}

export const setUserDropdownText = (user) => {
  return (user.username + (user.first_name.length > 0 || user.last_name > 0 ? ` - ${user.first_name} ${user.last_name}` : ''));
};

export const renderSimpleAppUserLabel = (label, index, props) => {
  const {username, can_see_information, receiver} = label;
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             flowing
             hideOnScroll={true}
             trigger={
               <Label class={classnames("user-label static", (receiver !== null && receiver.accepted) ? 'accepted' : null, can_see_information ? 'can_see_information' : null)}>
                 {username}
                 <Icon link name={can_see_information ? 'unhide' : 'hide'} onClick={label.toggleCanSeeInformation}/>&nbsp;
                 {can_see_information &&
                 <Icon name='mobile'/>}
                 <Icon name="delete" onClick={e => {props.onRemove(e, label)}}/>
               </Label>
             }
             content={
               <div>
                 {(receiver === null || !receiver.accepted) && <span>App acceptation pending...</span>}
                 {receiver !== null && receiver.accepted && can_see_information &&
                 <span>Mobile access: on</span>}
                 {receiver !== null && receiver.accepted && !can_see_information &&
                 <span>Mobile access: off</span>}
                 <br/>
                 {receiver !== null && receiver.accepted && can_see_information &&
                 <span>Password copy: on</span>}
                 {receiver !== null && receiver.accepted && !can_see_information &&
                 <span>Password copy: off</span>}
               </div>}/>
  )
};

export const renderSimpleAppEditUserLabel = (label, index, props) => {
  const {username, can_see_information, receiver} = label;
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             hideOnScroll={true}
             trigger={
               <Label class={classnames("user-label static accepted", (receiver !== null && receiver.accepted) ? 'accepted' : null, can_see_information ? 'can_see_information' : null)}>
                 {username}
                 {/*<Icon link name={can_see_information ? 'unhide' : 'hide'} onClick={label.toggleCanSeeInformation}/>&nbsp;
                 {can_see_information &&
                 <Icon name='mobile'/>}*/}
                 <Icon
                     name="delete"
                     onClick={e => {props.onRemove(e, label)}}/>
               </Label>
             }
             content={
               <div>
                 This person can access the account on desktop and mobile.
                 {/*{can_see_information &&
                 <span>Mobile access: on</span>}
                 {!can_see_information &&
                 <span>Mobile access: off</span>}
                 <br/>
                 {can_see_information &&
                 <span>Password copy: on</span>}
                 {!can_see_information &&
                 <span>Password copy: off</span>}*/}
               </div>}/>
  )
};


export const renderSimpleAppAddUserLabel = (label, index, props) => {
  const {username, can_see_information} = label;
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             hideOnScroll={true}
             trigger={
               <Label class={classnames("user-label static", can_see_information ? 'can_see_information' : null)}>
                 {username}
                 {/*<Icon link name={can_see_information ? 'unhide' : 'hide'} onClick={label.toggleCanSeeInformation}/>&nbsp;*/}
                 {/*can_see_information &&
                 <Icon name='mobile'/>*/}
                 <Icon name="delete" onClick={e => {props.onRemove(e, label)}}/>
               </Label>
             }
             content={
               <div>
                 This person can access the account on desktop and mobile.
                 {/*{can_see_information &&
                 <span>Mobile access: on</span>}
                 {!can_see_information &&
                 <span>Mobile access: off</span>}
                 <br/>
                 {can_see_information &&
                 <span>Password copy: on</span>}
                 {!can_see_information &&
                 <span>Password copy: off</span>}*/}
               </div>}/>
  )
};

export const renderLinkAppAddUserLabel = (label, index, props) => {
  const {username, can_see_information} = label;
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             hideOnScroll={true}
             trigger={
               <Label class={"user-label static pinned can_see_information"}>
                 {username}
                 {/*{can_see_information &&
                 <Icon name='mobile'/>}*/}
                 <Icon name="delete" onClick={e => {props.onRemove(e, label)}}/>
               </Label>
             }
             content={
               <div>
                 This person can access the link on desktop and mobile.
               </div>}/>
  )
};

export const PasswordChangeDropdown = ({dispatch, team, value, onChange, disabled, roomManager}) => {
  return (
      <div class="display-inline-flex align_items_center">
        <Popup size="mini"
               position="top center"
               inverted
               trigger={
                 <Dropdown class="mini icon"
                           onFocus={team.plan_id === 0 ? () => {
                             dispatch(showUpgradeTeamPlanModal({
                               active: true,
                               feature_id:7,
                               team_id: team.id
                             }));
                           } : null}
                           disabled={disabled}
                           value={value}
                           onChange={onChange}
                           button
                           name="password_reminder_interval"
                           icon="refresh"
                           labeled
                           options={passwordChangeOptions}/>
               }
               content={value > 0 ? `The Room Manager (${roomManager}) will be in charge of updating the password` : `The room manager (${roomManager}) can be in charge of updating the password`}/>
        {team.plan_id === 0 &&
        <img src="/resources/images/upgrade.png" style={{height:'23px'}}/>}
      </div>
  )
};

export const PasswordChangeDropdownEnterprise = ({team, dispatch, value, onChange, disabled, roomManager}) => {
  return (
      <div class="display-inline-flex align_items_center">
        <Popup size="mini"
               position="top center"
               inverted
               trigger={
                 <Dropdown class="mini icon"
                           onFocus={team.plan_id === 0 ? () => {
                             dispatch(showUpgradeTeamPlanModal({
                               active: true,
                               feature_id:7,
                               team_id: team.id
                             }));
                           } : null}
                           disabled={disabled}
                           value={value}
                           onChange={onChange}
                           button
                           name="password_reminder_interval"
                           icon="refresh"
                           labeled
                           options={passwordChangeOptions}/>
               }
               content={value > 0 ? `Frequency at which members will update their password for this app.` : `You can choose at which frequency your members will update their password for this app.`}/>
        {team.plan_id === 0 &&
        <img src="/resources/images/upgrade.png" style={{height:'23px'}}/>}
      </div>
  )
};

export const ExtendFillSwitch = ({value, onClick}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <div class="enterprise-extend-switch">
                 I fill user’s credentials myself
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

export const PasswordChangeHolder = ({team, value, roomManager}) => {
  return (
      <div class="display-inline-flex align_items_center">
        <Popup size="mini"
               position="top center"
               inverted
               trigger={
                 <Button as='div' icon="refresh" size="mini" labelPosition='left' content={passwordChangeValues[value]}/>
               }
               content={value > 0 ? `The Room Manager (${roomManager}) will be in charge of updating the password` : `There isn’t password update policy setup.`}/>
      </div>
  )
};

export const PasswordChangeHolderEnterprise = ({team, value, roomManager}) => {
  return (
      <div class="display-inline-flex align_items_center">
        <Popup size="mini"
               position="top center"
               inverted
               trigger={
                 <Button as='div' icon="refresh" size="mini" labelPosition='left' content={passwordChangeValues[value]}/>
               }
               content={value > 0 ? `Frequency at which members will update their password for this app.` : `There isn’t password update policy setup.`}/>
      </div>
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
      <div><Icon name="asterisk" loading/> decrypting password locally</div>}
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

export class SingleAppCopyPasswordButton extends Component {
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
    api.teamApps.getSingleAppPassword({team_card_id: this.props.team_card_id}).then(pwd => {
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
      <div><Icon name="asterisk" loading/> decrypting password locally</div>}
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
