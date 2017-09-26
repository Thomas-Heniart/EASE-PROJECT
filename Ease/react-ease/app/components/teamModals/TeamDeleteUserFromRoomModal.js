import React, {Component} from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamDeleteUserFromChannelModal} from '../../actions/teamModalActions';
import {selectUserFromListById, selectChannelFromListById} from "../../utils/helperFunctions";
import {removeTeamUserFromChannel} from "../../actions/channelActions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

@connect((store)=>{
  return {
    users: store.users.users,
    channels: store.channels.channels,
    modal: store.teamModals.teamDeleteUserFromChannelModal
  };
})
class TeamDeleteUserFromRoomModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      channel: null,
      user: null,
      loading: false
    };
    this.state.channel = selectChannelFromListById(this.props.channels, this.props.modal.channel_id);
    this.state.user = selectUserFromListById(this.props.users, this.props.modal.team_user_id);
  }
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(removeTeamUserFromChannel(this.state.channel.id, this.state.user.id)).then(response => {
      this.props.dispatch(showTeamDeleteUserFromChannelModal(false));
    }).catch(err => {
      this.setState({loading: false, errorMessage:err});
    });
  };
  render(){
    const username = this.state.user.username;
    const channel = this.state.channel;
    return (
        <SimpleModalTemplate
            onClose={e => {this.props.dispatch(showTeamDeleteUserFromChannelModal(false))}}
            headerContent={<span>You are about to <strong>remove {username} from {channel.name}</strong></span>}>
          <Form class="container" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Form.Field class="first_word_capitalize">{username} will lose access to <strong>#{channel.name}</strong> and all Apps it includes.</Form.Field>
            <Form.Field class="first_word_capitalize">{username} will lose access to <strong>#{channel.name}</strong>, and to all Apps included in <strong>{channel.name}</strong></Form.Field>
            <Form.Field >Also, all Apps from #{channel.name} pinned to {username}'s dashboard will disapear.</Form.Field>
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