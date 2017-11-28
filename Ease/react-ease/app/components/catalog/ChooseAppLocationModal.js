import React from 'react';
import { Input, Button, Container, Form, Table, List, Loader, Icon } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { Switch, Route } from 'react-router-dom';
import {dashboard} from "../../utils/post_api";
import {handleSemanticInput} from "../../utils/utils";
var api = require('../../utils/api');

@connect(store => ({
    teams: store.teams,
    dashboard: store.dashboard
}), reduxActionBinder)
class ChooseAppLocationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 1,
      profiles: [],
      profileName: '',
    }
  }
  handleInput = handleSemanticInput.bind(this);
  render() {
    const {
      website,
      appName,
      handleInput,
      selectedProfile,
      selectedRoom,
      confirm,
      selectProfile,
      selectRoom
    } = this.props;
    const profiles = this.props.profiles.map(profile => (
      <List.Item as="a"
                 key={profile.id}
                 class="display_flex"
                 active={selectedProfile === profile.id}
                 onClick={e => selectProfile(profile.id)}>
          <strong className="overflow-ellipsis">{profile.name}</strong>
          &nbsp;&nbsp;
      </List.Item>
    ));
    const teamsList = Object.entries(this.props.teams).map((teams, i) => (
      teams.map((team) => (
        team.rooms &&
        <Table.Row key={team.id}>
            <Table.Cell style={{backgroundColor: '#e1e1e1', width: '170px'}}>
                <div style={{display: 'inline-flex'}}>
                    <Icon name='users'/>
                    <span className='overflow-ellipsis' style={{maxWidth: '100px'}}>{team.name}</span>
                </div>
            </Table.Cell>
            <Table.Cell verticalAlign='bottom'>
                <List link style={{width: '180px'}}>
                  {Object.entries(team.rooms).map(rooms => (
                    rooms.map((room) => (
                      room.id &&
                      <List.Item as="a"
                                 class="display_flex"
                                 active={selectedRoom === room.id}
                                 onClick={e => selectRoom(team.id, room.id)}
                                 key={room.id}>
                          <strong className='overflow-ellipsis'># {room.name}</strong>
                          &nbsp;&nbsp;
                      </List.Item>
                    ))
                  ))}
                </List>
            </Table.Cell>
        </Table.Row>
      ))
    ));
    return (
      <Form class="container" id="add_bookmark_form" onSubmit={confirm}>
          <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div class="squared_image_handler">
                  <img src={website.logo} alt="Website logo"/>
              </div>
              <span class="app_name">{appName}</span>
          </Form.Field>
          <Form.Field>
              <div style={{marginBottom: '10px'}}>Where would you like to send this app?</div>
              <Container class="newProfiles pushable">
                  <Table striped>
                      <Table.Body>
                          <Table.Row>
                              <Table.Cell style={{backgroundColor: '#e1e1e1'}}>
                                  <div style={{display: 'inline-flex'}}>
                                      <Icon name='user'/>
                                      <span className='overflow-ellipsis'
                                            style={{maxWidth: '100px'}}>Personal Space</span>
                                  </div>
                              </Table.Cell>
                              <Table.Cell verticalAlign='bottom'>
                                  <List link style={{width: '180px'}}>
                                    {this.props.loading ?
                                      <Loader inline={'centered'} active size="tiny"/> :
                                      profiles}
                                  </List>
                                {(!this.props.loading && !this.props.profileAdded) &&
                                <form style={{marginBottom: 0}} onSubmit={this.props.createProfile}>
                                    <Input
                                      value={this.props.profileName}
                                      style={{fontSize: '14px'}}
                                      name="profileName"
                                      required
                                      transparent
                                      onChange={handleInput}
                                      class="create_profile_input"
                                      icon={<Icon name="plus square" link onClick={this.props.createProfile}/>}
                                      placeholder='Create new group'
                                    />
                                </form>}
                              </Table.Cell>
                          </Table.Row>
                        {teamsList}
                      </Table.Body>
                  </Table>
              </Container>
          </Form.Field>
          <Button
            attached='bottom'
            type="submit"
            positive
            disabled={(selectedProfile === -1 && selectedRoom === -1) || appName.length === 0}
            onClick={confirm}
            className="modal-button"
            content="NEXT"/>
      </Form>
    )
  }
}

export default ChooseAppLocationModal;