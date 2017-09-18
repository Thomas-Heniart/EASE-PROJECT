import React, {Component} from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamDeleteUserModal} from '../../actions/teamModalActions';
import {handleSemanticInput} from "../../utils/utils";
import {deleteTeamUser} from "../../actions/userActions";
import {selectUserFromListById} from "../../utils/helperFunctions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

@connect((store)=>{
  return {
    users: store.users.users,
    modal: store.teamModals.teamDeleteUserModal
  };
})
class TeamDeleteUserModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false,
      confirm: false,
      user: selectUserFromListById(this.props.users, this.props.modal.team_user_id)
    }
  }
  handleInput = handleSemanticInput.bind(this);
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(deleteTeamUser(this.state.user.id)).then(response => {
      this.props.dispatch(showTeamDeleteUserModal(false));
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    const username = this.state.user.username;
    return (
        <SimpleModalTemplate
            onClose={e => {this.props.dispatch(showTeamDeleteUserModal(false))}}
            headerContent={`You are about to delete ${username}'s membership`}>
          <Form class="container" error={this.state.errorMessage.length > 0}>
            <Form.Field class="first_word_capitalize">
              {username} will lose access to all accounts. But be carefull : we don't delete accounts on the websites themselves... Yet ;) Don't forget to go delete them by yourself.
            </Form.Field>
            <Form.Field>
              <Checkbox label="I'm sure to remove this member" name="confirm" checked={this.state.confirm} onChange={this.handleInput}/>
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
                content="REMOVE THIS MEMBER"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = TeamDeleteUserModal;