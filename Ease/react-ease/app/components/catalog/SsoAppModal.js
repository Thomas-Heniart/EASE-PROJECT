import React from 'react';
import { Image, Icon, Form, Button, Message, Checkbox, Segment, Input, Container, List, Grid } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import InputModalCatalog from './InputModalCatalog';
import {selectItemFromListById} from "../../utils/helperFunctions";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import {dashboard} from "../../utils/post_api";
var api = require('../../utils/api');
import {reflect} from "../../utils/utils";

@connect(store => ({
    catalog: store.catalog,
    modal: store.teamModals.catalogAddSSOAppModal
}), reduxActionBinder)
class SsoAppModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            name: this.props.modal.website.name,
            errorMessage: '',
            url: this.props.modal.website.landing_url,
            login: '',
            password: '',
            loading: false,
            addBookmark: false,
            addGoogleAccount: false,
            accountGoogleSelected: '',
            profiles: [],
            profileName: '',
            selectedProfile: -1,
            addingProfile: false,
            view: 1,
            multipleApps: [],
            ssoSelected: [],
            editSSOName: [],
            websiteIdOfLoginSelected: null,
            appIdOfLoginSelected: null,
            logWith_websites: []
        }
    }

    componentWillMount(){
        api.dashboard.fetchProfiles().then(profiles => {
            this.setState({ profiles: profiles });
            const ssoList = this.props.catalog.websites.filter(item => {
                return item.sso_id === this.props.modal.website.sso_id;
            });
            let ssoWebsiteId = [];
            ssoList.map(item => {
                 ssoWebsiteId.push(item.id);
            });
            let logwith = ssoWebsiteId.map(item => {
                let website = selectItemFromListById(this.props.catalog.websites, item);
                let apps = [];
                profiles.map(item => {
                    item.apps.map(app => {
                        if (app.website_id === website.id)
                            apps.push(app);
                    });
                });
                website.personal_apps = apps;
                return website;
            });
            this.setState({logWith_websites: logwith});
        });
    }

    handleInput = (e, { name, value }) => this.setState({ [name]: value });

    toggleBookmark = () => {
        if (this.state.addBookmark)
            this.setState({ addBookmark: false, url: '', errorMessage: '' });
        else
            this.setState({ addBookmark: true, login: '', password: '', accountGoogleSelected: '', errorMessage: '' });
    };

    back = () => {
        if (this.checkAccountGoogle() === true)
            this.setState({ accountGoogleSelected: '', view: 2, login: '', password: '', addGoogleAccount: false, errorMessage: '', ssoSelected: [{ website_id: this.props.modal.website.id, name: this.state.name }] });
        else
            this.setState({ accountGoogleSelected: '', view: 2, login: '', password: '', addGoogleAccount: true, errorMessage: '', ssoSelected: [{ website_id: this.props.modal.website.id, name: this.state.name }] });
    };

    selectAccount = (account, website_id, app_id) => {
        this.setState({ accountGoogleSelected: account, view: 3, login: account, websiteIdOfLoginSelected: website_id, appIdOfLoginSelected: app_id});
    };

    addGoogleAccount = () => {
      this.setState({ addGoogleAccount: true, accountGoogleSelected: '' });
    };

    selectProfile = (id) => {
        this.setState({ selectedProfile: id });
    };

    checkActive = (id) => {
        let check = false;
        this.state.ssoSelected.filter(item => {
            if (item.website_id  === id)
                return check = true;
        });
        if (check)
            return true;
        else
            return false
    };

    addMainAppToSSOSelected = () => {
        const newSelectedSSO = this.state.ssoSelected.slice();
        newSelectedSSO.push({ website_id: this.props.modal.website.id, name: this.state.name });
        if (this.checkAccountGoogle() === true)
            this.setState({ ssoSelected: newSelectedSSO, view: 2 });
        else
            this.setState({ ssoSelected: newSelectedSSO, view: 2, addGoogleAccount: true });
    };

    editSSO = (e, sso) => {
        const newSelectedSSO = this.state.ssoSelected.slice();
        let key = 0;
        this.state.ssoSelected.filter((item, keyItem) => {
            if (item.website_id  === sso.id)
                return key = keyItem;
        });
        newSelectedSSO.splice(key, 1);
        newSelectedSSO.push({website_id: sso.id, name: e.target.value});
        this.setState({ ssoSelected: newSelectedSSO });
    };

    checkNameSSO = (item) => {
        let name = null;
        this.state.ssoSelected.filter(sso => {
            if (sso.website_id  === item.id) {
                return name = sso.name;
            }
        });
        return name;
    };

    selectSSO = (id, name) => {
        const newSelectedSSO = this.state.ssoSelected.slice();
        if (!this.checkActive(id)) {
            newSelectedSSO.push({website_id: id, name: name});
            this.setState({ ssoSelected: newSelectedSSO });
        }
        // else {
        //     let key = 0;
        //     this.state.ssoSelected.filter((item, keyItem) => {
        //         if (item.website_id  === id)
        //             return key = keyItem;
        //     });
        //     newSelectedSSO.splice(key, 1);
        //     this.setState({ ssoSelected: newSelectedSSO});
        // }
    };

    deselectSSO = (id) => {
        const newSelectedSSO = this.state.ssoSelected.slice();
        if (this.checkActive(id)) {
            let key = 0;
            this.state.ssoSelected.filter((item, keyItem) => {
                if (item.website_id  === id)
                    return key = keyItem;
            });
            newSelectedSSO.splice(key, 1);
            this.setState({ ssoSelected: newSelectedSSO});
        }
    };

    checkEditNameSSO = (item) => {
        let name = null;
        this.state.editSSOName.filter(sso => {
            if (sso.id === item.id)
                return name = sso.name;
        });
        return name;
    };

    selectEditNameSSO = (sso) => {
        // let newEditSSOName = this.state.editSSOName.slice();
        let newEditSSOName = [{id: sso.id, name: sso.name}];
        if (!this.checkEditNameSSO(sso)) {
            // newEditSSOName.push({id: sso.id, name: sso.name});
            this.setState({ editSSOName: newEditSSOName });
        }
        else {
            // let key = 0;
            // this.state.editSSOName.filter((item, keyItem) => {
            //     if (item.id  === sso.id)
            //         return key = keyItem;
            // });
            // newEditSSOName.splice(key, 1);
            this.setState({ editSSOName: [] });
        }
    };

    checkDouble = (double, login) => {
        const doubleLogin = double.filter(account => {
            if (login === account)
                return true;
        });
        if (doubleLogin.length) {
            return false;
        }
        else {
            return true;
        }
    };

    checkNameEmpty = () => {
        const empty = this.state.ssoSelected.map(item => {
           if(item.name === '')
               return item;
        });
        const response = empty.filter(item => {
            if (item)
                return true
        });
        if (response.length)
            return false;
        else
            return true;
    };

    checkAccountGoogle = () => {
        const account = this.state.logWith_websites.map(item => {
            return item.personal_apps.map(key => {
                if (key.account_information) {
                    return key;
                }
            });
        });
        const response = account.filter(item => {
            if (item.length)
                return true
        });
        if (response.length)
            return true;
        else
            return false;
    };

    confirm = () => {
        if (this.state.view === 1) {
            this.addMainAppToSSOSelected();
        }
        else if (this.state.addGoogleAccount) {
            this.setState({ accountGoogleSelected: this.state.login, view: 3, addGoogleAccount: false });
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
                    this.props.showCatalogAddSSOAppModal({active: false})
                }).catch(err => {
                    this.setState({loading: false});
                    this.setState({errorMessage: err});
                });
            }
            else {
                if (this.state.appIdOfLoginSelected === null) {
                    this.props.catalogAddMultipleClassicApp({
                        profile_id: this.state.selectedProfile,
                        apps_to_add: this.state.ssoSelected,
                        account_information: {login: this.state.login, password: this.state.password}
                    }).then(r => {
                        this.setState({loading: false});
                        this.props.showCatalogAddSSOAppModal({active: false})
                    }).catch(err => {
                        this.setState({loading: false});
                        this.setState({errorMessage: err});
                    });
                }
                else {
                    const calls = this.state.ssoSelected.map(item => {
                        if (this.props.modal.website.id !== item.website_id)
                            return this.props.catalogAddClassicAppSameAs({
                                website_id: item.website_id,
                                name: item.name,
                                same_app_id: this.state.appIdOfLoginSelected,
                                profile_id: this.state.selectedProfile
                            });
                    });
                    const response = calls.filter(item => {
                                        if (item)
                                            return true
                                    });
                    Promise.all(response.map(reflect)).then(r => {
                        this.setState({loading: false});
                        this.props.showCatalogAddSSOAppModal({active: false})
                    }).catch(err => {
                        this.setState({loading: false});
                        this.setState({errorMessage: err});
                    });
                }
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
        const ssoList = this.props.catalog.websites.filter(item => {
            if (item.id !== this.props.modal.website.id)
                return item.sso_id === this.props.modal.website.sso_id;
        });

        let double = [];

        return (
            <SimpleModalTemplate
                onClose={e => {
                    this.props.showCatalogAddSSOAppModal({active: false})
                }}
                headerContent={'Setup your App'}>
                {this.state.view === 1 ?
                    <Form class="container" id="add_bookmark_form" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
                        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
                            <div class="squared_image_handler">
                                <img src={this.props.modal.website.logo} alt="Website logo"/>
                            </div>
                            <span class="app_name">
                                <Input size="mini" type="text" placeholder="App name..."
                                       name="name"
                                       class="input_unstyle modal_input name_input"
                                       autoFocus={true}
                                       value={this.state.name}
                                       onChange={this.handleInput} />
                            </span>
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
                            type="submit"
                            loading={this.state.loading}
                            positive
                            disabled={this.state.selectedProfile === -1 || this.state.loading || !this.state.name}
                            className="modal-button"
                            content="NEXT"/>
                    </Form>
                    :  this.state.view === 2 ?
                <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
                    <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
                        <div className="squared_image_handler">
                            <Image src={this.props.modal.website.logo}  alt="Website logo"/>
                        </div>
                        <span className='app_name'>{this.state.name}</span>
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
                                            {this.state.logWith_websites.map(item => {
                                                return item.personal_apps.map(key => {
                                                    if (key.account_information) {
                                                        if (this.checkDouble(double, key.account_information.login) === true) {
                                                            double.push(key.account_information.login);
                                                            return (
                                                                <List.Item key={key.id} as="p"
                                                                           className="overflow-ellipsis">
                                                                    <a onClick={e => this.selectAccount(key.account_information.login, key.website_id, key.id)}>
                                                                        <Icon name='user circle'/>
                                                                        <span>{key.account_information.login}</span>
                                                                    </a>
                                                                </List.Item>
                                                            )
                                                        }
                                                    }
                                                })
                                            })}
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
                                    valueInput={!this.state.url ? this.props.modal.website.landing_url : this.state.url} />
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
                            type="submit"
                            positive
                            loading={this.state.loading}
                            class="modal-button uppercase"
                            content={'CONFIRM'} />
                </Form>
                    :
                    <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
                        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
                            <div className="squared_image_handler">
                                <Image src={this.props.modal.website.logo}  alt="Website logo"/>
                            </div>
                            <span className='app_name'>{this.state.name}</span>
                        </Form.Field>
                        <Form.Field>
                            <p className='backPointer' onClick={this.back}><Icon name='arrow left'/>Back</p>
                        </Form.Field>
                        <Form.Field>
                            <Segment.Group className='connectWithGoogle'>
                                <Segment className='first overflow-ellipsis'>
                                    <Icon name='google'/>
                                    {this.state.accountGoogleSelected}
                                </Segment>
                            </Segment.Group>
                        </Form.Field>
                        <Form.Field>
                            <p>You can connect your Google account with other apps. <strong>Click to add them</strong>.</p>
                        </Form.Field>
                        <Form.Field>
                            <Segment className='pushable ssoListSegment'>
                                <Grid columns={2} className='ssoListGrid'>
                                    {this.state.logWith_websites.map((item) => {
                                        if (!item.personal_apps.filter(key => {if (key.account_information){if (key.account_information.login === this.state.login)return true}}).length && this.props.modal.website.id !== item.id) {
                                            return (
                                                <Grid.Column key={item.id} className="showSegment">
                                                    <List.Item as='a' active={this.checkActive(item.id)} onClick={e => this.selectSSO(item.id, item.name)}>
                                                        <div className='appLogo' onClick={e => this.deselectSSO(item.id)}>
                                                            <Image src={item.logo}/>
                                                            <Icon className='iconCheck' name="check"/>
                                                        </div>
                                                        <Input disabled={!this.checkActive(item.id)}
                                                               focus={this.checkActive(item.id)}
                                                               value={this.checkNameSSO(item) === null ? item.name : this.checkNameSSO(item)}
                                                               name="ssoAppName"
                                                               transparent
                                                               onChange={e => this.editSSO(e, item)}
                                                               class='create_profile_input'
                                                               placeholder='Change name of your App'/>
                                                        <Icon className='iconWrite' name="write"/>
                                                    </List.Item>
                                                </Grid.Column>)
                                        }
                                        else if (item.personal_apps.filter(key => {if (key.account_information){if (key.account_information.login === this.state.login)return true}}).length && this.props.modal.website.id !== item.id) {
                                            return (
                                                <Grid.Column key={item.id} className="showSegment">
                                                    <List.Item as='a' active>
                                                        <div className='appLogo'>
                                                            <Image src={item.logo}/>
                                                            <Icon className='iconCheck' name="check"/>
                                                        </div>
                                                        <Input disabled
                                                               value={item.name}
                                                               name="ssoAppName"
                                                               transparent
                                                               class="create_profile_input"
                                                               placeholder='Change name of your App'/>
                                                    </List.Item>
                                                </Grid.Column>)
                                        }
                                    })}
                                </Grid>
                            </Segment>
                        </Form.Field>
                        <Message error content={this.state.errorMessage} />
                        <Button
                            disabled={!this.state.addBookmark && (!this.state.login || !this.state.password) && !this.state.accountGoogleSelected || this.checkNameEmpty() === false}
                            type="submit"
                            positive
                            loading={this.state.loading}
                            class="modal-button uppercase"
                            content={'CONFIRM'} />
                    </Form>
                }
            </SimpleModalTemplate>
        )
    }
}

export default SsoAppModal;