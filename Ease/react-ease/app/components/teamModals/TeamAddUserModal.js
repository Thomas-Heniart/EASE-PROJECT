var React = require('react');
import WhiteModalTemplate from "../common/WhiteModalTemplate";
import {connect} from "react-redux";
import * as userActions from "../../actions/userActions";
import {addTeamUserToChannel} from "../../actions/channelActions";
import {showAddTeamUserModal, showTeamAddMultipleUsersModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions"
import {renderRoomLabel} from "../../utils/renderHelpers";
import {selectItemFromListById} from "../../utils/helperFunctions";
import {reflect, handleSemanticInput} from "../../utils/utils";
import { Header, Container, Divider, Icon, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

@connect((store)=>{
  return {
    channels: store.channels.channels,
    users: store.users.users,
    team_id: store.team.id,
    me: store.users.me,
    plan_id: store.team.plan_id
  };
})
class TeamAddUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      email: '',
      fname: '',
      lname: '',
      role: 1,
      username: '',
      departure_date: '',
      options: [],
      defaultRooms: [],
      value: [],
      loading: false,
      errorMessage: ''
    };
    this.state.options = this.props.channels.map(item => {
      return {
        key: item.id,
        text: item.purpose.length > 0 ? `${item.name} - ${item.purpose}` : item.name,
        value: item.id,
        name: item.name
      }
    });
    this.state.value = this.props.channels.map(item => {
      if (item.default)
        return item.id;
      return null;
    }).filter(item => {
      return item !== null;
    });
    this.state.defaultRooms = this.state.value.slice();
  }
  handleInput = handleSemanticInput.bind(this);
  usernameInput = (e) => {
      e.target.value = e.target.value.replace(" ", "_").toLowerCase();
      this.setState({[e.target.name] : e.target.value});
  };
  userRoleInput = (e, {value}) => {
    if (this.props.plan_id === 0 && value === 2) {
      this.props.dispatch(showUpgradeTeamPlanModal(true, 3));
      return;
    }
    this.setState({role: value});
  };
  dropdownChange = (e, {value}) => {
    let defaultRooms = this.state.defaultRooms;
    for (let i = 0; i < defaultRooms.length; i ++){
      if (value.indexOf(defaultRooms[i]) === -1)
        return;
    }
    this.setState({value: value});
  };
  confirm = (e) =>{
    e.preventDefault();
    const departureDate = this.state.departure_date.length > 0 ? new Date(this.state.departure_date).getTime() : null;
    if (this.props.users.length > 29 && this.props.plan_id === 0){
      this.props.dispatch(showUpgradeTeamPlanModal(true, 4));
      return;
    }
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(userActions.createTeamUser(this.state.fname, this.state.lname, this.state.email, this.state.username,departureDate, this.state.role)).then(response => {
      const user = response;
      const calls = this.state.value.map(item => {
        const channel  = selectItemFromListById(this.props.channels, item);
        if (channel.default)
          return null;
        return this.props.dispatch(addTeamUserToChannel(item, user.id));
      }).filter(item => (item !== null));
      Promise.all(calls.map(reflect)).then(values => {
        this.props.dispatch(showAddTeamUserModal(false));
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  switchToMultipleUsers = () => {
    this.props.dispatch(showAddTeamUserModal(false));
    this.props.dispatch(showTeamAddMultipleUsersModal(true));
  };
  render(){
    const dropdownOptions = [
      {key:1, value:1, content:<span>Member</span>, text:'Member'},
      {key:2, value:2, content:<span>Admin{this.props.plan_id === 0 && <img style={{height: '14px', paddingLeft: '2px'}} src="/resources/images/upgrade.png"/>}</span>, text:'Admin'}
    ];
    return (
        <WhiteModalTemplate onClose={e => {this.props.dispatch(showAddTeamUserModal(false))}}>
          <Container>
            <Header as="h1">
              Invite a team member
            </Header>
            <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0} id="add_user_modal">
              <Form.Group>
                <Form.Input label="Email address" type="email" name="email"
                            onChange={this.handleInput}
                            placeholder="name@company.com"
                            required
                            width={6}/>
                <Form.Input label="First name" type="text" name="fname"
                            onChange={this.handleInput}
                            placeholder="Optional"
                            width={5}/>
                <Form.Input label="Last name" type="text" name="lname"
                            onChange={this.handleInput}
                            placeholder="Optional" width={5}/>
              </Form.Group>
              <Form.Group>
                <Form.Input label="Username" type="text" name="username"
                            onChange={this.usernameInput}
                            required
                            placeholder="username" width={6}/>
                <Form.Select
                    style={{minWidth: '0px'}}
                    name="role"
                    value={this.state.role}
                    onChange={this.userRoleInput}
                    label="User role"
                    width={4}
                    options={dropdownOptions}/>
                <Form.Field>
                  <label>
                    Departure date&nbsp;
                    {this.props.plan_id === 0 &&
                    <img style={{height: '22px'}} src="/resources/images/upgrade.png"/>}
                  </label>
                  <Input type="date"
                         onFocus={this.props.plan_id === 0 ? e => {this.props.dispatch(showUpgradeTeamPlanModal(true, 5))} : null}
                         onChange={this.handleInput}
                         name="departure_date"
                         placeholder="Optional" width={6}/>
                </Form.Field>
              </Form.Group>
              <Form.Dropdown
                  search={true}
                  fluid
                  selection={true}
                  multiple
                  options={this.state.options}
                  value={this.state.value}
                  onChange={this.dropdownChange}
                  renderLabel={renderRoomLabel}
                  placeholder="Choose room(s)"
                  label="Room(s)"/>
              <Message error content={this.state.errorMessage}/>
              <Form.Field class="overflow-hidden">
                <Button
                    positive
                    floated='right'
                    loading={this.state.loading}
                    type="submit">
                  Next
                </Button>
                <Button floated='right' type="button" onClick={e => {this.props.dispatch(showAddTeamUserModal(false))}}>Cancel</Button>
              </Form.Field>
              <Divider horizontal>Or</Divider>
              <Form.Group class="justify_content_center">
                <Form.Button primary type="button" onClick={this.switchToMultipleUsers}>
                  <Icon name="add user"/>
                  Add a list of users
                </Form.Button>
              </Form.Group>
            </Form>
          </Container>
        </WhiteModalTemplate>
    )
  }
}

module.exports = TeamAddUserModal;