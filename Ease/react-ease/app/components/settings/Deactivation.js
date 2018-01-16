import React from 'react';
import { Button, Segment, Form, Header, Message } from 'semantic-ui-react';
import {deleteAccount} from "../../actions/commonActions";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

@connect(store => ({
    common: store.common
}), reduxActionBinder)
class Deactivation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            view: 1,
            accept: false,
            errorMessage: '',
            password: '',
            loading: false
        }
    }
    handleInput = (e, {name, value}) => {
        this.setState({ [name]: value });
    };
    changeView = () => {
        this.setState({ view: 2 });
    };
    cancel = () => {
        this.setState({ view: 1 });
    };
    accept = () => {
        if (this.state.accept === true)
            this.setState({ accept: false });
        else
            this.setState({ accept: true });
    };
    confirm = () => {
        this.setState({ loading: true, errorMessage: '' });
        this.props.dispatch(deleteAccount({
                password: this.state.password
        })).then(response => {
            this.setState({ loading: false });
            location.reload();
        }).catch(err => {
            this.setState({ loading: false, errorMessage: err });
        });
    };
    render() {
        return (
            <Segment clearing>
                {this.state.view === 1 ?
                    <Form>
                        <Header as='h5'>Your account is activated</Header>
                        <p>For a personal use, Ease.space is free and will remain free.</p>
                        <p>Deactivating your account implies a permanent loss of all passwords and IDs secured on Ease.space. Once you deactivate your account, you will not be able to step back. </p>
                        <Button floated='right'
                                color='red'
                                loading={this.state.loading}
                                content='Deactivate account'
                                onClick={this.changeView} />
                    </Form>
                    :
                    <Form onSubmit={this.confirm} size={'mini'} error={this.state.errorMessage.length > 0}>
                        <Header as='h5'>Your account is activated</Header>
                        <p>For a personal use, Ease.space is free and will remain free.</p>
                        <p>Deactivating your account implies a permanent loss of all passwords and IDs secured on Ease.space. Once you deactivate your account, you will not be able to step back. </p>
                        <Form.Field style={{ display: 'inline-flex', width: '100%' }}>
                            <Form.Checkbox onChange={this.accept}
                                           checked={this.state.accept === true}
                                           required
                                           label='I accept the condition of unsubscription' />
                        </Form.Field>
                        <Form.Field>
                            <Form.Input className='inputInSegment'
                                        label='Your password'
                                        type='password'
                                        name='password'
                                        onChange={this.handleInput}
                                        required />
                            <Message error content={this.state.errorMessage} />
                        </Form.Field>
                        <Form.Field>
                            <Button type='submit'
                                    content='Deactivate account'
                                    color={'red'}
                                    floated={'right'}
                                    loading={this.state.loading}
                                    disabled={!this.state.accept || !this.state.password}
                                    size='medium' />
                            <Button content='Cancel'
                                    floated={'right'}
                                    size='medium'
                                    style={{ color: '#5a5a5a', marginRight: '10px' }}
                                    onClick={this.cancel} />
                        </Form.Field>
                    </Form>
                }
            </Segment>
        )
    }
}

export default Deactivation;