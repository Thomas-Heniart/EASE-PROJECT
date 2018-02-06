import React, {Component,Fragment} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showInviteTeamUsersModal} from "../../actions/teamModalActions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import post_api from "../../utils/post_api";
import {connect} from "react-redux";
import {sendInvitationToTeamUserList} from "../../actions/userActions";

@connect(store => ({
  team: store.teams[store.teamModals.inviteTeamUsersModal.team_id]
}))
class InviteTeamUsersModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  close = () => {
    this.props.dispatch(showInviteTeamUsersModal({
      active: false,
      team_id: this.props.team.id
    }))
  };
  confirm = (e) => {
    const {team} = this.props;
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    let team_user_id_list = [];
    Object.keys(team.team_users).forEach(team_user_id => {
      const team_user = team.team_users[team_user_id];
      if (!team_user.invitation_sent)
        team_user_id_list.push(team_user.id);
    });
    this.props.dispatch(sendInvitationToTeamUserList({
      team_id: team.id,
      team_user_id_list: team_user_id_list
    })).then(response => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Invite your Team 👋'}>
          <Form class="container" onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
            <Form.Field>
              We noticed that some people in your team haven’t been invited yet. Do you want to invite them now?
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                loading={this.state.loading}
                disabled={this.state.loading}
                positive
                className="modal-button"
                content={
                  <React.Fragment>
                    SEND INVITES
                    <Icon style={{margin: '0 0 0 5px', height: 'auto'}} name="send"/>
                  </React.Fragment>
                }/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default InviteTeamUsersModal;