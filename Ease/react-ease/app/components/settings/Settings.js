import React from 'react';
import { Grid, Menu, Header } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { Switch, Route } from 'react-router-dom';
import Preferences from './Preferences';
import PersonalInfo from './PersonalInfo';
import Password from './Password';
import Deactivation from './Deactivation';
import DoubleFactor from './DoubleFactor';
import { NavLink } from 'react-router-dom';

@connect(store => ({
    common: store.common
}), reduxActionBinder)
class Settings extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            query: ''
        }
    }
    componentWillMount() {
        document.title = "Personal Settings"
    }
    resetQuery = () => {
        this.setState({query: ''});
        // this.main_container.scrollTo(0,0);
    };
    render() {
        return (
            <div id="personal_settings">
                <Grid className='grid'>
                    <Grid.Column width={16} textAlign={'center'}>
                        <Header as='h2'>Personal Settings</Header>
                    </Grid.Column>
                    <Grid.Column width={5}>
                        <Menu pointing vertical fluid className='menu'>
                            <Menu.Item name='Preferences'
                                       as={NavLink}
                                       exact to={`/main/settings`}
                                       activeClassName='active'
                                       onClick={e => {this.props.location.pathname !== `/main/settings` && this.resetQuery()}}>
                                <Header as='h4'>
                                    Preferences
                                    <Header.Subheader>
                                        Homepage & background picture
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Personal info'
                                       index={2}
                                       as={NavLink} to={`/main/settings/personalInfo`}
                                       activeClassName="active"
                                       onClick={e => {this.props.location.pathname !== `/main/settings/personalInfo` && this.resetQuery()}}>
                                <Header as='h4'>
                                    Personal info
                                    <Header.Subheader>
                                        Username & email
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Password'
                                       index={3}
                                       as={NavLink} to={`/main/settings/password`}
                                       activeClassName="active"
                                       onClick={e => {this.props.location.pathname !== `/main/settings/password` && this.resetQuery()}}>
                                <Header as='h4'>
                                    Password
                                    <Header.Subheader>
                                        Password modification
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Account activation'
                                       index={4}
                                       as={NavLink} to={`/main/settings/deactivation`}
                                       activeClassName="active"
                                       onClick={e => {this.props.location.pathname !== `/main/settings/deactivation` && this.resetQuery()}}>
                                <Header as='h4'>
                                    Account activation
                                    <Header.Subheader>
                                        Deactivate account
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Double Factor Authentication'
                                       disabled>
                                <Header as='h4'>
                                    Double Factor Authentication <img src="/resources/images/soon_mobile.png" />
                                    <Header.Subheader>
                                        Securization of your account
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                        </Menu>
                    </Grid.Column>
                    <Grid.Column width={11}>
                        <Switch>
                            <Route exact path={`/main/settings`} component={Preferences} />
                            <Route path={`/main/settings/personalInfo`} render={(props) => <PersonalInfo userInfo={this.props.common.user} />} />
                            <Route path={`/main/settings/password`} component={Password} />
                            <Route path={`/main/settings/deactivation`} component={Deactivation} />
                        </Switch>
                    </Grid.Column>
                </Grid>
            </div>
        )
    }
}

export default Settings;