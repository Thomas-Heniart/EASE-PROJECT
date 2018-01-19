import React from 'react';
import {handleSemanticInput, isEmail} from "../../utils/utils";
import { Menu, Header, Checkbox, Form, Input, Icon, Dropdown, Button, Message } from 'semantic-ui-react';

class InformationEmail extends React.Component {
  render() {
    const {
      email,
      check,
      onChange
    } = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>What is your email?</Header>
        <label className='for_input'>Email address</label>
        <Input
          required
          autoFocus
          id='email'
          name='email'
          value={email}
          onChange={onChange}
          placeholder='elon@spacex.com'/>
        <Checkbox
          checked={check}
          name='checkEmail'
          onChange={onChange}
          label='It’s okay to send me very occasional emails about Ease.space.'/>
      </React.Fragment>
    )
  }
}

class InformationConfirmationCode extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      focus: true
    }
  }
  onClick = () => {
    this.refs.code.focus();
    this.setState({focus: true})
  };
  render() {
    const {
      onChange,
      confirmationCode
    } = this.props;
    const codeSplit = confirmationCode.split('',6);
    return (
      <React.Fragment>
        <Header as='h1'>Verify your email</Header>
        <label className='for_input'>Your confirmation code</label>
        <Input
          required
          autoFocus
          ref="code"
          onChange={onChange}
          id='confirmationCode'
          name='confirmationCode'
          className='hidden_input'
          value={confirmationCode}
          onBlur={e => this.setState({focus: false})}/>
        <div className='confirmation_code' onClick={this.onClick}>
          <div className={(!codeSplit[0] && this.state.focus) ? 'div_input left focus' : 'div_input left'}><span>{codeSplit[0]}</span></div>
          <div className={(codeSplit[0] && !codeSplit[1] && this.state.focus) ? 'div_input focus' : 'div_input'}><span>{codeSplit[1]}</span></div>
          <div className={(codeSplit[1] && !codeSplit[2] && this.state.focus) ? 'div_input focus' : 'div_input'}><span>{codeSplit[2]}</span></div>
          <div className={(codeSplit[2] && !codeSplit[3] && this.state.focus) ? 'div_input focus' : 'div_input'}><span>{codeSplit[3]}</span></div>
          <div className={(codeSplit[3] && !codeSplit[4] && this.state.focus) ? 'div_input focus' : 'div_input'}><span>{codeSplit[4]}</span></div>
          <div className={(codeSplit[4] && this.state.focus) ? 'right div_input focus' : 'right div_input'}><span>{codeSplit[5]}</span></div>
        </div>
        <p>Keep this window open while getting your code</p>
      </React.Fragment>
    )
  }
}

class InformationPassword extends React.Component {
  render() {
    const {
      onChange,
      onChangePassword,
      phone,
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
          onChange={onChange}/>
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
      </React.Fragment>
    )
  }
}

class InformationCompany extends React.Component {
  render() {
    const {
      onChange,
      companyName,
      companySize,
      firstName,
      lastName
    } = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Account information</Header>
        <div style={{display:'inline-flex'}}>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Company name</label>
            <Input
              required
              autoFocus
              id='companyName'
              name='companyName'
              value={companyName}
              placeholder='SpaceX'
              onChange={onChange}/>
          </div>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Company size</label>
            <Input
              required
              id='companySize'
              placeholder='65'
              name='companySize'
              value={companySize}
              onChange={onChange}/>
          </div>
        </div>
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
      </React.Fragment>
    )
  }
}

class NewTeamCreationView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      activeItem: 1,
      view: 1,
      email: '',
      checkEmail: false,
      confirmationCode: '',
      phone: '',
      password: '',
      verificationPassword: '',
      passwordError: true,
      companyName: '',
      companySize: '',
      firstName: '',
      lastName: ''
    };
  }
  handleInput = handleSemanticInput.bind(this);
  handleConfirmationCode = (e, {name, value}) => {
    if (value.match(/^[0-9]{0,6}$/g))
      this.setState({[name]: value});
  };
  handlePasswordInput = (e, {name, value}) => {
    if (/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(value))
      this.setState({[name]: value, passwordError: false});
    else
      this.setState({[name]: value, passwordError: true});
  };
  checkPassword = () => {
    return (this.state.password !== '' && this.state.password === this.state.verificationPassword && this.state.phone.length > 9)
  };
  next = () => {
    if (this.state.view === 1) {
      // request send email w/ clearbit
      this.setState({view: 2});
    }
    if (this.state.view === 2) {
      // request check if is the good confirmation code
      this.setState({view: 3});
    }
    if (this.state.view === 3) {
      this.setState({view: 4})
    }
  };
  render() {
    const firstP = this.state.view === 1 ? <p>Confirm your email. You will receive a verification code.</p>
      : this.state.view === 2 ? <p>We’ve sent a code to <strong>{this.state.email}</strong>, it will expire shortly.<br/><br/>
          Haven’t received our email?<br/>Try our spam folder or <br/><u>Resend email</u></p>
        : this.state.view === 3 ? <p>Enter a <strong>strong password</strong><br/>without names, dates or info<br/>related to you.<br/><br/>
            Your password must contain<br/>at least <strong>8 characters,<br/>an uppercase, a lowercase<br/>and a number</strong>.</p>
          : <p>This information will be<br/>available only to your team.</p>;
    return (
      <div id='new_team_creation'>
        <div id='left_bar'>
          {firstP}
          <img src='/resources/images/ease_logo_white.svg'/>
        </div>
        <div id='center'>
          <Menu id='top_bar' pointing secondary fluid>
            <Menu.Item name='Information' active={this.state.activeItem === 1}/>
            <Menu.Item name='Rooms' active={this.state.activeItem === 2}/>
            <Menu.Item name='Members' active={this.state.activeItem === 3}/>
            <Menu.Item name='Groups' active={this.state.activeItem === 4}/>
            <Menu.Item name='Accounts' active={this.state.activeItem === 5}/>
          </Menu>
          <div id='content'>
            <Form onSubmit={this.next}>
              {this.state.view === 1 &&
                <InformationEmail email={this.state.email} check={this.state.checkEmail} onChange={this.handleInput}/>}
              {this.state.view === 2 &&
                <InformationConfirmationCode onChange={this.handleConfirmationCode} confirmationCode={this.state.confirmationCode}/>}
              {this.state.view === 3 &&
                <InformationPassword
                  phone={this.state.phone}
                  password={this.state.password}
                  verificationPassword={this.state.verificationPassword}
                  onChange={this.handleInput}
                  onChangePassword={this.handlePasswordInput}
                  passwordError={this.state.passwordError}/>}
              {this.state.view === 4 &&
                <InformationCompany
                  onChange={this.handleInput}
                  companyName={this.state.companyName}
                  companySize={this.state.companySize}
                  firstName={this.state.firstName}
                  lastName={this.state.lastName}/>}
            </Form>
          </div>
          <div id='bottom_bar'>
            <Button positive
                    size='tiny'
                    type='submit'
                    onClick={this.next}
                    disabled={(this.state.view === 1 && !isEmail(this.state.email))
                    || (this.state.view === 2 && this.state.confirmationCode.length < 6)
                    || (this.state.view === 3 && !this.checkPassword())}>
              Next
              <Icon name='arrow right'/>
            </Button>
          </div>
        </div>
      </div>
    )
  }
}

export default NewTeamCreationView;