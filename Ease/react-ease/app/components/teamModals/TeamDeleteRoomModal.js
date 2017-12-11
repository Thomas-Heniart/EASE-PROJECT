import React, {Component} from 'react';
import {showTeamDeleteChannelModal} from "../../actions/teamModalActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {handleSemanticInput} from "../../utils/utils";
import {deleteTeamChannel} from "../../actions/channelActions";
import {selectChannelFromListById} from "../../utils/helperFunctions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

@connect((store)=>{
  return {
    team: store.teams[store.teamModals.teamDeleteChannelModal.team_id],
    room_id: store.teamModals.teamDeleteChannelModal.room_id,
    teams: store.teams
  };
})
class TeamDeleteRoomModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false,
      confirm: false,
      room: this.props.team.rooms[this.props.room_id]
    }
  }
  handleInput = handleSemanticInput.bind(this);
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(deleteTeamChannel({
      team_id: this.props.team.id,
      room_id: this.props.room_id
    })).then(response => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showTeamDeleteChannelModal({active: false}));
  };
  render(){
    const channel_name = this.state.room.name;

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={`You are about to delete #${channel_name}`}>
          <Form class="container" error={this.state.errorMessage.length > 0}>
            <Form.Field>
              Deleting a Room is usefull to clean things up when you do not anticipate using this room anymore. If you delete this room:
            </Form.Field>
            <Form.Field>
              <List bulleted>
                <List.Item>
                  No one will be able to send apps in it anymore
                </List.Item>
                <List.Item>
                  It will be closed for anyone who has it open and all members will be removed
                </List.Item>
                <List.Item>
                  All apps included in it will be deleted from the dashboard of the users concerned
                </List.Item>
                <List.Item>
                  All its content will be removed from Ease.space, and you cannot restore the content once deleted
                </List.Item>
              </List>
            </Form.Field>
            <Form.Field>
              <span>Are you sure you want to delete <strong>#{channel_name}</strong> ?</span>
              <Checkbox label="Yes, I am absolutely sure" name="confirm" checked={this.state.confirm} onClick={this.handleInput}/>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                attached='bottom'
                type="submit"
                disabled={!this.state.confirm}
                loading={this.state.loading}
                positive
                onClick={this.confirm}
                className="modal-button"
                content="REMOVE THIS ROOM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = TeamDeleteRoomModal;