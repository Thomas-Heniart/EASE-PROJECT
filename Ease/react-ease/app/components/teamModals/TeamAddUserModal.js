import React, {Component, Fragment} from 'react';
import WhiteModalTemplate from "../common/WhiteModalTemplate";
import {connect} from "react-redux";
import * as userActions from "../../actions/userActions";
import {addTeamUserToChannel} from "../../actions/channelActions";
import {showAddTeamUserModal, showTeamAddMultipleUsersModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions"
import {renderRoomLabel} from "../../utils/renderHelpers";
import {reflect, handleSemanticInput, isEmail} from "../../utils/utils";
import { Header, Container, Icon, Form, Input, Message, Button } from 'semantic-ui-react';
import {addNotification} from "../../actions/notificationBoxActions";
import {withRouter} from "react-router-dom";
import {teamShareCard} from "../../actions/appsActions";

@connect((store) => ({
  team: store.teams[store.teamModals.addUserModal.team_id],
  team_apps: store.team_apps
}))
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
      arrival_date: '',
      options: [],
      defaultRooms: [],
      value: [],
      loading: false,
      errorMessage: '',
      loadingInvitationNow: false,
      usernameError: false,
      checkTagUser: false
    };
    const team = this.props.team;
    this.state.options = Object.keys(team.rooms).filter(id => (!team.rooms[id].default)).map(id => {
      const item = team.rooms[id];
      return {
        key: item.id,
        text: item.purpose.length > 0 ? `${item.name} - ${item.purpose}` : item.name,
        value: item.id,
        name: item.name
      }
    });
    this.state.defaultRooms = this.state.value.slice();
  }
  handleInput = handleSemanticInput.bind(this);
  emailInput = (e, {name, value}) => {
//    const newValue = value.replace(".", "").toLowerCase();
//    const username = newValue.split('@')[0];
    this.setState({[name]: value/*, username: username*/});
  };
  handleUsernameInput = (e, {name, value}) => {
    if (value && value.match(/[a-zA-Z0-9\s_\-]/gi)) {
      if (value.match(/[a-zA-Z0-9\s_\-]/gi).length === value.length && value.length <= 22)
        this.setState({ [name]: value.toLowerCase().replace(/\s/gi, '_'), usernameError: false });
      else
        this.setState({ usernameError: true });
    }
    else
      this.setState({ [name]: '', usernameError: true });
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
    this.setState({errorMessage: '', loading: true});
    const departureDate = this.state.departure_date.length > 0 ? new Date(this.state.departure_date).getTime() : null;
    const arrivalDate = this.state.arrival_date.length > 0 ? new Date(this.state.arrival_date).getTime() : null;
    this.props.dispatch(userActions.createTeamUser({
      team_id: team.id,
      first_name: this.state.fname,
      last_name: this.state.lname,
      email: this.state.email,
      username: this.state.username,
      departure_date: departureDate,
      arrival_date: arrivalDate,
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
      let callback = [];
      if (this.state.checkTagUser) {
        const room_ids = this.state.value;
        room_ids.push(Object.keys(team.rooms).find(room_id => {return team.rooms[room_id].default}));
        room_ids.map(id => {
          team.rooms[id].team_card_ids.map(card_id => {
            return callback.push(this.props.dispatch(teamShareCard({
              type: this.props.team_apps[card_id].type,
              team_id: team.id,
              team_card_id: card_id,
              team_user_id: user.id,
              account_information: {}
            })));
          });
        });
      }
      Promise.all(calls.map(reflect)).then(values => {
        Promise.all(callback.map(reflect)).then(response => {
          this.setState({loading: false});
          this.props.dispatch(addNotification({
            text: "New team user(s) successfully created!"
          }));
          this.props.dispatch(showAddTeamUserModal({active: false}));
          this.props.history.push(`/teams/${team.id}/@${user.id}`);
        });
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  sendNow = (e) => {
    e.preventDefault();
    const team = this.props.team;
    this.setState({errorMessage: '', loadingInvitationNow: true});
    const departureDate = this.state.departure_date.length > 0 ? new Date(this.state.departure_date).getTime() : null;
    const arrivalDate = this.state.arrival_date.length > 0 ? new Date(this.state.arrival_date).getTime() : null;
    this.props.dispatch(userActions.createTeamUser({
      team_id: team.id,
      first_name: this.state.fname,
      last_name: this.state.lname,
      email: this.state.email,
      username: this.state.username,
      departure_date: departureDate,
      arrival_date: arrivalDate,
      role: this.state.role
    })).then(response => {
      const user = response;
      let calls = this.state.value.filter(id => {
        return !team.rooms[id].default;
      }).map(id => {
        return this.props.dispatch(addTeamUserToChannel({
          team_id: team.id,
          channel_id: id,
          team_user_id: user.id
        }));
      });
      let callback = [];
      if (this.state.checkTagUser) {
        const room_ids = this.state.value;
        room_ids.push(Object.keys(team.rooms).find(room_id => {return team.rooms[room_id].default}));
        room_ids.map(id => {
          team.rooms[id].team_card_ids.map(card_id => {
            return callback.push(this.props.dispatch(teamShareCard({
              type: this.props.team_apps[card_id].type,
              team_id: team.id,
              team_card_id: card_id,
              team_user_id: user.id,
              account_information: {}
            })));
          });
        });
      }
      Promise.all(calls.map(reflect)).then(values => {
        Promise.all(callback.map(reflect)).then(response => {
          this.setState({loadingInvitationNow: false});
          this.props.dispatch(addNotification({
            text: "New team user(s) successfully created!"
          }));
          this.props.history.push(`/teams/${team.id}/@${user.id}`);
          this.props.dispatch(showAddTeamUserModal({active: false}));
          this.props.dispatch(userActions.sendInvitationToTeamUserList({
            team_id: team.id,
            team_user_id_list: [user.id]
          }));
        });
      });
    }).catch(err => {
      this.setState({loadingInvitationNow: false, errorMessage: err});
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
        <WhiteModalTemplate onClose={e => {this.props.dispatch(showAddTeamUserModal({active: false}))}}>
          <Container>
            <Header as="h1">
              Add a new user
                <Button style={{float:'right'}} primary type="button" onClick={this.switchToMultipleUsers}>
                  <Icon name="add user"/>
                  Add a list of users
                </Button>
            </Header>
            <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0} id="add_user_modal">
              <Form.Group>
                <Form.Input label="Email address" type="email" name="email"
                            onChange={this.emailInput}
                            placeholder="name@company.com"
                            value={this.state.email}
                            required
                            width={8}/>
                <Form.Select
                    style={{minWidth: '0px'}}
                    name="role"
                    value={this.state.role}
                    onChange={this.userRoleInput}
                    label="User role"
                    width={8}
                    options={dropdownOptions}/>
              </Form.Group>
              <Form.Group>
                <Form.Field width={8}>
                  <label class="display_flex align_items_center">
                    Arrival date&nbsp;
                    {team.plan_id === 0 &&
                    <img style={{height: '22px'}} src="/resources/images/upgrade.png"/>}
                  </label>
                  <Input type="date"
                         onFocus={team.plan_id === 0 ?
                             e => {
                               this.props.dispatch(showUpgradeTeamPlanModal({active: true, feature_id: 6, team_id: this.props.team.id}))
                             } : null}
                         min={moment().add(1, 'days').format('YYYY-MM-DD')}
                         max={!!this.state.departure_date ? moment(this.state.departure_date, 'YYYY-MM-DD').subtract(1, 'days').format('YYYY-MM-DD') : null}
                         onChange={this.handleInput}
                         name="arrival_date"
                         placeholder="Optional" width={8}/>
                </Form.Field>
                <Form.Field width={8}>
                  <label class="display_flex align_items_center">
                    Departure date&nbsp;
                    {team.plan_id === 0 &&
                    <img style={{height: '22px'}} src="/resources/images/upgrade.png"/>}
                  </label>
                  <Input type="date"
                         onFocus={team.plan_id === 0 ?
                             e => {
                               this.props.dispatch(showUpgradeTeamPlanModal({active: true, feature_id: 5, team_id: this.props.team.id}))
                             } : null}
                         min={!!this.state.arrival_date ? moment(this.state.arrival_date, 'YYYY-MM-DD').add(1, 'days').format('YYYY-MM-DD') : moment().add(1, 'days').format('YYYY-MM-DD')}
                         onChange={this.handleInput}
                         name="departure_date"
                         placeholder="Optional" width={8}/>
                </Form.Field>
              </Form.Group>
              {this.state.usernameError &&
              <Message color="red" content={'Please choose a username that is all lowercase, containing, only letters, numbers, periods, hyphens and underscores. From 3 to 22 characters.'}/>}
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
              <p>New members will automatically join #openspace</p>
              <label className='check_tag_user_label'>Setting</label>
              <Form.Checkbox
                toggle
                name='checkTagUser'
                className='check_tag_user'
                onChange={this.handleInput}
                checked={this.state.checkTagUser}
                label="Add this user in all apps of his rooms"/>
              <Message error content={this.state.errorMessage}/>
              <Form.Group id='invitationButton' class="overflow-hidden">
                {!this.state.arrival_date ?
                    <React.Fragment>
                      <Form.Button
                          basic color='green'
                          positive
                          floated='right'
                          loading={this.state.loading}
                          disabled={this.state.loading || !isEmail(this.state.email)}
                          type="submit"
                          width={8}>
                        Send invitation <u><strong>later</strong></u>
                      </Form.Button>
                      <Form.Button
                          color='green'
                          floated='right'
                          type="button"
                          loading={this.state.loadingInvitationNow}
                          disabled={this.state.loadingInvitationNow || !isEmail(this.state.email)}
                          onClick={this.sendNow}
                          width={8}>
                        Send invitation <u><strong>now</strong></u>
                      </Form.Button>
                    </React.Fragment> :
                    <React.Fragment>
                      <Form.Field width={8}/>
                      <Form.Button
                          color='green'
                          positive
                          width={8}
                          loading={this.state.loading}
                          disabled={this.state.loading || !isEmail(this.state.email)}
                          type="submit">
                        Send invitation on {moment(this.state.arrival_date, 'YYYY-MM-DD').format('DD/MM/YYYY')}
                      </Form.Button>
                    </React.Fragment>
                }
              </Form.Group>
            </Form>
          </Container>
        </WhiteModalTemplate>
    )
  }
}

module.exports = withRouter(TeamAddUserModal);