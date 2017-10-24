import React from 'react';
import { Image, Icon, Form, Button, Message, Checkbox, Segment } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import InputModalCatalog from './InputModalCatalog';


class SsoAppModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            errorMessage: '',
            url: '',
            login: '',
            password: '',
            loading: false,
            addBookmark: false,
            addGoogleAccount: false,
            accountGoogleSelected: ''
        }
    }

    handleInput = (e, { name, value }) => this.setState({ [name]: value });

    toggleBookmark = () => {
        if (this.state.addBookmark)
            this.setState({ addBookmark: false, url: '' });
        else
            this.setState({ addBookmark: true, login: '', password: '', accountGoogleSelected: '' });
    };

    back = () => {
        this.setState({ accountGoogleSelected: '' });
    };

    selectAccount = (account) => {
        this.setState({ accountGoogleSelected: account });
    };

    addGoogleAccount = () => {
      this.setState({ addGoogleAccount: true, accountGoogleSelected: '' });
    };

    render() {

        return (
            <SimpleModalTemplate
                onClose={e => {
                    this.props.dispatch(showDepartureDateEndModal(false))
                }}
                headerContent={'Setup your App'}>
                <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
                    <Form.Field>
                        <Image src={this.props.logo} style={{ width:'80px', marginRight: '10px', display: 'inline-block', borderRadius: '5px'}}/>
                        <p style={{ display: 'inline-block', fontSize: '20px', fontWeight: '300', color: '#939eb7' }}>{this.props.name}</p>
                    </Form.Field>
                    {!this.state.addGoogleAccount ?
                        <div>
                            <Form.Field>
                                <p style={{display: 'inline-block', fontSize: '20px', color: '#414141'}}><strong>Add just a Bookmark</strong></p>
                                <Checkbox toggle onClick={e => this.toggleBookmark()}
                                          style={{marginLeft: '20px', marginBottom: '0'}}/>
                            </Form.Field>
                            {!this.state.addBookmark ?
                            <div>
                                <Form.Field>
                                    <Segment.Group className='connectWithGoogle'>
                                        <Segment className='first'>
                                            <Icon name='google'/>
                                            Sign in with your Google Account
                                        </Segment>
                                        <Segment>
                                            <p>
                                                <a onClick={e => this.selectAccount('victor_nivet@hotmail.fr')}>
                                                    <Icon name='user circle' />
                                                    victor_nivet@hotmail.fr
                                                </a>
                                            </p>
                                            <p>
                                                <a onClick={e => this.selectAccount('victor_nivet@gmail.com')}>
                                                    <Icon name='user circle' />
                                                    victor_nivet@gmail.com
                                                </a>
                                            </p>
                                            <p>
                                                <a onClick={e => this.selectAccount('victor_nivet@outlook.fr')}>
                                                    <Icon name='user circle' />
                                                    victor_nivet@outlook.fr
                                                </a>
                                            </p>
                                            <p>
                                                <a onClick={e => this.selectAccount('victor_nivet@free.fr')}>
                                                    <Icon name='user circle' />
                                                    victor_nivet@free.fr
                                                </a>
                                            </p>
                                            <p>
                                                <a onClick={this.addGoogleAccount}>
                                                    <Icon name='add square' />
                                                    Add a new Google Account</a>
                                            </p>
                                        </Segment>
                                    </Segment.Group>
                                </Form.Field>
                            </div>
                            :
                            <div>
                                <InputModalCatalog
                                    nameLabel='Here is the link'
                                    nameInput='url'
                                    inputType='url'
                                    placeholderInput=''
                                    handleInput={this.handleInput}
                                    iconLabel='home'
                                    valueInput={!this.state.url ? this.props.url : this.state.url} />
                            </div>
                        }
                        </div>
                        :
                        <div>
                            <InputModalCatalog
                                nameLabel='Login'
                                nameInput='login'
                                inputType='text'
                                placeholderInput='Your login'
                                handleInput={this.handleInput}
                                iconLabel='user' />
                            <InputModalCatalog
                                nameLabel='Password'
                                nameInput='password'
                                inputType='password'
                                placeholderInput='Your password'
                                handleInput={this.handleInput}
                                iconLabel='lock' />
                        </div>
                    }
                    <Message error content={this.state.errorMessage} />
                        <Button
                            disabled={!this.state.addBookmark && (!this.state.login || !this.state.password) && !this.state.accountGoogleSelected}
                            attached='bottom'
                            type="submit"
                            positive
                            loading={this.state.loading}
                            onClick={this.confirm}
                            class="modal-button uppercase"
                            content={'CONFIRM'} />
                </Form>
            </SimpleModalTemplate>
        )
    }
}

export default SsoAppModal;