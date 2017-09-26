import React, {Component} from "react";
import classnames from "classnames";
import api from '../../utils/api';
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {getClearbitLogo} from "../../utils/api";
import {setUserDropdownText,
    PinAppButton,
    TeamAppActionButton} from "./common";
import * as modalActions from "../../actions/teamModalActions";
import {teamEditLinkAppNew} from "../../actions/appsActions";
import {handleSemanticInput,
    reflect
} from "../../utils/utils";
import {selectItemFromListById,
    findMeInReceivers,
    getReceiverInList,
    sortReceiversAndMap,
    isAdmin} from "../../utils/helperFunctions";
import {connect} from "react-redux";

const TeamLinkAppButtonSet = ({app, me, dispatch, editMode}) => {
    return (
        <div class="team_app_actions_holder">
            {isAdmin(me.role) &&
            <TeamAppActionButton text='Edit App' icon='pencil' onClick={editMode}/>}
            {isAdmin(me.role) &&
            <TeamAppActionButton text='Delete App' icon='trash outline' onClick={e => {dispatch(modalActions.showTeamDeleteAppModal(true, app))}}/>}
        </div>
    )
};

const TeamAppReceiverLabel = ({username}) => {
    return (
        <Popup size="mini"
               position="bottom center"
               inverted
               flowing
               hideOnScroll={true}
               trigger={
                   <Label class={classnames("user-label static accepted")}>
                       {username}
                   </Label>
               }
               header={<h5 class="mrgn0 text-center">User informations</h5>}
               content={
                   <div>
                       <span><Icon name='circle' style={{color: '#949eb7'}}/> User pinned the app</span>
                   </div>}/>
    )
};

class ReceiversLabelGroup extends Component {
    constructor(props){
        super(props);
        this.state = {
            show_all: false
        }
    }
    showAll = (state) => {
        this.setState({show_all: state});
    };
    render() {
        const receivers = this.props.receivers;
        return (
            <Label.Group>
                {this.props.receivers.map((item, idx) => {
                    if (!this.state.show_all && idx > 15)
                        return null;
                    const user = item.user;
                    const receiver = item.receiver;
                    if (item.receiver.profile_id !== -1)
                        return (
                            <TeamAppReceiverLabel key={receiver.team_user_id}
                                              username={user.username}
                                              can_see_information={receiver.can_see_information}
                                              accepted={receiver.accepted}/>
                        );
                })}
                {receivers.length > 15 && !this.state.show_all &&
                <Button size="mini" type="button" class="label fw-normal" onClick={this.showAll.bind(null, true)}><Icon name="add user"/>
                    {receivers.length - 15}&nbsp;users
                </Button>}
                {receivers.length > 15 && this.state.show_all &&
                <Button size="mini" type="button"  class="label fw-normal" onClick={this.showAll.bind(null, false)}><Icon name="hide"/>Hide</Button>}
            </Label.Group>
        )
    }
};

class LinkTeamApp extends Component {
    constructor(props){
        super(props);
        this.state = {
            name: '',
            url: '',
            img_url: '',
            loading: false,
            edit: false,
            description: '',
            users: [],
            selected_users: []
        }
    }
    getLogo = () => {
        getClearbitLogo(this.state.url).then(response => {
            this.setState({img_url: response});
        });
    };
    changeUrl = (e, {value}) => {
        this.setState({url: value}, this.getLogo);
    };
    handleInput = handleSemanticInput.bind(this);
    modify = (e) => {
        e.preventDefault();
        this.setState({loading: true});
        this.props.dispatch(teamEditLinkAppNew({
            team_id: this.props.team_id,
            name: this.state.name,
            url: this.state.url,
            img_url: this.state.img_url,
            app_id: this.props.app.id,
            description: this.state.description,
        })).then(response => {
            this.setEdit(false);
        }).catch(err => {
            console.log(err);
        });
    };
    setEdit = (state) => {
        if (state){
            const app = this.props.app;
            this.setState({name: app.name, url: app.url, description: app.description, img_url: app.logo});
        }
        this.setState({edit: state, loading: false});
    };

    render(){
        const app = this.props.app;
        const me = this.props.me;
        const meReceiver = getReceiverInList(app.receivers, me.id);
        const userReceiversMap = sortReceiversAndMap(app.receivers, this.props.users, me.id);

        return (
            <Container fluid class="team-app mrgn0 simple-team-app" as="form" onSubmit={this.modify}>

                    <Segment>
                        <Header as="h4">
                            {!this.state.edit ?
                                app.name :
                                <Input size="mini"
                                       class="team-app-input"
                                       onChange={this.handleInput}
                                       name="name"
                                       readOnly={!this.state.edit}
                                       value={this.state.edit ? this.state.name : app.name}
                                       placeholder="Name your link"
                                       type="text"
                                       label={<Label><Icon name="home"/></Label>}
                                       labelPosition="left"
                                       required/>
                            }
                            {!this.state.edit &&
                            <PinAppButton is_pinned={meReceiver !== null && meReceiver.profile_id !== -1} onClick={e => {this.props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app))}}/>}
                        </Header>
                        {!this.state.edit &&
                        <TeamLinkAppButtonSet app={app}
                                                me={me}
                                                dispatch={this.props.dispatch}
                                                editMode={this.setEdit.bind(null, true)}/>}

                        <div class="display_flex">
                            <div class="logo_column">
                                <div class="logo">
                                    <img src={this.state.img_url ? this.state.img_url : app.logo}/>
                                </div>
                            </div>

                            <div class="main_column">
                                <div>
                                    <Input size="mini"
                                           class="team-app-input width48"
                                           onChange={this.changeUrl}
                                           name="url"
                                           readOnly={!this.state.edit}
                                           value={this.state.edit ? this.state.url : app.url}
                                           placeholder="Paste or type url"
                                           type="text"
                                           label={<Label><Icon name="linkify"/></Label>}
                                           labelPosition="left"
                                           required/>
                                </div>
                                    <ReceiversLabelGroup receivers={userReceiversMap}/>
                                <div>
                                    <Input size="mini"
                                           fluid
                                           class="team-app-input"
                                           onChange={this.handleInput}
                                           name="description"
                                           readOnly={!this.state.edit}
                                           value={this.state.edit ? this.state.description : app.description}
                                           placeholder="What is this about? Any comment?"
                                           type="text"
                                           label={<Label><Icon name="sticky note"/></Label>}
                                           labelPosition="left"/>
                                </div>
                            </div>
                        </div>
                    </Segment>
                    {this.state.edit &&
                    <div>
                        <Button content="Save" floated="right" positive size="mini" loading={this.state.loading} disabled={this.state.loading}/>
                        <Button content="Cancel" type="button" floated="right" onClick={this.setEdit.bind(null, false)} size="mini"/>
                    </div>}
            </Container>
        )
    }
}

module.exports = LinkTeamApp;