import React from 'react';
import {credentialIconType} from "../../../utils/utils";
import { Segment, Button, Icon, Form, Message, Input, Label} from 'semantic-ui-react';

class ChromeFirstStep extends React.Component {
  render() {
    const {
      login,
      password,
      error,
      back,
      next,
      onChange
    } = this.props;
    return (
      <Form className='chromeForm' error={error !== ''} onSubmit={next}>
        <Segment id='chromeSteps'>
          <p className='title'><img src="/resources/other/Chrome.png"/> Import your passwords from Chrome</p>
          <div className='inline'>
            <p>Your passwords will be imported from <br />
              <a href="https://passwords.google.com"> passwords.google.com</a>.
              If they aren't synchronized there &nbsp;
              <a style={{color:'#949eb7', textDecoration: 'underline'}}
                 href="https://blog.ease.space/get-the-best-of-your-chrome-importation-on-ease-space-b2f955dbf8f4">check here</a>
              &nbsp; to do it in few clicks</p>
          </div>
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
          <Form.Field>
            <Message error size="mini" content={error}/>
          </Form.Field>
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