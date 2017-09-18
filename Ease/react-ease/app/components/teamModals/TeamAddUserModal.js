var React = require('react');
var api = require('../../utils/api');
import WhiteModalTemplate from "../common/WhiteModalTemplate";
import {connect} from "react-redux";
import * as userActions from "../../actions/userActions";
import {addTeamUserToChannel} from "../../actions/channelActions";
import {showAddTeamUserModal, showTeamAddMultipleUsersModal} from "../../actions/teamModalActions"
import {renderRoomLabel} from "../../utils/renderHelpers";
import {reflect} from "../../utils/utils";
import { Header, Container, Divider, Icon, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

function FirstStepAddUser(props){
  return (
      <Container>
        <Header as="h1">
          Invite a team member
        </Header>
        <Form onSubmit={props.validateStep} error={props.errorMessage.length > 0}>
          <Form.Group>
            <Form.Input label="Email address" type="email" name="email"
                        onChange={props.handleReactInput}
                        placeholder="name@company.com"
                        required
                        width={6}/>
            <Form.Input label="First name" type="text" name="fname"
                        onChange={props.handleReactInput}
                        placeholder="Optional"
                        width={5}/>
            <Form.Input label="Last name" type="text" name="lname"
                        onChange={props.handleReactInput}
                        placeholder="Optional" width={5}/>
          </Form.Group>
          <Form.Group>
            <Form.Input label="Username" type="text" name="username"
                        onChange={props.handleReactInput}
                        required
                        placeholder="Username" width={6}/>
            <Form.Select
                style={{minWidth: '0px'}}
                name="role"
                defaultValue={1}
                onChange={props.handleReactInput}
                label="User role"
                width={4}
                options={[{key:1, value:1, text:'Member'}, {key:2, value:2, text:'Admin'}]}/>
            <Form.Input label="Departure date (optional)" type="date"
                        onChange={props.handleReactInput}
                        name="departure_date"
                        placeholder="Optional" width={6}/>
          </Form.Group>
          <Form.Dropdown
              search={true}
              fluid
              selection={true}
              multiple
              options={props.options}
              value={props.value}
              onChange={props.dropdownChange}
              renderLabel={renderRoomLabel}
              placeholder="Choose room(s)"
              label="Room(s)"/>
          <Message error content={props.errorMessage}/>
          <Form.Field class="overflow-hidden">
            <Button
                positive
                floated='right'
                loading={props.loading}
                type="submit">
              Next
            </Button>
            <Button floated='right' onClick={e => {props.dispatch(showAddTeamUserModal(false))}}>Cancel</Button>
          </Form.Field>
          <Divider horizontal>Or</Divider>
          <Form.Group class="justify_content_center">
            <Form.Button primary type="button" onClick={props.switchMultipleUsers}>
              <Icon name="add user"/>
              Add a list of users
            </Form.Button>
          </Form.Group>
        </Form>
      </Container>
  )
}

@connect((store)=>{
  return {
    channels: store.channels.channels,
    users: store.users.users,
    team_id: store.team.id,
    me: store.users.me
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
      firstStepLoading: false,
      firstStepErrorMessage: ''
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

    this.handleReactInput = this.handleReactInput.bind(this);
    this.validateFirstStep = this.validateFirstStep.bind(this);
    this.dropdownChange = this.dropdownChange.bind(this);
  }
  handleReactInput(e, {name, value}){
    this.setState({[name]: value});
  }
  dropdownChange(e, {value}){
    let defaultRooms = this.state.defaultRooms;
    for (let i = 0; i < defaultRooms.length; i ++){
      if (value.indexOf(defaultRooms[i]) === -1)
        return;
    }
    this.setState({value: value});
  }
  validateFirstStep(e){
    e.preventDefault();
    this.setState({firstStepErrorMessage: '', firstStepLoading: true});
    this.props.dispatch(userActions.createTeamUser(this.state.fname, this.state.lname, this.state.email, this.state.username,this.state.departure_date, this.state.role)).then(response => {
      const user = response;
      const calls = this.state.value.map(item => {
        return this.props.dispatch(addTeamUserToChannel(item, user.id));
      });
      Promise.all(calls.map(reflect)).then(values => {
        this.props.dispatch(showAddTeamUserModal(false));
      });
    }).catch(err => {
      this.setState({firstStepLoading: false, firstStepErrorMessage: err});
    });
  }
  switchToMultipleUsers = () => {
    this.props.dispatch(showAddTeamUserModal(false));
    this.props.dispatch(showTeamAddMultipleUsersModal(true));
  };
  render(){
    return (
        <WhiteModalTemplate onClose={e => {this.props.dispatch(showAddTeamUserModal(false))}}>
          <FirstStepAddUser key="1"
                            me={this.props.me}
                            email={this.state.email}
                            fname={this.state.fname}
                            lname={this.state.lname}
                            role={this.state.role}
                            username={this.state.username}
                            handleReactInput={this.handleReactInput}
                            departure_date={this.state.departure_date}
                            validateStep={this.validateFirstStep}
                            options={this.state.options}
                            value={this.state.value}
                            errorMessage={this.state.firstStepErrorMessage}
                            loading={this.state.firstStepLoading}
                            dropdownChange={this.dropdownChange}
                            switchMultipleUsers={this.switchToMultipleUsers}
                            dispatch={this.props.dispatch}/>
        </WhiteModalTemplate>
    )
  }
}

module.exports = TeamAddUserModal;