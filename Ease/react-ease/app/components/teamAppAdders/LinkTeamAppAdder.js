import React, {Component} from "react";
import classnames from 'classnames';
import {getClearbitLogo, dashboardAndTeamAppSearch, fetchWebsiteInfo} from "../../utils/api";
import {handleSemanticInput,
    transformWebsiteInfoIntoList,
    passwordChangeOptions,
    credentialIconType} from "../../utils/utils";
import {selectUserFromListById} from "../../utils/helperFunctions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
import * as appActions from "../../actions/appsActions";
import {teamCreateLinkAppNew} from "../../actions/appsActions";
import {requestWebsite} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import { Header, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

@connect(store => ({
    team_id: store.team.id,
    users: store.users.users,
    selectedItem: store.selection
}))
class LinkTeamAppAdder extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            appName: '',
            url: '',
            logoSrc: '/resources/icons/link_app.png',
            img_url: '',
            comment: '',
            selectedUsers: [],
            loading: false,
            users: [],
        };
        this.handleAppNameChange = this.handleAppNameChange.bind(this);
        this.handleUrlInput= this.handleUrlInput.bind(this);
        this.handleUserSelect = this.handleUserSelect.bind(this);
        this.handleUserDeselect = this.handleUserDeselect.bind(this);
        this.handleComment = this.handleComment.bind(this);
    }
    componentDidMount(){
        if (this.props.selectedItem.type === 'channel'){
            this.props.item.userIds.map(function(item){
                var user = selectUserFromListById.bind(this.props.users, item);
                user.selected = false;
                user.removable = true;
                this.state.users.push(user);
            }, this);
        } else {
            var item = this.props.item;
            item.selected = false;
            item.removable = false;
            this.state.users.push(item);
            this.handleUserSelect(item.id);
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
    send = (e) => {
        e.preventDefault();
        this.setState({loading: true});
        this.props.dispatch(teamCreateLinkAppNew({
            team_id: this.props.team_id,
            channel_id: this.props.item.id,
            name: this.state.appName,
            description: this.state.comment,
            url: this.state.url,
            img_url: this.state.img_url
        })).then(response => {
            this.setState({loading: false});
            this.close();
        });
    };
    close = () => {
        this.props.dispatch(closeAppAddUI());
    };
    handleComment(event){
        this.setState({comment: event.target.value});
    }
    handleAppNameChange(event){
        this.setState({appName: event.target.value});
    }
    handleUrlInput(event){
        this.setState({url: event.target.value});
    }
    handleUserDeselect(id){
        var users = this.state.users;
        var selectedUsers = this.state.selectedUsers;
        for (var i = 0; i < users.length; i++){
            if (users[i].id === id){
                if (users[i].selected){
                    users[i].selected = false;
                    selectedUsers.splice(selectedUsers.indexOf(users[i]), 1);
                }
                break;
            }
        }
        this.setState({users: users, selectedUsers: selectedUsers});
    }
    handleUserSelect(id){
        var users = this.state.users;
        var selectedUsers = this.state.selectedUsers;
        for (var i = 0; i < users.length; i++){
            if (users[i].id === id){
                if (!users[i].selected){
                    users[i].selected = true;
                    selectedUsers.push(users[i]);
                }
                break;
            }
        }
        this.setState({users: users, selectedUsers: selectedUsers});
    }
    render(){

        return (
            <Container fluid id="simple_team_app_add" class="team-app" as="form" onSubmit={this.send}>
                <Segment>
                    <Button icon="delete" style={{margin: '0 0 0 .6rem'}} size="mini" class="close" onClick={this.close} color="grey"/>
                    <div className="display_flex margin_b5rem">
                        <div>
                            <Input  className="team-app-input"
                                    placeholder="Name your link"
                                    name="app_name"
                                    value={this.state.appName}
                                    autoComplete="off"
                                    onChange={this.handleAppNameChange}
                                    size="mini"
                                    label={<Label><Icon name="home"/></Label>}
                                    labelPosition="left"
                                    required/>
                        </div>
                    </div>
                    <div class="display_flex">
                        <div class="logo_column">
                            <div class="logo">
                                <img src={this.state.img_url ? this.state.img_url : this.state.logoSrc} alt="website logo"/>
                            </div>
                        </div>
                        <div class="main_column">
                            <div class="display-inline-flex width48">
                                <Input placeholder="Paste or type url"
                                       className="team-app-input width100"
                                       autoComplete="off"
                                       type="text"
                                       name="url"
                                       value={this.state.url}
                                       onChange={this.changeUrl}
                                       size="mini"
                                       label={<Label><Icon name="linkify"/></Label>}
                                       labelPosition="left"
                                       required/>
                            </div>
                            <div>
                                <Input size="mini"
                                       fluid
                                       class="team-app-input"
                                       onChange={this.handleComment}
                                       name="comment"
                                       value={this.state.comment}
                                       placeholder="What is this about? Any comment?"
                                       type="text"
                                       label={<Label><Icon name="sticky note"/></Label>}
                                       labelPosition="left" />
                            </div>
                        </div>
                    </div>
                </Segment>
                <div>
                    <Button positive
                            size="mini"
                            floated="right"
                            loading={this.state.loading}
                            disabled={this.state.loading}>
                        <Icon name="send" />
                        Send
                    </Button>
                </div>
            </Container>
        )
    }
}

module.exports = LinkTeamAppAdder;