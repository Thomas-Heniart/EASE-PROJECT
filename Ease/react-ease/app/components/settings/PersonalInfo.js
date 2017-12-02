import React from 'react';
import { Button, Segment, Form, Header, Message } from 'semantic-ui-react';
import {editPersonalUsername, editEmail} from "../../actions/commonActions";
import {isEmail} from "../../utils/utils";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

@connect(store => ({
    common: store.common
}), reduxActionBinder)
class PersonalInfo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      newUsername: '',
      username: this.props.userInfo.first_name,
      newEmail: '',
      email: '',
      confirmationCode: '',
      modifyingUsername: false,
      modifyingMail: 1,
      loading: false,
      loadingResendEmail: false,
      errorMessage: '',
      usernameError: false,
      password: ''
    }
  }

  handleInput = (e, {name, value}) => {
    this.setState({[name]: value});
  };
  handleUsernameInput = (e, {name, value}) => {
    if (value && value.match(/[a-zA-Z0-9\s_\-]/gi)) {
      if (value.match(/[a-zA-Z0-9\s_\-]/gi).length === value.length && value.length <= 22)
        this.setState({[name]: value.toLowerCase().replace(/\s/gi, '_'), usernameError: false});
      else
        this.setState({usernameError: true});
    }
    else
      this.setState({[name]: '', usernameError: true});
  };
  modify = (modify) => {
    this.setState({
      [modify]: true,
      modifyingMail: 1,
      confirmationCode: '',
      newEmail: '',
      newUsername: this.state.username
    });
  };
  modifyEmail = () => {
    this.setState({modifyingMail: this.state.modifyingMail + 1, modifyingUsername: false, newUsername: ''});
  };
  askEditEmail = () => {
    this.setState({loading: true, errorMessage: '', loadingResendEmail: true});
    this.props.dispatch(askEditEmail({
      password: this.state.password,
      new_email: this.state.newEmail
    })).then(response => {
      this.setState({loading: false, modifyingMail: 3, errorMessage: '', loadingResendEmail: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err, loadingResendEmail: false});
    });
  };
  cancelModify = (cancel) => {
    this.setState({[cancel]: false, usernameError: false, newUsername: '', errorMessage: ''})
  };
  cancelModifyEmail = () => {
    this.setState({modifyingMail: 1, confirmationCode: '', newEmail: '', errorMessage: ''});
  };
  confirm = () => {
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editEmail({
      password: this.state.password,
      new_email: this.state.newEmail,
      digits: this.state.confirmationCode
    })).then(response => {
      this.props.common.user.email = this.state.newEmail;
      this.setState({
        loading: false,
        modifyingMail: 1,
        errorMessage: '',
        confirmationCode: '',
        email: this.state.newEmail,
        newEmail: ''
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  confirmNewUsername = () => {
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editPersonalUsername({
      username: this.state.newUsername
    })).then(response => {
      this.props.common.user.first_name = this.state.newUsername;
      this.setState({
        loading: false,
        modifyingUsername: false,
        errorMessage: '',
        username: this.state.newUsername,
        newUsername: ''
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };

  render() {
    return (
      <div>
        <Segment clearing>
          <Form size='mini' onSubmit={this.confirmNewUsername}
                error={this.state.errorMessage.length > 0 && this.state.modifyingUsername}>
            <Header as='h5'>Your username</Header>
            <Form.Field>
              <Form.Input disabled={!this.state.modifyingUsername}
                          name='newUsername'
                          className='inputInSegment'
                          label='Username'
                          onChange={this.handleUsernameInput}
                          value={this.state.modifyingUsername ? this.state.newUsername : this.state.username}/>
            </Form.Field>
            {this.state.modifyingUsername &&
            <Message error content={this.state.errorMessage}/>}
            {!this.state.usernameError ?
              <p>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and
                underscores. From 3 to 22 characters.</p>
              :
              <p style={{color: '#eb555c'}}>Please choose a username that is all lowercase, containing only letters,
                numbers, periods, hyphens and underscores. From 3 to 22 characters.</p>
            }
            {!this.state.modifyingUsername ?
              <Button content='Modify'
                      color={'blue'}
                      floated={'right'}
                      size='medium'
                      onClick={e => this.modify('modifyingUsername')}/>
              :
              <Form.Field>
                <Button type='submit'
                        disabled={this.state.newUsername.length < 3}
                        loading={this.state.loading && this.state.modifyingUsername}
                        color={'blue'}
                        content='Save'
                        floated={'right'}
                        size='medium'/>
                <Button content='Cancel'
                        floated={'right'}
                        size='medium'
                        style={{color: '#5a5a5a', marginRight: '10px'}}
                        onClick={e => this.cancelModify('modifyingUsername')}/>
              </Form.Field>
            }
          </Form>
        </Segment>
        <Segment clearing>
          <Header as='h5'>Your reference email</Header>
          {this.state.modifyingMail === 1 ?
            <Form size='mini' onSubmit={this.modifyEmail}>
              <Form.Input disabled
                          className='inputInSegment'
                          label='Email'
                          value={this.state.email ? this.state.email : this.props.userInfo.email}/>
              <Form.Button onClick={this.modifyEmail}
                           color={'blue'}
                           content='Replace email'
                           floated={'right'}
                           size='medium'/>
            </Form>
            : this.state.modifyingMail === 2 ?
              <Form size='mini' onSubmit={this.askEditEmail} error={this.state.errorMessage.length > 0}>
                <Form.Field>
                  <Form.Input onChange={this.handleInput}
                              name='password'
                              type='password'
                              className='inputInSegment'
                              label='Your Ease.space password'
                              required/>
                </Form.Field>
                <Form.Field style={{display: 'inline-flex', width: '100%'}}>
                  <Form.Input className='inputInSegment'
                              type='email'
                              label='New email'
                              name='newEmail'
                              value={this.state.newEmail}
                              onChange={this.handleInput}
                              required/>
                  <Form.Button disabled={!isEmail(this.state.newEmail)}
                               color={'blue'}
                               type='submit'
                               loading={this.state.loading && this.state.modifyingMail === 2}
                               content='Verify new email'
                               size='medium'
                               style={{marginLeft: '15px'}}/>
                </Form.Field>
                {this.state.modifyingMail === 2 &&
                <Message error content={this.state.errorMessage}/>}
              </Form>
              :
              <Form size='mini' onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
                <Form.Field>
                  <Form.Input disabled
                              type='password'
                              error={this.state.modifyingMail === 2}
                              className='inputInSegment'
                              label='Your Ease.space password'/>
                </Form.Field>
                <Form.Field style={{display: 'inline-flex', width: '100%'}}>
                  <Form.Input className='inputInSegment'
                              type='email'
                              label='New email'
                              name='newEmail'
                              error={this.state.modifyingMail === 2}
                              value={this.state.newEmail}
                              disabled/>
                  <Form.Button content='Resend email'
                               size='medium'
                               color={'blue'}
                               type='button'
                               loading={this.state.loadingResendEmail}
                               onClick={this.askEditEmail}
                               style={{marginLeft: '15px'}}/>
                </Form.Field>
                <p>We’ve sent you a 6 digit code to verify your email. It will expire soon, please enter it bellow
                  soon.</p>
                <Form.Field>
                  <Form.Input className='inputInSegment'
                              label='Your confirmation code'
                              name='confirmationCode'
                              value={this.state.confirmationCode}
                              onChange={this.handleInput}
                              required/>
                </Form.Field>
                {this.state.modifyingMail === 3 &&
                <Message error content={this.state.errorMessage}/>}
                <Form.Field>
                  <Button type='submit'
                          content='Save'
                          color={'blue'}
                          loading={this.state.loading && this.state.modifyingMail === 3 && !this.state.loadingResendEmail}
                          floated={'right'}
                          size='medium'
                          disabled={!this.state.confirmationCode}/>
                  <Button content='Cancel'
                          floated={'right'}
                          size='medium'
                          style={{color: '#5a5a5a', marginRight: '10px'}}
                          onClick={e => this.cancelModifyEmail('modifyingEmail')}/>
                </Form.Field>
              </Form>
          }
        </Segment>
      </div>
    )
  }
}

export default PersonalInfo;