import React, {Component} from "react";
import classnames from "classnames";
import {getClearbitLogo} from '../../utils/api';
import {Button, Container, Header, Icon, Input, Label, Popup, Segment, Dropdown} from 'semantic-ui-react';
import {PinAppButton,   renderLinkAppAddUserLabel, setUserDropdownText, TeamAppActionButton} from "./common";
import * as modalActions from "../../actions/teamModalActions";
import {teamEditLinkAppNew, teamShareLinkCard, removeTeamCardReceiver} from "../../actions/appsActions";
import {handleSemanticInput, reflect} from "../../utils/utils";
import {getReceiverInList, isAdmin, sortReceiversAndMap, selectItemFromListById,} from "../../utils/helperFunctions";

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
                 <Label class={classnames("user-label static pinned")}>
                   {username}
                   <Icon name='mobile'/>
                 </Label>
             }
             content={
                 <div>
                     <span>User pinned the app</span>
                     <br/>
                     <span>Mobile access: on</span>
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
  filterReceivers = (obj) => {
    if (obj.receiver.profile_id !== -1)
      return true;
    else
      return false;
  };
  render() {
    const receivers = this.props.receivers.filter(this.filterReceivers);
    return (
        <Label.Group>
          {receivers.map((item, idx) => {
            if (!this.state.show_all && idx > 15)
              return null;
            const user = item.user;
            const receiver = item.receiver;
            // if (item.receiver.profile_id !== -1)
            return (
                <TeamAppReceiverLabel username={user.username} key={receiver.team_user_id}/>
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
      team_card_id: this.props.app.id,
      name: this.state.name,
      url: this.state.url,
      img_url: this.state.img_url,
      description: this.state.description
    })).then(response => {
      const app = response;
      const receivers = app.receivers;
      let deleting = [];
      let edit = [];
      let sharing = [];
      this.state.users.map(item => {
        const selected = this.state.selected_users.indexOf(item.id) !== -1;
        const receiver = getReceiverInList(receivers, item.id);
        if (!selected && !!receiver)
          deleting.push(this.props.dispatch(removeTeamCardReceiver({
            team_id:this.props.app.team_id,
            team_card_id: this.props.app.id,
            team_card_receiver_id: receiver.id})));
        if (!receiver && selected)
          sharing.push(this.props.dispatch(teamShareLinkCard({
            team_id: this.props.app.team_id,
            team_card_id: app.id,
            team_user_id: item.id,
            allowed_to_see_password: item.can_see_information})));
      });
      const calls = deleting.concat(sharing, edit);
      Promise.all(calls.map(reflect)).then(response => {
        this.setEdit(false);
      });
    }).catch(err => {
      console.log(err);
    });
  };
  setupUsers = () => {
    const channel = selectItemFromListById(this.props.channels, this.props.app.channel_id);
    let selected_users = [];
    const users = channel.team_user_ids.map(item => {
      const user = selectItemFromListById(this.props.users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        username: user.username,
        user: user,
        can_see_information: true
      }
    }).sort((a, b) => {
      if (a.id === this.props.me.id)
        return -1000;
      else if (b.id === this.props.me.id)
        return 1000;
      return a.username.localeCompare(b.username);
    }).map(item => {
      const receiver = getReceiverInList(this.props.app.receivers, item.id);
      const can_see_information = receiver !== null ? receiver.allowed_to_see_password : false;
      if (receiver !== null)
        selected_users.push(item.id);
      return {
        ...item,
        can_see_information: can_see_information,
        receiver: receiver
      }
    });
    this.setState({users: users, selected_users:selected_users});
  };
  setEdit = (state) => {
    if (state){
      const app = this.props.app;
      this.setupUsers();
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
        <Container fluid id={`app_${app.id}`} class="team-app mrgn0 simple-team-app" as="form"
                   onSubmit={this.modify}>

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
                        {!this.state.edit ?
                          <ReceiversLabelGroup receivers={userReceiversMap}/> :
                          <Dropdown
                            class="mini"
                            search={true}
                            fluid
                            name="selected_users"
                            options={this.state.users}
                            onChange={this.handleInput}
                            value={this.state.selected_users}
                            selection={true}
                            renderLabel={renderLinkAppAddUserLabel}
                            multiple
                            noResultsMessage='No more results found'
                            placeholder="Tag your team members here..."/>}
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