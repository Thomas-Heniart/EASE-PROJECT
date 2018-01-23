import React from 'react';
import { Header, Checkbox, Input } from 'semantic-ui-react';

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
          className='checkbox_email'
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
          <div className={(!codeSplit[0] && this.state.focus) ? 'div_input left focus' : 'div_input left'}>
            <span>{codeSplit[0]}</span>
          </div>
          <div className={(codeSplit[0] && !codeSplit[1] && this.state.focus) ? 'div_input focus' : 'div_input'}>
            <span>{codeSplit[1]}</span>
          </div>
          <div className={(codeSplit[1] && !codeSplit[2] && this.state.focus) ? 'div_input focus' : 'div_input'}>
            <span>{codeSplit[2]}</span>
          </div>
          <div className={(codeSplit[2] && !codeSplit[3] && this.state.focus) ? 'div_input focus' : 'div_input'}>
            <span>{codeSplit[3]}</span>
          </div>
          <div className={(codeSplit[3] && !codeSplit[4] && this.state.focus) ? 'div_input focus' : 'div_input'}>
            <span>{codeSplit[4]}</span>
          </div>
          <div className={(codeSplit[4] && this.state.focus) ? 'right div_input focus' : 'right div_input'}>
            <span>{codeSplit[5]}</span>
          </div>
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
      check,
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
        <div style={{display:'inline-flex'}}>
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

class OnBoardingInformations extends React.Component {
  render() {
    const {
      view,
      email,
      checkEmail,
      handleInput,
      handleConfirmationCode,
      confirmationCode,
      phone,
      password,
      verificationPassword,
      handlePasswordInput,
      passwordError,
      companyName,
      companySize,
      firstName,
      lastName,
      checkCGU
    } = this.props;
    return (
      <React.Fragment>
        {view === 1 &&
          <InformationEmail
            email={email}
            check={checkEmail}
            onChange={handleInput}/>}
        {view === 2 &&
          <InformationConfirmationCode
            onChange={handleConfirmationCode}
            confirmationCode={confirmationCode}/>}
        {view === 3 &&
          <InformationPassword
            phone={phone}
            password={password}
            onChange={handleInput}
            passwordError={passwordError}
            onChangePassword={handlePasswordInput}
            verificationPassword={verificationPassword}/>}
        {view === 4 &&
          <InformationCompany
            companyName={companyName}
            companySize={companySize}
            onChange={handleInput}
            firstName={firstName}
            lastName={lastName}
            check={checkCGU}/>}
      </React.Fragment>
    )
  }
}

export default OnBoardingInformations;