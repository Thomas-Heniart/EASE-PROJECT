import React, {Component} from "react";
import {logoLetter, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {fetchWebsiteInfo, getClearbitLogo, getClearbitLogoAutoComplete} from "../../utils/api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {newSelectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite} from "../../actions/teamModalActions";
import {teamCreateEnterpriseCard, teamCreateAnyEnterpriseCard, teamCreateSoftwareEnterpriseCard} from "../../actions/appsActions";
import {connect} from "react-redux";
import {setUserDropdownText, PasswordChangeDropdownEnterprise} from "./common";
import { Header, Label, Container, Icon, Transition, Segment, Input, Dropdown, Button, Popup } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {deleteUpdate, testCredentials} from "../../actions/catalogActions";
import {updateAccepted} from "../../actions/dashboardActions";

const CredentialInput = ({item, onChange, removeField, receiver_id, readOnly, isMe, classic, first, testConnection}) => {
  return (
      <div style={{position: 'relative'}}>
        {first &&
        <Icon name='circle' style={{position:'absolute',bottom:'27px',left:'199px',zIndex:'1',color:'white',margin:'0'}} />}
        {first &&
        <Icon onClick={e => removeField(item)} name='remove circle' style={{cursor:'pointer',position:'absolute',bottom:'27px',left:'199px',zIndex:'1',color:'#e0e1e2',margin:'0'}} />}
        <Input size="mini"
               id={item.priority}
               autoFocus={item.autoFocus && isMe}
               readOnly={readOnly}
               class="team-app-input"
               required={isMe}
               autoComplete='on'
               name={item.name}
               onChange={(e, data) => {onChange(receiver_id, data)}}
               label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
               labelPosition="left"
               placeholder={isMe ? item.placeholder : `${item.placeholder} (Optional)`}
               value={item.value}
               type={item.type}/>
        {(isMe && item.name === 'password' && classic) &&
        <Popup
          inverted
          trigger={
            <p
              className='underline_hover test_connection'
              onClick={e => testConnection(receiver_id)}>
              <Icon name='magic'/>Test this password
            </p>}
          content='We will open a new tab to test if the password works or not.'/>}
      </div>
  )
};

const OtherInput = ({item, onChange, removeField, receiver_id, readOnly, isMe, first}) => {
  return (
      <div style={{position: 'relative'}}>
        {first &&
        <Icon name='circle' style={{position:'absolute',bottom:'27px',left:'199px',zIndex:'1',color:'white',margin:'0'}} />}
        {first &&
        <Icon onClick={e => removeField(item)} name='remove circle' style={{cursor:'pointer',position:'absolute',bottom:'27px',left:'199px',zIndex:'1',color:'#e0e1e2',margin:'0'}} />}
        <Input size="mini"
               id={item.priority}
               autoFocus={item.autoFocus}
               readOnly={readOnly}
               class="team-app-input"
               required={isMe}
               autoComplete='on'
               name={item.name}
               onChange={(e, data) => {onChange(receiver_id, data)}}
               label={<Label><Icon name={'wait'}/></Label>}
               labelPosition="left"
               placeholder={isMe ? 'New field' : 'New field (Optional)'}
               value={item.value}
               type={item.type}/>
      </div>
  )
};

const TeamAppCredentialInput = ({item, onChange, receiver_id, readOnly, isMe}) => {
  return <Input size="mini"
                class="team-app-input"
                readOnly={readOnly}
                name={item.name}
                required={isMe}
                onChange={(e, data) => {onChange(receiver_id, data)}}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={isMe ? item.placeholder : `${item.placeholder} (Optional)`}
                value={item.value}
                type={item.type}/>;
};

const ExtendedReceiverCredentialsInput = ({receiver, onChange, onDelete, readOnly, isMe, addFields, removeField, classic, first, testConnection}) => {
  return (
      <div class="receiver" style={isMe && classic ? {marginBottom:'27px'} : null}>
        <Label class="receiver-label">
          <span>{receiver.username}</span> <Icon name="delete" link onClick={onDelete.bind(null, receiver.id)}/>
        </Label>
        {
          receiver.credentials.map(item => {
            if (item.name !== 'login' && item.name !== 'password' && !item.placeholderIcon)
              return <OtherInput
                readOnly={readOnly}
                receiver_id={receiver.id}
                isMe={isMe}
                key={item.priority}
                onChange={onChange}
                item={item}
                removeField={removeField}
                first={first}/>;
            else
              return <CredentialInput
                readOnly={readOnly}
                receiver_id={receiver.id}
                isMe={isMe}
                key={item.priority}
                onChange={onChange}
                item={item}
                removeField={removeField}
                classic={classic}
                first={first}
                testConnection={testConnection}/>;
          })
        }
        {first &&
        <span onClick={addFields} className={'add_field'} style={{fontSize:'14px',marginTop:'8px'}}><Icon name='plus circle'/>Add a field</span>}
      </div>
  )
};

const Receivers = ({receivers, onChange, onDelete, myId, addFields, removeField, classic, testConnection}) => {
  return (
      <div class="receivers">
        {receivers.map((item, idx) => {
          return <ExtendedReceiverCredentialsInput
              key={item.id}
              isMe={item.id === myId}
              receiver={item}
              onChange={onChange}
              onDelete={onDelete}
              addFields={addFields}
              removeField={removeField}
              classic={classic}
              testConnection={testConnection}
              first={idx === 0 && !classic}/>;
        })}
      </div>
  )
};

@connect(store => ({
  team_id: store.teamCard.team_id,
  teams: store.teams,
  card: store.teamCard,
  updates: store.catalog.updates
}), reduxActionBinder)
class EnterpriseTeamAppAdder extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      app: this.props.card.app,
      app_url: this.props.card.app.url ? this.props.card.app.url : this.props.card.url,
      app_name: this.props.card.name,
      password_reminder_interval: 0,
      description: '',
      users: [],
      selected_users: [],
      fill_in_switch: false,
      img_url: this.props.card.app.logo,
      subtype: this.props.card.subtype,
      priority: 2
    }
  }
  handleInput = handleSemanticInput.bind(this);
  onDeleteReceiver = (id) => {
    const selected_users = this.state.selected_users.filter(item => (item !== id));
    this.setState({selected_users: selected_users});
  };
  handleReceiverInput = (userId, {id, value}) => {
    const users = this.state.users.map(user => {
      if (user.id === userId){
        user.credentials.map(item => {
          if (item.priority === id)
            item.value = value;
          return item;
        })
      }
      return user;
    });
    this.setState({users: users});
  };
  addFields = () => {
    const users = this.state.users.map(user => {
      let inputs = user.credentials.slice();
      const newInput = {name:`field${this.state.priority + 1}`,placeholder:`Field ${this.state.priority + 1}`,priority:this.state.priority,type:"text",value:""};
      inputs.push(newInput);
      user.credentials = inputs;
      return user;
    });
    this.setState({users: users, priority: this.state.priority + 1});
  };
  removeField = (field) => {
    const users = this.state.users.map(user => {
      const inputs = user.credentials.filter(item => {
        return item.priority !== field.priority;
      }).map((item, idx) => {
        item.priority = idx;
        return item
      });
      user.credentials = inputs;
      return user;
    });
    this.setState({users: users});
  };
  getLogo = () => {
    if (this.props.card.subtype === 'AnyApp')
      getClearbitLogo(this.state.app_url).then(response => {
        this.setState({img_url: response});
      }).catch(err => {
        this.setState({img_url: ''});
      });
    else if (this.props.card.subtype === 'softwareApp') {
      const name = this.state.app_name.replace(/\s+/g, '').toLowerCase();
      if (name === '')
        this.setState({img_url: ''});
      else
        getClearbitLogoAutoComplete(name).then(response => {
          this.setState({img_url: response});
        }).catch(err => {
          this.setState({img_url: ''});
        });
    }
  };
  changeUrl = (e, {value}) => {
    this.setState({app_url: value}, this.getLogo);
  };
  handleInputName = (e, {value}) => {
    this.setState({app_name: value}, this.getLogo);
  };
  imgNone = (e) => {
    e.preventDefault();
    this.setState({img_url:''});
  };
  chooseAllUsers = () => {
    let selected = [];
    this.state.users.map(user => {
      if (selected.length) {
        selected.splice(selected.length + 1, 0, user.id);
      }
      else {
        selected.splice(0, 0, user.id);
      }
    });
    this.setState({ selected_users: selected });
  };
  setApp = (app) => {
    if (app.request){
      requestWebsite(this.props.dispatch).then(app => {
        this.setUsers(app);
        this.setState({app: app});
      }).catch(err => ({}));
      return;
    }
    fetchWebsiteInfo(app.id).then(app => {
      this.setUsers(app);
      this.setState({app: app});
    });
  };
  componentWillMount(){
    const meId = this.props.teams[this.props.card.team_id].my_team_user_id;
    let users = this.props.item.team_user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        credentials: item === meId && this.props.card.account_information !== -1 ?
          transformWebsiteInfoIntoListAndSetValues(this.props.card.app.information, this.props.card.account_information)
          : transformWebsiteInfoIntoList(this.props.card.app.information),
        username: user.username
      }
    });
    this.setState({users: users});
  };
  componentDidMount(){
    this.chooseAllUsers();
  };
  setUsers = (app) => {
    const meId = this.props.teams[this.props.card.team_id].my_team_user_id;
    let users = this.props.item.user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        credentials: item === meId && this.props.card.account_information !== -1 ?
          transformWebsiteInfoIntoListAndSetValues(app.information, this.props.card.account_information)
          : transformWebsiteInfoIntoList(app.information),
        username: user.username
      }
    });
    this.setState({users: users});
  };
  testConnection = (user_id) => {
    let credentials = [];
    this.state.users.map(user => {
      if (user.id === user_id)
        credentials = user.credentials;
    });
    this.props.dispatch(testCredentials({
      account_information: transformCredentialsListIntoObject(credentials),
      website_id: this.state.app.id
    }));
  };
  finish = () => {
    if (this.props.card.app.update_id) {
      this.props.dispatch(updateAccepted({
        type: this.props.updates.find((update) => update.id === this.props.card.app.update_id).type
      }));
      this.props.dispatch(deleteUpdate({id: this.props.card.app.update_id})).then(() => {
        this.setState({loading: false});
        this.close();
        this.props.resetTeamCard();
      });
    }
    else {
      this.setState({loading: false});
      this.close();
      this.props.resetTeamCard();
    }
  };
  send = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const receivers = this.state.users
        .filter(item => (this.state.selected_users.indexOf(item.id) !== -1))
        .map(item => ({
          [item.id]: {account_information: transformCredentialsListIntoObject(item.credentials)}
        }));
    const newReceivers = receivers.reduce(function (result, item) {
      result = Object.assign(result, item);
      return result;
    }, {});
    const connection_information = this.state.users[0].credentials.reduce((prev, curr) =>{
      return {...prev, [curr.name]: {type:curr.type,priority:curr.priority,placeholder:curr.placeholder}}
    }, {});
    if (this.props.card.subtype === 'AnyApp')
      this.props.dispatch(teamCreateAnyEnterpriseCard({
        team_id: this.props.team_id,
        channel_id: this.props.item.id,
        name: this.state.app_name,
        description: this.state.description,
        password_reminder_interval: this.state.password_reminder_interval,
        url: this.state.app_url,
        img_url: this.state.img_url,
        connection_information: connection_information,
        receivers: newReceivers
      })).then(response => {
        this.props.dispatch(cardAdded({
          card: response
        }));
        this.finish();
      });
    else if (this.props.card.subtype === 'softwareApp')
      this.props.dispatch(teamCreateSoftwareEnterpriseCard({
        team_id: this.props.team_id,
        channel_id: this.props.item.id,
        name: this.state.app_name,
        description: this.state.description,
        password_reminder_interval: this.state.password_reminder_interval,
        logo_url: this.state.img_url,
        connection_information: connection_information,
        receivers: newReceivers
      })).then(response => {
        this.props.dispatch(cardAdded({
          card: response
        }));
        this.finish();
      });
    else
      this.props.dispatch(teamCreateEnterpriseCard({
        team_id: this.props.team_id,
        channel_id: this.props.item.id,
        website_id: this.state.app.id,
        name: this.state.app_name,
        description: this.state.description,
        password_reminder_interval: this.state.password_reminder_interval,
        receivers: newReceivers
      })).then(response => {
        this.props.dispatch(cardAdded({
          card: response
        }));
        this.finish();
      });
  };
  close = () => {
    this.props.resetTeamCard();
  };
  render(){
    const app = this.state.app;
    const selected_users = this.state.users.filter(item => (this.state.selected_users.indexOf(item.id) !== -1));
    const team = this.props.teams[this.props.card.team_id];
    const room_manager = team.team_users[team.rooms[this.props.card.channel_id].room_manager_id];
    return (
        <Container fluid id='enterprise-app-adder' class="team-app team-app-adder mrgn0" as="form" onSubmit={this.send}>
          <Transition visible={app !== null} unmountOnHide={true} mountOnShow={true} animation='scale' duration={300}>
            {app !== null &&
            <div>
              <Segment>
                <Header as="h5">
                  <div className="display_flex margin_b5rem">
                    <div>
                      <Input className="team-app-input"
                             placeholder="Name your card"
                             name="app_name"
                             value={this.state.app_name}
                             autoComplete="off"
                             onChange={this.handleInputName}
                             size="mini"
                             label={<Label><Icon name="home"/></Label>}
                             labelPosition="left"
                             required/>
                    </div>
                  </div>
                </Header>
                <Button icon="delete" type="button" size="mini" class="close" onClick={this.close}/>
                <div class="display_flex">
                  <div class="logo_column">
                    <div class="logo">
                      {this.state.img_url ?
                          <div style={{backgroundImage:`url('${this.state.img_url}')`}}>
                            {(this.state.subtype === 'AnyApp' || this.state.subtype === 'softwareApp') &&
                            <button className="button-unstyle action_button close_button" onClick={this.imgNone}>
                              <Icon name="close" class="mrgn0" link/>
                            </button>}
                          </div>
                          : this.state.app_name ?
                              <div style={{backgroundColor:'#373b60',color:'white'}}>
                                <p style={{margin:'auto'}}>{logoLetter(this.state.app_name)}</p>
                              </div>
                              :
                              <div style={{backgroundColor:'white',color: '#dededf'}}>
                                <Icon name='wait' style={{margin:'auto'}}/>
                              </div>}
                    </div>
                  </div>
                  <div class="main_column">
                    <div class="credentials">
                      <div class="display-inline-flex align_items_center">
                        {this.props.card.subtype === 'AnyApp' &&
                        <Input className="team-app-input any_app"
                               placeholder="Website URL"
                               name="app_url"
                               value={this.state.app_url}
                               autoComplete="off"
                               onChange={this.changeUrl}
                               size="mini"
                               label={<Label><Icon name="home"/></Label>}
                               labelPosition="left"
                               required/>}
                        <PasswordChangeDropdownEnterprise
                          team={team}
                          dispatch={this.props.dispatch}
                          value={this.state.password_reminder_interval}
                          onChange={this.handleInput}
                          roomManager={room_manager.username}/>
                      </div>
                    </div>
                    <Receivers
                        myId={this.props.teams[this.props.card.team_id].my_team_user_id}
                        receivers={selected_users}
                        onDelete={this.onDeleteReceiver}
                        onChange={this.handleReceiverInput}
                        removeField={this.removeField}
                        addFields={this.addFields}
                        testConnection={this.testConnection}
                        classic={this.props.card.subtype === 'classic'}/>
                    <div>
                      {this.state.selected_users.length !== this.state.users.length &&
                      <Dropdown
                          class="mini users-dropdown"
                          search
                          fluid
                          name="selected_users"
                          options={this.state.users}
                          onChange={this.handleInput}
                          value={this.state.selected_users}
                          selection
                          multiple
                          noResultsMessage='No more results found'
                          placeholder="Tag your team members here..."/>}
                    </div>
                    <div>
                      <Input size="mini"
                             fluid
                             class="team-app-input"
                             onChange={this.handleInput}
                             name="description"
                             value={this.state.description}
                             placeholder="You can add a comment here"
                             type="text"
                             label={<Label><Icon name="sticky note"/></Label>}
                             labelPosition="left"/>
                    </div>
                  </div>
                </div>
              </Segment>
              <Button
                  icon="send"
                  content="Send"
                  loading={this.state.loading}
                  disabled={this.state.loading || this.state.users[0].credentials.length < 1}
                  floated="right"
                  class="mrgn0"
                  positive
                  size="mini"/>
            </div>}
          </Transition>
        </Container>
    )
  }
}

module.exports = EnterpriseTeamAppAdder;