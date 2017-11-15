import React, {Component} from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamDeleteUserFromChannelModal} from '../../actions/teamModalActions';
import {selectUserFromListById, selectChannelFromListById} from "../../utils/helperFunctions";
import {removeTeamUserFromChannel} from "../../actions/channelActions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

@connect((store)=>({
  team: store.teams[store.teamModals.teamDeleteUserFromChannelModal.team_id],
  team_user_id: store.teamModals.teamDeleteUserFromChannelModal.team_user_id,
  room_id: store.teamModals.teamDeleteUserFromChannelModal.room_id
}))
class TeamDeleteUserFromRoomModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      room: null,
      user: null,
      loading: false
    };
    this.state.room = this.props.team.rooms[this.props.room_id];
    this.state.user = this.props.team.team_users[this.props.team_user_id];
  }
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(removeTeamUserFromChannel({
      team_id: this.props.team.id,
      room_id: this.state.room.id,
      team_user_id: this.state.user.id
    })).then(response => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage:err});
    });
  };
  close = () => {
    this.props.dispatch(showTeamDeleteUserFromChannelModal({active: false}));
  };
  render(){
    const username = this.state.user.username;
    const room = this.state.room;
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={<span>You are about to <strong>remove {username} from {room.name}</strong></span>}>
          <Form class="container" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Form.Field class="first_word_capitalize">{username} will lose access to <strong>#{room.name}</strong> and all Apps it includes.</Form.Field>
            <Form.Field class="first_word_capitalize">{username} will lose access to <strong>#{room.name}</strong>, and to all Apps included in <strong>{room.name}</strong></Form.Field>
            <Form.Field >Also, all Apps from #{room.name} pinned to {username}'s dashboard will disappear.</Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                attached='bottom'
                type="submit"
                loading={this.state.loading}
                positive
                onClick={this.confirm}
                className="modal-button"
                content="REMOVE FROM THIS ROOM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = TeamDeleteUserFromRoomModal;