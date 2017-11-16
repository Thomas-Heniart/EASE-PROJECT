import React from 'react';
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showVerifyTeamUserModal} from "../../actions/teamModalActions";
import {verifyTeamUserArrive} from "../../actions/userActions";
import {connect} from "react-redux";

@connect((store)=>{
  return {
    team: store.teams[store.teamModals.verifyTeamUserModal.team_id],
    team_user_id: store.teamModals.verifyTeamUserModal.team_user_id
  };
})
class VerifyTeamUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(verifyTeamUserArrive({
      team_id: this.props.team.id,
      team_user_id: this.props.team_user_id
    })).then(() => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err})
    });
  };
  close = () => {
    this.props.dispatch(showVerifyTeamUserModal({active: false}));
  };
  render(){
    const team = this.props.team;
    const user = team.team_users[this.props.team_user_id];

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Ready to get in the team!'}>
          <Form class="container" onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
            <Form.Field>
              <strong>{user.username}</strong> accepted your invitation to join {team.name}.
            </Form.Field>
            <Form.Field>
              <strong>{user.username}</strong>'s user information is:<br/>{user.first_name} {user.last_name} - {user.email}
            </Form.Field>
            <Form.Field>
              Congrats! Together you can now work on the tools of your team without worrying about passwords.
            </Form.Field>
            <Form.Field>
              This is your final confirmation to let {user.username} access the team {team.name}
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                class="modal-button uppercase"
                content='CONFIRM ACCESS'/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = VerifyTeamUserModal;