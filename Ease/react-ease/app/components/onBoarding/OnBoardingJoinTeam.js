import React from 'react';
import {connect} from "react-redux";
import queryString from "query-string";
import { Menu, Form, Icon, Button, Checkbox, Input, Header, Message } from 'semantic-ui-react';
import {handleSemanticInput, isEmail, reflect} from "../../utils/utils";
import {withRouter, Switch, Route, NavLink} from "react-router-dom";
import {
  askRegistration, changeStep, checkAskRegistration, createTeam, createTeamProfile, editFirstNameAndLastName,
  fetchOnBoardingRooms,
  getInfoClearbit, joinTeamRegistration,
  newRegistration, onBoardingImportation, resetOnBoardingImportation
} from "../../actions/onBoardingActions";
import {addTeamUserToChannel, createTeamChannel} from "../../actions/channelActions";
import {teamCreateEnterpriseCard, teamCreateSingleApp} from "../../actions/appsActions";
import {processConnection, processLogout, setLoginRedirectUrl} from "../../actions/commonActions";
import {testCredentials} from "../../actions/catalogActions";
import {createTeamUser} from "../../actions/userActions";
import * as api from '../../utils/api';
import * as post_api from '../../utils/post_api';
import Password from "../settings/Password";

class PasswordAndPhone extends React.Component {
  render() {
    const {
      onChange,
      onChangePhone,
      onChangePassword,
      phoneError,
      phone,
      check,
      password,
      verificationPassword,
      passwordError
    } = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Phone number and password</Header>
        <label className='for_input'>Mobile phone</label>
        <Input
          required
          autoFocus
          id='phone'
          name='phone'
          value={phone}
          placeholder='+33'
          icon='check circle'
          onChange={onChangePhone}
          className={!phoneError ? 'password_verified' : null}/>
        <label className='for_input'>Password</label>
        <Input
          required
          id='password'
          type='password'
          name='password'
          value={password}
          icon='check circle'
          onChange={onChangePassword}
          className={!passwordError ? 'password_verified' : null}/>
        <label className='for_input'>Confirm password</label>
        <Input
          required
          type='password'
          icon='check circle'
          onChange={onChange}
          id='verificationPassword'
          name='verificationPassword'
          value={verificationPassword}
          className={(password !== '' && password === verificationPassword) ? 'password_verified' : null}/>
        <div>
          <Checkbox
            name='checkCGU'
            checked={check}
            onChange={onChange}
            label='By ticking here you agree and understand our'/>
          <span>
            <a target='_blank' href='/resources/CGU_Ease.pdf'> General Terms</a> and
            <a target='_blank' href='/resources/Privacy_Policy.pdf'> Privacy Policy</a>.
          </span>
        </div>
      </React.Fragment>
    )
  }
}

class FirstAndLastName extends React.Component {
  render() {
    const {
      check,
      error,
      onChange,
      firstName,
      lastName
    } = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Account information</Header>
        <div style={{display:'inline-flex'}}>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Your Firstname</label>
            <Input
              required
              id='firstName'
              name='firstName'
              value={firstName}
              placeholder='Elon'
              onChange={onChange}/>
          </div>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Your Lastname</label>
            <Input
              required
              id='lastName'
              name='lastName'
              value={lastName}
              placeholder='Musk'
              onChange={onChange}/>
          </div>
        </div>
        <div>
          <Checkbox
            name='checkEmail'
            checked={check}
            onChange={onChange}
            label='It’s okay to send me very occasional emails about Ease.space.'/>
        </div>
        <Message error content={error}/>
      </React.Fragment>
    )
  }
}

@connect((store) => ({
  common: store.common
}))
class OnBoardingJoinTeam extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      phoneError: true,
      error: '',
      team_id: 0,
      activeItem: 1,
      view: 1,
      email: '',
      checkEmail: false,
      phone: '',
      password: '',
      verificationPassword: '',
      passwordError: true,
      firstName: '',
      lastName: '',
      checkCGU: false,
      account_exists: false,
      skipRegistration: false
    };
  }
  handleInput = handleSemanticInput.bind(this);
  handlePasswordInput = (e, {name, value}) => {
    if (/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(value))
      this.setState({[name]: value, passwordError: false});
    else
      this.setState({[name]: value, passwordError: true});
  };
  handleInputPhone = (e, {name, value}) => {
    if (/^(\+|[0-9])(?:[0-9] ?){5,13}[0-9]$/.test(value))
      this.setState({[name]: value, phoneError: false});
    else
      this.setState({[name]: value, phoneError: true});
  };
  login = () => {
    this.props.dispatch(setLoginRedirectUrl(this.props.match.url + '?skip'));
    this.props.history.replace('/login');
  };
  componentDidMount(){
    this.setState({loading: true});
    const query = queryString.parse(this.props.location.search);
    if (query.skip !== undefined) {
      this.state.skipRegistration = true;
    }
    if (this.props.common.authenticated && !this.state.skipRegistration)
      this.props.dispatch(processLogout()).then(() => {
        api.teams.getInvitationInformation({code: this.props.match.params.code}).then(response => {
          const info = response;
          const teamUser = info.teamUser;
          this.setState({
            code: this.props.match.params.code,
            access_code: this.props.match.params.access_code,
            firstName: teamUser.first_name,
            lastName: teamUser.last_name,
            username: teamUser.username,
            email: info.email,
            account_exists:info.account_exists,
            loading: false,
            team_id: info.team_id
          });
        }).catch(err => {
          window.location.href = '/';
        });
      });
    else {
      api.teams.getInvitationInformation({code: this.props.match.params.code}).then(response => {
        const info = response;
        const teamUser = info.teamUser;
        this.setState({
          code: this.props.match.params.code,
          access_code: this.props.match.params.access_code,
          firstName: teamUser.first_name,
          lastName: teamUser.last_name,
          username: teamUser.username,
          email: info.email,
          account_exists: info.account_exists,
          loading: false,
          team_id: info.team_id
        });
      }).catch(err => {
        window.location.href = '/';
      });
    }
  }
  checkPassword = () => {
    return (this.state.checkCGU === true && this.state.password !== '' && this.state.password === this.state.verificationPassword && !this.state.phoneError && /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(this.state.password));
  };
  submit = () => {
    this.setState({loading: true});
    if (this.state.view === 1 && this.state.account_exists && !this.props.common.authenticated) {
      post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.firstName, this.state.lastName, this.state.email.split('@')[0], this.state.code).then(response => {
        this.setState({loading: false});
        window.location.reload();
        window.location.href = '/';
      }).catch(err => {
        this.setState({loading: false})
      });
    }
    else if (this.state.view === 1 && this.state.account_exists && this.props.common.authenticated) {
      post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.firstName, this.state.lastName, this.state.email.split('@')[0], this.state.code).then(response => {
        this.setState({loading: false});
        window.location.reload();
        window.location.href = '/';
      }).catch(err => {
        this.setState({loading: false})
      });
    }
    else if (this.state.view === 1 && !this.state.account_exists) {
      this.setState({view: 2, loading: false});
    }
    else if (this.state.view === 2) {
      this.props.dispatch(joinTeamRegistration({
        email: this.state.email,
        password: this.state.password,
        code: this.state.code,
        phone_number: this.state.phone,
        newsletter: this.state.checkEmail,
        first_name: this.state.firstName,
        last_name: this.state.lastName,
        access_code: this.state.access_code
      })).then(r => {
        post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.firstName, this.state.lastName, this.state.email.split('@')[0], this.state.code).then(response => {
          this.setState({loading: false});
          window.location.reload();
          window.location.href = '/';
        }).catch(err => {
          this.setState({loading: false})
        });
      })
    }
  };
  render() {
    if ((!this.state.skipRegistration || !this.props.common.authenticated) && this.state.account_exists) {
     this.login();
    }
    const firstP = this.state.view === 1 ? <p>This information will be available only to your team.</p>
      : <p>Enter a <strong>strong password</strong> without names, dates or info related to you. <br/><br/>
        Your password must contain at least <stong>8  characters,an uppercase, a lowercase  and a number</stong>.</p>;
    return (
      <div id='new_team_creation'>
        <div id='left_bar'>
          {firstP}
          <img src='/resources/images/ease_logo_white.svg'/>
        </div>
        <div id='center'>
          <Menu id='top_bar' pointing secondary fluid>
            <Menu.Item name='Information' active={this.state.activeItem === 1}/>
            <Menu.Item name='Password' active={this.state.activeItem === 2}/>
          </Menu>
          <div id='content' className='stepUsers'>
            <Form
              onSubmit={this.state.view === 1 ? this.nextInformation : this.state.view === 5 ? this.nextAccounts : this.next}
              error={this.state.error !== ''}>
                {this.state.view === 1 &&
                  <FirstAndLastName
                    check={this.state.checkEmail}
                    error={this.state.error}
                    onChange={this.handleInput}
                    firstName={this.state.firstName}
                    lastName={this.state.lastName}/>}
                {this.state.view === 2 &&
                  <PasswordAndPhone
                    onChange={this.handleInput}
                    onChangePhone={this.handleInputPhone}
                    onChangePassword={this.handlePasswordInput}
                    phone={this.state.phone}
                    check={this.state.checkCGU}
                    phoneError={this.state.phoneError}
                    passwordError={this.state.passwordError}
                    password={this.state.password}
                    verificationPassword={this.state.verificationPassword}/>}
            </Form>
          </div>
          <div id='bottom_bar'>
            <Button positive
                    size='tiny'
                    type='submit'
                    loading={this.state.loading}
                    onClick={this.submit}
                    disabled={(this.state.loading)
                              || (this.state.view === 1 && (this.state.firstName === '' || this.state.lastName === ''))
                              || (this.state.view === 2 && !this.checkPassword())}>
              Next
              <Icon name='arrow right'/>
            </Button>
          </div>
        </div>
      </div>
    )
  }
}

export default OnBoardingJoinTeam;