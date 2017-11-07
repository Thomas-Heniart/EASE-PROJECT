import React from 'react';
import { Input, Button, Grid, Segment, Menu, Header, Table, List } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { Switch, Route } from 'react-router-dom';
import Preferences from './Preferences';
import PersonalInfo from './PersonalInfo';
import Password from './Password';
import Deactivation from './Deactivation';
import DoubleFactor from './DoubleFactor';

@connect(store => ({
    common: store.common
}), reduxActionBinder)
class Settings extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            view: 1
        }
    }

    handleItemClick = (e, { index }) => this.setState({ view: index });

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
                                       index={1}
                                       active={this.state.view === 1}
                                       onClick={this.handleItemClick}>
                                <Header as='h4'>
                                    Preferences
                                    <Header.Subheader>
                                        Homepage & background picture
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Personal info'
                                       index={2}
                                       active={this.state.view === 2}
                                       onClick={this.handleItemClick}>
                                <Header as='h4'>
                                    Personal info
                                    <Header.Subheader>
                                        Username & email
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Password'
                                       index={3}
                                       active={this.state.view === 3}
                                       onClick={this.handleItemClick}>
                                <Header as='h4'>
                                    Password
                                    <Header.Subheader>
                                        Password modification
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Account activation'
                                       index={4}
                                       active={this.state.view === 4}
                                       onClick={this.handleItemClick}>
                                <Header as='h4'>
                                    Account activation
                                    <Header.Subheader>
                                        Deactivate account
                                    </Header.Subheader>
                                </Header>
                            </Menu.Item>
                            <Menu.Item name='Double Factor Authentication'
                                       disabled
                                       index={5}
                                       active={this.state.view === 5}>
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
                        {this.state.view === 1 ?
                                <Preferences />
                            : this.state.view === 2 ?
                                <PersonalInfo userInfo={this.props.common.user}/>
                            : this.state.view === 3 ?
                                <Password />
                            :
                                    <Deactivation />
                        }
                    </Grid.Column>
                </Grid>
            </div>
        )
    }
}

export default Settings;