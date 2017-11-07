import React from 'react';
import { Input, Button, Segment, Form, Header, Message } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import {checkPassword} from "../../actions/commonActions";
import {editPassword} from "../../actions/commonActions";

@connect(store => ({
    common: store.common
}), reduxActionBinder)
class Password extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            view: 1,
            currentPassword: '',
            newPassword: '',
            confirmNewPassword: '',
            errorMessage: '',
            fakePassword: 'haha',
            loading: false
        }
    }

    accessModification = () => {
        this.setState({ loading: true, errorMessage: '' });
        this.props.dispatch(checkPassword({
            password: this.state.currentPassword
        })).then(response => {
            if (response.valid)
                this.setState({ loading: false, view: 2, errorMessage: '' });
            else
                this.setState({ loading: false, errorMessage: 'Wrong Password' });
        }).catch(err => {
            this.setState({ loading: false, errorMessage: err });
        });
    };
    cancel = () => {
        this.setState({ view: 1, newPassword: '', currentPassword: '', confirmNewPassword: '', errorMessage: '' });
    };
    handleInput = (e, {name, value}) => {
        this.setState({ [name]: value });
    };
    confirm = () => {
        if (this.state.newPassword !== this.state.confirmNewPassword)
            this.setState({ errorMessage: 'Password do not match' });
        else {
            if (this.state.newPassword.match(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+).{8,}/gi)) {
                this.setState({loading: true, errorMessage: ''});
                this.props.dispatch(editPassword({
                    password: this.state.currentPassword,
                    new_password: this.state.newPassword
                })).then(response => {
                    if (response.msg === 'Password edited')
                        this.setState({
                            loading: false,
                            view: 1,
                            errorMessage: '',
                            newPassword: '',
                            confirmNewPassword: '',
                            currentPassword: ''
                        });
                }).catch(err => {
                    this.setState({loading: false, errorMessage: err});
                });
            }
            else {
                this.setState({ errorMessage: 'Your password must contain at least 8 character, 1 uppercase, 1 lowercase and 1 number.' });
            }
        }
    };
    render() {
        return (
            <Segment.Group>
                <Segment clearing>
                    <Header as='h5'>
                        To modify your password, type your current password first
                        <Header.Subheader style={{ fontSize: '16px' }}>
                            Your password must contain at least 8 character, 1 uppercase, 1 lowercase and 1 number.
                        </Header.Subheader>
                    </Header>
                    {this.state.view === 1 &&
                    <Form onSubmit={this.accessModification} size='mini' error={this.state.errorMessage.length > 0}>
                        <Form.Field style={{ display: 'inline-flex', width: '100%' }}>
                            <Form.Input className='inputInSegment'
                                        label='Current password'
                                        name='currentPassword'
                                        type='password'
                                        error={this.state.errorMessage.length > 0 && this.state.view === 1}
                                        value={this.state.currentPassword}
                                        onChange={this.handleInput}
                                        disabled={this.state.view === 2}
                                        required />
                            <Form.Button type='submit'
                                         loading={this.state.loading}
                                         content='Access modification'
                                         style={{ marginLeft: '15px' }} />
                        </Form.Field>
                        <Message error content={this.state.errorMessage} />
                    </Form>}
                    {this.state.view === 2 &&
                    <Form onSubmit={this.confirm} size='mini' error={this.state.errorMessage.length > 0}>
                        <Form.Field style={{ display: 'inline-flex', width: '100%' }}>
                            <Form.Input className='inputInSegment'
                                        label='Current password'
                                        name='currentPassword'
                                        type='password'
                                        value={this.state.currentPassword}
                                        disabled />
                        </Form.Field>
                        <Form.Field>
                            <Form.Input className='inputInSegment'
                                        label='New password'
                                        type='password'
                                        name='newPassword'
                                        error={this.state.errorMessage.length > 0 && this.state.view === 2}
                                        onChange={this.handleInput} />
                        </Form.Field>
                        <Form.Field>
                            <Form.Input className='inputInSegment'
                                        label='Confirm new password'
                                        type='password'
                                        name='confirmNewPassword'
                                        error={this.state.errorMessage.length > 0 && this.state.view === 2}
                                        onChange={this.handleInput} />
                        </Form.Field>
                        <Message error content={this.state.errorMessage} />
                        <p>Take care of remembering this password. In case you loose it, for safety reasons all passwords on your Ease.space account will be deleted and you will have to add them again. </p>
                        <Form.Field>
                            <Button type='submit'
                                    content='Save'
                                    floated={'right'}
                                    size='medium'
                                    disabled={!this.state.newPassword} />
                            <Button content='Cancel'
                                    floated={'right'}
                                    size='medium'
                                    style={{ backgroundColor: '#e0e1e2', color: '#5a5a5a', marginRight: '10px' }}
                                    onClick={this.cancel} />
                        </Form.Field>
                    </Form>
                    }
                </Segment>
            </Segment.Group>
        )
    }
}

export default Password;