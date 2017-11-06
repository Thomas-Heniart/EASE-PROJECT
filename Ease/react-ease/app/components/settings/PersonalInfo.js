import React from 'react';
import { Input, Button, Segment, Form, Header, Message } from 'semantic-ui-react';

class PersonalInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            newUsername: '',
            newEmail: '',
            confirmationCode: '',
            modifyingUsername: false,
            modifyingMail: 1,
            loading: false,
            errorMessage: '',
            fakePassword: 'haha',
            usernameError: false
        }
    }
    handleInput = (e, {name, value}) => {
        this.setState({ [name]: value });
    };
    handleUsernameInput = (e, {name, value}) => {
        console.log(value);
        if (value && value.match(/[a-zA-Z0-9\s_\-]/gi)) {
            if (value.match(/[a-zA-Z0-9\s_\-]/gi).length === value.length && value.length <= 22)
                this.setState({ [name]: value.toLowerCase().replace(/\s/gi, '_'), usernameError: false });
            else
                this.setState({ usernameError: true });
        }
        else
            this.setState({ [name]: '', usernameError: true });
    };
    modify = (modify) => {
        this.setState({ [modify]: true, newUsername: this.props.userInfo.first_name, modifyingMail: 1, newEmail: '', confirmationCode: '' });
    };
    modifyEmail = () => {
        this.setState({ modifyingMail: this.state.modifyingMail + 1, modifyingUsername: false });
    };
    cancelModify = (cancel) => {
        this.setState({ [cancel]: false })
    };
    cancelModifyEmail = () => {
        this.setState({ modifyingMail: 1, newEmail: '', confirmationCode: '' });
    };
    confirm = () => {
        console.log('change email');
        this.setState({ modifyingMail: 1, newEmail: '', confirmationCode: '' });
    };
    render() {
        return (
            <div>
                <Segment clearing>
                    <Form size='mini' onSubmit={e => console.log('send form username')} error={this.state.errorMessage.length > 0}>
                        <Header as='h5'>Your username</Header>
                        <Form.Field>
                            <Form.Input disabled={!this.state.modifyingUsername}
                                        name='newUsername'
                                        className='inputInSegment'
                                        label='Username'
                                        onChange={this.handleUsernameInput}
                                        value={this.state.modifyingUsername ? this.state.newUsername : this.props.userInfo.first_name} />
                        </Form.Field>
                        <Message error content={this.state.errorMessage} />
                        {!this.state.usernameError ?
                            <p>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. From 3 to 22 characters.</p>
                            :
                            <p style={{ color: '#eb555c' }}>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. From 3 to 22 characters.</p>
                        }
                        {!this.state.modifyingUsername ?
                            <Button content='Modify'
                                    floated={'right'}
                                    size='medium'
                                    onClick={e => this.modify('modifyingUsername')} />
                            :
                            <Form.Field>
                                <Button type='submit'
                                        disabled={this.state.newUsername.length < 3}
                                        content='Save'
                                        floated={'right'}
                                        size='medium' />
                                <Button content='Cancel'
                                        floated={'right'}
                                        size='medium'
                                        style={{ backgroundColor: '#e0e1e2', color: '#5a5a5a', marginRight: '10px' }}
                                        onClick={e => this.cancelModify('modifyingUsername')} />
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
                                        value={this.props.userInfo.email} />
                            <Form.Button onClick={this.modifyEmail}
                                         content='Replace email'
                                         floated={'right'}
                                         size='medium' />
                        </Form>
                        : this.state.modifyingMail === 2 ?
                            <Form size='mini' onSubmit={this.modifyEmail} error={this.state.errorMessage.length > 0}>
                                <Form.Field>
                                    <Form.Input type='password'
                                                className='inputInSegment'
                                                label='Your Ease.space password'
                                                required />
                                </Form.Field>
                                <Form.Field style={{ display: 'inline-flex', width: '100%' }}>
                                    <Form.Input className='inputInSegment'
                                                type='email'
                                                label='New email'
                                                name='newEmail'
                                                value={this.state.newEmail}
                                                onChange={this.handleInput} />
                                    <Form.Button disabled={!this.state.newEmail}
                                                 type='submit'
                                                 content='Verify new email'
                                                 size='medium'
                                                 style={{ marginLeft: '15px' }} />
                                </Form.Field>
                                <Message error content={this.state.errorMessage} />
                            </Form>
                            :
                            <Form  size='mini' onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
                                <Form.Field>
                                    <Form.Input disabled
                                                type='password'
                                                className='inputInSegment'
                                                label='Your Ease.space password' />
                                </Form.Field>
                                <Form.Field style={{ display: 'inline-flex', width: '100%' }}>
                                    <Form.Input className='inputInSegment'
                                                type='email'
                                                label='New email'
                                                name='newEmail'
                                                value={this.state.newEmail}
                                                disabled />
                                    <Form.Button content='Resend email'
                                                 size='medium'
                                                 type='button'
                                                 onClick={e => console.log('resend email')}
                                                 style={{ marginLeft: '15px' }} />
                                </Form.Field>
                                <p>We’ve sent you a 6 digit code to verify your email. It will expire soon, please enter it bellow soon.</p>
                                <Form.Field>
                                    <Form.Input className='inputInSegment'
                                                label='Your confirmation code'
                                                name='confirmationCode'
                                                value={this.state.confirmationCode}
                                                onChange={this.handleInput}
                                                required />
                                </Form.Field>
                                <Message error content={this.state.errorMessage} />
                                <Form.Field>
                                    <Button type='submit'
                                            content='Save'
                                            floated={'right'}
                                            size='medium'
                                            disabled={!this.state.confirmationCode} />
                                    <Button content='Cancel'
                                            floated={'right'}
                                            size='medium'
                                            style={{ backgroundColor: '#e0e1e2', color: '#5a5a5a', marginRight: '10px' }}
                                            onClick={e => this.cancelModifyEmail('modifyingEmail')} />
                                </Form.Field>
                            </Form>
                        }
                </Segment>
            </div>
        )
    }
}

export default PersonalInfo;