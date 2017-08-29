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
import {teamUserRoles,
    selectChannelFromListById,
    selectUserFromListById,
    isAdmin,
    isAdminOrMe,
    isSuperior} from "../utils/helperFunctions";
import {teamUserRoleValues} from "../utils/utils";
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
    return (
        <div className="flex_contents_panel active" id="team_tab">
          <div className="tab_heading">
            <div className="heading_row">
            <span className="heading_text">
              Team's information
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
                      <h4>{this.props.item.name}
                        {isAdmin(me.role) &&
                        <button class="button-unstyle mrgnLeft5 action_button"
                                onClick={this.setNameModifying.bind(null, true)}>
                          <i class="fa fa-pencil"/>
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
                          <Button primary size="mini" onClick={this.confirmNameChange}>Edit</Button>
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
                          <i class="fa fa-pencil"/>
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
                          <Button primary size="mini" onClick={this.confirmPurposeChange}>Edit</Button>
                        </Form.Field>
                      </Form>
                  }
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <Header as="h5">
                    {isAdmin(me.role) && this.props.item.join_requests.length > 0 ?
                        <Label circular color="red" size="mini">{this.props.item.join_requests.length}</Label>: null}
                    Members :
                  </Header>
                  <List>
                    {this.props.item.userIds.map(function (item) {
                      const user = selectUserFromListById(this.props.users, item);
                      return (
                          <List.Item key={item}>
                            <Label size="mini">
                              <Icon name="user"/>
                              {user.username}
                              {(isSuperior(user, me) || user.id === me.id) &&
                              <Icon name="delete" link
                                    onClick={e => {
                                      this.props.dispatch(showTeamDeleteUserFromChannelModal(true, this.props.item.id, user.id))
                                    }}/>}
                            </Label>
                          </List.Item>)}, this)}
                  </List>
                  {isAdmin(me.role) &&
                  <ChannelJoinRequestList
                      channel_id={this.props.item.id}
                      users={this.props.users}
                      requests={this.props.item.join_requests}
                      dispatch={this.props.dispatch}/>}
                  {isAdmin(me.role) &&
                  <Button primary size="mini"
                          onClick={e => {this.props.dispatch(teamModalActions.showTeamChannelAddUserModal(true, this.props.item.id))}}>
                    <Icon name="add user"/>
                    Add member
                  </Button>}
                </Grid.Column>
              </Grid.Row>
              {isAdmin(me.role) &&
              <Grid.Row>
                <Grid.Column>
                  <Button basic color="red" size="mini"
                          onClick={e => {this.props.dispatch(showTeamDeleteChannelModal(true, this.props.item.id))}}>
                    Delete this group
                  </Button>
                </Grid.Column>
              </Grid.Row>}
            </Grid>
          </div>
        </div>
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
    if (this.state.role == 3){
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
    if (this.state.departureDate != this.props.item.departureDate){
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
                        {isAdminOrMe(user, me) &&
                        <Icon link name="pencil" onClick={this.setFirstLastNameModifying.bind(null, true)}/>}
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
                          <Button primary size="mini" onClick={this.confirmUserLastFirstNameChange}>Edit</Button>
                        </Form.Field>
                      </Form>}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  {!this.state.usernameModifying ?
                      <div>
                        @{user.username}
                        {isAdminOrMe(user, me) &&
                        <Icon link name="pencil" onClick={this.setUsernameModifying.bind(null, true)}/>}
                      </div> :
                      <Form as="div">
                        <Form.Input
                            size="mini"
                            placeholder="Username"
                            type="text" name="username" fluid
                            value={this.state.username}
                            onChange={this.handleInput}/>
                        <Form.Field>
                          <Button basic size="mini" onClick={this.setUsernameModifying.bind(null, false)}>Cancel</Button>
                          <Button primary size="mini" onClick={this.confirmUsernameChange}>Edit</Button>
                        </Form.Field>
                      </Form>}
                </Grid.Column>
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
                        {isSuperior(user, me) && user.id != me.id &&
                        <Icon link name="pencil" onClick={this.setRoleModifying.bind(null, true)}/>}
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
                        <Icon link name="pencil" onClick={this.setDepartureDateModifying.bind(null, true)}/>}
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
                <Grid.Column>
                  <Header as="h5">Rooms:</Header>
                  <List>
                    {user.channel_ids.map(item => {
                      const channel = selectChannelFromListById(this.props.channels, item);
                      return (
                          <List.Item key={item}>
                            <Label size="mini">
                              <Icon name="users"/>
                              {channel.name}
                              {isAdmin(me.role) &&
                              <Icon name="delete" link
                                    onClick={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal(true, channel.id, this.props.item.id))}}/>}
                            </Label>
                          </List.Item>
                      )
                    }, this)}
                  </List>
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
          {item.purpose != undefined &&
          <TeamChannelFlexTab
              me={me}
              item={item}
              apps={selectionProps.apps}
              flexActive={this.props.flexActive}
              toggleFlexFunc={this.closePanel}
              users={this.props.users}
              dispatch={this.props.dispatch}/>}
          {item.username != undefined &&
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