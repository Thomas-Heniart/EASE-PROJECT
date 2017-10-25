import React from 'react';
import { Image, Icon, Form, Button, Message, Checkbox, Divider, Segment, List, Container, Input } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {dashboard} from "../../utils/post_api";
import InputModalCatalog from './InputModalCatalog';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
var api = require('../../utils/api');
var classnames = require('classnames');


@connect(store => ({
    catalog: store.catalog,
    modal: store.teamModals.catalogAddAppModal
}), reduxActionBinder)
class ClassicAppModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            name: this.props.modal.website.name,
            errorMessage: '',
            url: this.props.modal.website.landing_url,
            login: '',
            password: '',
            loading: false,
            facebook: false,
            linkedin: false,
            addBookmark: false,
            accountFacebookSelected: '',
            profiles: [],
            profileName: '',
            selectedProfile: -1,
            addingProfile: false,
            view: 1
        }
    }

    componentWillMount(){
        api.dashboard.fetchProfiles().then(response => {
            this.setState({profiles: response});
        });
    }

    handleInput = (e, { name, value }) => this.setState({ [name]: value });

    connectWith = (string) => {
        if (string === 'facebook')
            this.setState({ facebook: true, login: '', password: '' });
        else
            this.setState({ linkedin: true, login: '', password: '' });
    };

    toggleBookmark = () => {
        if (this.state.addBookmark)
            this.setState({ addBookmark: false, url: '' });
        else
            this.setState({ addBookmark: true, login: '', password: '' });
    };

    back = () => {
        this.setState({ facebook: false, linkedin: false, accountFacebookSelected: '' });
    };

    selectAccount = (account) => {
      this.setState({ accountFacebookSelected: account });
    };

    selectProfile = (id) => {
        this.setState({selectedProfile: id});
    };

    confirm = () => {
      if (this.state.view === 1) {
          this.setState({ view: 2 });
      }
      else {
          this.setState({loading: true, errorMessage: ''});
          if (this.state.addBookmark) {
              this.props.catalogAddBookmark({
                  name: this.state.name,
                  profile_id: this.state.selectedProfile,
                  url: this.state.url,
                  img_url: this.props.modal.website.logo
              }).then(r => {
                  this.setState({loading: false});
                  this.props.showCatalogAddAppModal({active: false})
              }).catch(err => {
                  this.setState({loading: false});
                  this.setState({errorMessage: err});
              });
          }
          else {
              this.props.catalogAddClassicApp({
                  name: this.state.name,
                  website_id: this.props.modal.website.id,
                  profile_id: this.state.selectedProfile,
                  account_information: { login: this.state.login, password: this.state.password }
          }).then(r => {
                  this.setState({loading: false});
                  this.props.showCatalogAddAppModal({active: false})
              }).catch(err => {
                  this.setState({loading: false});
                  this.setState({errorMessage: err});
              });
          }
      }
    };

    createProfile = (e) => {
        e.preventDefault();
        e.stopPropagation();
        if (this.state.profileName.length === 0)
            return;
        this.setState({addingProfile: true});
        dashboard.createProfile({name: this.state.profileName}).then(response => {
            let profiles = this.state.profiles.slice();
            profiles.push(response);
            this.setState({profiles: profiles, profileName: '', profile_id: response.id, addingProfile: false});
        }).catch(err => {
            this.setState({addingProfile: false});
        });
    };

    render() {
        const profiles = this.state.profiles.map(profile => {
            return (
                <List.Item as="a"
                           key={profile.id}
                           class="display_flex"
                           active={this.state.selectedProfile === profile.id}
                           onClick={e => this.selectProfile(profile.id)}>
                    <strong>{profile.name}</strong>
                    &nbsp;&nbsp;
                    <em class="overflow-ellipsis">
                        {
                            profile.apps.map(function(app, idx){
                                var ret = app.name;
                                ret += (idx === profile.apps.length - 1) ? '' : ', ';
                                return (ret)
                            }, this)
                        }
                    </em>
                </List.Item>
            )
        });

    return (
        <SimpleModalTemplate
            onClose={e => {
                this.props.showCatalogAddAppModal({active: false})
            }}
            headerContent={'Setup your App'}>
            {this.state.view === 1 ?
                <Form class="container" id="add_bookmark_form" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
                    <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
                        <div class="squared_image_handler" style={{boxShadow:'none'}}>
                            <img src={this.props.modal.website.logo} alt="Website logo"/>
                        </div>
                        <span class="app_name"><Input size="mini" type="text" placeholder="App name..."
                                                      name="name"
                                                      class="input_unstyle modal_input name_input"
                                                      autoFocus={true}
                                                      value={this.state.name}
                                                      onChange={this.handleInput}/></span>
                    </Form.Field>
                    <Form.Field>
                        <div style={{marginBottom: '10px'}}>App location (you can always change it later)</div>
                        <Container class="profiles">
                            <List link>
                                {profiles}
                            </List>
                            <form ref={(ref) => {this.form = ref}} style={{marginBottom: 0}} onSubmit={this.createProfile}>
                                <Input
                                    loading={this.state.addingProfile}
                                    value={this.state.profileName}
                                    name="profileName"
                                    required
                                    transparent
                                    onChange={this.handleInput}
                                    class="create_profile_input"
                                    icon={<Icon name="plus square" link onClick={this.createProfile}/>}
                                    placeholder='Create new group'
                                />
                            </form>
                        </Container>
                    </Form.Field>
                    <Message error content={this.state.errorMessage}/>
                    <Button
                        attached='bottom'
                        type="submit"
                        loading={this.state.loading}
                        positive
                        disabled={this.state.selectedProfile === -1 || this.state.loading || !this.state.name}
                        onClick={this.confirm}
                        className="modal-button"
                        content="NEXT"/>
                </Form>
                :
                <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
                    <Form.Field>
                        <Image src={this.props.modal.website.logo} style={{ width:'80px', marginRight: '10px', display: 'inline-block', borderRadius: '5px'}}/>
                        <p style={{ display: 'inline-block', fontSize: '20px', fontWeight: '300', color: '#939eb7' }}>{this.state.name}</p>
                    </Form.Field>
                    {!this.state.facebook && !this.state.linkedin ?
                        <div>
                            <Form.Field>
                                <p style={{ display: 'inline-block', fontSize: '20px', color: '#414141' }}><strong>Add just a Bookmark</strong></p>
                                <Checkbox toggle onClick={e => this.toggleBookmark()} style={{ marginLeft: '20px', marginBottom: '0' }} />
                            </Form.Field>
                            {!this.state.addBookmark ?
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
                                :
                                <div>
                                    <InputModalCatalog
                                        nameLabel='Here is the link'
                                        nameInput='url'
                                        inputType='url'
                                        placeholderInput=''
                                        handleInput={this.handleInput}
                                        iconLabel='home'
                                        valueInput={!this.state.url ? this.props.modal.website.landing_url : this.state.url} />
                                </div>
                            }
                            {!this.state.addBookmark && this.props.modal.website.connectWith_websites.length ?
                                <div>
                                    <Form.Field>
                                        <Divider horizontal>Or</Divider>
                                    </Form.Field>
                                    <Form.Field>
                                        <Button fluid
                                                className='buttonModalConnect'
                                                color='facebook'
                                                content={'Connect with Facebook'}
                                                icon='facebook'
                                                onClick={e => this.connectWith('facebook')} />
                                    </Form.Field>
                                    <Form.Field>
                                        <Button fluid
                                                className='buttonModalConnect'
                                                color='linkedin'
                                                content={'Connect with Linkedin'}
                                                icon='linkedin square'
                                                onClick={e => this.connectWith('linkedin')} />
                                    </Form.Field>
                                </div>
                                :
                                <div/>
                            }
                        </div>
                        : this.state.facebook ?
                            <div>
                                <Form.Field>
                                    <p className='backPointer' onClick={this.back}><Icon name='arrow left'/>Back</p>
                                </Form.Field>
                                <Form.Field>
                                    <Segment.Group className='connectWithFacebook'>
                                        <Segment className='first'>
                                            <Icon name='facebook'/>
                                            Select your Facebook account
                                        </Segment>
                                        <Segment>
                                            <List link className="listCategory">
                                                <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@hotmail.fr')}><Icon name='user circle' />victor_nivet@hotmail.fr</a></p></List.Item>
                                                <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@gmail.com')}><Icon name='user circle' />victor_nivet@gmail.com</a></p></List.Item>
                                                <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@outlook.fr')}><Icon name='user circle' />victor_nivet@outlook.fr</a></p></List.Item>
                                                <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@free.fr')}><Icon name='user circle' />victor_nivet@free.fr</a></p></List.Item>
                                            </List>
                                        </Segment>
                                    </Segment.Group>
                                </Form.Field>
                            </div>
                            :
                            <div>
                                <Form.Field>
                                    <p className='backPointer' onClick={this.back}><Icon name='arrow left'/>Back</p>
                                </Form.Field>
                                <Form.Field>
                                    <Segment.Group className='connectWithLinkedin'>
                                        <Segment className='first'>
                                            <Icon name='linkedin square'/>
                                            Select your Linkedin account
                                        </Segment>
                                        <Segment>
                                            <p><a><Icon name='user circle' />victor_nivet@hotmail.fr</a></p>
                                            <p><a><Icon name='user circle' />victor_nivet@gmail.com</a></p>
                                            <p><a><Icon name='user circle' />victor_nivet@outlook.fr</a></p>
                                            <p><a><Icon name='user circle' />victor_nivet@free.fr</a></p>
                                        </Segment>
                                    </Segment.Group>
                                </Form.Field>
                            </div>
                    }
                    <Message error content={this.state.errorMessage} />
                    <Button
                        disabled={!this.state.addBookmark && (!this.state.login || !this.state.password) && !this.state.accountFacebookSelected}
                        attached='bottom'
                        type="submit"
                        positive
                        loading={this.state.loading}
                        onClick={this.confirm}
                        class="modal-button uppercase"
                        content={'CONFIRM'} />
                </Form>
            }
        </SimpleModalTemplate>
        )
    }
}

export default ClassicAppModal;