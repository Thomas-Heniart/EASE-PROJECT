import React, {Component} from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamDeleteUserModal} from '../../actions/teamModalActions';
import {handleSemanticInput} from "../../utils/utils";
import {deleteTeamUser} from "../../actions/userActions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

@connect((store)=>({
  team: store.teams[store.teamModals.teamDeleteUserModal.team_id],
  team_user_id: store.teamModals.teamDeleteUserModal.team_user_id
}))
class TeamDeleteUserModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false,
      confirm: false,
      user: this.props.team.team_users[this.props.team_user_id]
    }
  }
  handleInput = handleSemanticInput.bind(this);
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(deleteTeamUser({
      team_id: this.props.team.id,
      team_user_id: this.state.user.id
    })).then(response => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showTeamDeleteUserModal({active: false}));
  };
  render(){
    const username = this.state.user.username;
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={`You are about to delete ${username}'s membership`}>
          <Form onSubmit={this.confirm} class="container" error={!!this.state.errorMessage.length}>
            <Form.Field class="first_word_capitalize">
              {username} will lose access to all accounts. But be carefull : we don't delete accounts on the websites themselves... Yet ;) Don't forget to go delete them by yourself.
            </Form.Field>
            <Form.Field>
              <Checkbox label="I'm sure to remove this member" name="confirm" checked={this.state.confirm} onClick={this.handleInput}/>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                disabled={!this.state.confirm}
                loading={this.state.loading}
                positive
                className="modal-button"
                content="REMOVE THIS MEMBER"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = TeamDeleteUserModal;