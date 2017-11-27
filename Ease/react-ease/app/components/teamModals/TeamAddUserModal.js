var React = require('react');
import WhiteModalTemplate from "../common/WhiteModalTemplate";
import {connect} from "react-redux";
import * as userActions from "../../actions/userActions";
import {addTeamUserToChannel} from "../../actions/channelActions";
import {showAddTeamUserModal, showTeamAddMultipleUsersModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions"
import {renderRoomLabel} from "../../utils/renderHelpers";
import {reflect, handleSemanticInput} from "../../utils/utils";
import { Header, Container, Divider, Icon, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

@connect((store) => {
  return {
    team: store.teams[store.teamModals.addUserModal.team_id]
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
    const team = this.props.team;
    this.state.options = Object.keys(team.rooms).map(id => {
      const item = team.rooms[id];
      return {
        key: item.id,
        text: item.purpose.length > 0 ? `${item.name} - ${item.purpose}` : item.name,
        value: item.id,
        name: item.name
      }
    });
    this.state.value = Object.keys(team.rooms).map(id => {
      const item = team.rooms[id];
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
    if (this.props.team.plan_id === 0 && value === 2) {
      this.props.dispatch(showUpgradeTeamPlanModal({
        active: true,
        feature_id: 3,
        team_id: this.props.team.id
      }));
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
    const team = this.props.team;

    const departureDate = this.state.departure_date.length > 0 ? new Date(this.state.departure_date).getTime() : null;
    if (Object.keys(team.team_users).length > 29 && team.plan_id === 0){
      this.props.dispatch(showUpgradeTeamPlanModal({
        active: true,
        feature_id: 4,
        team_id: team.id
      }));
      return;
    }
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(userActions.createTeamUser({
      team_id: team.id,
      first_name: this.state.fname,
      last_name: this.state.lname,
      email: this.state.email,
      username: this.state.username,
      departure_date: departureDate,
      role: this.state.role
    })).then(response => {
      const user = response;
      const calls = this.state.value.filter(id => {
        return !team.rooms[id].default;
      }).map(id => {
        return this.props.dispatch(addTeamUserToChannel({
          team_id: team.id,
          channel_id: id,
          team_user_id: user.id
        }));
      });
      Promise.all(calls.map(reflect)).then(values => {
        this.props.dispatch(showAddTeamUserModal({active: false}));
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  sendNow = (e) => {
    e.preventDefault();
    const team = this.props.team;

    const departureDate = this.state.departure_date.length > 0 ? new Date(this.state.departure_date).getTime() : null;
    if (Object.keys(team.team_users).length > 29 && team.plan_id === 0){
      this.props.dispatch(showUpgradeTeamPlanModal({
        active: true,
        feature_id: 4,
        team_id: team.id
      }));
      return;
    }
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(userActions.createTeamUserNow({
      team_id: team.id,
      first_name: this.state.fname,
      last_name: this.state.lname,
      email: this.state.email,
      username: this.state.username,
      departure_date: departureDate,
      role: this.state.role
    })).then(response => {
      const user = response;
      const calls = this.state.value.filter(id => {
        return !team.rooms[id].default;
      }).map(id => {
        return this.props.dispatch(addTeamUserToChannel({
          team_id: team.id,
          channel_id: id,
          team_user_id: user.id
        }));
      });
      Promise.all(calls.map(reflect)).then(values => {
        this.props.dispatch(showAddTeamUserModal({active: false}));
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  switchToMultipleUsers = () => {
    this.props.dispatch(showAddTeamUserModal({active: false}));
    this.props.dispatch(showTeamAddMultipleUsersModal({
      active: true,
      team_id: this.props.team.id
    }));
  };
  render(){
    const team = this.props.team;
    const dropdownOptions = [
      {key:1, value:1, content:<span>Member</span>, text:'Member'},
      {key:2, value:2, content:<span>Admin{team.plan_id === 0 && <img style={{height: '14px', paddingLeft: '2px'}} src="/resources/images/upgrade.png"/>}</span>, text:'Admin'}
    ];
    return (
        <WhiteModalTemplate onClose={e => {this.props.dispatch(showAddTeamUserModal(false))}}>
          <Container>
            <Header as="h1">
              Create a team member
            </Header>
            <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0} id="add_user_modal">
              <Form.Group>
                <Form.Input label="Email address" type="email" name="email"
                            onChange={this.handleInput}
                            placeholder="name@company.com"
                            required
                            width={8}/>
                <Form.Input label="Username" type="text" name="username"
                            onChange={this.usernameInput}
                            required
                            placeholder="username" width={8}/>
                {/*<Form.Input label="First name" type="text" name="fname"
                            onChange={this.handleInput}
                            placeholder="Optional"
                            width={5}/>
                <Form.Input label="Last name" type="text" name="lname"
                            onChange={this.handleInput}
                            placeholder="Optional" width={5}/>*/}
              </Form.Group>
              <Form.Group>
                {/*<Form.Input label="Username" type="text" name="username"
                            onChange={this.usernameInput}
                            required
                            placeholder="Username" width={6}/>*/}
                <Form.Select
                    style={{minWidth: '0px'}}
                    name="role"
                    value={this.state.role}
                    onChange={this.userRoleInput}
                    label="User role"
                    width={8}
                    options={dropdownOptions}/>
                <Form.Field width={8}>
                  <label>
                    Departure date&nbsp;
                    {team.plan_id === 0 &&
                    <img style={{height: '22px'}} src="/resources/images/upgrade.png"/>}
                  </label>
                  <Input type="date"
                         onFocus={team.plan_id === 0 ? e => {this.props.dispatch(showUpgradeTeamPlanModal({active: true, feature_id: 5, team_id: this.props.team_id}))} : null}
                         onChange={this.handleInput}
                         name="departure_date"
                         placeholder="Optional" width={8}/>
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
              <Form.Group id='invitationButton' class="overflow-hidden">
                <Form.Button
                    basic color='green'
                    positive
                    floated='right'
                    loading={this.state.loading}
                    type="submit"
                    width={8}>
                  Send invitation <u><strong>later</strong></u>
                </Form.Button>
                <Form.Button
                    color='green'
                    floated='right'
                    type="button"
                    onClick={this.sendNow}
                    width={8}>
                  Send invitation <u><strong>now</strong></u>
                </Form.Button>
              </Form.Group>
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