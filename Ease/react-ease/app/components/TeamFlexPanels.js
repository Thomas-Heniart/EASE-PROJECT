import React from 'react';
import {
  showTeamDeleteChannelModal,
  showTeamDeleteUserFromChannelModal,
  showTeamDeleteUserModal,
  showTeamTransferOwnershipModal,
  showUpgradeTeamPlanModal
} from "../actions/teamModalActions";
import * as channelActions from "../actions/channelActions"
import * as userActions from "../actions/userActions"
import {
  isAdmin,
  isOwner,
  isSuperior,
  isSuperiorOrMe,
  selectChannelFromListById,
  selectItemFromListById,
  selectUserFromListById
} from "../utils/helperFunctions";
import {renderUserLabel} from "../utils/renderHelpers";
import {basicDateFormat, handleSemanticInput, reflect, teamUserRoles, teamUserRoleValues, userNameRuleString, objectToList} from "../utils/utils";
import {
  Button,
  Dropdown,
  Form,
  Grid,
  Header,
  Icon,
  Input,
  Label,
  List,
  Message,
  Popup,
  TextArea
} from 'semantic-ui-react';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";

function ChannelJoinRequestList(props){
  const {room, team} = props;
  const requests = room.join_requests;

  return (
      <List>
        {requests.map(item => {
          const user = team.team_users[item];
          return (
              <List.Item key={item}>
                <Label basic size="mini">
                  <Icon name="user"/>
                  {user.username} would like to access this group.&nbsp;
                  <a onClick={e => {props.dispatch(channelActions.addTeamUserToChannel({
                    team_id: team.id,
                    channel_id: room.id,
                    team_user_id: item
                  }))}}>
                    accept</a>
                  &nbsp;or&nbsp;
                  <a onClick={e => {props.dispatch(channelActions.deleteJoinChannelRequest({
                    team_id: team.id,
                    room_id: room.id,
                    team_user_id: item
                  }))}}>
                    refuse</a> ?
                </Label>
              </List.Item>
          )
        })}
      </List>
  )
}

@connect()
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
      return this.props.dispatch(channelActions.addTeamUserToChannel({
        team_id: this.props.team.id,
        channel_id: room.id,
        team_user_id: item
      }));
    });
    Promise.all(calls.map(reflect)).then(results => {
      this.setState({loading: false, modifying:false});
    });
  };
  setModifying = (state) => {
    if (state){
      const room = this.props.room;
      const team = this.props.team;

      const options = objectToList(team.team_users).filter(item => {
        return room.team_user_ids.indexOf(item.id) === -1;
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
                      noResultsMessage="All users selected"
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

const RoomManagerInfoIcon = () => {
  return (
      <Popup size="mini"
             position="bottom right"
             inverted
             trigger={
               <Icon class="mrgnRight5" name="info circle"/>
             }
             content='Room Managers receive notifications related to their rooms, answer join requests and update some Single Apps passwords, if needed. There can be only 1 Room Manager per room. This person has to be Admin or Owner.'/>
  )
};

@connect()
class RoomManagerSection extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  editRoomManager = (id) => {
    if (id === this.props.room.room_manager_id)
      return;
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(channelActions.editRoomManager({
      team_id: this.props.team.id,
      room_id: this.props.room.id,
      team_user_id: id
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage:err});
      setTimeout(() => {this.setState({errorMessage: ''})}, 3000);
    });
  };
  render(){
    const {team, room, me} = this.props;
    const manager = team.team_users[room.room_manager_id];
    return (
        <Grid.Column>
          <h5>Room Manager</h5>
          <div>
            <Label class="display-inline-block" style={{margin: '0 .5em .5em 0'}}><Icon name="user" link class="mrgnRight5"/>{manager.username}</Label>
            {(isOwner(me.role) || me.id === manager.id) &&
            <AdminsDropdown
                style={{marginBottom: '.5em'}}
                value={manager.id}
                users={this.props.users}
                onSelect={this.editRoomManager}
                loading={this.state.loading}/>}
          </div>
          {this.state.errorMessage.length > 0 &&
          <Message  color="red" content={this.state.errorMessage}/>}
          <p>Room Managers are responsible for administer rooms and apps in it. <RoomManagerInfoIcon/></p>
        </Grid.Column>
    )
  }
}

const AdminsDropdown = ({value, users, onSelect, loading, style}) => {
  const admins = users.filter(item => (isAdmin(item.role))).map(item => {
    return {
      key: item.id,
      text: `${item.username}(${teamUserRoles[item.role]})`,
      value: item.id
    }
  });
  return (
      <Dropdown onChange={(e, {value}) => {onSelect(value)}}
                button
                style={style}
                class="icon mini"
                basic
                value={value}
                labeled
                size="mini"
                loading={loading}
                text="Modify"
                icon="exchange"
                options={admins}/>
  )
};

class RoomNameSection extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      name: '',
      modifying: false
    }
  }
  setModifying = (state) => {
    this.setState({modifying: state, loading: false, name:this.props.room.name, errorMessage: ''})
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    this.props.dispatch(channelActions.editTeamChannelName({
      team_id: this.props.team_id,
      room_id: this.props.room.id,
      name: this.state.name
    })).then(response => {
      this.setModifying(false);
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
    });
  };
  handleInput = (e) => {
    e.target.value = e.target.value.replace(" ", "_").toLowerCase();
    this.setState({[e.target.name] : e.target.value});
  };
  render(){
    const room = this.props.room;
    const me = this.props.me;
    return (
        <Grid.Column>
          {!this.state.modifying ?
              <h4>{room.name}
                {isAdmin(me.role) && !room.default &&
                <button class="button-unstyle mrgnLeft5 action_button"
                        onClick={this.setModifying.bind(null, true)}>
                  <i class="fa fa-pencil mrgnLeft5"/>
                </button>}
              </h4>
              :
              <Form as="div" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
                <Form.Field>
                  <Input  label="Name"
                          size="mini"
                          type="text" name="name"
                          fluid
                          value={this.state.name}
                          onChange={this.handleInput}/>
                </Form.Field>
                <Message error size="mini" content={this.state.errorMessage}/>
                <Form.Field>
                  <Button basic size="mini" type="button" onClick={this.setModifying.bind(null, false)}>Cancel</Button>
                  <Button primary size="mini" onClick={this.confirm} loading={this.state.loading} disabled={this.state.loading}>Save</Button>
                </Form.Field>
              </Form>
          }
        </Grid.Column>
    )
  }
}

class RoomPurposeSection extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      purpose: '',
      errorMessage: '',
      modifying: false
    }
  }
  setModifying = (state) => {
    this.setState({modifying: state, loading: false, purpose:this.props.room.purpose, errorMessage: ''})
  };
  handleInput = handleSemanticInput.bind(this);
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(channelActions.editTeamChannelPurpose({
      team_id: this.props.team_id,
      room_id: this.props.room.id,
      purpose: this.state.purpose
    })).then(response => {
      this.setModifying(false);
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    const room = this.props.room;
    const me = this.props.me;
    return (
        <Grid.Column>
          <h5>Purpose</h5>
          {!this.state.modifying ?
              <span>{room.purpose}
                {isAdmin(me.role) &&
                <button class="button-unstyle mrgnLeft5 action_button"
                        onClick={this.setModifying.bind(null, true)}>
                  <i class="fa fa-pencil mrgnLeft5"/>
                </button>}
                  </span>
              :
              <Form as="div">
                <Form.Field>
                          <TextArea style={{fontSize: '.8em'}}
                                    size="mini"
                                    type="text"
                                    name="purpose"
                                    value={this.state.purpose}
                                    onChange={this.handleInput}/>
                </Form.Field>
                <Form.Field>
                  <Button basic size="mini" onClick={this.setModifying.bind(null,false)}>Cancel</Button>
                  <Button primary size="mini" onClick={this.confirm}>Save</Button>
                </Form.Field>
              </Form>
          }
        </Grid.Column>
    )
  }
}

class TeamChannelFlexTab extends React.Component{
  constructor(props){
    super(props);
  }
  render() {
    const {me, team, channel} = this.props;
    const channel_users = channel.team_user_ids.map(id => {
      return team.team_users[id];
    });
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
                <RoomNameSection
                    dispatch={this.props.dispatch}
                    room={channel}
                    me={me}
                    team_id={team.id}/>
              </Grid.Row>
              <Grid.Row>
                <RoomPurposeSection
                    dispatch={this.props.dispatch}
                    room={channel}
                    me={me}
                    team_id={team.id}/>
              </Grid.Row>
              <Grid.Row>
                <RoomManagerSection
                    me={me}
                    team={team}
                    room={channel}
                    users={channel_users}/>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column class="users">
                  <Header as="h5">
                    {isAdmin(me.role) && channel.join_requests.length > 0 ?
                        <Label circular color="red" size="mini">{channel.join_requests.length}</Label>: null}
                    Members :
                  </Header>
                  {channel_users.map(user => {
                    return (
                        <Label size="mini" key={user.id}>
                          <Icon name="user"/>
                          {user.username}
                          {isAdmin(me.role) && !channel.default &&
                          <Icon
                              name="delete"
                              link
                              onClick={e => {
                                this.props.dispatch(showTeamDeleteUserFromChannelModal({
                                  active: true,
                                  room_id:channel.id,
                                  team_user_id: user.id,
                                  team_id: team.id
                                }))
                              }}/>}
                        </Label>)})}
                  {isAdmin(me.role) &&
                  <ChannelJoinRequestList
                      room={channel}
                      team={team}
                      dispatch={this.props.dispatch}/>}
                  <div>
                    {isAdmin(me.role) && !channel.default &&
                    <AddMemberToRoomDiv
                        team={team}
                        room={channel}/>}
                  </div>
                </Grid.Column>
              </Grid.Row>
              {isAdmin(me.role) && !channel.default &&
              <Grid.Row>
                <Grid.Column>
                  <Button basic color="red" size="mini"
                          onClick={e => {this.props.dispatch(showTeamDeleteChannelModal({
                            active: true,
                            room_id: channel.id,
                            team_id: team.id
                          }))}}>
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

@connect()
class UsernameModifier extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      username: '',
      modifying: false,
      loading: false,
      error: false
    }
  };
  handleInput = (e) => {
    e.target.value = e.target.value.replace(" ", "_").toLowerCase();
    this.setState({[e.target.name] : e.target.value});
  };
  setModifying  = (state) => {
    this.setState({username: this.props.user.username, modifying: state, error : false, loading: false});
  };
  validate = (e) => {
    e.preventDefault();
    const {team, user} = this.props;
    this.setState({loading: true, error: false});
    this.props.dispatch(userActions.editTeamUserUsername({
      team_id: team.id,
      team_user_id: user.id,
      username: this.state.username
    })).then(response => {
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
                  <Button primary size="mini" loading={this.state.loading}>Save</Button>
                </Form.Field>
              </Form>}
        </Grid.Column>
    )
  }
}

@connect()
class TeamUserRole extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      edit: false,
      role: 1
    }
  }
  handleInput = (e,{name, value}) =>{
    if (this.props.team.plan_id === 0 && value === 2){
      this.props.dispatch(showUpgradeTeamPlanModal({
        active: true,
        feature_id: 3,
        team_id: this.props.team.id
      }));
      return;
    }
    this.setState({[name]: value});
  };
  setEdit = (state) => {
    this.setState({edit: state, role: this.props.user.role, errorMessage: ''});
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({errorMessage: ''});
    if (this.state.role === 3){
      this.props.dispatch(showTeamTransferOwnershipModal({
        active: true,
        team_id: this.props.team.id,
        team_user_id: this.props.user.id
      }));
      this.setState({edit: false});
      return;
    }
    if (this.state.role !== this.props.user.role){
      this.props.dispatch(userActions.editTeamUserRole({
        team_id: this.props.team.id,
        team_user_id: this.props.user.id,
        role: this.state.role
      })).then(response => {
        this.setEdit(false);
      }).catch(err => {
        this.setState({errorMessage: err});
      });
    }else
      this.setEdit(false);
  };
  render(){
    const {user, me, team} = this.props;
    const userRoles = teamUserRoleValues.filter(item => {
      return item.value <= me.role;
    });
    let adminRole = userRoles.find(item => (item.value === 2));

    if (adminRole !== undefined){
      adminRole.content = <span>Admin{team.plan_id === 0 && <img style={{height: '14px', paddingLeft: '2px'}} src="/resources/images/upgrade.png"/>}</span>;
    }
    return (
        <Grid.Column>
          <strong>Role: </strong>
          {!this.state.edit ?
              <span>
                        {teamUserRoles[user.role]}
                {isSuperior(user, me) && user.id !== me.id &&
                <Icon link name="pencil" className="mrgnLeft5" onClick={this.setEdit.bind(null, true)}/>}
                   </span> :
              <span>
                      <Dropdown floating inline class="mini" name="role" options={userRoles}
                                value={this.state.edit ? this.state.role : this.props.user.role}
                                onChange={this.handleInput}/>
                       <Icon link name="delete" onClick={this.setEdit.bind(null, false)}/>
                       <Icon link name="checkmark" onClick={this.confirm}/>
                {!!this.state.errorMessage.length &&
                <Message color="red" content={this.state.errorMessage}/>}
                        </span>}
        </Grid.Column>
    )
  }
}

@connect()
class FirstLastNameSection extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      first_name: '',
      last_name: '',
      edit: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  setEdit = (state) => {
    const {user} = this.props;
    this.setState({loading: false, first_name: user.first_name, last_name: user.last_name, edit:state});
  };
  confirm = (e) => {
    e.preventDefault();
    const {dispatch, team, me, user} = this.props;
    const calls = [
      dispatch(userActions.editTeamUserFirstName({
        team_id: team.id,
        team_user_id: user.id,
        first_name: this.state.first_name
      })),
      dispatch(userActions.editTeamUserLastName({
        team_id: team.id,
        team_user_id: user.id,
        last_name: this.state.last_name
      }))
    ];
    this.setState({loading: true});
    Promise.all(calls.map(reflect)).then(response => {
      this.setState({loading: false});
      this.setEdit(false);
    });
  };
  render(){
    const {team, me, user} = this.props;

    return (
        <Grid.Column>
          {!this.state.edit ?
              <h4>
                {user.first_name} {user.last_name}
                {isSuperiorOrMe(user, me) &&
                <Icon link name="pencil" class="mrgnLeft5" onClick={this.setEdit.bind(null, true)}/>}
              </h4> :
              <Form onSubmit={this.confirm}>
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
                  <Button type="button" basic size="mini" onClick={this.setEdit.bind(null, false)}>Cancel</Button>
                  <Button primary size="mini" loading={this.state.loading}>Save</Button>
                </Form.Field>
              </Form>}
        </Grid.Column>
    )
  }
}

@connect()
class DepartureDateSection extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      edit: false,
      departure_date: '',
      loading: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  setEdit = (state) => {
    const {user} = this.props;
    this.setState({
      departure_date: !!user.departure_date ? moment(user.departure_date).format('YYYY-MM-DD') : '',
      edit: state,
      loading: false
    });
  };
  confirm = (e) => {
    e.preventDefault();
    const {team, dispatch, user} = this.props;
    this.setState({loading: true});
    dispatch(userActions.editTeamUserDepartureDate({
      team_id: team.id,
      team_user_id: user.id,
      departure_date: !!this.state.departure_date.length ? new Date(this.state.departure_date).getTime() : null
    })).then(response => {
      this.setEdit(false);
    }).catch(err => {
      this.setState({loading: false});
    });
  };
  render(){
    const {team, user, me} = this.props;
    return (
        <Grid.Column>
          <strong>Departure date: </strong>
          {!this.state.edit ?
              <span>
                      {user.departure_date !== null ? basicDateFormat(user.departure_date) : 'not planned'}
                {isSuperior(user, me) && me.id !== user.id &&
                <Icon link name="pencil" className="mrgnLeft5" onClick={this.setEdit.bind(null, true)}/>}
                {isSuperior(user, me) && me.id !== user.id && team.plan_id === 0 &&
                <img style={{height: '16px'}} src="/resources/images/upgrade.png"/>}
                        </span> :
              <Input type="date" size="mini"
                     fluid action name="departure_date"
                     value={this.state.departure_date}
                     onChange={this.handleInput}>
                <input/>
                <Button icon="delete" basic size="mini" onClick={this.setEdit.bind(null, false)}/>
                <Button icon="checkmark" primary size="mini" loading={this.state.loading} onClick={this.confirm}/>
              </Input>}
        </Grid.Column>
    )
  }
}

class TeamUserFlexTab extends React.Component{
  constructor(props){
    super(props);
  }
  render() {
    const {me, team, user} = this.props;

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
                <FirstLastNameSection
                    team={team}
                    me={me}
                    user={user}/>
              </Grid.Row>
              <Grid.Row>
                <UsernameModifier
                    team={team}
                    user={user}
                    me={me}/>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <strong>Email: </strong>
                  {user.email}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <TeamUserRole
                    team={team}
                    user={user}
                    me={me}/>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column>
                  <strong>First connection: </strong>
                  {basicDateFormat(user.arrival_date)}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <DepartureDateSection
                    team={team}
                    user={user}
                    me={me}/>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column class="rooms">
                  <Header as="h5">Rooms:</Header>
                  {user.room_ids.map(id => {
                    const room = team.rooms[id];
                    return (
                        <Label size="mini" key={id}>
                          <Icon name="hashtag"/>
                          {room.name}
                          {isAdmin(me.role) && !room.default &&
                          <Icon name="delete" link
                                onClick={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal({
                                  active: true,
                                  team_id: team.id,
                                  room_id: room.id,
                                  team_user_id: user.id
                                }))}}/>}
                        </Label>
                    )
                  }, this)}
                </Grid.Column>
              </Grid.Row>
              {isSuperior(user,me) &&
              <Grid.Row>
                <Grid.Column>
                  <Button basic color="red" size="mini"
                          onClick={e => {this.props.dispatch(showTeamDeleteUserModal({
                            active: true,
                            team_id: team.id,
                            team_user_id: user.id
                          }))}}>
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
    selectionProps: store.selection,
    plan_id: store.team.plan_id
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
    const {item, team, me} = this.props;

    return (
        <div id="flex_contents">
          {item.purpose !== undefined &&
          <TeamChannelFlexTab
              me={me}
              channel={item}
              team={team}
              toggleFlexFunc={this.closePanel}
              plan_id={team.plan_id}
              dispatch={this.props.dispatch}/>}
          {item.username !== undefined &&
          <TeamUserFlexTab
              me={me}
              user={item}
              team={team}
              toggleFlexFunc={this.closePanel}
              plan_id={team.plan_id}
              dispatch={this.props.dispatch}/>}
        </div>
    )
  }
}

module.exports = withRouter(FlexPanels);