var React = require('react');
var classnames = require('classnames');
import * as teamModalActions from "../actions/teamModalActions"
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"
import {showTeamDeleteUserModal,
  showTeamDeleteChannelModal,
  showTeamDeleteUserFromChannelModal,
  showTeamTransferOwnershipModal}
  from "../actions/teamModalActions";
import {selectChannelFromListById,
  selectUserFromListById,
  isAdmin,
  isAdminOrMe,
  isSuperior,
  isSuperiorOrMe} from "../utils/helperFunctions";
import {renderUserLabel} from "../utils/renderHelpers";
import {teamUserRoleValues, userNameRuleString, handleSemanticInput, teamUserRoles, reflect} from "../utils/utils";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, TextArea, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
import {withRouter, Switch, Route} from "react-router-dom";
import {connect} from "react-redux";

function ChannelJoinRequestList(props){
  const users =  props.users;
  const requests = props.requests;

  return (
      <List>
        {requests.map(item => {
          const user = selectUserFromListById(users, item);
          return (
              <List.Item key={item}>
                <Label basic size="mini">
                  <Icon name="user"/>
                  {user.username} would like to access this group.&nbsp;
                  <a onClick={e => {props.dispatch(channelActions.addTeamUserToChannel(props.channel_id, item))}}>
                    accept</a>
                  &nbsp;or&nbsp;
                  <a onClick={e => {props.dispatch(channelActions.deleteJoinChannelRequest(props.channel_id, item))}}>
                    refuse</a> ?
                </Label>
              </List.Item>
          )
        })}
      </List>
  )
}

@connect(store => ({
  users: store.users.users
}))
class AddMemberToRoomDiv extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying: false,
      loading:false,
      options: [],
      value: []
    };
  }
  handleInput = handleSemanticInput.bind(this);
  validate = (e) => {
    e.preventDefault();
    const room = this.props.room;
    this.setState({loading: true});
    const calls = this.state.value.map(item => {
      return this.props.dispatch(channelActions.addTeamUserToChannel(room.id, item));
    });
    Promise.all(calls.map(reflect)).then(results => {
      this.setState({loading: false, modifying:false});
    });
  };
  setModifying = (state) => {
    if (state){
      const room = this.props.room;
      const options = this.props.users.filter(item => {
        return room.userIds.indexOf(item.id) === -1;
      }).map(item => {
        return {
          key: item.id,
          text: `${item.username} (${teamUserRoles[item.role]})`,
          value: item.id,
          username: item.username
        }
      });
      this.setState({modifying:true, options: options, loading: false, value : []});
      return;
    }
    this.setState({modifying: state});
  };
  render(){
    return (
        <div>
          {!this.state.modifying ?
              <Button primary size="mini"
                      onClick={this.setModifying.bind(null, true)}>
                <Icon name="add user"/>
                Add a member
              </Button>
              :
              <Form onSubmit={this.validate}>
                <Form.Field>
                  <Dropdown
                    search={true}
                    options={this.state.options}
                    value={this.state.value}
                    name="value"
                    class="mini"
                    onChange={this.handleInput}
                    fluid
                    selection={true}
                    multiple
                    renderLabel={renderUserLabel}
                    placeholder="Tag users here..."/>
                </Form.Field>
                <Form.Field>
                  <Button basic size="mini" type="button" onClick={this.setModifying.bind(null, false)}>Cancel</Button>
                  <Button primary size="mini" loading={this.state.loading}>Save</Button>
                </Form.Field>
              </Form>}
        </div>
    )
  }
}

class TeamChannelFlexTab extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      purposeModifying: false,
      modifiedPurpose: null,
      nameModifying: false,
      modifiedName: null
    };
    this.handlePurposeInput = this.handlePurposeInput.bind(this);
    this.setPurposeModifying = this.setPurposeModifying.bind(this);
    this.handleNameInput = this.handleNameInput.bind(this);
    this.setNameModifying = this.setNameModifying.bind(this);
    this.confirmNameChange = this.confirmNameChange.bind(this);
    this.confirmPurposeChange = this.confirmPurposeChange.bind(this);
  }
  confirmNameChange(){
    this.props.dispatch(channelActions.editTeamChannelName(this.props.item.id, this.state.modifiedName)).then(response => {
      this.setState({
        nameModifying: false
      });
    });
  }
  handleNameInput(event){
    this.setState({modifiedName: event.target.value})
  }
  setNameModifying(state){
    if (state){
      this.setState({
        nameModifying: state,
        modifiedName: this.props.item.name
      });
    } else {
      this.setState({
        nameModifying: state
      });
    }
  }
  confirmPurposeChange(){
    this.props.dispatch(channelActions.editTeamChannelPurpose(this.props.item.id, this.state.modifiedPurpose)).then(response => {
      this.setState({
        purposeModifying: false
      });
    });
  }
  handlePurposeInput(event){
    this.setState({modifiedPurpose: event.target.value})
  }
  setPurposeModifying(state){
    if (state){
      this.setState({
        purposeModifying: state,
        modifiedPurpose: this.props.item.purpose
      });
    } else {
      this.setState({
        purposeModifying: state
      });
    }
  }
  render() {
    const me = this.props.me;
    const channel = this.props.item;

    return (
        <div className="flex_contents_panel active" id="team_tab">
          <div className="tab_heading">
            <div className="heading_row">
            <span className="heading_text">
              Room information
            </span>
              <button className="button-unstyle button_close_flexpanel" onClick={this.props.toggleFlexFunc}>
                <i className="fa fa-times"/>
              </button>
            </div>
          </div>
          <div className="tab_content_body">
            <Grid container celled="internally" columns={1} padded>
              <Grid.Row>
                <Grid.Column>
                  {!this.state.nameModifying ?
                      <h4>{channel.name}
                        {isAdmin(me.role) && !channel.default &&
                        <button class="button-unstyle mrgnLeft5 action_button"
                                onClick={this.setNameModifying.bind(null, true)}>
                          <i class="fa fa-pencil mrgnLeft5"/>
                        </button>}
                      </h4>
                      :
                      <Form as="div">
                        <Form.Field>
                          <Input  label="Name"
                                  size="mini"
                                  type="text" name="name"
                                  fluid
                                  value={this.state.modifiedName}
                                  onChange={this.handleNameInput}/>
                        </Form.Field>
                        <Form.Field>
                          <Button basic size="mini" onClick={this.setNameModifying.bind(null, false)}>Cancel</Button>
                          <Button primary size="mini" onClick={this.confirmNameChange}>Save</Button>
                        </Form.Field>
                      </Form>
                  }
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <h5>Purpose</h5>
                  {!this.state.purposeModifying ?
                      <span>{this.props.item.purpose}
                        {isAdmin(me.role) &&
                        <button class="button-unstyle mrgnLeft5 action_button"
                                onClick={this.setPurposeModifying.bind(null, true)}>
                          <i class="fa fa-pencil mrgnLeft5"/>
                        </button>}
                  </span>
                      :
                      <Form as="div">
                        <Form.Field>
                          <TextArea style={{fontSize: '.8em'}}
                                    size="mini"
                                    type="text" name="name"
                                    value={this.state.modifiedPurpose}
                                    onChange={this.handlePurposeInput}/>
                        </Form.Field>
                        <Form.Field>
                          <Button basic size="mini" onClick={this.setPurposeModifying.bind(null, false)}>Cancel</Button>
                          <Button primary size="mini" onClick={this.confirmPurposeChange}>Save</Button>
                        </Form.Field>
                      </Form>
                  }
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column class="users">
                  <Header as="h5">
                    {isAdmin(me.role) && this.props.item.join_requests.length > 0 ?
                        <Label circular color="red" size="mini">{this.props.item.join_requests.length}</Label>: null}
                    Members :
                  </Header>
                    {this.props.item.userIds.map(function (item) {
                      const user = selectUserFromListById(this.props.users, item);
                      return (
                            <Label size="mini" key={item}>
                              <Icon name="user"/>
                              {user.username}
                              {(isSuperior(user, me) || user.id === me.id) && !channel.default &&
                              <Icon name="delete" link
                                    onClick={e => {
                                      this.props.dispatch(showTeamDeleteUserFromChannelModal(true, this.props.item.id, user.id))
                                    }}/>}
                            </Label>)}, this)}
                  {isAdmin(me.role) &&
                  <ChannelJoinRequestList
                      channel_id={this.props.item.id}
                      users={this.props.users}
                      requests={this.props.item.join_requests}
                      dispatch={this.props.dispatch}/>}
                  <div>
                    {isAdmin(me.role) && !channel.default &&
                    <AddMemberToRoomDiv room={channel}/>}
                  </div>
                </Grid.Column>
              </Grid.Row>
              {isAdmin(me.role) && !channel.default &&
              <Grid.Row>
                <Grid.Column>
                  <Button basic color="red" size="mini"
                          onClick={e => {this.props.dispatch(showTeamDeleteChannelModal(true, this.props.item.id))}}>
                    Delete this room
                  </Button>
                </Grid.Column>
              </Grid.Row>}
            </Grid>
          </div>
        </div>
    )
  }
}


class UsernameModifier extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      username: '',
      modifying: false,
      loading: false,
      error: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  setModifying  = (state) => {
    this.setState({username: this.props.user.username, modifying: state, error : false, loading: false});
  };
  validate = (e) => {
    e.preventDefault();
    const user = this.props.user;
    this.setState({loading: true, error: false});
    this.props.dispatch(userActions.editTeamUserUsername(user.id, this.state.username)).then(response => {
      this.setModifying(false);
    }).catch(err => {
      this.setState({error: true, loading: false});
    });
  };
  render(){
    const user = this.props.user;
    const me = this.props.me;
    return (
        <Grid.Column>
          {!this.state.modifying ?
              <div>
                @{user.username}
                {isSuperiorOrMe(user, me) &&
                <Icon link name="pencil" class="mrgnLeft5" onClick={this.setModifying.bind(null, true)}/>}
              </div> :
              <Form onSubmit={this.validate}>
                <Form.Field>
                  <Input
                      size="mini"
                      placeholder="Username"
                      type="text" name="username" fluid
                      value={this.state.username}
                      onChange={this.handleInput}/>
                  <Label pointing basic={this.state.error} size="mini" color={this.state.error ? 'red': null}>
                    {userNameRuleString}
                  </Label>
                </Form.Field>
                <Form.Field>
                  <Button basic size="mini" type="button" onClick={this.setModifying.bind(null, false)}>Cancel</Button>
                  <Button primary size="mini">Save</Button>
                </Form.Field>
              </Form>}
        </Grid.Column>
    )
  }
}

class TeamUserFlexTab extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      first_name: null,
      last_name: null,
      firstNameLastNameModifying: false,
      username: null,
      usernameModifying: false,
      role: null,
      roleModifying: false,
      departureDate:'',
      departureDateModifying: false
    };
    this.setFirstLastNameModifying = this.setFirstLastNameModifying.bind(this);
    this.setUsernameModifying = this.setUsernameModifying.bind(this);
    this.setRoleModifying = this.setRoleModifying.bind(this);
    this.setDepartureDateModifying = this.setDepartureDateModifying.bind(this);
    this.handleInput = this.handleInput.bind(this);
    this.confirmUsernameChange = this.confirmUsernameChange.bind(this);
    this.confirmUserLastFirstNameChange = this.confirmUserLastFirstNameChange.bind(this);
    this.confirmUserRoleChange = this.confirmUserRoleChange.bind(this);
    this.confirmUserDepartureDateChange = this.confirmUserDepartureDateChange.bind(this);
  }
  handleInput(e, {name, value}){
    this.setState({[name]: value});
  }
  setDepartureDateModifying(state){
    if (state){
      this.setState({
        departureDateModifying: true,
        role: this.props.item.departureDate
      });
    }else {
      this.setState({
        departureDateModifying: false
      });
    }
  }
  setRoleModifying(state){
    if (state){
      this.setState({
        roleModifying: true,
        role: this.props.item.role
      });
    }else {
      this.setState({
        roleModifying: false
      });
    }
  }
  setFirstLastNameModifying(state){
    if (state){
      this.setState({
        firstNameLastNameModifying: true,
        first_name: this.props.item.first_name,
        last_name: this.props.item.last_name
      });
    }else {
      this.setState({
        firstNameLastNameModifying: false
      });
    }
  }
  setUsernameModifying(state){
    if (state){
      this.setState({
        usernameModifying: true,
        username: this.props.item.username,
      });
    }else {
      this.setState({
        usernameModifying: false
      });
    }
  }
  confirmUsernameChange(){
    if (this.state.username !== this.props.item.username){
      this.props.dispatch(userActions.editTeamUserUsername(this.props.item.id, this.state.username)).then(response => {
        this.setState({usernameModifying: false});
      });
    }
  }
  confirmUserLastFirstNameChange(){
    this.props.dispatch(userActions.editTeamUserFirstName(this.props.item.id, this.state.first_name)).then(response => {
      this.setState({firstNameLastNameModifying: false});
    });
    this.props.dispatch(userActions.editTeamUserLastName(this.props.item.id, this.state.last_name)).then(response => {
      this.setState({firstNameLastNameModifying: false});
    });
  }
  confirmUserRoleChange(){
    if (this.state.role === 3){
      this.props.dispatch(showTeamTransferOwnershipModal(true, this.props.item));
      this.setState({roleModifying: false});
      return;
    }
    if (this.state.role !== this.props.item.role){
      this.props.dispatch(userActions.editTeamUserRole(this.props.item.id, this.state.role)).then(response => {
        this.setState({roleModifying: false});
      });
    }else
      this.setState({roleModifying: false});
  }
  confirmUserDepartureDateChange(){
    if (this.state.departureDate !== this.props.item.departureDate){
      this.props.dispatch(userActions.editTeamUserDepartureDate(this.props.item.id, this.state.departureDate)).then(response => {
        this.setState({departureDateModifying: false});
      });
    }
  }
  render() {
    const user = this.props.item;
    const me = this.props.me;
    const userRoles = teamUserRoleValues.filter(item => {
      return item.value <= me.role;
    });
    return (
        <div className="flex_contents_panel active" id="team_user_tab">
          <div className="tab_heading">
            <div className="heading_row">
            <span className="heading_text">
              User's information
            </span>
              <button className="button-unstyle button_close_flexpanel" onClick={this.props.toggleFlexFunc}>
                <i className="fa fa-times"/>
              </button>
            </div>
          </div>
          <div className="tab_content_body">
            <Grid container celled="internally" columns={1} padded>
              <Grid.Row>
                <Grid.Column>
                  {!this.state.firstNameLastNameModifying ?
                      <h4>
                        {user.first_name} {user.last_name}
                        {isSuperiorOrMe(user, me) &&
                        <Icon link name="pencil" class="mrgnLeft5" onClick={this.setFirstLastNameModifying.bind(null, true)}/>}
                      </h4> :
                      <Form as="div">
                        <Form.Input
                            size="mini"
                            placeholder="First name"
                            type="text" name="first_name" fluid
                            value={this.state.first_name}
                            onChange={this.handleInput}/>
                        <Form.Input
                            size="mini"
                            placeholder="Last name"
                            type="text" name="last_name" fluid
                            value={this.state.last_name}
                            onChange={this.handleInput}/>
                        <Form.Field>
                          <Button basic size="mini" onClick={this.setFirstLastNameModifying.bind(null, false)}>Cancel</Button>
                          <Button primary size="mini" onClick={this.confirmUserLastFirstNameChange}>Save</Button>
                        </Form.Field>
                      </Form>}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <UsernameModifier dispatch={this.props.dispatch} user={user} me={me}/>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <strong>Email: </strong>
                  {user.email}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <strong>Role: </strong>
                  {!this.state.roleModifying ?
                      <span>
                        {teamUserRoles[user.role]}
                        {isSuperior(user, me) && user.id !== me.id &&
                        <Icon link name="pencil" className="mrgnLeft5" onClick={this.setRoleModifying.bind(null, true)}/>}
                   </span> :
                      <span>
                      <Dropdown floating inline name="role" options={userRoles} defaultValue={user.role} onChange={this.handleInput}/>
                       <Icon link name="delete" onClick={this.setRoleModifying.bind(null, false)}/>
                       <Icon link name="checkmark" onClick={this.confirmUserRoleChange}/>
                        </span>}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <strong>First connection: </strong>
                  {user.arrival_date}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <strong>Departure date: </strong>
                  {!this.state.departureDateModifying ?
                      <span>
                      {user.departure_date.length > 0 ? user.departure_date : 'not planned'}
                        {isSuperior(user, me) && me.id !== user.id &&
                        <Icon link name="pencil" className="mrgnLeft5" onClick={this.setDepartureDateModifying.bind(null, true)}/>}
                        </span> :
                      <Input type="date" size="mini"
                             fluid action name="departureDate"
                             value={this.state.departureDate}
                             onChange={this.handleInput}>
                        <input/>
                        <Button icon="delete" basic size="mini" onClick={this.setDepartureDateModifying.bind(null, false)}/>
                        <Button icon="checkmark" primary size="mini" onClick={this.confirmUserDepartureDateChange}/>
                      </Input>}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column class="rooms">
                  <Header as="h5">Rooms:</Header>
                    {user.channel_ids.map(item => {
                      const channel = selectChannelFromListById(this.props.channels, item);
                      return (
                            <Label size="mini" key={item}>
                              <Icon name="hashtag"/>
                              {channel.name}
                              {isAdmin(me.role) && !channel.default &&
                              <Icon name="delete" link
                                    onClick={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal(true, channel.id, this.props.item.id))}}/>}
                            </Label>
                      )
                    }, this)}
                </Grid.Column>
              </Grid.Row>
              {isSuperior(user,me) &&
              <Grid.Row>
                <Grid.Column>
                  <Button basic color="red" size="mini"
                          onClick={e => {this.props.dispatch(showTeamDeleteUserModal(true, this.props.item.id))}}>
                    Delete this user
                  </Button>
                </Grid.Column>
              </Grid.Row>}
            </Grid>
          </div>
        </div>
    )
  }
}

@connect((store)=>{
  return {
    channels: store.channels.channels,
    users: store.users.users,
    selectionProps: store.selection
  };
})
class FlexPanels extends React.Component {
  constructor(props){
    super(props);
    this.closePanel = this.closePanel.bind(this);
  }
  closePanel(){
    this.props.history.replace(this.props.backLink);
  }
  render(){
    const item = this.props.item;
    const selectionProps = this.props.selectionProps;
    const me = this.props.me;

    return (
        <div id="flex_contents">
          {item.purpose !== undefined &&
          <TeamChannelFlexTab
              me={me}
              item={item}
              apps={selectionProps.apps}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.closePanel}
              users={this.props.users}
              dispatch={this.props.dispatch}/>}
          {item.username !== undefined &&
          <TeamUserFlexTab
              me={me}
              item={item}
              apps={selectionProps.apps}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.closePanel}
              channels={this.props.channels}
              dispatch={this.props.dispatch}/>}
        </div>
    )
  }
}

module.exports = withRouter(FlexPanels);