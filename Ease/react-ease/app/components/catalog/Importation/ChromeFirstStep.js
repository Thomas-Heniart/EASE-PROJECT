import React from 'react';
import {connect} from "react-redux";
import {credentialIconType} from "../../../utils/utils";
import { Segment, Button, Icon, Form, Input, Label, List} from 'semantic-ui-react';

@connect(store => ({
  ssoGroups: store.dashboard.sso_groups
}))
class ChromeFirstStep extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      addGoogleAccount: false
    }
  }
  addGoogleAccount = () => {
    this.setState({addGoogleAccount: true}, this.props.resetChromeCredentials());
  };
  back = () => {
    this.setState({addGoogleAccount: false});
  };
  render() {
    const {
      back,
      next,
      login,
      loginId,
      onChange,
      password,
      selectAccount
    } = this.props;
    const mailList = !this.state.addGoogleAccount && Object.keys(this.props.ssoGroups).length > 0 ?
      Object.keys(this.props.ssoGroups).map(key => {
        const loginChose = loginId === this.props.ssoGroups[key].id;
        return (
          <List.Item key={this.props.ssoGroups[key].id} as="p" className="overflow-ellipsis">
            <a onClick={e => selectAccount(this.props.ssoGroups[key])}>
              <Icon style={loginChose ? {color: '#eb555c'} : null} name='user circle'/>
              <span style={loginChose ? {color: '#eb555c', textDecoration: 'underline'} : null}>
                {this.props.ssoGroups[key].account_information.login}
              </span>
            </a>
          </List.Item>);
      }) : null;
    return (
      <Form className='chromeForm' onSubmit={next}>
        <Segment id='chromeSteps'>
          <p className='title'><img src="/resources/other/Chrome.png"/> Import your passwords from Chrome</p>
          <div className='inline' style={mailList ? {margin:'10px 0 30px 0'} : {margin:'10px 0 10px 0'}}>
            <p style={{fontSize:'16px'}}>Your passwords will be imported from <br />
              <a target='_blank' href="https://passwords.google.com"> passwords.google.com</a>.
              If they aren't synchronized there&nbsp;
              <a target='_blank' style={{color:'#949eb7', textDecoration: 'underline'}}
                 href="https://blog.ease.space/get-the-best-of-your-chrome-importation-on-ease-space-b2f955dbf8f4">check here</a>
              &nbsp;to do it in few clicks.</p>
          </div>
          {!mailList &&
          <React.Fragment>
            {Object.keys(this.props.ssoGroups).length > 0 &&
            <p className='backPointer chrome_import' onClick={this.back} style={{color:'#414141'}}><Icon name='arrow left'/><span>Back</span></p>}
            <Form.Field>
              <label>Login</label>
              <Input size="large"
                     autoFocus
                     class="modalInput team-app-input"
                     required
                     autoComplete='on'
                     name='chromeLogin'
                     onChange={onChange}
                     label={<Label><Icon name={credentialIconType['login']}/></Label>}
                     labelPosition="left"
                     placeholder='Your login'
                     value={login}
                     type='text'/>
            </Form.Field>
            <Form.Field>
              <label>Password</label>
              <Input size="large"
                     class="modalInput team-app-input"
                     required
                     autoComplete='on'
                     name='chromePassword'
                     onChange={onChange}
                     label={<Label><Icon name={credentialIconType['password']}/></Label>}
                     labelPosition="left"
                     placeholder='Your password'
                     value={password}
                     type='password'/>
            </Form.Field>
          </React.Fragment>}
          {mailList &&
          <Form.Field>
            <Segment.Group className='connectWithGoogle' style={{maxWidth:'none',transform:'none',left:'unset'}}>
              <Segment className='first'>
                <Icon name='google'/>
                Sign in with your Google Account
              </Segment>
              <Segment>
                {mailList}
                <p>
                  <a onClick={this.addGoogleAccount}>
                    <Icon name='add square'/>
                    Use another Google Account</a>
                </p>
              </Segment>
            </Segment.Group>
          </Form.Field>}
        </Segment>
        <Button className={'left'} onClick={back} type='button'>
          <Icon name='arrow left'/> Back
        </Button>
        <Button className={'right'} positive onClick={next} type='submit' disabled={login === '' || password === ''}>
          Next <Icon name='arrow right'/>
        </Button>
      </Form>
    )
  }
}

export default ChromeFirstStep;