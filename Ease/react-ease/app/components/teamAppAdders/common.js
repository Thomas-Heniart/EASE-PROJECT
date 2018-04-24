import {
  Header,
  Popup,
  Grid,
  Label,
  List,
  Search,
  SearchResult,
  Container,
  Divider,
  Icon,
  Transition,
  TextArea,
  Segment,
  Checkbox,
  Form,
  Input,
  Select,
  Dropdown,
  Button,
  Message,
  Loader
} from 'semantic-ui-react';
import classnames from "classnames";
import api from "../../utils/api";
import {
  credentialIconType,
  basicDateFormat
} from "../../utils/utils";
import {
  showTeamEditEnterpriseAppModal, showFillSimpleCardCredentialsModal, showUpgradeTeamPlanModal,
  showSimpleAppFillerChooserModal, showManageMagicLinkModal
} from "../../actions/teamModalActions";
import {passwordChangeOptions, passwordChangeValues, copyTextToClipboard} from "../../utils/utils";
import React, {Component, Fragment} from "react";
import post_api from "../../utils/post_api";
import {isAdmin} from "../../utils/helperFunctions";
import extension from "../../utils/extension_api";
import {connect} from "react-redux";
import {renewMagicLink} from "../../actions/magicLinkActions";
import {passwordCopied} from "../../actions/dashboardActions";

export const enterpriseCardPasswordStrengthSumDescription = {
  1: 'Some passwords here have previously appeared in a data breach and thus shouldn’t be used.',
  2: 'Some passwords here are quite (or even really) weak and thus shouldn’t be used.',
  3: 'Some passwords here have previously appeared in a data breach or are too weak, and thus shouldn’t be used.',
  4: 'Some passwords here are quite (or even really) weak and thus shouldn’t be used.',
  5: 'Some passwords here have previously appeared in a data breach or are too weak, and thus shouldn’t be used.',
  6: 'Some passwords here have previously appeared in a data breach and thus shouldn’t be used.',
  7: 'Some passwords here have previously appeared in a data breach or are too weak, and thus shouldn’t be used.'
};

export class EnterpriseCardPasswordStrengthIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      alertSent: false
    }
  }
  sendAlert = () => {
    this.setState({alertSent: true});
    setTimeout(() => {
      this.setState({alertSent: false})
    }, 2000);
    this.props.passwordChangeAlert();
  };
  getText = () => {
    const {weaknessStatus, weakPasswordsCount} = this.props;

    if (weaknessStatus === 0)
      return (
          <span className="password_strength_indicator strong"><Icon name="lock" fitted/></span>
      );
    else if (weaknessStatus === 4)
      return (
          <span className="password_strength_indicator medium"><Icon name="lock" fitted/> {weakPasswordsCount} Weak password{weakPasswordsCount > 1 ? 's' : null}</span>
      );
    else
      return (
          <span className="password_strength_indicator"><Icon name="lock" fitted/> {weakPasswordsCount} Weak password{weakPasswordsCount > 1 ? 's' : null}</span>
      );
  };
  getPopupText = () => {
    const {weaknessStatus, lastPasswordChangeAlertDate} = this.props;

    if (weaknessStatus === 0)
      return (
          <span>The passwords for this app are strong <i class="em-svg em-closed_lock_with_key"/>. Keep them like this <i className="em-svg em-ok_hand"/></span>
      );
    else
      return (
          <Fragment>
            <span>{enterpriseCardPasswordStrengthSumDescription[weaknessStatus]}</span><br/>
            {this.state.alertSent ?
                <span>Request sent!</span>
                :
                <span><a className="simple_link"
                         onClick={this.sendAlert}>Require people to make their password stronger</a>&nbsp;💪<i
                    className="em-svg em-muscle"/></span>
            }
            {!!lastPasswordChangeAlertDate &&
            <Fragment>
              <br/>
              <span>
                 (Last request sent {basicDateFormat(lastPasswordChangeAlertDate)})
              </span>
            </Fragment>}
          </Fragment>
      )
  };
  render(){
    const text = this.getText();
    const popupText = this.getPopupText();
    return (
        <Popup
            inverted
            size="mini"
            hoverable
            hideOnScroll
            position='bottom center'
            trigger={text}
            content={popupText}
        />
    )
  }
};

export const scanEnterpriseCardForWeakPasswords = (app) => {
  let pwned = 0;
  let weak = 0;
  let reallyWeak = 0;
  let totalFilled = 0;
  let totalNotScored = 0;

  app.receivers.forEach(receiver => {
    if (!!receiver.empty)
      return;
    totalFilled++;
    if (receiver.password_score === null){
      totalNotScored++;
      return;
    }
    if (receiver.password_score === -1)
      pwned++;
    else if (receiver.password_score < 3)
      reallyWeak++;
    else if (receiver.password_score === 3)
      weak++;
  });
  return {
    notChecked: totalNotScored === totalFilled,
    weakPasswordsCount: pwned + weak + reallyWeak,
    weaknessStatus: !!pwned * 1 + !!reallyWeak * 2 + !!weak * 4
  }
};

export const PasswordStrengthLoading = () => {
  return (
      <Popup
          size='mini'
          position="bottom center"
          inverted
          hideOnScroll
          trigger={
            <Loader active
                    inline
                    size='mini'
                    class="password_strength_loader"/>
          }
          content={'Checking password strength...'}
      />
  )
};

export const passwordStrengthDescription = {
  '-1': "This password has previously appeared in a data breach and should never be used.",
  0: "This password is really weak and shouldn’t be used.",
  1: "This password is really weak and shouldn’t be used.",
  2: "This password is really weak and shouldn’t be used.",
  3: "This password is quite weak and shouldn’t be used."
};

export class EnterpriseTeamCardPasswordInputStrengthIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      alertSent: false
    }
  }
  goToUrl = () => {
    const {appName, login} = this.props;

    window.open(this.props.websiteUrl);
    extension.showPasswordUpdateAskHelperModal({
      appName: appName,
      login: login
    });
  };
  sendAlert = () => {
    this.setState({alertSent: true});
    setTimeout(() => {
      this.setState({alertSent: false});
    }, 2000);
    this.props.passwordChangeAlert();
  };
  render(){
    const {score, myPassword, lastPasswordChangeAlert, websiteUrl} = this.props;
    return (
        <Popup
            size="mini"
            position="bottom center"
            inverted
            hoverable
            trigger={
              <Icon name="warning sign"
                    fitted
                    color={score < 3 ? 'red' : 'orange'}
                    class="password_input_strength_indicator"/>
            }
            content={
              <Fragment>
                <span>{passwordStrengthDescription[score]}</span>
                <br/>
                {myPassword ?
                    <span>{!!websiteUrl ? <a class="simple_link" onClick={this.goToUrl}>Change it to a strong one</a> : 'Change it to a strong one'}&nbsp;💪<i
                        class="em-svg em-muscle"/></span>
                    :
                    this.state.alertSent ?
                        <span>Request sent!</span>
                        :
                        <span><a class="simple_link" onClick={this.sendAlert}>Require people to make their password stronger</a>&nbsp;💪<i
                            class="em-svg em-muscle"/></span>
                }
                {!myPassword && !!lastPasswordChangeAlert &&
                <Fragment>
                  <br/>
                  (Last request sent {basicDateFormat(lastPasswordChangeAlert)})
                </Fragment>}
              </Fragment>
            }
        />
    )
  }
};

export const StaticEnterpriseTeamCardPasswordInput = ({item, passwordScore, myPassword, lastPasswordChangeAlert, websiteUrl, passwordChangeAlert, appName, login}) => {
  return (
      <Input size="mini"
             class="team-app-input"
             readOnly
             name={item.name}
             labelPosition="left"
             placeholder={'(Password encrypted)'}
             value={item.value}
             type={'password'}>
        <Label><Icon name={credentialIconType[item.name]}/></Label>
        <input/>
        {!!passwordScore && passwordScore < 4 &&
        <EnterpriseTeamCardPasswordInputStrengthIndicator score={passwordScore}
                                                          lastPasswordChangeAlert={lastPasswordChangeAlert}
                                                          appName={appName}
                                                          login={login}
                                                          passwordChangeAlert={passwordChangeAlert}
                                                          websiteUrl={websiteUrl}
                                                          myPassword={myPassword}/>}
      </Input>
  )
};

export class TeamSimpleCardPasswordStrengthIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      alertSent: false
    }
  }
  sendAlert = () => {
    this.setState({alertSent: true});
    setTimeout(() => {
      this.setState({alertSent: false});
    }, 2000);
    this.props.passwordChangeAlert();
  };
  goToUrl = () => {
    const {teamCard} = this.props;

    window.open(teamCard.website.login_url);
    extension.showPasswordUpdateAskHelperModal({
      appName: teamCard.name,
      login: teamCard.account_information.login
    });
  };
  getText = () => {
    const {score} = this.props;

    if (score < 3)
      return (
          <span class="password_strength_indicator"><Icon name="lock" fitted/> Weak password</span>
      );
    else if (score < 4)
      return (
          <span class="password_strength_indicator medium"><Icon name="lock" fitted/> Weak password</span>
      );
    else if (score === 4)
      return (
          <span class="password_strength_indicator strong"><Icon name="lock" fitted/></span>
      );
  };
  getPopupContent = () => {
    const {score, meReceiver, teamCard} = this.props;
    const lastPasswordScoreAlertDate = teamCard.last_password_score_alert_date;

    if (score === 4)
      return (
          <span>The password for this app is strong <i class="em-svg em-closed_lock_with_key"/>. Keep it like this <i class="em-svg em-ok_hand"/></span>
      );
    else if (score < 4)
      return (
          <Fragment>
            <span>{passwordStrengthDescription[score]}</span>
            <br/>
            <span><a class="simple_link" onClick={this.goToUrl}>Change it to a strong one</a>&nbsp;💪<i class="em-svg em-muscle"/></span>
          </Fragment>
      );
  };
  getPopupContentSoftware = () => {
    const {score, meReceiver, teamCard} = this.props;
    const lastPasswordScoreAlertDate = teamCard.last_password_score_alert_date;

    if (score === 4)
      return (
          <span>The password for this app is strong <i class="em-svg em-closed_lock_with_key"/>. Keep it like this <i class="em-svg em-ok_hand"/></span>
      );
    else if (score < 4)
      return (
          <Fragment>
            <span>{passwordStrengthDescription[score]}</span>
            <br/>
            <span>Change it to a strong one 💪<i class="em-svg em-muscle"/></span>
          </Fragment>
      );
  };
  render(){
    const {teamCard} = this.props;
    const text = this.getText();
    const popupContent = teamCard.software ? this.getPopupContentSoftware() : this.getPopupContent();

    return (
        <Popup
            size="mini"
            inverted
            position="bottom center"
            hoverable
            trigger={text}
            content={popupContent}/>
    )
  }
}

@connect((store, ownProps) => ({
  team_card: store.team_apps[ownProps.team_card_id]
}))
class SimpleTeamCardPasswordInputStrengthIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      sent: false
    }
  }
  sendAlert = () => {
    this.setState({sent: true});
    setTimeout(() => {
      this.setState({sent: false});
    }, 2000);
    this.props.changePasswordAlert();
  };
  goToUrl = () => {
    const {team_card} = this.props;

    window.open(team_card.website.login_url);
  };
  render(){
    const {score, changePasswordAlert, canChangePassword, lastPasswordScoreAlertDate} = this.props;
    return (
        <Popup
            size="mini"
            position="bottom center"
            inverted
            hoverable
            trigger={
              <Icon name="warning sign"
                    fitted
                    color={score < 3 ? 'red' : 'orange'}
                    class="password_input_strength_indicator"/>
            }
            content={
              <Fragment>
                <span>{passwordStrengthDescription[score]}</span>
                <br/>
                {canChangePassword ?
                    <span><a class="simple_link" onClick={this.goToUrl}>Change it to a strong one</a>&nbsp;💪<i class="em-svg em-muscle"/></span>
                    :
                    this.state.sent ?
                        <span>Request sent!</span>
                        :
                        <span><a class="simple_link" onClick={this.sendAlert}>Require to change it to a strong one</a> <i class="em-svg em-muscle"/></span>
                }
                {!!lastPasswordScoreAlertDate && !canChangePassword &&
                <Fragment>
                  <br/>
                  (Last request sent {basicDateFormat(lastPasswordScoreAlertDate)})
                </Fragment>}
              </Fragment>
            }
        />
    )
  }
};

export const StaticSimpleTeamCardPasswordInput = ({item, passwordScore, canChangePassword, lastPasswordScoreAlertDate, changePasswordAlert, team_card_id}) => {
  return (
      <div class='credentials_single_card'>
        <Input
            size="mini"
            class="team-app-input"
            placeholder='(Password encrypted)'
            type="password"
            readOnly
            labelPosition="left">
          <Label><Icon name={credentialIconType[item.name]}/></Label>
          <input/>
          {!!passwordScore && passwordScore < 4 &&
          <SimpleTeamCardPasswordInputStrengthIndicator
              team_card_id={team_card_id}
              canChangePassword={canChangePassword}
              changePasswordAlert={changePasswordAlert}
              lastPasswordScoreAlertDate={lastPasswordScoreAlertDate}
              score={passwordScore}/>}
        </Input>
      </div>
  )
};

export class EmptyCredentialsSimpleAppIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      reminderSent: false,
      timestamp: null
    }
  }
  componentWillMount() {
    this.getTime();
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
      this.getTime();
    });
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
  getTime = () => {
    if (this.props.team_card.magic_link !== '' && this.props.team_card.magic_link_expiration_date > this.state.timestamp) {
      setTimeout(() => {
        this.getTime();
      }, 1000);
      this.setState({timestamp: new Date().getTime()});
    }
    else
      this.setState({timestamp: null});
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
          <u onClick={this.chooseMember}>Choose a user to fill connection info.</u>}
          {(team_card.team_user_filler_id === -1 && team_card.magic_link_expiration_date > new Date().getTime() && isAdmin(me.role)) &&
          <span>Waiting for login and password. <u onClick={() => this.props.dispatch(showManageMagicLinkModal({active: true, team_card: team_card}))}>Manage request link</u></span>}
          {(team_card.team_user_filler_id === -1 && team_card.magic_link_expiration_date < new Date().getTime() && isAdmin(me.role)) &&
          <span>Link has expired. <u onClick={this.renewLink}>Get a new link</u></span>}
          {(team_card.team_user_filler_id === -1 && !isAdmin(me.role)) &&
          <span>Waiting for login and password.</span>}
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

export const SharingRequestButton = ({onClick, requestNumber}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             flowing
             trigger={
               <Button size='mini' className="button-card-request" onClick={onClick}>{requestNumber} {requestNumber > 1 ? 'requests' : 'request'} pending</Button>
             }
             content='User(s) would like to access this App'/>
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
